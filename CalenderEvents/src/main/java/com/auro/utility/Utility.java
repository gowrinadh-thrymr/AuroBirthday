package com.auro.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Utility {
	public static Document getConnectionFeed(String url) throws IOException {
		Document doc = null;
		InputStream is = null;
		try {
			URLConnection openConnection = new URL(url.trim()).openConnection();
			is = openConnection.getInputStream();

			if ("gzip".equals(openConnection.getContentEncoding())) {
				is = new GZIPInputStream(is);
			}
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

		} catch (Exception e) {
			 e.printStackTrace();
			System.out.println("Exception occured when building the feed object out of the url" + e.getMessage());
		} finally {
			if (is != null)
				is.close();
		}
		return doc;
	}

}
