package io.sealights.tool.build

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.Line
import io.sealights.tool.LineList
import io.sealights.tool.Method

class BuildLineService (
    val buildLinesClient: BuildLinesClient
) {
    fun mergeMethodNames(it: Map<FileName, LineList>): Either<Error, Map<FileName, Method>> {
        println(it)
        
        val mapped = it.mapValues { (_, lineList) ->
            createMethodWithLines(lineList)
        }.toMap()
        
        return Either.Right(mapped)

    }

    private fun createMethodWithLines(lineList: MutableList<Line>): Method {
        val method = Method("asd", lineList)
        return method;
    }

}
