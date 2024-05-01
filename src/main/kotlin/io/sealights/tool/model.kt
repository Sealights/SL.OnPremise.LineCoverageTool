package io.sealights.tool

typealias FileName = String
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
    val content: String
)

data class LineWithCoverage(
    val number: Int,
    val modified: Boolean,
    val covered: Boolean,
    val content: String
)