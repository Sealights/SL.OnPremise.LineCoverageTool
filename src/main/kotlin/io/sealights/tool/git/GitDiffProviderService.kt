package io.sealights.tool.git

import mu.KotlinLogging
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class GitDiffProviderService {

    fun gitDiffOutput(workspace: String, baseCommit: String, endCommit: String?): List<String> {
        log.info { "git diff execution for workspace '$workspace', baseCommit '$baseCommit'" }

        val gitLsTreeCommand = "git diff -U0 <base-commit>..<end-commit> <path-placeholder>".split(SPACE_PATTERN)
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()

        gitLsTreeCommand[gitLsTreeCommand.size - 3] = baseCommit
        gitLsTreeCommand[gitLsTreeCommand.size - 2] = endCommit ?: "HEAD"
        gitLsTreeCommand[gitLsTreeCommand.size - 1] = workspace

        val processBuilder = ProcessBuilder(*gitLsTreeCommand)
        var exitCode = -1

        processBuilder.directory(File(workspace))
        processBuilder.redirectErrorStream(true)

        val process = processBuilder.start()
        val inputStream = process.inputStream

        val terminalOutput = ArrayList<String>()

        try {
            inputStream.use {
                val reader = BufferedReader(InputStreamReader(it))
                reader.use {
                    terminalOutput.addAll(reader.readLines())
                    exitCode = process.waitFor()
                }
            }
        } catch (e: Exception) {
            log.error { "Verify if the path exists and there are correct permissions to it: '$workspace'" }
            log.error(e) { "Reading non GIT initiated repository or GIT processing exception has has occurred" }
            log.error(e) { "Process exit code was: $exitCode" }
        }

        return terminalOutput
    }

    companion object {
        val SPACE_PATTERN = "\\s".toRegex()
        private val log = KotlinLogging.logger {}
    }
}
