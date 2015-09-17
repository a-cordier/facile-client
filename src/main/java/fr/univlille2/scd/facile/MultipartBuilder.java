package fr.univlille2.scd.facile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MultipartBuilder {
	private final String boundary;
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection connection;
	private PrintWriter writer;
	private OutputStream outputStream;

	public MultipartBuilder(String url, String charset) throws IOException {
		boundary = Long.toHexString(System.currentTimeMillis());
		connection = (HttpURLConnection) new URL(url).openConnection();
		//connection.setUseCaches(false);
		connection.setDoOutput(true); // indicates POST method
		connection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundary);
		connection.setRequestProperty("User-Agent", buildUserAgentString());
		outputStream = connection.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, charset));
	}

	public void addFilePart(String name, File file, String contentType) throws IOException {
		String fileName = file.getName();
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append(
				"Content-Disposition: form-data; name=\"" + name
						+ "\"; filename=\"" + fileName + "\"")
				.append(LINE_FEED);
		writer.append("Content-Type: " + contentType).append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();
		FileInputStream inputStream = new FileInputStream(file);
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.flush();
		inputStream.close();

		writer.append(LINE_FEED);
		writer.flush();
	}
	
	public void addFilePart(String name, InputStream inputStream, String contentType) throws IOException {
		String fileName = name;
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append(
				"Content-Disposition: form-data; name=\"" + name
						+ "\"; filename=\"" + fileName + "\"")
				.append(LINE_FEED);
		writer.append("Content-Type: " + contentType).append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.flush();
		inputStream.close();
		writer.append(LINE_FEED);
		writer.flush();
	}
	
	
	public String doPost() throws IOException {
		writer.append(LINE_FEED).flush();
		writer.append("--" + boundary + "--").append(LINE_FEED);
		if (writer != null) {
			writer.close();
		} // done
		String response = "";
		InputStream stream = connection.getInputStream();
		InputStreamReader inputStremReader = new InputStreamReader(stream);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(inputStremReader);
			for (String line; (line = reader.readLine()) != null;) {
				response += line;
			}
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {

				}
		}
		return response;
	}

	public String buildUserAgentString() {
		return String.format("Ul2WebClient/1.0;%s/%s;Java/%s/%s",
				System.getProperty("os.name"),
				System.getProperty("os.version"),
				System.getProperty("java.vendor"),
				System.getProperty("java.version"));
	}
}
