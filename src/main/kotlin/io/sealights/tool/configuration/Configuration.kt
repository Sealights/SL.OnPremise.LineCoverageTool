package io.sealights.tool.configuration

import arrow.core.getOrElse
import mu.KotlinLogging

object Configuration {
    private val log = KotlinLogging.logger {}

    var workspace: String = ""
    var token: String = ""
    var baseCommit: String = ""
    var baseApp: String = ""
    var baseBranch: String = ""
    var baseBuild: String = ""
    var currentApp: String = ""
    var currentBranch: String = ""
    var currentBuild: String = ""
    lateinit var apiUrl: String

    fun build(parser: ApplicationArgParser, tokenResolver: TokenResolver) {
        workspace = parser.workspace
        token = parser.token
        baseCommit = parser.baseCommit
        baseApp = parser.baseApp
        baseBranch = parser.baseBranch
        baseCommit = parser.baseCommit
        currentApp = parser.currentApp
        currentBranch = parser.currentBranch
        currentBuild = parser.currentBuild
        apiUrl = tokenResolver.resolve(token).map { tokenData -> tokenData.apiUrl }.getOrElse { "" }
    }

    fun printConfiguration() {
        log.info { "=== CONFIGURATION DATA ===" }
        log.info { "  workspace: $workspace" }
        log.info { "  baseCommit: $baseCommit" }
        log.info { "  baseApp: $baseApp" }
        log.info { "  baseBranch: $baseBranch" }
        log.info { "  baseBuild: $baseBuild" }
        log.info { "  currentApp: $currentApp" }
        log.info { "  currentBranch: $currentBranch" }
        log.info { "  currentBuild: $currentBuild" }
        log.info { "  token: ${token.substring(0, 10)}..." }
        log.info { "  apiUrl: $apiUrl" }
        log.info { "=== CONFIGURATION DATA END ===" }
    }

    fun validate() {
        var shouldExit = false

        if (apiUrl.isNullOrEmpty()) {
            log.info { "Could not extract api URL from provided token" }
            shouldExit = true
        }

        if (shouldExit) {
            System.exit(2)
        }
    }


}