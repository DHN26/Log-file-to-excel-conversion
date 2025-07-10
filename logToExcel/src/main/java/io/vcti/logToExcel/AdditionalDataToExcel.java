package io.vcti.logToExcel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AdditionalDataToExcel {

	public static Map<String, String[]> extractAndFilterLogs() {
		HashMap<String, String[]> map = new HashMap<String, String[]>();

		// reading from log file, filtering and adding data to map
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Constants.EDIT_LOG_FILE));
			String eachLine;

			while ((eachLine = reader.readLine()) != null) {
				Matcher matcher = Constants.ADDITIONAL_LOG_PATTERN.matcher(eachLine);
				if (matcher.find()) {
					String OrderNumber = matcher.group(1);
					String StateCode = matcher.group(2);
					String OrderType = matcher.group(3);
					map.put(OrderNumber, new String[] { StateCode, OrderType });
				}
			}
			reader.close();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	public static void writingDataIntoExcel(Map<String, String[]> map) {

		try {
			// open existing excel file
			FileInputStream fis = new FileInputStream(Constants.EXCEL_FILE);
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sheet = workbook.getSheetAt(0);
			fis.close();

			// calling header method to add header
			LogFilterToExcel.setHeaders(Constants.ADDITIONAL_HEADERS, sheet);

			// adding rows of data
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Cell orderNumberCell = row.getCell(1); // order number is column 1 since we follow 0 based numbering
				if (orderNumberCell == null)
					continue;

				String orderNumber = orderNumberCell.getStringCellValue();
				// checking if map contains the column 1 data i.e order number, if yes get the
				// values i.e string[] and assign to column 5 and 6.
				if (map.containsKey(orderNumber)) {
					String[] values = map.get(orderNumber);
					row.createCell(7).setCellValue(values[0]); // State Code
					row.createCell(8).setCellValue(values[1]); // Order Type
				}
			}

			FileOutputStream out = new FileOutputStream(Constants.EXCEL_FILE);
			workbook.write(out);
			out.close();
			workbook.close();

			System.out.println("Excel file updated with state and order type.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
