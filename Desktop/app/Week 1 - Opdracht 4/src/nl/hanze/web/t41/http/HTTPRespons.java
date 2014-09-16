package nl.hanze.web.t41.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.xml.internal.messaging.saaj.util.Base64;

public class HTTPRespons {
	private OutputStream out;
	private HTTPRequest request;

	public HTTPRespons(OutputStream out) {
		this.out = out;
	}

	public void setRequest(HTTPRequest request) {
		this.request = request;
	}

	public void sendResponse() throws IOException {
		byte[] bytes = new byte[HTTPSettings.BUFFER_SIZE];
		FileInputStream fis = null;
		String fileName = request.getUri();

		try {		
			File file = new File(HTTPSettings.DOC_ROOT, fileName);
			FileInputStream inputStream = getInputStream(file);

			if (file.exists()) out.write(getHTTPHeader(fileName)); 
			else out.write(getHTTPHeader(""));
						
			int ch = inputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
			
			String fileType = getFileType(fileName);
			if (fileType == "gif" || fileType == "png" || fileType == "jpeg" || fileType == "jpg" || fileType == "pdf")
				bytes = Base64.encode(bytes);
			

			while (ch != -1) {
				out.write(bytes, 0, ch);
				ch = inputStream.read(bytes, 0, HTTPSettings.BUFFER_SIZE);
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) fis.close();
		}

	}
	
	private FileInputStream getInputStream(File file) {	
		/*
		  *** OPGAVE 4: 1b ***
		  Stuur het bestand terug wanneer het bestaat; anders het standaard 404-bestand.
		*/
		
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			file = new File(HTTPSettings.DOC_ROOT, HTTPSettings.FILE_NOT_FOUND);
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException f) {
				f.printStackTrace();
			}
		}
		return null;
	}

	private byte[] getHTTPHeader(String fileName) {
		String fileType = getFileType(fileName);
		
		/* *** OPGAVE 4: 1b, 1c en 1d
		   zorg voor een goede header:
		   200 als het bestand is gevonden;
		   404 als het bestand niet bestaat
		   500 als het bestand wel bestaat maar niet van een ondersteund type is
		   
		   zorg ook voor ene juiste datum en tijd, de juiste content-type en de content-length.
		*/

		String statusMessage = null;
		
		try {
			if ((new File(HTTPSettings.DOC_ROOT, fileName)).exists()) {
				if (HTTPSettings.dataTypes.containsKey(fileType)) {
					statusMessage = "200 OK";
				} else {
					statusMessage = "500 Internal Server Error";
					fileName = HTTPSettings.FILE_NOT_FOUND;
					throw new Exception("500 Internal Server Error");
				}			
			} else {
				statusMessage = "404 Not Found";
				fileName = HTTPSettings.FILE_NOT_FOUND;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String contentType = "text/plain";
		if (HTTPSettings.dataTypes.containsKey(fileType)) {
			contentType = HTTPSettings.dataTypes.get(fileType);
		}
		
		int contentLength = (int)(new File(HTTPSettings.DOC_ROOT, fileName)).length();
				
		String header = ""
				+ "HTTP/1.1 " + statusMessage + "\r\n"
				+ "Date: " + HTTPSettings.getDate() + "\r\n"
				+ "Content-Type: " + contentType + "\r\n"
				+ "Content-Length: " + contentLength + "\r\n";
		
		byte[] rv = header.getBytes();
		return rv;
	}

	private String getFileType(String fileName) {
		int i = fileName.lastIndexOf(".");
		String ext = "";
		if (i > 0 && i < fileName.length() - 1) {
			ext = fileName.substring(i + 1);
		}

		return ext;
	}

	private void showResponse(byte[] respons) {
		StringBuffer buf = new StringBuffer(HTTPSettings.BUFFER_SIZE);

		for (int i = 0; i < respons.length; i++) {
			buf.append((char) respons[i]);
		}
		System.out.print(buf.toString());

	}

}
