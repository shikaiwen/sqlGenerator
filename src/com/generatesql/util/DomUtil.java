package com.generatesql.util;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.w3c.dom.Document;

public class DomUtil {

	private static DocumentBuilderFactory documentBuilderFactory = null;
	private static DocumentBuilder documentBuilder = null;

	private static TransformerFactory transformerFactory = null;
	private static Transformer transformer = null;
	
	
	static{
		documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		transformerFactory = TransformerFactory.newInstance();
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	
	public static void transformToFile(Document doc,File file) throws TransformerException{
		Source source = new DOMSource(doc);
		StreamResult streamResult = new StreamResult(file);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.transform(source, streamResult);
	}
	
	

	public static DocumentBuilderFactory getDocumentBuilderFactory() {
		return documentBuilderFactory;
	}


	public static DocumentBuilder getDocumentBuilder() {
		return documentBuilder;
	}


	public static TransformerFactory getTransformerFactory() {
		return transformerFactory;
	}


	public static Transformer getTransformer() {
		return transformer;
	}


	
	
}
