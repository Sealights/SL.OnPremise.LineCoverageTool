package io.sealights.tool.footprints

import arrow.core.Either
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.sealights.tool.HttpClient
import io.sealights.tool.UniqueMethodId
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class CoverageClientTest : ShouldSpec({
    should("Should fetch and transform fetched lines coverage data") {
        // given
        val httpClientMock = mockk<HttpClient>()
        every { httpClientMock.post(any(), any(), any()) } returns fileAsListOfStrings("responses/coverage.json")
        val coverageClient = CoverageClient(httpClientMock)

        val coverageForFiles = listOf(
            "src/main/java/dev/futa/exec/NewReplicaMainJavaExecClass.java",
            "src/main/java/dev/futa/skipping/Bbbb05.java",
            "src/main/java/dev/futa/skipping/Aaaa05.java",
            "src/main/java/dev/futa/exec/NewUtil.java"
        )

        // when
        val actualMethods = coverageClient.fetchCoverage(appName = "appName", branchName = "branchName", buildName = "buildName", physicalPaths = coverageForFiles)

        // then - HTTP client is called
        verify {
            httpClientMock.post(
                url = "v5/agents/footprints/appName/branchName/buildName/queryLineHits",
                payload = "[\"src/main/java/dev/futa/exec/NewReplicaMainJavaExecClass.java\",\"src/main/java/dev/futa/skipping/Bbbb05.java\",\"src/main/java/dev/futa/skipping/Aaaa05.java\",\"src/main/java/dev/futa/exec/NewUtil.java\"]",
                queryParams = mapOf()
            )
        }

        // and - expected valid codeElementIds
        assertContentEquals(expectedLIneCoverage().keys.toList(), actualMethods.keys.toList())

        // and - expected lines has been hit
        expectedLIneCoverage().entries.forEach { expectedEntry ->
            assertContentEquals(expectedEntry.value.toList(), actualMethods[expectedEntry.key], "Lines hits of '${expectedEntry.key}' should be valid")
        }
        
    }
}) {

    companion object {
        private fun fileAsListOfStrings(inputFile: String): Either<String, String> {
            val resource = object {}.javaClass.classLoader.getResource(inputFile)
            return Either.Right(String(Files.readAllBytes(Paths.get(resource.toURI()))))
        }

        fun expectedLIneCoverage(): Map<UniqueMethodId, Set<Int>> {
            return mapOf(
                "9 | public static Z dev.futa.exec.NewUtil.alwaysTrue[] | ()Z | null" to setOf(5),
                "1 | public V dev.futa.exec.NewUtil.<init>[] | ()V | null" to setOf(3),
                "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.newlyAddedMethod[I, I] | (II)Ljava/lang/String; | null" to setOf(36),
                "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method003[I] | (I)Ljava/lang/String; | null" to setOf(35, 36, 37, 40),
                "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method005[I] | (I)Ljava/lang/String; | null" to setOf(),
                "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method002[I] | (I)Ljava/lang/String; | null" to setOf(),
                "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method001[I] | (I)Ljava/lang/String; | null" to setOf(),
                "1 | public V dev.futa.skipping.Aaaa05.<init>[] | ()V | null" to setOf(),
                "1 | public V dev.futa.skipping.Bbbb05.<init>[] | ()V | null" to setOf(),
                "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.modifiedLongSignatureMethod[I, Ljava/lang/String;, Ljava/util/Set;] | (ILjava/lang/String;Ljava/util/Set;)Ljava/lang/String; | null" to setOf(),
                "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method003[I] | (I)Ljava/lang/String; | null" to setOf(),
                "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method004[I] | (I)Ljava/lang/String; | null" to setOf(),
                "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method002[I] | (I)Ljava/lang/String; | null" to setOf(),
                "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method001[I] | (I)Ljava/lang/String; | null" to setOf(),
            )
        }
    }

}