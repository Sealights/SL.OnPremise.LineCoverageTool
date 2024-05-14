package io.sealights.tool.buildmap

import io.kotest.core.spec.style.ShouldSpec
import io.mockk.every
import io.mockk.mockk
import io.sealights.tool.FileName
import io.sealights.tool.MethodName
import io.sealights.tool.ScannedMethod

class BuildLineServiceTest : ShouldSpec({
    should("should merge git diff with build scan data") {
        // given
        val buildLinesClientMock = mockk<BuildLinesClient>()
        every { buildLinesClientMock.getMethodsForFiles(any()) } returns mockedBuildMap()
        val buildLineService = BuildLineService(buildLinesClientMock)

//        buildLineService.mergeMethodNames ()
        assert(true)
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