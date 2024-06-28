package io.sealights.tool.build

data class BuildInfo(
    val commitHash: String,
    val appName: String,
    val branchName: String,
    val buildName: String,
    val buildSessionId: String,
    val referenceBuildSessionId: String,
    val buildMethodology: BuildMethodology
) {
    enum class BuildMethodology {
        METHOD_LINE, METHOD
    }

    companion object {
        fun empty() =
            BuildInfo(commitHash = "null", appName = "", branchName = "", buildName = "", buildSessionId = "", referenceBuildSessionId = "", buildMethodology = BuildMethodology.METHOD)
    }
}

data class BuildInfoPair(
    val actualBuildInfo: BuildInfo,
    val referenceBuildInfo: BuildInfo
)