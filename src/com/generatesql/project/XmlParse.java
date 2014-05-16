package com.generatesql.project;

import java.io.ByteArrayInputStream;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

public class XmlParse {

	public static Document parseFromStr(String xmlStr){
		SAXBuilder builder = new SAXBuilder();
		ByteArrayInputStream bais = new ByteArrayInputStream(xmlStr.getBytes());
		try{
			Document doc = builder.build(bais);
			
			System.out.println(doc.getRootElement().getName());
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
}
