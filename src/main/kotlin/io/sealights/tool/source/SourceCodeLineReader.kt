package io.sealights.tool.source

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.MethodLines
import java.io.File

class SourceCodeLineReader {
    fun attacheLineContent(methodLinesMap: Map<FileName, List<MethodLines>>): Either<Error, Map<FileName, List<MethodLines>>> {

        methodLinesMap.forEach { methodLinesEntry ->
            run {
                val fileLines = readFileLines(methodLinesEntry.key)
                methodLinesEntry.value.forEach { methodLine ->
                    methodLine.lines.forEach { line ->
                        line.content = getLineContent(fileLines, line.number)
                    }
                }
            }
        }

        return Either.Right(methodLinesMap)
    }

    private fun getLineContent(fileLines: List<String>, number: Int) = 
        fileLines.getOrElse(number - 1) { idx -> "--- line: $idx ---" }


    private fun readFileLines(physicalPath: FileName): List<String> {
        val file = File(physicalPath)
        val content = mutableListOf<String>()

        if (file.exists()) {
            file.forEachLine { line ->
                content.add(line)
            }
        }

        return content
    }
}