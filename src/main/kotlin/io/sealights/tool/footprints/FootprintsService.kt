package io.sealights.tool.footprints

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.LineWithCoverage
import io.sealights.tool.MethodLines
import io.sealights.tool.MethodLinesWithCoverage
import io.sealights.tool.MethodName

class FootprintsService {
    fun appendLineExecutionData(it: Map<FileName, List<MethodLines>>): Either<Error, Map<FileName, Set<MethodLinesWithCoverage>>> {
        return Either.Right(
            mapOf(
                "file001" to setOf(
                    MethodLinesWithCoverage(
                        MethodName(uniqueId = "uniqieid", displayName = "displayName"),
                        listOf(LineWithCoverage(number = 12, modified = true, covered = true, content = ""))
                    )
                )
            )
        )
    }

}
