package io.sealights.tool.configuration

import arrow.core.Either
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mu.KotlinLogging
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class TokenResolver {
    @OptIn(ExperimentalEncodingApi::class)
    fun resolve(jwtToken: String): Either<String, TokenData> {
        try {
            val afterFirstDotPosition = jwtToken.indexOf(".") + 1
            val jwtPayload = jwtToken.substring(afterFirstDotPosition, jwtToken.indexOf(".", afterFirstDotPosition))

            val decodedPayloadArray = Base64.decode(jwtPayload)
            val decodedPayload = String(decodedPayloadArray, Charsets.UTF_8)

            val type = object : TypeToken<Map<String, Any>>() {}.type
            val fromJson = Gson().fromJson<Map<String, Any>>(decodedPayload, type)

            return Either.Right(
                TokenData(
                    token = jwtToken,
                    role = fromJson["x-sl-role"] as String,
                    apiUrl = removeApiSufix(fromJson["x-sl-server"])
                )
            )
        } catch (exception: Exception) {
            log.error (exception) { "Could not fetch data from token `$jwtToken`" }
            return Either.Left("Could not extract data from provided token")
        }
    }

    private fun removeApiSufix(urlToParse: Any?) = urlToParse.toString().replace("/api", "")

    companion object {
        private val log = KotlinLogging.logger {}
    }
}

data class TokenData(
    val token: String,
    val role: String,
    val apiUrl: String
)