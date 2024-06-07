package io.sealights.tool.buildmap

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.Line
import io.sealights.tool.LineList
import io.sealights.tool.MethodLines
import io.sealights.tool.ScannedMethod

class BuildLineService(
    private val buildLinesClient: BuildLinesClient
) {
    fun mergeMethodNames(gitModifiedLines: Map<FileName, LineList>, appName: String, branchName: String, buildName: String): Either<Error, Map<FileName, List<MethodLines>>> {
        val sourceFile2MethodsMapping = buildLinesClient.getMethodsForFiles(gitModifiedLines.keys, appName, branchName, buildName)

        val mapped = gitModifiedLines
            .filterKeys { fileName ->
                sourceFile2MethodsMapping.containsKey(fileName)
            }
            .mapValues { (fileName, lineList) ->
                createMethodWithLines(sourceFile2MethodsMapping[fileName]!!, lineList)
            }
            .mapValues { mapEntry ->
                removeUnchangedMethods(mapEntry.value)
            }
            .toMap()
        return Either.Right(mapped)

    }

    private fun removeUnchangedMethods(methodLines: List<MethodLines>): List<MethodLines> {
        return methodLines.filter { methodLine ->
            run {
                methodLine.lines
                    .map { line -> line.modified }
                    .reduce { acc, modified -> acc || modified }
            }
        }.toList()
    }

    private fun createMethodWithLines(scannedMethods: List<ScannedMethod>, modifiedLinesList: List<Line>): List<MethodLines> {
        return scannedMethods
            .filter { scannedMethod -> scannedMethod.end >= scannedMethod.start }
            .map { scannedMethod ->
                MethodLines(
                    name = scannedMethod.name,
                    lines = createLineListForMethod(scannedMethod.start, scannedMethod.end, modifiedLinesList)
                )
            }.toList()
    }

    private fun createLineListForMethod(start: Int, end: Int, modifiedLinesList: List<Line>): MutableList<Line> {
        return IntRange(start, end).map { lineNumber ->
            val isLineModified = modifiedLinesList.any { line ->
                line.number == lineNumber
            }
            Line(lineNumber, isLineModified, "")
        }.toMutableList()
    }

}
