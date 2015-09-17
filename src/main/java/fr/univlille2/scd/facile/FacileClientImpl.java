package fr.univlille2.scd.facile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FacileClientImpl implements FacileClient {

	private static final String FACILE_URL = "http://facile.cines.fr/xml";
	private String response;

	@Override
	public String submit(File file) throws FacileClientException {
		MultipartBuilder builder;
		try {
			builder = new MultipartBuilder(FACILE_URL, StandardCharsets.UTF_8.toString());
			builder.addFilePart("file", file, "application/octet-stream");
			this.response = builder.doPost();
		} catch (IOException e) {
			throw new FacileClientException(e);
		}
		return response;
	}
	
	@Override
	public String submit(InputStream inputStream) throws FacileClientException {
		MultipartBuilder builder;
		try {
			builder = new MultipartBuilder(FACILE_URL, StandardCharsets.UTF_8.toString());
			builder.addFilePart("file", inputStream, "application/octet-stream");
			this.response = builder.doPost();
		} catch (IOException e) {
			throw new FacileClientException(e);
		}
		return response;
	}
	
	@Override
	public boolean isValid()  {
		String valid = parse("valid");
		return "true".equals(valid);
	}
	
	@Override
	public String getMessage(){
		String message = parse("message");
		return "null".equals(message)? "Manquant" : message;
	}

	@Override
	public String getResponse()  {
		return response;
	}

	private String parse(String tag) {
		Pattern pattern = Pattern.compile(String.format("<%s>(.*?)</%s>", tag,
				tag));
		Matcher matcher = pattern.matcher(response);
		if (matcher.find()) {
			return matcher.group(1).replaceAll("\\s\\n", "");
		} else {
			return null;
		}
	}

}
