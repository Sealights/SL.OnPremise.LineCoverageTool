package io.sealights.tool.configuration

import arrow.core.getOrElse
import mu.KotlinLogging

object Configuration {
    private val log = KotlinLogging.logger {}

    var workspace: String = ""
    var token: String = ""
    var buildSessionId: String = ""
    var referenceBuildSessionId: String = ""
    var integrationReferenceBuildSessionId: String = ""
    var integrationBuildSessionId: String = ""
    var componentName: String = ""
    lateinit var apiUrl: String

    fun build(parser: ApplicationArgParser, tokenResolver: TokenResolver) {
        workspace = parser.workspace
        token = parser.token
        buildSessionId = parser.buildSessionId
        referenceBuildSessionId = parser.referenceBuildSessionId
        integrationReferenceBuildSessionId = parser.integrationReferenceBuildSessionId
        integrationBuildSessionId = parser.integrationBuildSessionId
        componentName = parser.componentName
        apiUrl = tokenResolver.resolve(token).map { tokenData -> tokenData.apiUrl }.getOrElse { "" }
    }

    fun buildSessionId(): String =
        buildSessionId.ifEmpty {
            integrationBuildSessionId
        }

    fun referenceBuildSessionId(): String =
        referenceBuildSessionId.ifEmpty {
            integrationReferenceBuildSessionId
        }


    fun printConfiguration() {
        log.info { "=== CONFIGURATION DATA ===" }
        log.info { "  workspace: $workspace" }
        log.info { "  buildSessionId: $buildSessionId" }
        log.info { "  referenceBuildSessionId: $referenceBuildSessionId" }
        log.info { "  integrationReferenceBuildSessionId: $integrationReferenceBuildSessionId" }
        log.info { "  integrationBuildSessionId: $integrationBuildSessionId" }
        log.info { "  componentName: $componentName" }
        log.info { "  token: ${token.substring(0, 10)}..." }
        log.info { "  apiUrl: $apiUrl" }
        log.info { "=== CONFIGURATION DATA END ===" }
    }

    fun validate() {
        var shouldExit = false

        if (apiUrl.isEmpty()) {
            log.info { "Could not extract api URL from provided token" }
            shouldExit = true
        }

        if (shouldExit) {
            System.exit(2)
        }
    }


}