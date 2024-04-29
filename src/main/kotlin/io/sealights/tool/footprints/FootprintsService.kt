package io.sealights.tool.footprints

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.LineCoverage
import io.sealights.tool.Method
import io.sealights.tool.MethodLineCoverage

class FootprintsService {
    fun appendLineExecutionData(it: Map<FileName, Method>): Either<Error, Map<FileName, Set<MethodLineCoverage>>> {
        return Either.Right(
            mapOf(
                "file001" to setOf(
                    MethodLineCoverage(
                        "method", setOf(LineCoverage(12, true))
                    )
                )
            )
        )
    }

}
