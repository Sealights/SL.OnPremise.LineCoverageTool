package io.sealights.tool.build

import mu.KotlinLogging


class BuildDataService(
    private val buildLinesClient: BuildDataClient
) {
    fun fetchBuildData(buildSessionId: String, componentName: String?): BuildInfo {
        log.info { "Fetching the build information for buildSessionId '$buildSessionId' and component: '$componentName'" }
        if (buildSessionId.isEmpty()) {
            log.error { """
                The provided build session id is empty. Possible reasons of this:
                  * The '--referenceBuildSessionId' parameter was not specified in the command line.
                    The 'Reference build' is not defined for the '--buildSessionId'. Possible first build? 
                  * The '--integrationReferenceBuildSessionId' parameter was not specified in the command line.
                    The 'Reference build' is not defined for the '--integrationBuildSessionId'. Possible first integration build? 
            """.trimIndent()}
            throw RuntimeException("BuildSessionId can not be empty")
        }
        
        val buildInfo = buildLinesClient.fetchBuildInfo(buildSessionId)
        
        log.info { "Build data fetched successfully for build session id 'buildSessionId'" }
        log.info { "> app / branch / build: ${buildInfo.appName} / ${buildInfo.branchName} / ${buildInfo.buildName},  commitHash: ${buildInfo.commitHash}, reference build: ${buildInfo.referenceBuildSessionId}" }
        
        return buildInfo
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}
