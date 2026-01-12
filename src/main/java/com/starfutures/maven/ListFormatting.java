package com.starfutures.maven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ListFormatting 
{
	final Font headerFont;
	final Font highlightFont;
	
	final CellStyle headerStyle; //cell style for the header cells.
	final CellStyle topPlainStyle; //top of a group, normal cell.
	final CellStyle plainStyle; //normal cell
	final CellStyle bottomPlainStyle; // bottom of a group, normal cell.
	final CellStyle topHighlightStyle; //top of a group, highlight cell.
	final CellStyle highlightStyle; //highlighted cell
	final CellStyle bottomHighlightStyle; //bottom of a group, highlight cell
	
	ListFormatting(Workbook workbook, Sheet sheet, List<ChoreBoy> choreBoyList)
	{
		headerFont = workbook.createFont();
		headerFont.setBold(true);
		
		highlightFont = workbook.createFont();
		highlightFont.setColor(IndexedColors.RED.getIndex());
		
		
		headerStyle = workbook.createCellStyle();
		headerStyle.setWrapText(true);
		headerStyle.setFont(headerFont);
		headerStyle.setBorderTop(BorderStyle.DASHED);
		headerStyle.setBorderBottom(BorderStyle.DASHED);
		
		topPlainStyle = workbook.createCellStyle();
		topPlainStyle.setWrapText(true);
		topPlainStyle.setBorderTop(BorderStyle.THICK);
		topPlainStyle.setBorderBottom(BorderStyle.DASHED);
		
		plainStyle = workbook.createCellStyle();
		plainStyle.setWrapText(true);
		plainStyle.setBorderTop(BorderStyle.DASHED);
		plainStyle.setBorderBottom(BorderStyle.DASHED);
		
		bottomPlainStyle = workbook.createCellStyle();
		bottomPlainStyle.setWrapText(true);
		bottomPlainStyle.setBorderTop(BorderStyle.DASHED);
		bottomPlainStyle.setBorderBottom(BorderStyle.THICK);
		
		topHighlightStyle = workbook.createCellStyle();
		topHighlightStyle.setWrapText(true);
		topHighlightStyle.setFont(highlightFont);
		topHighlightStyle.setBorderTop(BorderStyle.THICK);
		topHighlightStyle.setBorderBottom(BorderStyle.DASHED);
		
		highlightStyle = workbook.createCellStyle();
		highlightStyle.setWrapText(true);
		highlightStyle.setFont(highlightFont);
		highlightStyle.setBorderTop(BorderStyle.DASHED);
		highlightStyle.setBorderBottom(BorderStyle.DASHED);
		
		bottomHighlightStyle = workbook.createCellStyle();
		bottomHighlightStyle.setWrapText(true);
		bottomHighlightStyle.setFont(highlightFont);
		bottomHighlightStyle.setBorderTop(BorderStyle.DASHED);
		bottomHighlightStyle.setBorderBottom(BorderStyle.THICK);
		
		/*---------------------- ^ Style Definitions ^ ---------------------------------------*/
		
		List<String> listHeader = List.of("Chores To Do", "Extra Description", "Housekeeper");
		List<String> blankHeader = List.of("", "", "", "");
		
		//tack on the header row and blank row.
		writeRow(sheet, listHeader);
		writeRow(sheet, blankHeader);
		
		// add all the chores for each chore boy.
		for(ChoreBoy choreBoy: choreBoyList)
		{
			writeChoreBoy(sheet, choreBoy);
		}
	}
	
	//overloaded function to work with lists instead of string arrays.
	//function to write a list of strings to a row on a spreadsheet.
	private static void writeRow(Sheet sheet, List<String> values) {
		//get the number for the next row.
		int nextRowNumb = sheet.getLastRowNum() + 1;
		//create a new row
		Row newRow = sheet.createRow(nextRowNumb);
		//iteratively fill in each cell of the new row
	    for (int col = 0; col < values.size(); col++) {
	        newRow.createCell(col).setCellValue(values.get(col));
	    }
	}
	
	private static void writeChoreBoy(Sheet sheet, ChoreBoy choreBoy)
	{
		List<String[]> chores = choreBoy.getPersonalList();
		String[] choreBoyDetails = choreBoy.getBoyDetails();
		
		for(String[] chore : chores)
		{
			//create a chore entry with the chore as well as the person details.
			List<String> choreEntry = new ArrayList<>();
			choreEntry.addAll(Arrays.asList(chore));
			choreEntry.addAll(Arrays.asList(choreBoyDetails));
			
			//add the chore to the list
			writeRow(sheet, choreEntry);
		}
	}
	
}
