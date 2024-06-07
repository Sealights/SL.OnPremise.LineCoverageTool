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
    val baseApp by argParser.storing(
        "--baseApp",
        help = "comparison base application name"
    )
    val baseBranch by argParser.storing(
        "--baseBranch",
        help = "comparison base branch name"
    )
    val baseBuild by argParser.storing(
        "--baseBuild",
        help = "comparison base build name"
    )
    val currentApp by argParser.storing(
        "--currentApp",
        help = "current application name"
    )
    val currentBranch by argParser.storing(
        "--currentBranch",
        help = "current branch name"
    )
    val currentBuild by argParser.storing(
        "--currentBuild",
        help = "current build name"
    )
    val baseCommit by argParser.storing(
        "--baseCommit",
        help = "start commit has"
    )

//    fun set(token: String, startCommit: String, endCommit: String = "HEAD", workspace: String = ".") {
//        Configuration.token = token
//        Configuration.workspace = workspace
//        Configuration.startCommit = startCommit
//        Configuration.endCommit = endCommit
//    }
}