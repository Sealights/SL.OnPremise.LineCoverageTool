package io.sealights.tool.report

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook


object FormattingStyles {

    fun createHeaderStyle(workbook: Workbook): CellStyle {
        val headerStyle: CellStyle = workbook.createCellStyle()
        headerStyle.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

        val font = (workbook as XSSFWorkbook).createFont()
        font.fontName = "Calibri"
        font.fontHeightInPoints = 11.toShort()
        font.bold = true
        headerStyle.setFont(font)
        
        return headerStyle
    }

    fun createAppBranchBuildStyle(workbook: Workbook): CellStyle {
        val cellStyle = workbook.createCellStyle()
//        headerStyle.fillForegroundColor = IndexedColors.GREY_25_PERCENT.index
//        headerStyle.fillPattern = FillPatternType.SOLID_FOREGROUND

        val font = (workbook as XSSFWorkbook).createFont()
        font.fontName = "Calibri"
        font.fontHeightInPoints = 11.toShort()
        font.bold = false
        cellStyle.setFont(font)

        return cellStyle
    }

}