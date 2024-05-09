package io.sealights.tool.report

import arrow.core.Either
import io.sealights.tool.FileName
import io.sealights.tool.LineWithCoverage
import io.sealights.tool.MethodLinesWithCoverage
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ExcelReportFormatter(
    val excelFilename: String,
    val appBranchBuild: String
) {
    private lateinit var workbook: Workbook
    private lateinit var sheet: Sheet
    private var lastWrittenRowIndex: Int = 1
    private lateinit var lineNumberCellStyle: CellStyle
    private lateinit var lineNumberModifiedCellStyle: CellStyle
    private lateinit var sourceCellStyle: CellStyle
    private lateinit var sourceModifiedCellStyle: CellStyle
    private lateinit var coveredCellStyle: CellStyle
    private lateinit var uncoveredCellStyle: CellStyle

    fun createReport(dataToProcess: Map<FileName, Set<MethodLinesWithCoverage>>): Either<Error, String> {
        createExcelWorkbook()
        createCellStyles()
        createHeader()
        createAppBranchBuildNameRow()
        createSourceCodeRows(dataToProcess)

        saveWorkbook()

        return Either.Right("SUCCESS")
    }

    private fun createCellStyles() {
        lineNumberCellStyle = FormattingStyles.createLineNumberCellStyle(workbook, false)
        lineNumberModifiedCellStyle = FormattingStyles.createLineNumberCellStyle(workbook, true)
        sourceCellStyle = FormattingStyles.createSourceLineCellStyle(workbook, false)
        sourceModifiedCellStyle = FormattingStyles.createSourceLineCellStyle(workbook, true)
        coveredCellStyle = FormattingStyles.createCoveredCellStyle(workbook, true)
        uncoveredCellStyle = FormattingStyles.createCoveredCellStyle(workbook, false)

    }

    private fun createSourceCodeRows(dataToProcess: Map<FileName, Set<MethodLinesWithCoverage>>) {

        dataToProcess.forEach { (filename, methodLinesWithCoverageSet) ->
            createFilenameCell(filename)
            createMethodsCells(methodLinesWithCoverageSet)
            createEmptyRow()
        }

    }

    private fun createMethodsCells(methodLinesWithCoverageSet: Set<MethodLinesWithCoverage>) {
        methodLinesWithCoverageSet.forEach { methodLinesCoverage ->
            createMethodNameCell(methodLinesCoverage.name.displayName)
            createSourceLineCells(methodLinesCoverage.linesCoverage)
        }
    }

    private fun createSourceLineCells(linesCoverage: List<LineWithCoverage>) {
        linesCoverage.forEach { lineWithCoverage ->
            run {
                // cell with line number
                val lineRow = sheet.createRow(fetchNewRowIndex())
                val numberCell = lineRow.createCell(Column.B)
                numberCell.setCellValue("${lineWithCoverage.number}")
                numberCell.cellStyle = if (lineWithCoverage.modified) lineNumberModifiedCellStyle else lineNumberCellStyle

                // source code
                var sourceCell = lineRow.createCell(Column.E)
                sourceCell.setCellValue(lineWithCoverage.content)
                sourceCell.cellStyle = if (lineWithCoverage.modified) sourceModifiedCellStyle else sourceCellStyle

                // empty cells before source code
                sourceCell = lineRow.createCell(Column.C)
                sourceCell.cellStyle = if (lineWithCoverage.modified) sourceModifiedCellStyle else sourceCellStyle
                sourceCell = lineRow.createCell(Column.D)
                sourceCell.cellStyle = if (lineWithCoverage.modified) sourceModifiedCellStyle else sourceCellStyle

                // changed code column
                val changedCodeCell = lineRow.createCell(Column.F)
                changedCodeCell.setCellValue(
                    if (lineWithCoverage.modified) "Yes" else "-"
                )
                changedCodeCell.cellStyle = if (lineWithCoverage.modified) sourceModifiedCellStyle else sourceCellStyle
                
                // changed code column
                val coveredCodeCell = lineRow.createCell(Column.G)
                coveredCodeCell.setCellValue(
                    if (lineWithCoverage.covered) "Yes" else "No"
                )
                coveredCodeCell.cellStyle = if (lineWithCoverage.covered) coveredCellStyle else uncoveredCellStyle
            }
        }
    }

    private fun createMethodNameCell(methodDisplayName: String) {
        val filenameCellStyle = FormattingStyles.createFilenameStyle(workbook)
        val filenameRow = sheet.createRow(fetchNewRowIndex())
        createHeaderCell(filenameRow, 3, methodDisplayName, filenameCellStyle)
    }

    private fun createFilenameCell(filename: FileName) {
        val filenameCellStyle = FormattingStyles.createFilenameStyle(workbook)
        val filenameRow = sheet.createRow(fetchNewRowIndex())
        createHeaderCell(filenameRow, 2, filename, filenameCellStyle)
    }

    private fun createEmptyRow() = fetchNewRowIndex()
    private fun fetchNewRowIndex(): Int = ++lastWrittenRowIndex

    private fun createAppBranchBuildNameRow() {
        val headerStyle = FormattingStyles.createAppBranchBuildStyle(workbook)
        val row = sheet.createRow(1)
        createHeaderCell(row, Column.A, appBranchBuild, headerStyle)
    }

    private fun saveWorkbook() {
        val currDir = File(".")
        val path = currDir.absolutePath
        val fileLocation = path.substring(0, path.length - 1) + "$excelFilename.xlsx"

        val outputStream = FileOutputStream(fileLocation)
        workbook.write(outputStream)
        workbook.close()
    }

    private fun createHeader() {
        val headerStyle = FormattingStyles.createHeaderStyle(workbook)

        val headerRow = sheet.createRow(0)
        createHeaderCell(headerRow, Column.A, "App / Branch / Build", headerStyle)
        createHeaderCell(headerRow, Column.B, "Line", headerStyle)
        createHeaderCell(headerRow, Column.C, "File", headerStyle)
        createHeaderCell(headerRow, Column.D, "Method", headerStyle)
        createHeaderCell(headerRow, Column.E, "Code", headerStyle)
        createHeaderCell(headerRow, Column.F, "Changed Code", headerStyle)
        createHeaderCell(headerRow, Column.G, "Covered Code", headerStyle)
    }

    private fun createHeaderCell(headerRow: Row, columnIndex: Int, content: String, headerStyle: CellStyle) {
        var headerCell = headerRow.createCell(columnIndex)
        headerCell.setCellValue(content)
        headerCell.cellStyle = headerStyle
    }

    private fun createExcelWorkbook() {
        workbook = XSSFWorkbook()
        sheet = workbook.createSheet("Line coverage ${currentDataTIme()}")

        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 1688);
        sheet.setColumnWidth(2, 3750);
        sheet.setColumnWidth(3, 3750);
        sheet.setColumnWidth(4, 12750);
        sheet.setColumnWidth(5, 4320);
        sheet.setColumnWidth(6, 4320);
    }

    private fun currentDataTIme(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH,mm")
        return currentDateTime.format(formatter)
    }
}

object Column {
    const val A = 0
    const val B = 1
    const val C = 2
    const val D = 3
    const val E = 4
    const val F = 5
    const val G = 6
}