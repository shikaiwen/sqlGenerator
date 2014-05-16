package com.tomcat.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomUtil {

	public static DocumentBuilderFactory factory = null;
	public static DocumentBuilder builder = null;
	
	static {
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static Node getFirstChildByNodeName(Node parentNode,String childNodeName){
		NodeList nodeList = parentNode.getChildNodes();
		
		for(int i = 0;i< nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if(childNodeName.equals(node.getNodeName())){
				return node;
			}
		}
		return null;
	}

	public static DocumentBuilderFactory getFactory() {
		return factory;
	}

	public static DocumentBuilder getBuilder() {
		return builder;
	}
	
	
	
	
	
	
	
	
	
	
	
}
