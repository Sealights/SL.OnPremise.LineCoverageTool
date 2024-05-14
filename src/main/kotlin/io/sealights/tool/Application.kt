package io.sealights.tool

import arrow.core.flatMap
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import io.sealights.tool.buildmap.BuildLineService
import io.sealights.tool.buildmap.BuildLinesClient
import io.sealights.tool.configuration.ApplicationArgParser
import io.sealights.tool.configuration.Configuration
import io.sealights.tool.configuration.TokenResolver
import io.sealights.tool.footprints.CoverageClient
import io.sealights.tool.footprints.FootprintsService
import io.sealights.tool.source.GitDiffProviderService
import io.sealights.tool.source.GitModifiedLineService
import io.sealights.tool.report.ExcelReportFormatter
import io.sealights.tool.source.SourceCodeLineReader

fun main(args: Array<String>) = mainBody {
    println("Sealights Line Level Coverage details")

    val tokenResolver = TokenResolver()
    
    ArgParser(args).parseInto(::ApplicationArgParser)
        .run {
            println("Token: $token")
            Configuration.build(this, tokenResolver)
        }

    println(Configuration.workspace)

    val httpClient = HttpClient.build()

    val gitDiffProviderService = GitDiffProviderService()
    val gitModifiedLineService = GitModifiedLineService(gitDiffProviderService)
    val buildLineService = BuildLineService(BuildLinesClient(httpClient))
    val footprintsService = FootprintsService(CoverageClient())
    val excelReportFormatter = ExcelReportFormatter("lineCoverageReport", "Application Name / Develop / Build 4.12.14")
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
    private val excelReportFormatter: ExcelReportFormatter,
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

