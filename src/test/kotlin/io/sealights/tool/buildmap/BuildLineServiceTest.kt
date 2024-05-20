package io.sealights.tool.buildmap

import arrow.core.getOrElse
import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.sealights.tool.FileName
import io.sealights.tool.Line
import io.sealights.tool.MethodLines
import io.sealights.tool.MethodName
import io.sealights.tool.ScannedMethod
import kotlin.test.assertEquals

class BuildLineServiceTest : ShouldSpec({
    should("should merge git diff with build scan data") {
        // given
        val buildLinesClientMock = mockk<BuildLinesClient>()
        every { buildLinesClientMock.getMethodsForFiles(any()) } returns mockedBuildMap()
        val buildLineService = BuildLineService(buildLinesClientMock)

        // and changed detected lines from git
        val gitBasedModifiedLines = prepareGitBasedModifiedLines()

        // when
        val actualMergedMethodNamesEither = buildLineService.mergeMethodNames(gitBasedModifiedLines)

        // then
        assert(actualMergedMethodNamesEither.isRight())
        val actualMergedMethodNames = actualMergedMethodNamesEither.getOrElse { mapOf() }

        // and then, four physical files left after processing 
        assertEquals(4, actualMergedMethodNames.size)

        // and then, methodDisplay names nad uniqueIds are correct
        assertValidUniqueIdAndDisplayNames(actualMergedMethodNames)

    }
}) {
    companion object {
        private fun mockedBuildMap(): Map<FileName, List<ScannedMethod>> {
            return mapOf(
                "src/main/java/dev/futa/exec/NewReplicaMainJavaExecClass.java" to listOf(
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public V dev.futa.exec.NewReplicaMainJavaExecClass.<init>[] | ()V | null",
                            displayName = "public NewReplicaMainJavaExecClass()"
                        ), 3, 3
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.main[[Ljava/lang/String;] | ([Ljava/lang/String;)V | null",
                            displayName = "public static void main(String[])"
                        ), 5, 9
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.main2[[Ljava/lang/String;] | ([Ljava/lang/String;)V | null",
                            displayName = "public static void main2(String[])"
                        ), 12, 13
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.main3[[Ljava/lang/String;] | ([Ljava/lang/String;)V | null",
                            displayName = "public static void main3(String[])"
                        ), 16, 17
                    )
                ), "src/main/java/dev/futa/exec/NewUtil.java" to listOf(
                    ScannedMethod(
                        MethodName(
                            uniqueId = "9 | public static Z dev.futa.exec.NewUtil.alwaysTrue[] | ()Z | null",
                            displayName = "public static boolean alwaysTrue()"
                        ),
                        5,
                        5
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public V dev.futa.exec.NewUtil.<init>[] | ()V | null",
                            displayName = "public NewUtil()"
                        ),
                        3,
                        3
                    ),
                ), "src/main/java/dev/futa/skipping/Aaaa05.java" to listOf(
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.newlyAddedMethod[I, I] | (II)Ljava/lang/String; | null",
                            displayName = "public String newlyAddedMethod(int, int)"
                        ), 35, 40
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method003[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method003(int)"
                        ), 18, 24
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method005[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method005(int)"
                        ), 28, 30
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method002[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method002(int)"
                        ), 12, 14
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method001[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method001(int)"
                        ), 6, 8
                    ),
                    ScannedMethod(MethodName(uniqueId = "1 | public V dev.futa.skipping.Aaaa05.<init>[] | ()V | null", displayName = "public Aaaa05()"), 3, 3),
                ), "src/main/java/dev/futa/skipping/Bbbb05.java" to listOf(
                    ScannedMethod(
                        MethodName(uniqueId = "1 | public V dev.futa.skipping.Bbbb05.<init>[] | ()V | null", displayName = "public Bbbb05()"),
                        7, 7
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.modifiedLongSignatureMethod[I, Ljava/lang/String;, Ljava/util/Set;] | (ILjava/lang/String;Ljava/util/Set;)Ljava/lang/String; | null",
                            displayName = "public String modifiedLongSignatureMethod(int, String, Set)"
                        ), 37, 42
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method003[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method003(int)"
                        ), 22, 24
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method004[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method004(int)"
                        ), 28, 30
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method002[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method002(int)"
                        ), 16, 18
                    ),
                    ScannedMethod(
                        MethodName(
                            uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method001[I] | (I)Ljava/lang/String; | null",
                            displayName = "public String method001(int)"
                        ), 10, 12
                    )
                )
            )
        }
    }
}

