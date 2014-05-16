package com.generatesql.xml;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;

import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tomcat.util.DomUtil;

public class JdkDomUtil {

	static Logger logger = Logger.getLogger(JdkDomUtil.class);
	
	//����ж�������ص�һ��
	public static Node getSingleNodeByNodeName(NodeList nodeList,String nodeName){
		if(nodeList == null) throw new RuntimeException("the nodeList is null");
		for(int i = 0; i< nodeList.getLength() ;i++){
			Node node = nodeList.item(i);
			if(nodeName.equals(node.getNodeName()) ){
				return node;
			}
		}
		return null;
	}
	
	//����ֵ����ֵ���ҽڵ㣬����ж�����ص�һ��
	public static Node getSingleNodeByAttrVal(NodeList nodeList,String attrName,String attrValue){
		if(nodeList == null) {
			logger.error("the nodeList passed in is null");
			throw new RuntimeException("the nodeList passed in is null");
		}
		
		for(int i = 0 ;i < nodeList.getLength() ; i++) {
			
			Node node = nodeList.item(i);
			NamedNodeMap attrMap = node.getAttributes();
			
			if(attrMap != null){
				Node attrNode = attrMap.getNamedItem(attrName);
				if(attrNode != null && attrValue.equals( attrNode.getNodeValue() )){
					return node; 
				}
			}

		}
		return null;
	}
	
	//�������Ͳ����ӽڵ㣬����ж�����ص�һ��
	public static Node getSingleNodeByNodeType(NodeList nodeList ,short nodeType){
		
		for(int index = 0; index < nodeList.getLength(); index ++){
			Node node = nodeList.item(index);
			if(nodeType == node.getNodeType() ){
				return node;
			}
		}
		return  null;
	}
	
	// get node attribute 
	public static String getNodeValue(Node node,String attrName){
		NamedNodeMap nameValMap = node.getAttributes();
		Node attrNode = nameValMap.getNamedItem(attrName);
		return attrNode.getNodeValue();
	}
	
	
	public static Node generateNodeWithStr(String nodeStr){
		
		Node node = null;
		try{
			DocumentBuilder builder = DomUtil.getBuilder();
			node = builder.parse(
					new  InputSource ( new StringReader(nodeStr) )
					).getDocumentElement();
		}catch(Exception e){
			e.printStackTrace();
		}
		return node;
	}
}
