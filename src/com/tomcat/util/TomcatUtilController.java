package com.tomcat.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.generatesql.xml.JdkDomUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Controller
public class TomcatUtilController {

	NodeList inUseContexts = null;
	NodeList notUseContexts = null;
	
	@RequestMapping(value="tomcatUtilController/tomcatUtil.htm")
	public void main(HttpServletRequest req,HttpServletResponse resp) throws Exception{

		
		Map<String,NodeList> contextsMap = XmlHandler.getContextsNode();
		
		inUseContexts = contextsMap.get("inUseContexts");
		notUseContexts = contextsMap.get("notUseContexts");
		
		List<String> inUseList = new ArrayList<String>();
		for(int i =0; i < inUseContexts.getLength() ;i ++){
			inUseList.add( XmlHandler.selfXmlStr(inUseContexts.item(i)) );
		}
		
		Map<String,Object> rootMap = new HashMap<String,Object>();
		rootMap.put("inUserContextsList", inUseList);
		
		List<String> notUseList = new ArrayList<String>();
		for(int i =0; i<notUseContexts.getLength();i++){
			notUseList.add(  XmlHandler.selfXmlStr( notUseContexts.item(i) ) );
		}
		rootMap.put("notUseContextsList",notUseList );
		rootMap.put("rc", req);
		
		Template template = this.cofigFreeMarkerTemplate(req);
		
		StringWriter sw = new StringWriter();
 		
		template.process(rootMap, sw);

		resp.setContentType("text/html;charset=utf-8");
		resp.getWriter().write(sw.toString());
	}
	

	
	public Template cofigFreeMarkerTemplate(HttpServletRequest req) throws Exception{
		Configuration config = new Configuration();
		String filePath = req.getRealPath("/") + File.separator + "tomcatutil" + File.separator +"aa.html";
	
		
		InputStream is = new FileInputStream(filePath);
//		
//		
//		URL url = this.getClass().getResource("aa.html");
		
		File f = new File(filePath);
		 
		byte [] fileBytes = new byte[(int)f.length()];
		
		is.read(fileBytes);
		
		String htmlStr = new String(fileBytes);
		
	System.out.println(htmlStr);
		
		StringReader sr = new StringReader(htmlStr);
		
		Template template = new Template("contextsContent",sr,config);
		return template;
	}
	//13762842599
	
	@RequestMapping(value="tomcatUtilController/saveChanges.htm")
	public void saveChanges(HttpServletRequest req,HttpServletResponse resp) throws Exception{
		String [] turnOnContexts = req.getParameterValues("on[]");
		String [] turnOffContexts = req.getParameterValues("off[]");
		
		
		Document serverDocument = XmlHandler.serverDocument;
		//解析之后独立的document
		Node parsedCommentDoc = XmlHandler.getParsedCommentNode();
		
		
		if( turnOffContexts != null){
			for(int i = 0;i< turnOffContexts.length ;i++){
				String path = turnOffContexts[i];
				// server.xml中的context 节点
				Node context = XmlHandler.getContextNode(path);
				
				//不能直接克隆再添加，因为节点不属于同一个document
				String contextStr = XmlHandler.selfXmlStr(context);
				
				Node fragmentNode = JdkDomUtil.generateNodeWithStr(contextStr);
				
				fragmentNode = XmlHandler.parsedDocument.importNode(fragmentNode, true);
				parsedCommentDoc.appendChild( fragmentNode );
				
				
				
				//从document中删除该节点,必须从其父节点删除
				context.getParentNode().removeChild(context);
				
			//	Node parseCommentInXmlNode = serverDocument.importNode(parsedComment, true);
				Comment commentNow =  serverDocument.createComment( XmlHandler.selfXmlStr(parsedCommentDoc) );
				//替换原有的comment
				Node commentInXml = XmlHandler.getCommentInXml();
				commentInXml.getParentNode().replaceChild(commentNow, commentInXml);
			}
		}
		
		if(turnOnContexts != null){
			
			//找到第一个
			NodeList pathList = parsedCommentDoc.getChildNodes();
			System.out.println(pathList.getLength());
			for(int i =0;i< turnOnContexts.length;i++){
				Node context = JdkDomUtil.getSingleNodeByAttrVal(pathList, "path", turnOnContexts[i]);
			//	Node contextWithNoFather = JdkDomUtil.generateNodeWithStr( XmlHandler.selfXmlStr(context));
				Node contextInXml = serverDocument.importNode(context, true);
				
				//删除注释中的
				context.getParentNode().removeChild(context);
				
				//	Node parseCommentInXmlNode = serverDocument.importNode(parsedComment, true);
				Comment commentNow =  serverDocument.createComment( XmlHandler.selfXmlStr(parsedCommentDoc) );
				//替换原有的comment
				Node commentInXml = XmlHandler.getCommentInXml();
				commentInXml.getParentNode().replaceChild(commentNow, commentInXml);
				
				
			//	XmlHandler.getHostNode().appendChild(contextInXml);
				//insert插入前会检查，如果已经有了这个节点就会先删除再插入 
			//	contextInXml.insertBefore(XmlHandler.getCommentInXml(), contextInXml);
				
			//	插入节点必须在父节点上调用方法
				XmlHandler.getHostNode().insertBefore(contextInXml, XmlHandler.getHostNode().getFirstChild());
			}
			
		}
		
		XmlHandler.saveModify(serverDocument);
	}
	
	@RequestMapping(value="tomcatUtilController/stopTomcat.htm")
	public void stopTomcat(){
		Socket socket = new Socket();
		//socket.getOutputStream();
	}
}
