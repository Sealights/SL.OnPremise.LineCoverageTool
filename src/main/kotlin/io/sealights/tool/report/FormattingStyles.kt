package io.sealights.tool.report

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Color
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFWorkbook


object FormattingStyles {

    private val WHITE_COLOR = createXSSFColor(255, 255, 255)
    private val LIGHT_GRAY_COLOR = createXSSFColor(217, 217, 217)
    private val LIGHT_RED_COLOR = createXSSFColor(244, 204, 204)
    private val LIGHTER_GRAY_COLOR = createXSSFColor(239, 239, 239)
    private val LIGHT_GREEN_COLOR = createXSSFColor(217, 234, 211)
    private val LIGHT_BLUE_COLOR = createXSSFColor(207, 226, 243)
    private val FONT_GRAY_COLOR = createXSSFColor(153, 153, 153)
    private val FONT_BLACK_COLOR = createXSSFColor(0, 0, 0)
    
    
    private val FONT_SIZE_DEFAULT = readStringFromProperty("sl.style.font.size", 11)
    private val MONOSPACED_FONT_SIZE_DEFAULT = readStringFromProperty("sl.style.monofont.size", 10)
    private val FONT_NAME_DEFAULT = readStringFromProperty("sl.style.font", "Calibri")
    private val MONOSPACED_FONT_NAME_DEFAULT = readStringFromProperty("sl.style.monofont", "Courier New")

    fun createHeaderStyle(workbook: Workbook): CellStyle {
        val headerStyle = createCellStyle(workbook as XSSFWorkbook, fillColor = LIGHT_GRAY_COLOR)
        val font = createFont(workbook, bold = true)
        headerStyle.setFont(font)

        return headerStyle
    }

    fun createFilenameStyle(workbook: Workbook): CellStyle {
        val headerStyle = createCellStyle(workbook as XSSFWorkbook)
        val font = createFont(workbook, bold = true)
        headerStyle.setFont(font)
        return headerStyle
    }

    fun createAppBranchBuildStyle(workbook: Workbook): CellStyle {
        val cellStyle = createCellStyle(workbook as XSSFWorkbook)
        val font = createFont(workbook)
        cellStyle.setFont(font)
        return cellStyle
    }

    fun createLineNumberCellStyle(workbook: Workbook, modified: Boolean): CellStyle {
        val cellStyle = createCellStyle(workbook as XSSFWorkbook, fillColor = returnOnCondition(modified, LIGHT_BLUE_COLOR, LIGHTER_GRAY_COLOR))
        val font = createFont(workbook, color = FONT_GRAY_COLOR)
        cellStyle.setFont(font)

        return cellStyle
    }

    fun createSourceLineCellStyle(workbook: Workbook, modified: Boolean): CellStyle {
        val cellStyle = createCellStyle(workbook as XSSFWorkbook, fillColor = returnOnCondition(modified, LIGHT_BLUE_COLOR, LIGHTER_GRAY_COLOR))
        val font = createFont(workbook, fontName = MONOSPACED_FONT_NAME_DEFAULT, fontHeightInPoints = MONOSPACED_FONT_SIZE_DEFAULT.toShort())
        cellStyle.setFont(font)

        return cellStyle
    }

    fun codeChangeLineCellStyle(workbook: Workbook, modified: Boolean): CellStyle {
        val cellStyle = createCellStyle(workbook as XSSFWorkbook, fillColor = returnOnCondition(modified, LIGHT_BLUE_COLOR, LIGHTER_GRAY_COLOR))
        val font = createFont(workbook)
        cellStyle.setFont(font)

        return cellStyle
    }
    
    fun createCoveredCellStyle(workbook: Workbook, covered: Boolean): CellStyle {
        val cellStyle = createCellStyle(workbook as XSSFWorkbook, fillColor = returnOnCondition(covered, LIGHT_GREEN_COLOR, LIGHT_RED_COLOR))
        val font = createFont(workbook)
        cellStyle.setFont(font)

        return cellStyle
    }

    fun createUncoverableCellStyle(workbook: Workbook, covered: Boolean): CellStyle {
        val cellStyle = createCellStyle(workbook as XSSFWorkbook, fillColor = LIGHTER_GRAY_COLOR)
        val font = createFont(workbook)
        cellStyle.setFont(font)

        return cellStyle
    }
    
    private fun createCellStyle(
        workbook: XSSFWorkbook,
        fillColor: Color = WHITE_COLOR,
        fillPattern: FillPatternType = FillPatternType.SOLID_FOREGROUND
    ): XSSFCellStyle {
        val cellStyle = workbook.createCellStyle()
        cellStyle.fillPattern = fillPattern
        cellStyle.setFillForegroundColor(fillColor)
        return cellStyle
    }

    private fun createFont(
        workbook: XSSFWorkbook,
        fontName: String = FONT_NAME_DEFAULT,
        fontHeightInPoints: Short = FONT_SIZE_DEFAULT.toShort(),
        bold: Boolean = false,
        color: XSSFColor = FONT_BLACK_COLOR
    ): XSSFFont {
        val font = workbook.createFont()
        font.fontName = fontName
        font.fontHeightInPoints = fontHeightInPoints
        font.bold = bold
        font.setColor(color)
        return font
    }

    private fun <T> returnOnCondition(condition: Boolean, onTrue: T, onFalse: T) = if (condition) {
        onTrue
    } else {
        onFalse
    }


    @OptIn(ExperimentalUnsignedTypes::class)
    private fun createXSSFColor(red: Int, green: Int, blue: Int) = XSSFColor(ubyteArrayOf(0u, red.toUByte(), green.toUByte(), blue.toUByte()).toByteArray())
    
    private fun <T> readStringFromProperty(propertyName: String, defaultValue: T): T {
        val property = System.getProperty(propertyName)
        if (property.isNullOrEmpty()) {
            return defaultValue
        }
        return when (defaultValue) {
            is Int -> property.toInt()
            else -> property
        } as T
    }

}