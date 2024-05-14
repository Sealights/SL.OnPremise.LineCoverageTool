package io.sealights.tool

import arrow.core.Either
import io.sealights.tool.configuration.Configuration
import mu.KotlinLogging
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class HttpClient(
    private val okHttpClient: OkHttpClient,
    private val apiUrl: String,
    private val token: String
) {
    fun get(
        url: String,
        queryParams: Map<String, String> = mapOf(),
        customHeaders: Map<String, String> = mapOf()
    ) = httpCallExecute(method = GET, url = url, payload = null, queryParams = queryParams, customHeaders = customHeaders)

    fun post(
        url: String,
        payload: String,
        queryParams: Map<String, String> = mapOf(),
        customHeaders: Map<String, String> = mapOf()
    ) = httpCallExecute(method = POST, url = url, payload = payload, queryParams = queryParams, customHeaders = customHeaders)

    private fun httpCallExecute(
        method: String,
        url: String,
        payload: String?,
        queryParams: Map<String, String> = mapOf(),
        customHeaders: Map<String, String> = mapOf()
    ): Either<String, String> = try {
        val requestBuilder = prepareRequestBuilder(url, queryParams, customHeaders)
        val request = requestBuilder.method(method, payload?.toRequestBody()).build()
        val response = okHttpClient.newCall(request).execute()
        postProcessResponse(request, response)
    } catch (e: RuntimeException) {
        log.error { "Could not fetch data from $url. The reason is: `${e.message}` " }
        Either.Left("Could not GET the data due to `${e.message}`")
    }

    private fun prepareRequestBuilder(url: String, queryParams: Map<String, String>, customHeaders: Map<String, String>): Request.Builder {
        val requestBuilder = Request.Builder()
            .url("$apiUrl/$url")
        addSealightsHeaders(requestBuilder)
        addCustomHeaders(requestBuilder, customHeaders)
        return requestBuilder
    }

    private fun postProcessResponse(request: Request, response: Response): Either<String, String> {
        return if (response.isSuccessful) {
            log.debug { "Successfully get data from `${request.url}`." }
            Either.Right(response.body.toString())
        } else {
            log.warn { "Error getting remote data from `${request.url}`. Status code: `${response.code}` and body `${response.body?.string()}`" }
            Either.Left("Service `${request.url}` responded with status `${response.code}`")
        }
    }

    private fun addCustomHeaders(requestBuilder: Request.Builder, customHeaders: Map<String, String>) {
        customHeaders.forEach { (name, value) -> requestBuilder.addHeader(name, value) }
    }

    private fun addSealightsHeaders(requestBuilder: Request.Builder) {
        requestBuilder
            .addHeader(HEADER_ACCEPT, CONTENT_TYPE_JSON)
            .addHeader(HEADER_AUTHORIZATION, "Bearer $token")
    }

    private fun buildUrl(url: String, queryParams: Map<String, String>): HttpUrl {
        HttpUrl.Builder
        val httpUrlBuilder = HttpUrl.Builder().scheme("$apiUrl/$url")
        queryParams.forEach { (name, value) -> httpUrlBuilder.addQueryParameter(name, value) }
        return httpUrlBuilder.build()
    }

    companion object {
        fun build(): HttpClient {
            return HttpClient(OkHttpClient(), Configuration.apiUrl, Configuration.token)
        }

        const val HEADER_ACCEPT = "Accept"
        const val HEADER_AUTHORIZATION = "Authorization"
        const val CONTENT_TYPE_JSON = "application/json"
        
        const val GET = "GET"
        const val POST = "POST"
        
        private val log = KotlinLogging.logger {}
    }
}
