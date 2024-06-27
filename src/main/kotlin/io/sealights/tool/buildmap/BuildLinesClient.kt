package io.sealights.tool.buildmap

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrElse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.sealights.tool.ApplicationProcess
import io.sealights.tool.FileName
import io.sealights.tool.HttpClient
import io.sealights.tool.MethodName
import io.sealights.tool.ScannedMethod
import mu.KotlinLogging

class BuildLinesClient(private val httpClient: HttpClient) {

    fun getMethodsForFiles(physicalPaths: List<String>, buildSessionId: String): Map<FileName, List<ScannedMethod>> {
        
        val buildMap = httpClient.post(
            url = "v5/agents/builds/$buildSessionId/queryModifiedMethods",
            payload = physicalPaths.joinToString(",", "[", "]") { file -> "\"$file\"" },
            queryParams = mapOf()
        )

        return buildMap.flatMap { unmarshallResponse(it) }
            .flatMap { transformToDomainModel(it) }
            .mapLeft(ApplicationProcess::handleExit)
            .getOrElse { mapOf() }
    }

    private fun unmarshallResponse(buildMapResponseBody: String): Either<String, List<BuildMapElement>> {
        log.info { "processing build map response" }
        log.debug { "Data fetched: $buildMapResponseBody" }

        val type = object : TypeToken<List<BuildMapElement>>() {}.type
        val mappedResponse = Gson().fromJson<List<BuildMapElement>>(buildMapResponseBody, type)

        return Either.Right(mappedResponse)
    }

    private fun transformToDomainModel(unmarshalledJson: List<BuildMapElement>): Either<String, Map<FileName, List<ScannedMethod>>> {
        val mapValues = unmarshalledJson.groupBy { it.file }
            .mapValues { entry -> entry.value.map { buildMapElement -> buildMapElement.toScannedMethod() } }
            .mapValues { entry -> entry.value.flatten() }

        return Either.Right(mapValues)
    }

    fun getMethodsForFiles2(physicalPaths: Set<String>, buildSessionId: String): Map<FileName, List<ScannedMethod>> {
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
                ScannedMethod(
                    MethodName(
                        codeElementId = "9 | public static V dev.futa.exec.NewReplicaMainJavaExecClass.commentsMethod[] | ()V | null",
                        displayName = "public static void commentsMethod()"
                    ), 21, 50
                )
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

    companion object {
        private val log = KotlinLogging.logger {}
    }
}

private fun BuildMapElement.toScannedMethod() = methods.map {
    ScannedMethod(
        MethodName(codeElementId = it.codeElementId, displayName = it.displayName),
        start = it.start,
        end = it.end
    )
}


private data class BuildMapElement(
    val file: String,
    val methods: List<Method>
) {
    data class Method(
        val codeElementId: String,
        val displayName: String,
        val start: Int,
        val end: Int
    )
}
