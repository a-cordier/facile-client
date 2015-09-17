package fr.univlille2.scd.facile;

import java.io.File;
import java.io.InputStream;

public interface FacileClient {
	public boolean isValid();
	public String getMessage();
	public String getResponse();
	public String submit(File file) throws FacileClientException;
	public String submit(InputStream inputStream) throws FacileClientException;
}