private fun assertValidUniqueIdAndDisplayNames(actualMergedMethodNames: Map<FileName, List<MethodLines>>) {
    assertEquals(
        setOf(
            MethodName(
                uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.newlyAddedMethod[I, I] | (II)Ljava/lang/String; | null",
                displayName = "public String newlyAddedMethod(int, int)"
            ),
            MethodName(
                uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method003[I] | (I)Ljava/lang/String; | null",
                displayName = "public String method003(int)"
            ),
            MethodName(
                uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method005[I] | (I)Ljava/lang/String; | null",
                displayName = "public String method005(int)"
            ),
        ),
        actualMergedMethodNames["src/main/java/dev/futa/skipping/Aaaa05.java"]?.extractMethodNames()
    )

    assertEquals(
        setOf(
            MethodName(
                uniqueId = "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.modifiedLongSignatureMethod[I, Ljava/lang/String;, Ljava/util/Set;] | (ILjava/lang/String;Ljava/util/Set;)Ljava/lang/String; | null",
                displayName = "public String modifiedLongSignatureMethod(int, String, Set)"
            )
        ),
        actualMergedMethodNames["src/main/java/dev/futa/skipping/Bbbb05.java"]?.extractMethodNames()
    )

    assertEquals(
        setOf(
            MethodName(
                uniqueId = "9 | public static Z dev.futa.exec.NewUtil.alwaysTrue[] | ()Z | null",
                displayName = "public static boolean alwaysTrue()"
            ),
            MethodName(
                uniqueId = "1 | public V dev.futa.exec.NewUtil.<init>[] | ()V | null",
                displayName = "public NewUtil()"
            )
        ),
        actualMergedMethodNames["src/main/java/dev/futa/exec/NewUtil.java"]?.extractMethodNames()
    )

    assertEquals(
        setOf(
            MethodName(
                uniqueId = "1 | public V dev.futa.exec.NewReplicaMainJavaExecClass.<init>[] | ()V | null",
                displayName = "public NewReplicaMainJavaExecClass()"
            ),
            MethodName(
                uniqueId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.main2[[Ljava/lang/String;] | ([Ljava/lang/String;)V | null",
                displayName = "public static void main2(String[])"
            ),
            MethodName(
                uniqueId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.main3[[Ljava/lang/String;] | ([Ljava/lang/String;)V | null",
                displayName = "public static void main3(String[])"
            ),
            MethodName(
                uniqueId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.main[[Ljava/lang/String;] | ([Ljava/lang/String;)V | null",
                displayName = "public static void main(String[])"
            ),
        ),
        actualMergedMethodNames["src/main/java/dev/futa/exec/NewReplicaMainJavaExecClass.java"]?.extractMethodNames()
    )
}

private fun List<MethodLines>.extractMethodNames(): Set<MethodName> = this.map { ml -> ml.name }.toSet()

private fun prepareGitBasedModifiedLines(): Map<FileName, MutableList<Line>> {

    val Aaaa05JavaLines = createModifiedLinesArray(20, 21, 22, 23, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39)
    val Bbbb05JavaLines = createModifiedLinesArray(3, 4, 5, 6, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42)
    val NewUtilJavaLines = createModifiedLinesArray(1, 2, 3, 4, 5, 6, 7)
    val NewReplicaMainJavaExecClassJavaLines = createModifiedLinesArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18)

    return mapOf(
        "src/main/java/dev/futa/skipping/Aaaa05.java" to mutableListOf(
            *Aaaa05JavaLines
        ),
        "src/main/java/dev/futa/skipping/Bbbb05.java" to mutableListOf(
            *Bbbb05JavaLines
        ),
        "src/main/java/dev/futa/exec/NewUtil.java" to mutableListOf(
            *NewUtilJavaLines
        ),
        "src/main/java/dev/futa/exec/NewReplicaMainJavaExecClass.java" to mutableListOf(
            *NewReplicaMainJavaExecClassJavaLines
        )
    )
}

fun createModifiedLinesArray(vararg lineNumber: Int): Array<Line> = lineNumber
    .map { number -> Line(number, true, "") }
    .toTypedArray()
