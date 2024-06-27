package io.sealights.tool.configuration

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

class ApplicationArgParser(argParser: ArgParser) {
    val token by argParser.storing(
        "-t", "--token",
        help = "agent token"
    )
    val workspace by argParser.storing(
        "-w", "--workspace",
        help = "workspace"
    ).default(".")
    val buildSessionId by argParser.storing(
        "--buildSessionId",
        help = "current buildSessionId"
    )
    val referenceBuildSessionId by argParser.storing(
        "--referenceBuildSessionId",
        help = "base build session id to compare"
    ).default("")
    val integrationReferenceBuildSessionId by argParser.storing(
        "--integrationReferenceBuildSessionId",
        help = "integrationReferenceBuildSessionId value"
    ).default("")
    val integrationBuildSessionId by argParser.storing(
        "--integrationBuildSessionId",
        help = "integrationBuildSessionId value"
    ).default("")
    val componentName by argParser.storing(
        "--componentName",
        help = "componentName - required id the integrationBuildSessionId is set"
    ).default("")

//    fun set(token: String, startCommit: String, endCommit: String = "HEAD", workspace: String = ".") {
//        Configuration.token = token
//        Configuration.workspace = workspace
//        Configuration.startCommit = startCommit
//        Configuration.endCommit = endCommit
//    }
}