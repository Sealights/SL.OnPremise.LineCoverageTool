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
import io.sealights.tool.git.GitDiffProviderService
import io.sealights.tool.git.GitModifiedLineService
import io.sealights.tool.report.ExcelReportFormater

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
    
    val coverageTool = CoverageTool(
        gitModifiedLineService,
        buildLineService,
        footprintsService,
        excelReportFormatter
    )

    coverageTool.run()

    println("Sealights Line Level Coverage end.")
}

class CoverageTool(
    private val gitLinesService: GitModifiedLineService,
    private val buildLineService: BuildLineService,
    private val footprintsService: FootprintsService,
    private val excelReportFormatter: ExcelReportFormater
) {
    fun run() {
        gitLinesService.modifiedFileLines(Configuration.workspace, Configuration.startCommit, "HEAD")
            .flatMap { buildLineService.mergeMethodNames(it) }
            .flatMap { footprintsService.appendLineExecutionData(it) }
            .flatMap { excelReportFormatter.createReport(it) }
    }
}

