package io.sealights.tool.build

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.Line
import io.sealights.tool.LineList
import io.sealights.tool.MethodLines
import io.sealights.tool.ScannedMethod

class BuildLineService(
    private val buildLinesClient: BuildLinesClient
) {
    fun mergeMethodNames(gitModifiedLines: Map<FileName, LineList>): Either<Error, Map<FileName, List<MethodLines>>> {
        val methodsForFiles = buildLinesClient.getMethodsForFiles(gitModifiedLines.keys)

        val mapped = gitModifiedLines
            .filterKeys { key ->
                methodsForFiles.containsKey(key)
            }
            .mapValues { (key, lineList) ->
                createMethodWithLines(methodsForFiles[key]!!, lineList)
            }
            .toMap()

        return Either.Right(mapped)

    }

    private fun createMethodWithLines(scannedMethods: List<ScannedMethod>, modifiedLinesList: List<Line>): List<MethodLines> {
        return scannedMethods
            .filter { scannedMethod -> scannedMethod.end > scannedMethod.start }
            .map { scannedMethod ->
                MethodLines(scannedMethod.name, createLineListForMethod(scannedMethod.start, scannedMethod.end, modifiedLinesList))
            }.toList()
    }

    private fun createLineListForMethod(start: Int, end: Int, modifiedLinesList: List<Line>): MutableList<Line> {
        return MutableList(end - start - 1) { lineNumber ->
            val isLineModified = modifiedLinesList.any { line ->
                line.number == lineNumber
            }
            Line(lineNumber, isLineModified, "")
        }
    }

}
