package io.sealights.tool

import arrow.core.flatMap
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import io.sealights.tool.build.BuildLineService
import io.sealights.tool.build.BuildLinesClient
import io.sealights.tool.configuration.ApplicationArgParser
import io.sealights.tool.configuration.Configuration
import io.sealights.tool.footprints.CoverageClient
import io.sealights.tool.footprints.FootprintsService
import io.sealights.tool.source.GitDiffProviderService
import io.sealights.tool.source.GitModifiedLineService
import io.sealights.tool.report.ExcelReportFormater
import io.sealights.tool.source.SourceCodeLineReader

fun main(args: Array<String>) = mainBody {
    println("Sealights Line Level Coverage details")

    ArgParser(args).parseInto(::ApplicationArgParser)
        .run {
            println("Token: $token")
            Configuration.build(this)
        }

    println(Configuration.workspace)

    val gitDiffProviderService = GitDiffProviderService()
    val gitModifiedLineService = GitModifiedLineService(gitDiffProviderService)
    val buildLineService = BuildLineService(BuildLinesClient())
    val footprintsService = FootprintsService(CoverageClient())
    val excelReportFormatter = ExcelReportFormater()
    val sourceCodeLine = SourceCodeLineReader()
    
    val coverageTool = CoverageTool(
        gitModifiedLineService,
        buildLineService,
        footprintsService,
        excelReportFormatter,
        sourceCodeLine
    )

    coverageTool.run()

    println("Sealights Line Level Coverage end.")
}

class CoverageTool(
    private val gitLinesService: GitModifiedLineService,
    private val buildLineService: BuildLineService,
    private val footprintsService: FootprintsService,
    private val excelReportFormatter: ExcelReportFormater,
    private val sourceCodeLine: SourceCodeLineReader
) {
    fun run() {
        gitLinesService.modifiedFileLines(Configuration.workspace, Configuration.startCommit, "HEAD")
            .flatMap { buildLineService.mergeMethodNames(it) }
            .flatMap { sourceCodeLine.attacheLineContent(it) }
            .flatMap { footprintsService.appendLineExecutionData(it) }
            .flatMap { excelReportFormatter.createReport(it) }
    }
}

