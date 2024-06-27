package io.sealights.tool.buildmap

import arrow.core.Either
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.sealights.tool.FileName
import io.sealights.tool.HttpClient
import io.sealights.tool.MethodName
import io.sealights.tool.ScannedMethod
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertContentEquals

class BuildLinesClientTest : ShouldSpec({
    should("Should fetch and transform fetched buildmap data") {
        // given
        val httpClientMock = mockk<HttpClient>()
        every { httpClientMock.post(any(), any(), any()) } returns fileAsListOfStrings("responses/buildmap.json")
        val buildLinesClient = BuildLinesClient(httpClientMock)

        val filesInBuildMap = listOf(
            "src/main/java/dev/futa/exec/NewReplicaMainJavaExecClass.java",
            "src/main/java/dev/futa/skipping/Bbbb05.java",
            "src/main/java/dev/futa/skipping/Aaaa05.java",
            "src/main/java/dev/futa/exec/NewUtil.java"
        )


        // when
        val actualMethods = buildLinesClient.getMethodsForFiles(filesInBuildMap, "123-buildSessionId")

        // then - HTTP client is called
        verify {
            httpClientMock.post(
                url = "v5/agents/builds/123-buildSessionId/queryModifiedMethods",
                payload = "[\"src/main/java/dev/futa/exec/NewReplicaMainJavaExecClass.java\",\"src/main/java/dev/futa/skipping/Bbbb05.java\",\"src/main/java/dev/futa/skipping/Aaaa05.java\",\"src/main/java/dev/futa/exec/NewUtil.java\"]",
                queryParams = mapOf()
            )
        }

        // and - expected content is returned
        filesInBuildMap.forEach { filename ->
            assertContentEquals(
                expectedScannedMethods()[filename],
                actualMethods[filename],
                "Invalid content for file: $filename"
            )
        }
    }
}) {

    companion object {
        private fun fileAsListOfStrings(inputFile: String): Either<String, String> {
            val resource = object {}.javaClass.classLoader.getResource(inputFile)
            return Either.Right(String(Files.readAllBytes(Paths.get(resource.toURI()))))
        }

        fun expectedScannedMethods(): Map<FileName, List<ScannedMethod>> {
            return mapOf(
                "src/main/java/dev/futa/exec/NewReplicaMainJavaExecClass.java" to listOf(
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public V dev.futa.exec.NewReplicaMainJavaExecClass.<init>[] | ()V | null",
                            displayName = "public NewReplicaMainJavaExecClass()"
                        ), 3, 3
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.main[[Ljava/lang/String;] | ([Ljava/lang/String;)V | null",
                            displayName = "public static void main(String[])"
                        ), 5, 9
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.main2[[Ljava/lang/String;] | ([Ljava/lang/String;)V | null",
                            displayName = "public static void main2(String[])"
                        ), 12, 13
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.main3[[Ljava/lang/String;] | ([Ljava/lang/String;)V | null",
                            displayName = "public static void main3(String[])"
                        ), 16, 17
                    ),
//                    ScannedMethod(
//                        MethodName(
//                            codeElementId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.commentsMethod[] | ()V | null",
//                            displayName = "public static void commentsMethod()"
//                        ), 21, 50
//                    )
                ), "src/main/java/dev/futa/exec/NewUtil.java" to listOf(
                    ScannedMethod(
                        MethodName(
                            codeElementId = "9 | public static Z dev.futa.exec.NewUtil.alwaysTrue[] | ()Z | null",
                            displayName = "public static boolean alwaysTrue()"
                        ),
                        5,
                        5
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public V dev.futa.exec.NewUtil.<init>[] | ()V | null",
                            displayName = "public NewUtil()"
                        ),
                        3,
                        3
                    ),
                ), "src/main/java/dev/futa/skipping/Aaaa05.java" to listOf(
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.newlyAddedMethod[I, I] | (II)Ljava/lang/String; | null",
                            displayName = "public String newlyAddedMethod(int, int)"
                        ), 35, 40
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method003[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method003(int)"
                        ), 18, 24
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method005[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method005(int)"
                        ), 28, 30
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method002[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method002(int)"
                        ), 12, 14
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method001[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method001(int)"
                        ), 6, 8
                    ),
                    ScannedMethod(MethodName(codeElementId = "1 | public V dev.futa.skipping.Aaaa05.<init>[] | ()V | null", displayName = "public Aaaa05()"), 3, 3),
                ), "src/main/java/dev/futa/skipping/Bbbb05.java" to listOf(
                    ScannedMethod(
                        MethodName(codeElementId = "1 | public V dev.futa.skipping.Bbbb05.<init>[] | ()V | null", displayName = "public Bbbb05()"),
                        7, 7
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.modifiedLongSignatureMethod[I, Ljava/lang/String;, Ljava/util/Set;] | (ILjava/lang/String;Ljava/util/Set;)Ljava/lang/String; | null",
                            displayName = "public String modifiedLongSignatureMethod(int, String, Set)"
                        ), 37, 42
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method003[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method003(int)"
                        ), 22, 24
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method004[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method004(int)"
                        ), 28, 30
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method002[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method002(int)"
                        ), 16, 18
                    ),
                    ScannedMethod(
                        MethodName(
                            codeElementId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method001[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method001(int)"
                        ), 10, 12
                    )
                )
            )
        }

    }
}