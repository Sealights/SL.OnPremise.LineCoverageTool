package io.sealights.tool.footprints

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.Line
import io.sealights.tool.LineWithCoverage
import io.sealights.tool.MethodLines
import io.sealights.tool.MethodLinesWithCoverage
import io.sealights.tool.UniqueMethodId

class FootprintsService(
    private val coverageClient: CoverageClient
) {
    fun appendLineExecutionData(methodLinesMap: Map<FileName, List<MethodLines>>): Either<Error, Map<FileName, Set<MethodLinesWithCoverage>>> {
        val methodLineCoverage = coverageClient.fetchCoverage("appName", "branch", "buildName", methodLinesMap.keys.toSet())

        return Either.Right(methodLinesMap.mapValues { methodLines ->
            methodLines.value.map { methodLine ->
                MethodLinesWithCoverage(
                    name = methodLine.name,
                    linesCoverage = lineToLineWithCoverage(methodLineCoverage, methodLine.name.uniqueId, methodLine.lines)
                )
            }.toSet()
        })
    }

    private fun lineToLineWithCoverage(methodLineCoverage: Map<UniqueMethodId, Set<Int>>, uniqueId: String, lines: MutableList<Line>): List<LineWithCoverage> {
        return lines.map { line ->
            LineWithCoverage(
                number = line.number,
                covered = methodLineCoverage[uniqueId]?.contains(line.number) ?: false,
                modified = line.modified,
                content = line.content
            )
        }
    }

}
