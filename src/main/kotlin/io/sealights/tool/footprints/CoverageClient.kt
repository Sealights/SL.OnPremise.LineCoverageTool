package io.sealights.tool.footprints

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrElse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.sealights.tool.ApplicationProcess
import io.sealights.tool.FileName
import io.sealights.tool.HttpClient
import io.sealights.tool.UniqueMethodId
import mu.KotlinLogging
import kotlin.random.Random

class CoverageClient(private val httpClient: HttpClient) {

    fun fetchCoverage(appName: String, branchName: String, buildName: String, physicalPaths: List<FileName>): Map<UniqueMethodId, List<Int>> {
        val buildMap = httpClient.post(
            url = "v5/agents/footprints/$appName/$branchName/$buildName/queryLineHits",
            payload = physicalPaths.joinToString(",", "[", "]") { file -> "\"$file\"" },
            queryParams = mapOf()
        )

        return buildMap.flatMap { unmarshallResponse(it) }
            .flatMap { transformToDomainModel(it) }
            .mapLeft(ApplicationProcess::handleExit)
            .getOrElse { mapOf() }
    }

    private fun transformToDomainModel(codeElementLineHitsList: List<MethodElementLineHits>) = Either.Right(
        codeElementLineHitsList.groupBy { it.codeElementId }
            .mapValues { entry -> entry.value.map { it.lineHits } }
            .mapValues { entry -> entry.value.flatten().sorted() }
    )


    private fun unmarshallResponse(responseBody: String): Either<String, List<MethodElementLineHits>> {
        log.info { "processing line hits response" }
        log.debug { "Data fetched: $responseBody" }

        val type = object : TypeToken<List<MethodElementLineHits>>() {}.type
        val mappedResponse = Gson().fromJson<List<MethodElementLineHits>>(responseBody, type)

        return Either.Right(mappedResponse)
    }

    fun fetchCoverage2(appName: String, branchName: String, buildName: String, physicalPaths: List<FileName>): Map<UniqueMethodId, Set<Int>> {
        return mapOf(
            "9 | public static Z dev.futa.exec.NewUtil.alwaysTrue[] | ()Z | null" to randomNumbersFromRange(5, 5),
            "1 | public V dev.futa.exec.NewUtil.<init>[] | ()V | null" to randomNumbersFromRange(3, 3),
            "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.newlyAddedMethod[I, I] | (II)Ljava/lang/String; | null" to randomNumbersFromRange(36, 37),
            "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.newlyAddedMethod[I, I] | (II)Ljava/lang/String; | null" to randomNumbersFromRange(35, 40),
            "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method003[I] | (I)Ljava/lang/String; | null" to randomNumbersFromRange(18, 24),
            "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method005[I] | (I)Ljava/lang/String; | null" to randomNumbersFromRange(28, 30),
            "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method002[I] | (I)Ljava/lang/String; | null" to randomNumbersFromRange(12, 14),
            "1 | public Ljava/lang/String; dev.futa.skipping.Aaaa05.method001[I] | (I)Ljava/lang/String; | null" to randomNumbersFromRange(6, 8),
            "1 | public V dev.futa.skipping.Aaaa05.<init>[] | ()V | null" to randomNumbersFromRange(3, 3),
            "1 | public V dev.futa.skipping.Bbbb05.<init>[] | ()V | null" to randomNumbersFromRange(7, 7),
            "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.modifiedLongSignatureMethod[I, Ljava/lang/String;, Ljava/util/Set;] | (ILjava/lang/String;Ljava/util/Set;)Ljava/lang/String; | null" to randomNumbersFromRange(
                37,
                42
            ),
            "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method003[I] | (I)Ljava/lang/String; | null" to randomNumbersFromRange(22, 24),
            "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method004[I] | (I)Ljava/lang/String; | null" to randomNumbersFromRange(28, 30),
            "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method002[I] | (I)Ljava/lang/String; | null" to randomNumbersFromRange(16, 18),
            "1 | public Ljava/lang/String; dev.futa.skipping.Bbbb05.method001[I] | (I)Ljava/lang/String; | null" to randomNumbersFromRange(10, 12),
        )
    }

    private fun randomNumbersFromRange(startInclusive: Int, endInclusive: Int): Set<Int> {
        return (startInclusive..endInclusive)
            .filter { Random.nextDouble(0.0, 1.0) > 0.80 }
            .toSet()
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}

private data class MethodElementLineHits(
    val codeElementId: String,
    val lineHits: List<Int>
)
