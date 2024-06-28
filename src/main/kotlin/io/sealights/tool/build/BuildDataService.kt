package io.sealights.tool.build

import arrow.core.Either
import arrow.core.flatMap
import mu.KotlinLogging


class BuildDataService(
    private val buildLinesClient: BuildDataClient
) {
    fun fetchBuildData(buildSessionId: String, componentName: String?): Either<String, BuildInfo> {
        log.info { "Fetching the build information for buildSessionId '$buildSessionId' and component: '$componentName'" }
        if (buildSessionId.isEmpty()) {
            log.error {
                """
                The provided build session id is empty. Possible reasons of this:
                  * The '--referenceBuildSessionId' parameter was not specified in the command line.
                    The 'Reference build' is not defined for the '--buildSessionId'. Possible first build? 
                  * The '--integrationReferenceBuildSessionId' parameter was not specified in the command line.
                    The 'Reference build' is not defined for the '--integrationBuildSessionId'. Possible first integration build? 
            """.trimIndent()
            }
            throw RuntimeException("BuildSessionId can not be empty")
        }

        return buildLinesClient.fetchBuildInfo(buildSessionId)


    }

    fun fetchReferenceBuildData(buildInfo: BuildInfo, specifiedReferenceBuildSessionId: String, componentName: String?): Either<String, BuildInfoPair> {
        val referenceBuildSessionId = specifiedReferenceBuildSessionId.ifEmpty { buildInfo.referenceBuildSessionId }
        return buildLinesClient.fetchBuildInfo(referenceBuildSessionId).flatMap { Either.Right(BuildInfoPair(buildInfo, it)) }
    }

    fun validateBuildData(resolvedBuildData: BuildInfoPair): Either<String, BuildInfoPair> {

        return Either.Right(resolvedBuildData)
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}
