package io.sealights.tool

import arrow.core.flatMap
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import io.sealights.tool.build.BuildLineService
import io.sealights.tool.configuration.ApplicationArgParser
import io.sealights.tool.configuration.Configuration
import io.sealights.tool.footprints.FootprintsService
import io.sealights.tool.git.GitDiffProviderService
import io.sealights.tool.git.GitModifiedLineService

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
    val buildLineService = BuildLineService()
    val footprintsService = FootprintsService()


    val coverageTool = CoverageTool(
        gitModifiedLineService,
        buildLineService,
        footprintsService
    )

    coverageTool.run()

    println("Sealights Line Level Coverage end.")
}

class CoverageTool(
    private val gitLinesService: GitModifiedLineService,
    private val buildLineService: BuildLineService,
    private val footprintsService: FootprintsService
) {
    fun run() {
        gitLinesService.modifiedFileLines(Configuration.workspace, Configuration.startCommit, "HEAD")
            .flatMap { buildLineService.filter(it) }
            .flatMap { footprintsService.appendLineExecutionData(it) }

    }
}

