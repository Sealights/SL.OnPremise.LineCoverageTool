package io.sealights.tool.git

import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.sealights.tool.source.GitDiffProviderService
import io.sealights.tool.source.GitModifiedLineService
import java.io.BufferedReader
import java.io.InputStreamReader

class GitModifiedLineServiceTest : ShouldSpec({

    should("fix-me-or-implement") {
        // given
        val gitDiffProviderServiceMock = mockk<GitDiffProviderService>()
        val gitModifiedLineService = GitModifiedLineService(gitDiffProviderServiceMock)

        every { gitDiffProviderServiceMock.gitDiffOutput(workspace = any(), baseCommit = any(), endCommit = any()) } returns asd("git-diffs/git-diff-example-output.txt")

        // when
        val modifiedFileLines = gitModifiedLineService.modifiedFileLines(workspace = ".", startCommitHash = "any", endCommitHash = "HEAD")

    }

}) {
    companion object {
        private fun asd(inputFile: String): List<String> {
//            val inputStream = object {}.javaClass.getResourceAsStream(inputFile)
            val inputStream = object {}.javaClass.classLoader.getResourceAsStream(inputFile)

            return inputStream?.use {
                val reader = BufferedReader(InputStreamReader(it))
                reader.readLines()
            } ?: throw IllegalArgumentException("File not found or unable to read the file: '$inputFile'")

        }
    }
}