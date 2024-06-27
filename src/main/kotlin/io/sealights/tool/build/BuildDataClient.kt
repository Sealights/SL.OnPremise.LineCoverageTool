package io.sealights.tool.build

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrElse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.sealights.tool.ApplicationProcess
import io.sealights.tool.HttpClient
import mu.KotlinLogging

class BuildDataClient(private val httpClient: HttpClient) {

    fun fetchBuildInfo(buildSessionId: String): BuildInfo {

        val buildDataResponse = httpClient.get(
            url = "sl-api/v1/builds/${buildSessionId}",
            queryParams = mapOf()
        )

        return buildDataResponse.flatMap { unmarshallResponse(it) }
            .flatMap { createBuildInfo(it) }
            .mapLeft(ApplicationProcess::handleExit)
            .getOrElse { BuildInfo.empty() }
    }

    private fun createBuildInfo(responseMap: Map<String, Any>): Either<String, BuildInfo> {
        val x = Either.Right(
            BuildInfo(
                commitHash = responseMap["commitHash"].toString(),
                appName = responseMap["appName"] as String,
                branchName = responseMap["branchName"] as String,
                buildName = (responseMap["build"] as Map<String, Any>)["name"] as String,
                referenceBuildSessionId = if ((responseMap["isReference"] as Boolean)) responseMap["referenceBuildSessionId"].toString() else "",
                buildMethodology = buildExecutionMode(responseMap["appName"] as String)
            )
        )

        return x
    }

    private fun buildExecutionMode(buildExecutionMode: String): BuildInfo.BuildMethodology {
        return if (buildExecutionMode == "MethodLines") {
            BuildInfo.BuildMethodology.METHOD_LINE
        } else {
            BuildInfo.BuildMethodology.METHOD
        }
    }

    private fun unmarshallResponse(buildMapResponseBody: String): Either<String, Map<String, Any>> {
        log.info { "processing build data response" }
        log.debug { "Data fetched: $buildMapResponseBody" }

        try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val responseMap = Gson().fromJson<Map<String, Any>>(buildMapResponseBody, type)

            return Either.Right(responseMap["data"] as Map<String, Any>)
        } catch (e: RuntimeException) {
            log.error { "Error processing body: $buildMapResponseBody" }
            return Either.Left("Error processing the build data dut to body unmarshalling")
        }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}
