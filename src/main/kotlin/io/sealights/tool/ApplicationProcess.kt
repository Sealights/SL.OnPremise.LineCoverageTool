package io.sealights.tool

import mu.KotlinLogging
import kotlin.system.exitProcess

object ApplicationProcess {

    fun handleExit(message: String) {
        log.info { "Abnormal application exit with reason: $message" }
        exitProcess(2)
    }

    private val log = KotlinLogging.logger {}
    
}