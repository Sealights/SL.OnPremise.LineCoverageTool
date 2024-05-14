package io.sealights.tool.configuration

import arrow.core.getOrElse
import mu.KotlinLogging


object Configuration {
    private val log = KotlinLogging.logger {}
    
    var workspace: String = ""
    var token: String = ""
    var startCommit: String = ""
    lateinit var apiUrl: String 
    
    fun build(parser: ApplicationArgParser, tokenResolver: TokenResolver) {
        workspace = parser.workspace
        token = parser.token
        startCommit = parser.startCommit
        apiUrl = tokenResolver.resolve(token).map { tokenData -> tokenData.apiUrl }.getOrElse { "unknown-host" }
    }
    
    fun printConfiguration() {
        log.info { "=== CONFIGURATION DATA ===" }
        log.info { "  workspace: $workspace" }
        log.info { "  startCommit: $startCommit" }
        log.info { "  token: ${token.substring(0, 10)}..." }
        log.info { "  apiUrl: $apiUrl" }
        log.info { "=== CONFIGURATION DATA END ===" }
    }

    
}