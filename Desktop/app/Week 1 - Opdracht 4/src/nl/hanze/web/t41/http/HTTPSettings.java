package nl.hanze.web.t41.http;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public final class HTTPSettings {
	// Opgave 4: 1a
	// ZET HIER DE JUISTE DIRECTORY IN WAAR JE BESTANDEN STAAN.
	
	static final String DOC_ROOT = ""; /* In te vullen! */
	static final String FILE_NOT_FOUND = ""; /* In te vullen! */
	
	static final int BUFFER_SIZE = 2048;	
	static final int PORT_MIN = 0;
	static final int PORT_MAX = 65535;
	
	static final public int PORT_NUM = 4444;
	static final HashMap<String, String> dataTypes = new HashMap<String, String>(){{
		put("html", "text/html");
		put("css", "text/css");
		put("js", "application/x-javascript");
		put("txt", "text/plain");
		put("gif", "image/gif");
		put("png", "image/png");
		put("jpg", "image/jpeg");
		put("jpeg", "image/jpeg");
		put("pdf", "application/pdf"); 
	}};	

	static final String[] DAYS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	
	
	public static String getDate() {
		GregorianCalendar cal = new GregorianCalendar();
		SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss zzz");

		String rv = "";
		rv += DAYS[cal.get(Calendar.DAY_OF_WEEK) - 1] + ", " + cal.get(Calendar.DAY_OF_MONTH) + " " + MONTHS[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR) + " " + date.format(new Date());
		
		return rv;
	}
	
}
