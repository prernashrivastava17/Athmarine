package com.athmarine.service;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.athmarine.response.GetBidsQuotationResponse;

public class QuotationExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private GetBidsQuotationResponse quotation;

	Row row0;
	Row row1;
	Row row2;
	
	Row row4;
	Row row5;
	Row row6;
	Row row7;
	Row row8;
	Row row9;
	Row row10;
	Row row11;
	Row row12;
	Row row14;
	Row row15;
	Row row16;
	Row row17;
	Row row18;
	Row row19;
	Row row20;
	Row row21;
	Row row22;
	Row row23;

	public QuotationExcelExporter(GetBidsQuotationResponse bidsQuotation) {
		this.quotation = bidsQuotation;
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Users");
        sheet.setColumnWidth(0, 20000);
		row0 = sheet.createRow(0);
		row1 = sheet.createRow(1);
		row2 = sheet.createRow(2);
		
		row4 = sheet.createRow(4);
		row5 = sheet.createRow(5);
		row6 = sheet.createRow(6);
		row7 = sheet.createRow(7);
		row8 = sheet.createRow(8);
		row9 = sheet.createRow(9);
		row10 = sheet.createRow(10);
		row11 = sheet.createRow(11);
		row12 = sheet.createRow(12);
		row14 = sheet.createRow(14);
		 row15 = sheet.createRow(15);
		 row16 = sheet.createRow(16);
		 row17 = sheet.createRow(17);
		 row18 = sheet.createRow(18);
		 row19 = sheet.createRow(19);
		 row20 = sheet.createRow(20);
		 row21 = sheet.createRow(21);
		 row22 = sheet.createRow(22);
		 row23 = sheet.createRow(23);
	}

	private void writeHeaderLine() {

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(false);
		font.setFontHeight(12);
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.BRICKS);

		createCell(row0, 1, "Air Fare:", style);
		createCell(row1, 0, "Travel Cost:", style);
		createCell(row1, 1, "Transportation(Total):", style);
		createCell(row2, 1, "Travel Time Cost:", style);

		createCell(row4, 1, "Normal Days:", style);
		createCell(row5, 1, "Working Rate-Normal Hours-(0900hrs-1800hrs):", style);
		createCell(row6, 1, "Working Rate(OT1) - (0600hrs-900hrs)-(1800hrs-2400hrs):", style);
		createCell(row7, 1, "Working Rate(OT2) - (0000hrs-600hrs):", style);
		createCell(row8, 0, "Working", style);
		createCell(row9, 1, "Saturday/Sunday/Public Holidays", style);
		createCell(row10, 1, "Working Rate-Normal Hours-(0900hrs-1800hrs):", style);
		createCell(row11, 1, "Working Rate(OT1) - (0600hrs-900hrs)-(1800hrs-2400hrs):", style);
		createCell(row12, 1, "Working Rate(OT2) - (0000hrs-600hrs):", style);
		
		createCell(row14, 0, "Spares:", style);
		createCell(row14, 1, "Item 1 Description:", style);
		createCell(row15, 1, "Item 2 Description:", style);
		createCell(row16, 1, "Item 3 Description", style);
		
		createCell(row18, 0, "Miscellanceous", style);
		createCell(row18, 1, "Port Charges:", style);
		createCell(row19, 1, "COVID Test:", style);
		createCell(row20, 1, "Shipyard Charges:", style);
		createCell(row21, 1, "Others:", style);	
		
		createCell(row23, 0, "Total Cost:", style);
		
		
		
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		style.setFillPattern(FillPatternType.BRICKS);
//		Row row0 = sheet.createRow(0);
//		Row row1= sheet.createRow(1);
//		Row row2 = sheet.createRow(2);
//		
//
		int c1;
		int c2;
		int c3;
		int c4;
		
		createCell(row0, 4, quotation.getTravelCost().getAirFare(), style);
		createCell(row1, 4, quotation.getTravelCost().getTransportation(), style);
		createCell(row2, 4, quotation.getTravelCost().getTravelTimeCost(), style);
		createCell(row1, 5, c1=quotation.getTravelCost().getTravelTimeCost()+quotation.getTravelCost().getTransportation()+quotation.getTravelCost().getAirFare(), style);
		
		createCell(row5, 4, quotation.getWorking().getWorkingRate(), style);
		createCell(row6, 4, quotation.getWorking().getWorkingRateOT1(), style);
		createCell(row7, 4, quotation.getWorking().getWorkingRateOT2(), style);
		createCell(row6, 5, c2=quotation.getWorking().getWorkingRate()+quotation.getWorking().getWorkingRateOT1()+quotation.getWorking().getWorkingRateOT2(), style);
		
		createCell(row14, 4, quotation.getSpares().getItemOne(), style);
		createCell(row15, 4, quotation.getSpares().getItemTwo(), style);
		createCell(row16, 4, quotation.getSpares().getItemThree(), style);
		createCell(row15, 5, c3=quotation.getSpares().getItemOne()+quotation.getSpares().getItemTwo()+quotation.getSpares().getItemThree(), style);
		
		createCell(row18, 4, quotation.getMiscellaneous().getPortCharges(), style);
		createCell(row19, 4, quotation.getMiscellaneous().getCovidTestCharge(), style);
		createCell(row20, 4, quotation.getMiscellaneous().getShipyardCharges(), style);
		createCell(row21, 4, quotation.getMiscellaneous().getOtherCharge(), style);
		createCell(row20, 5, c4=quotation.getMiscellaneous().getPortCharges()+quotation.getMiscellaneous().getCovidTestCharge()+quotation.getMiscellaneous().getShipyardCharges()+quotation.getMiscellaneous().getOtherCharge(), style);
		
		createCell(row23, 5, c1+c2+c3+c4, style);
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();

	}

}
