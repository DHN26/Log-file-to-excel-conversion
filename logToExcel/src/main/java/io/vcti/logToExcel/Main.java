package io.vcti.logToExcel;

import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		List<String[]> filteredData = LogFilterToExcel.extractAndFilterLogs();
		LogFilterToExcel.writingDataIntoExcel(filteredData);
		
		Map<String, String[]> map = AdditionalDataToExcel.extractAndFilterLogs();
		AdditionalDataToExcel.writingDataIntoExcel(map);
	}

}
