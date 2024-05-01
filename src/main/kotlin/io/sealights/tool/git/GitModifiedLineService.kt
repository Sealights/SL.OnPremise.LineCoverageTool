package io.sealights.tool.git

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.Line
import io.sealights.tool.LineList
import io.sealights.tool.git.LineDetectResult.GIT_DIFF_LINE
import io.sealights.tool.git.LineDetectResult.LINE_DIFF_LINE
import mu.KotlinLogging
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.regex.Matcher
import java.util.regex.Pattern

internal enum class LineDetectResult {
    GIT_DIFF_LINE,
    LINE_DIFF_LINE,
    UNEXPECTED_LINE
}

class GitModifiedLineService(
    private val gitDiffProviderService: GitDiffProviderService,
) {
    fun modifiedFileLines(
        workspace: String,
        startCommitHash: String,
        endCommitHash: String?
    ): Either<Error, Map<FileName, LineList>> {
        val gitDiffOutput = gitDiffProviderService.gitDiffOutput(workspace, startCommitHash, "HEAD")
        log.info { "==== GIT DIFF OUTPUT START\n${gitDiffOutput.joinToString("\n")}\n==== GIT DIFF OUTPUT END" }

        var currentFile: String? = null
        val result = HashMap<FileName, LineList>()
        
        for (diffOutputLine in gitDiffOutput) {
            val sectionLineDetectResult = detectLine(diffOutputLine)
            when (sectionLineDetectResult) {
                GIT_DIFF_LINE -> currentFile = addNewFile(result, diffOutputLine)
                LINE_DIFF_LINE -> addChangedLines(result, diffOutputLine, currentFile!!)
                else -> {}
            }
        }

        return Either.Right(result)
    }

    private fun addChangedLines(result: MutableMap<FileName, LineList>, diffOutputLine: String, currentFile: String) {
        val matcher: Matcher = CHANGED_LINES_PATTERN.matcher(diffOutputLine)
        if (matcher.matches()) {
            val linesChangedInfo = matcher.group(2)
            val changedLinesNumbers = convertChangedLines(linesChangedInfo)
            if (changedLinesNumbers.isNotEmpty()) {
                result[currentFile]!!.addAll(changedLinesNumbers)
            }
        }
    }

    private fun convertChangedLines(linesChangedInfo: String): LineList {
        if (!linesChangedInfo.startsWith("+")) {
            return mutableListOf()
        }
        val split = linesChangedInfo.substring(1)
            .split(",".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()


        if (split.size == 1) {
            val lines: MutableList<Line> = ArrayList()
            lines.add(Line(split[0].toInt(), true, ""))
            return lines
        }
        if (split.size == 2) {
            val firstLine = split[0].toInt()
            val changedLinesCount = split[1].toInt()
            val lastLine = firstLine + changedLinesCount - 1
            val lines: MutableList<Line> = ArrayList(changedLinesCount)
            for (i in firstLine..lastLine) {
                lines.add(Line(i, true, ""))
            }
            return lines
        }
        return mutableListOf()
    }

    private fun addNewFile(result: MutableMap<FileName, LineList>, diffOutputLine: String): String? {
        val matcher: Matcher = CHANGED_FILES_LINE_PATTERN.matcher(diffOutputLine)
        if (matcher.matches()) {
            val fileName = matcher.group(2)
            result[fileName] = ArrayList()
            return fileName
        }
        return null
    }

    private fun detectLine(diffOutputLine: String): LineDetectResult {
        val matcher: Matcher = CHANGED_FILES_LINE_PATTERN.matcher(diffOutputLine)
        if (matcher.matches()) {
            return GIT_DIFF_LINE
        }
        if (CHANGED_LINES_PATTERN.matcher(diffOutputLine).matches()) {
            return LINE_DIFF_LINE
        }
        return LineDetectResult.UNEXPECTED_LINE
    }

    private fun readLinesFromFile(diffContentFile: File): List<String> {
        try {
            return Files.readAllLines(diffContentFile.toPath())
        } catch (e: IOException) {
            log.error(e) { "Could not read from the + ${diffContentFile.toPath()}" }
            return emptyList()
        }
    }


    companion object {
        private val CHANGED_FILES_LINE_PATTERN = Pattern.compile("diff\\s--git\\sa/(.+) b/(.+)")
        private val CHANGED_LINES_PATTERN = Pattern.compile("@@\\s([+\\-,0-9]+)\\s([+\\-,0-9]+)\\s@@(.*)")
        private val log = KotlinLogging.logger {}
    }
}
