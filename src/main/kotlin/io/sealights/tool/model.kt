package io.sealights.tool

typealias FileName = String
typealias MethodName = String
typealias LineList = MutableList<Line>

typealias Errors = List<String>

data class LineCoverage(
    val line: Int,
    val executed: Boolean
)

data class Method(
    val name: MethodName,
    val lines: LineList
)

data class MethodLineCoverage(
    val name: MethodName,
    val linesCoverage: Set<LineCoverage> 
)

data class Line(
    val number: Int,
    val content: String
)