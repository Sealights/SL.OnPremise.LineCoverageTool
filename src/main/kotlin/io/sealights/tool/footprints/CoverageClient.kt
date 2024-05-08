package io.sealights.tool.footprints

import io.sealights.tool.FileName
import io.sealights.tool.UniqueMethodId
import kotlin.random.Random

class CoverageClient {
    fun fetchCoverage(appName: String, branchName: String, buildName: String, physicalPaths: Set<FileName>): Map<UniqueMethodId, Set<Int>> {
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
}