package io.sealights.tool.git

import arrow.core.getOrElse
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.sealights.tool.Line
import io.sealights.tool.source.GitDiffProviderService
import io.sealights.tool.source.GitModifiedLineService
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class GitModifiedLineServiceTest : ShouldSpec({

    should("should parse gif diff content") {
        // given
        val gitDiffProviderServiceMock = mockk<GitDiffProviderService>()
        val gitModifiedLineService = GitModifiedLineService(gitDiffProviderServiceMock)

        every {
            gitDiffProviderServiceMock.gitDiffOutput(
                workspace = any(),
                baseCommit = any(),
                endCommit = any()
            )
        } returns fileAsListOfStrings("git-diffs/git-diff-example-output.txt")

        // when
        val modifiedFileLines = gitModifiedLineService.modifiedFileLines(workspace = ".", startCommitHash = "any", endCommitHash = "HEAD")

        // then
        assert(modifiedFileLines.isRight())

        val modifiedLinesResult = modifiedFileLines.getOrElse { mapOf() }
        assertEquals(7, modifiedLinesResult.size, "should return 7 changed files")

        // and then changed lines numbers are correct for each file
        assertContentEquals(
            listOf(20, 21, 22, 23, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39),
            modifiedLinesResult["src/main/java/dev/futa/skipping/Aaaa05.java"]?.extractLineNumbers()
        )

        assertContentEquals(
            listOf(),
            modifiedLinesResult["new-text-file.txt"]?.extractLineNumbers()
        )

        assertContentEquals(
            listOf(3, 4, 5, 6, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42),
            modifiedLinesResult["src/main/java/dev/futa/skipping/Bbbb05.java"]?.extractLineNumbers()
        )

        assertContentEquals(
            listOf(1, 2, 3, 4, 5, 6, 7),
            modifiedLinesResult["src/main/java/dev/futa/exec/NewUtil.java"]?.extractLineNumbers()
        )

        assertContentEquals(
            listOf(14, 317),
            modifiedLinesResult["pom.xml"]?.extractLineNumbers()
        )

        assertContentEquals(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18),
            modifiedLinesResult["src/main/java/dev/futa/exec/NewReplicaMainJavaExecClass.java"]!!.extractLineNumbers()
        )

        assertContentEquals(
            listOf(2, 9, 15),
            modifiedLinesResult["autodesk2"]?.extractLineNumbers()
        )
        
        // and then each line is marked as modified
        modifiedLinesResult.values.flatten().forAll {line -> 
            line.modified shouldBe true
        }
    }

}) {
    companion object {
        private fun fileAsListOfStrings(inputFile: String): List<String> {
            val inputStream = object {}.javaClass.classLoader.getResourceAsStream(inputFile)

            return inputStream?.use {
                val reader = BufferedReader(InputStreamReader(it))
                reader.readLines()
            } ?: throw IllegalArgumentException("File not found or unable to read the file: '$inputFile'")

        }
    }
}

private fun List<Line>.extractLineNumbers(): List<Int> = this.map { line -> line.number }

