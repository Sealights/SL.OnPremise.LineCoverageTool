package io.sealights.tool.report

import arrow.core.Either
import io.sealights.tool.FileName
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

    fun createReport(dataToProcess: Map<FileName, Set<MethodLinesWithCoverage>>): Either<Error, String> {
        createExcelWorkbook()
        createHeader()
        createAppBranchBuildNameRow()

        saveWorkbook()

        return Either.Right("SUCCESS")
    }

    private fun createAppBranchBuildNameRow() {
        val headerStyle = FormattingStyles.createAppBranchBuildStyle(workbook)
        val row = sheet.createRow(1)
        createHeaderCell(row, 0, appBranchBuild, headerStyle)
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
        createHeaderCell(headerRow, 0, "App / Branch / Build", headerStyle)
        createHeaderCell(headerRow, 1, "Line", headerStyle)
        createHeaderCell(headerRow, 2, "File", headerStyle)
        createHeaderCell(headerRow, 3, "Method", headerStyle)
        createHeaderCell(headerRow, 4, "Code", headerStyle)
        createHeaderCell(headerRow, 5, "Changed Code", headerStyle)
        createHeaderCell(headerRow, 6, "Covered Code", headerStyle)
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