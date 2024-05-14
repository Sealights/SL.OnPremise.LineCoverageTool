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
    val startCommit by argParser.storing(
        "--startCommit",
        help = "start commit has"
    )
    val endCommit by argParser.storing(
        "--endCommit",
        help = "end commit hash"
    ).default("HEAD")

//    fun set(token: String, startCommit: String, endCommit: String = "HEAD", workspace: String = ".") {
//        Configuration.token = token
//        Configuration.workspace = workspace
//        Configuration.startCommit = startCommit
//        Configuration.endCommit = endCommit
//    }
}