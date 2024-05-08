package io.sealights.tool.report

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.MethodLinesWithCoverage

class ExcelReportFormatter {
    fun createReport(dataToProcess: Map<FileName, Set<MethodLinesWithCoverage>>): Either<Error, String> {
        return Either.Right("SUCCESS")
    }
}