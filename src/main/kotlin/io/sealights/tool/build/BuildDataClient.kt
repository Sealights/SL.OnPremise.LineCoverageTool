package io.sealights.tool.build

import arrow.core.Either
import arrow.core.flatMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.sealights.tool.ApplicationProcess
import io.sealights.tool.HttpClient
import mu.KotlinLogging

class BuildDataClient(private val httpClient: HttpClient) {

    fun fetchBuildInfo(buildSessionId: String): Either<String, BuildInfo> {
        log.info { "Getting build information for $buildSessionId" }
        val buildDataResponse = httpClient.get(
            url = "sl-api/v1/builds/${buildSessionId}", queryParams = mapOf()
        )

        return buildDataResponse.flatMap { unmarshallResponse(it) }.flatMap { createBuildInfo(it) }.mapLeft(ApplicationProcess::handleExit)
    }


    private fun createBuildInfo(responseMap: Map<String, Any>): Either<String, BuildInfo> {
        try {
            val buildInfo = BuildInfo(
                commitHash = responseMap["commitHash"].toString(),
                appName = responseMap["appName"] as String,
                branchName = responseMap["branchName"] as String,
                buildName = (responseMap["build"] as Map<String, Any>)["name"] as String,
                referenceBuildSessionId = if ((responseMap["isReference"] as Boolean)) responseMap["referenceBuildSessionId"].toString() else "",
                buildMethodology = buildExecutionMode(responseMap["appName"] as String)
            )

            log.info { "Build data fetched successfully for build session id 'buildSessionId'" }
            log.info { "> app / branch / build: ${buildInfo.appName} / ${buildInfo.branchName} / ${buildInfo.buildName},  commitHash: ${buildInfo.commitHash}, reference build: ${buildInfo.referenceBuildSessionId}" }

            return Either.Right(buildInfo)
        } catch (exception: RuntimeException) {
            log.error(exception) { "Error processing unmarshalled build info data. Enable debug log level to see body returned from server." }
            return Either.Left("Processing the response fetched form backend failed. See error messages above")
        }
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
        } catch (exception: RuntimeException) {
            log.error(exception) { "Error processing body: $buildMapResponseBody" }
            return Either.Left("Error processing the build data dut to body unmarshalling")
        }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}
