package io.sealights.tool

typealias FileName = String
typealias UniqueMethodId = String
typealias LineList = MutableList<Line>

typealias Errors = List<String>

data class MethodName(
    val uniqueId: String,
    val displayName: String
)

data class MethodLines(
    val name: MethodName,
    val lines: LineList
)

data class ScannedMethod(
    val name: MethodName,
    val start: Int,
    val end: Int
)

data class MethodLinesWithCoverage(
    val name: MethodName,
    val linesCoverage: List<LineWithCoverage> 
)

data class Line(
    val number: Int,
    val modified: Boolean,
    var content: String
)

data class LineWithCoverage(
    val number: Int,
    val modified: Boolean,
    val covered: Boolean,
    val content: String,
    val coverability: LineCoverage
)

enum class LineCoverage {
    COVERABLE,
    EMPTY_LINE,
    SINGLE_COMMENT,
    MULTILINE_COMMENT,
    NO_EXPRESSION
}
