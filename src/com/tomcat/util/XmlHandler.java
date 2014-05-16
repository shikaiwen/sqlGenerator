package com.tomcat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.generatesql.xml.JdkDomUtil;

public class XmlHandler {

	
	
	public static final String serverXmlFileLocation = "J:/apache-tomcat-7.0.53-windows-x64/apache-tomcat-7.0.53/conf/server.xml";
	
	static DocumentBuilder docBuilder = null;
	static Document serverDocument = null;
	
	static{
		docBuilder = DomUtil.getBuilder();
		try {
			serverDocument = docBuilder.parse(new File(serverXmlFileLocation));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception{
		
		
		//new XmlHandler().getContextsNode();
		//getContextNode("/sqlGen1.3");
		Node commentNode = getCommentInXml();
		//short nodeType = commentNode.getNextSibling().getNodeType();
		Node tempNode = commentNode.getNextSibling();
//		do{
//			System.out.println( XmlHandler.selfXmlStr(tempNode));
//			while(tempNode.getNodeType() != Node.TEXT_NODE)
//		}while(tempNode.getNodeType() != Node.TEXT_NODE);

		while(true){
			short nodeType = tempNode.getNodeType();

			switch(nodeType){
			case (Node.DOCUMENT_TYPE_NODE):  System.out.println("DOCUMENT_TYPE_NODE");break;
			case (Node.TEXT_NODE): System.out.println("TEXT_NODE");break;
			case (Node.COMMENT_NODE): System.out.println("COMMENT_NODE");break;
			}
			System.out.println(nodeType);
			if( nodeType!= Node.TEXT_NODE){
				;
				System.out.println(selfXmlStr(tempNode));
			//	System.out.println( tempNode.getClass().getCanonicalName() );
				break;
			}else{
				selfXmlStr(tempNode);
			}
			tempNode = tempNode.getNextSibling();
		}

	}
	
	public static Node getHostNode(){
		return serverDocument.getElementsByTagName("Host").item(0);
	}
	
	// read the server file
	public static Map<String,NodeList> getContextsNode() throws Exception{
		
		Map<String,NodeList>  contextsMap = new HashMap<String,NodeList>();
		
		DocumentBuilder  builder = DomUtil.getBuilder();
		InputStream is = new FileInputStream(serverXmlFileLocation);
		
		Document doc = builder.parse(is);
		
		
		//System.out.println(doc.getElementsByTagName("Host").getLength());
		Node hostNode = doc.getElementsByTagName("Host").item(0);
		
		//第一个注释节点保存的
		Comment contextComment = (Comment)JdkDomUtil.getSingleNodeByNodeType(hostNode.getChildNodes(), Node.COMMENT_NODE);
		System.out.println(selfXmlStr(contextComment));
		//org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData)comment;
		String keepContextNodeStr = contextComment.getTextContent();
		
		Document keepContextDoc = builder.parse(new InputSource(new StringReader(keepContextNodeStr)));
		if(keepContextDoc == null){
			System.out.println("it's null");
		}
		
		//被注释了的Context
		NodeList contextsInComment = keepContextDoc.getElementsByTagName("Context");
	//	System.out.println(contextsInComment.getLength());
	//	System.out.println( selfXmlStr( contextsInComment.item(0) ));
		
		//未被注释的
		Element hostElt = (Element)hostNode;
		NodeList inUseContexts = hostElt.getElementsByTagName("Context");
		System.out.println(inUseContexts.getLength());
//		System.out.println( selfXmlStr(inUseContexts.item(0)) );
		
		contextsMap.put("notUseContexts", contextsInComment);
		contextsMap.put("inUseContexts", inUseContexts);

		return contextsMap;
	}
	
	
	// document Element of the comment Node
	static Document commentDoc = null;
	static{
		try {
			// init comment document
		//	getCommentDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Node getCommentInXml() throws Exception{
		
	//	if(commentDoc != null) return commentDoc;
		Node hostNode = serverDocument.getElementsByTagName("Host").item(0);
		//第一个注释节点保存的
		Comment contextComment = (Comment)JdkDomUtil.getSingleNodeByNodeType(hostNode.getChildNodes(), Node.COMMENT_NODE);
		
		return contextComment;
//		System.out.println(selfXmlStr(contextComment));
//		
//		String keepContextNodeStr = contextComment.getTextContent();
//		
//		commentDoc = docBuilder.parse(new InputSource(new StringReader(keepContextNodeStr)));
		
//		if(commentDoc == null) throw new RuntimeException("解析第一个注释节点有误!");
//		return commentDoc;
	}
	
	public static Node getCommentNode(){
		return commentDoc.getDocumentElement();
	}

	
	// document element  获取节点, getOwnerDocument等于null，文档参看getParentNode,说明了哪些节点没有
	public static String selfXmlStr(Node node){
//		Element tempElt = node.getOwnerDocument().createElement("temp");
		
		
		Node clonedNode = node.cloneNode(true);

		Document doc = node.getOwnerDocument();
		DOMImplementation domImp = doc.getImplementation();
	//	System.out.println(domImp == null);
		
		DOMImplementationLS ls = (DOMImplementationLS)domImp.getFeature("LS", "3.0");
		
		LSSerializer serializer = ls.createLSSerializer();
		serializer.getDomConfig().setParameter("xml-declaration", false);
//		
		return serializer.writeToString(clonedNode);
		//System.out.println(serializer.writeToString(clonedNode));
//		return serializer.writeToString(clonedNode);
//		tempElt.appendChild(clonedNode);
//		

		
	}
	
	public static String innerXml(Node node){
		DOMImplementation domImpl = node.getOwnerDocument().getImplementation();
		DOMImplementationLS ls = (DOMImplementationLS)domImpl.getFeature("LS", "3.0");
		
		LSSerializer serializer = ls.createLSSerializer();
		serializer.getDomConfig().setParameter("xml-declaration", false);
		
		
		NodeList childNodes = node.getChildNodes();
		
		StringBuilder sb = new StringBuilder();
		for( int i =0; i < childNodes.getLength(); i++){
			System.out.println("node "+i +": "+ serializer.writeToString(childNodes.item(i)));
		}
		return null;
	}
	
	
	public static Node getContextNode(String path){
//		NodeList hostList = serverDocument.getElementsByTagName("Host");
//		Node hostNode = JdkDomUtil.getSingleNodeByAttrVal(hostList, "name", "localhost");
		//直接找Context
		NodeList contexts = serverDocument.getElementsByTagName("Context");
		Node context = JdkDomUtil.getSingleNodeByAttrVal(contexts, "path", path);
		System.out.println(selfXmlStr(context));
		return context;
	}
	
	static Document parsedDocument = null;
	static {
		getParsedCommentNode();
	}
	//被解析出来的Comment Document Element对象
	public static Node getParsedCommentNode(){
		
		parsedDocument = null;
		//System.out.println(doc.getElementsByTagName("Host").getLength());
		Node hostNode = serverDocument.getElementsByTagName("Host").item(0);
		
		//第一个注释节点保存的
		Comment contextComment = (Comment)JdkDomUtil.getSingleNodeByNodeType(hostNode.getChildNodes(), Node.COMMENT_NODE);
		System.out.println(selfXmlStr(contextComment));
		//org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData)comment;
		String keepContextNodeStr = contextComment.getTextContent();
		
		try {
			parsedDocument = docBuilder.parse(new InputSource(new StringReader(keepContextNodeStr)));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return parsedDocument.getDocumentElement();
	}
	
	

	public static void saveModify(Document doc){
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(XmlHandler.serverXmlFileLocation));
			
			transformer.transform(source, result);
		}catch(Exception e){
			e.printStackTrace();
		}

		
	}
	
	
}
