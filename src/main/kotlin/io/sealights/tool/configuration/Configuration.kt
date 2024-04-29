package io.sealights.tool.configuration

object Configuration {
    var workspace: String = ""
    var token: String = ""
    var startCommit: String = ""
    
    fun build(parser: ApplicationArgParser) {
        workspace = parser.workspace
        token = parser.token
        startCommit = parser.startCommit
    }
}