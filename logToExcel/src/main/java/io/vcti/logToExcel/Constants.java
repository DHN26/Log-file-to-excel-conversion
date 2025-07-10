package io.vcti.logToExcel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Constants {
	
	public static final String MAIN_LOG_FILE = "C:\\Users\\divyashree.nataraja\\Downloads\\grep_orderdetails.txt";
	public static final String EXCEL_FILE = "C:\\Users\\divyashree.nataraja\\OneDrive - VCTI\\Desktop\\result.xlsx";
	public static final String EDIT_LOG_FILE = "C:\\Users\\divyashree.nataraja\\Downloads\\grep_ticketdetails.txt";

	//pattern data
	/*public static final Pattern MAIN_LOG_PATTERN = Pattern.compile(
			".*?(\\d{4}-\\d{2}-\\d{2})\\s\\d{2}:\\d{2}:\\d{2}.*?orderNumber=([A-Z0-9]+).*?deviceName=([A-Za-z0-9\\-]+).*?tnOrCkt=(\\d+).*?status=([A-Za-z ]+).*?outDeviceType=([A-Za-z0-9 \\-/]+)");
			*/
	public static final Pattern MAIN_LOG_PATTERN = Pattern.compile(".*?(\\d{4}-\\d{2}-\\d{2})\\s\\d{2}:\\d{2}:\\d{2}.*?orderNumber=([A-Z0-9]+).*?deviceName=([A-Za-z0-9\\-]+).*?tnOrCkt=(\\d+).*?status=([A-Za-z ]+).*?error=(?:ErrorDetails \\[message=([^\\]]+\\])|null).*?outDeviceType=([A-Za-z0-9 \\-/]+)");
	public static final Pattern ADDITIONAL_LOG_PATTERN = Pattern.compile("ServiceOrder\\s*:(\\w+),.*?State\\s*:(\\w+),\\s*OrderType\\s*:(\\w+)");
	

	// defining threshold date
	public static final LocalDate THRESHOLD_DATE = LocalDate.of(2025, 5, 7);
	// defining date format
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	//header data
	public static final String[] MAIN_HEADERS = {"Date", "Order Number", "Out Device Type", "Device Name", "Status", "Telephone Number", "Message"};
	public static final String[] ADDITIONAL_HEADERS = {"State Code", "Order Type"};
	
	//statuses
	public static final String[] STATUSES = {"Success", "Data Error", "Communication Error", "Failuer"};

}
