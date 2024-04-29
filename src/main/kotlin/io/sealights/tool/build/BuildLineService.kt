package io.sealights.tool.build

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.LineList
import io.sealights.tool.Method

class BuildLineService {
    fun filter(it: Map<FileName, LineList>): Either<Error, Map<FileName, Method>> {
        TODO("Not yet implemented")
    }
}
