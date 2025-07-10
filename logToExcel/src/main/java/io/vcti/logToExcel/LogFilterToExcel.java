package io.vcti.logToExcel;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LogFilterToExcel {

	public static List<String[]> extractAndFilterLogs() {
		// list to add each string array i.e filtered data from each line
		List<String[]> filteredData = new ArrayList<String[]>();

		// reading from file, filtering the data and adding into a list of array of
		// string.
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Constants.MAIN_LOG_FILE));
			String eachLine;
			int count = 0;

			while ((eachLine = reader.readLine()) != null) {
				System.out.println("Eachline : " + eachLine);
				Matcher matcher = Constants.MAIN_LOG_PATTERN.matcher(eachLine);
				if (matcher.find()) {
					System.out.println("MATCH FOUND");

					// LocalDate.parse(...) : Converts string → LocalDate
					LocalDate logDate = LocalDate.parse(matcher.group(1), Constants.DATE_FORMAT);
					if (logDate.isAfter(Constants.THRESHOLD_DATE)) {
						String orderNumber = matcher.group(2);
						String deviceName = matcher.group(3);
						String tnOrCkt = matcher.group(4);
						String status = matcher.group(5);
						String message = matcher.group(6);
						String outDeviceType = matcher.group(7);
						System.out.println(message);
						filteredData.add(new String[] { logDate.toString(), orderNumber, outDeviceType, deviceName,
								status, tnOrCkt, message });

						// to take only first 10 success counts within the threshold date
						count++;
						if (count > 100)
							break;
					}

				}
			}
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return filteredData;
	}

	public static void setHeaders(String[] headers, Sheet sheet) {
		// adding headers
		// done in order to avoid overwriting of row headers, which in my case was
		// erasing existing headers.
		Row header = sheet.getRow(0);
		if (header == null) {
			header = sheet.createRow(0);
		}

		int startIndex = 0;
		while (header.getCell(startIndex) != null && !header.getCell(startIndex).getStringCellValue().isEmpty()) {
			startIndex++;
		}

		for (int i = 0; i < headers.length; i++) {
			header.createCell(startIndex + i).setCellValue(headers[i]);
		}
	}

//	public static void writingDataIntoExcel(List<String[]> filteredData) {
//
//		// writing into excel file
//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Filtered Log");
//
//		// creating headers
//		setHeaders(Constants.MAIN_HEADERS, sheet);
//
//		// Rows
//		int rowNum = 1;
//		for (String[] rowData : filteredData) {
//			Row row = sheet.createRow(rowNum++);
//			for (int col = 0; col < rowData.length; col++) {
//				row.createCell(col).setCellValue(rowData[col]);
//				System.out.println("writing row into sheet");
//			}
//		}
//
//		try {
//			// write into file
//			FileOutputStream out = new FileOutputStream(Constants.EXCEL_FILE);
//			workbook.write(out);
//
//			// close the file
//			workbook.close();
//			System.out.println("Filtered data written to " + Constants.EXCEL_FILE);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public static void writingDataIntoExcel(List<String[]> filteredData) {
		Workbook workbook = new XSSFWorkbook();
		Map<String, Sheet> map = new HashMap<String, Sheet>();

		// creating headers
		for (String status : Constants.STATUSES) {
			Sheet sheet = workbook.createSheet(status);
			setHeaders(Constants.MAIN_HEADERS, sheet);
			map.put(status, sheet);
		}

		// row counter to insert data next to last inserted row
		Map<String, Integer> rowCounters = new HashMap<String, Integer>();
		for (String status : Constants.STATUSES) {
			rowCounters.put(status, 1);// since insertion starts after header
		}

		for (String[] row : filteredData) {
			String status = row[4];
			Sheet currentSheet = map.get(status);

			if (currentSheet == null)
				continue;

			// get current row and create row
			int rowNum = rowCounters.get(status);
			Row excelRow = currentSheet.createRow(rowNum);

			// insert data
			for (int i = 0; i < row.length; i++) {
				excelRow.createCell(i).setCellValue(row[i]);
			}

			// increase current row count
			rowCounters.put(status, rowNum + 1);

		}
		
		try {
			FileOutputStream file = new FileOutputStream(Constants.EXCEL_FILE);
			workbook.write(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
