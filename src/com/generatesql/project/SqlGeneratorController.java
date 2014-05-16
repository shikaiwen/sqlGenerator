package com.generatesql.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.generatesql.parse.RelationBlock;
import com.generatesql.parse.TableBlock;
import com.generatesql.util.CommUtil;
import com.generatesql.util.DomUtil;
import com.generatesql.util.JsonUtil;
import com.generatesql.util.SqlUtil;
import com.generatesql.xml.JdkDomUtil;
import com.generatesql.xml.NodeDefinition;
import com.google.gson.reflect.TypeToken;

@Controller
public class SqlGeneratorController {

	@RequestMapping(value="sqlGeneratorController/generateSql")
	public void generateSql(HttpServletRequest req,HttpServletResponse response)throws Exception{
		String jsonFileName = req.getParameter("id");
		
		int suffixLength = ".json".length();
		String withOutSuffixName = jsonFileName.substring(0, jsonFileName.length() - suffixLength);
		
		String configFileId = withOutSuffixName.substring(withOutSuffixName.lastIndexOf(".") +1 );
		
		//method �� ȫ��
		String toMethodName = jsonFileName.replace("."+configFileId+".json", "");
		
	//	String className
		String className = toMethodName.substring(0, toMethodName.lastIndexOf("."));
		
		String methodName = toMethodName.substring(className.length() +1);
		
		String sql = parseFile(req,jsonFileName);
		
		saveToFile(sql, className,methodName,configFileId,jsonFileName,req);
	}
	
	
	private String parseFile(HttpServletRequest req,String fileName) throws Exception{
		FileInputStream fis = new FileInputStream(req.getRealPath("/jsonConfigFiles") + File.separator + fileName);
		InputStreamReader isr = new InputStreamReader(fis,"utf-8");
		BufferedReader br = new BufferedReader(isr);
		String contentStr = br.readLine();
		if(contentStr == null || contentStr.length() <=0 ) new RuntimeException("δ��ȡ��"+fileName +"�ļ�����....");
		
		Type rootType = new TypeToken<Map<String,Object>>(){}.getType();
		Map<String,Object> rootObj = JsonUtil.getGson().fromJson(contentStr, rootType);
		
		
		Type tableBlockType1 = new TypeToken<List<TableBlock>>(){}.getType();
		Type relationBlockType2 = new TypeToken<List<RelationBlock>>(){}.getType();
		
		List<TableBlock> tableBlockList = JsonUtil.getGson().fromJson(JsonUtil.toJsonStr(rootObj.get("tableBlock")), tableBlockType1);
		List<RelationBlock> relationBlockList = JsonUtil.getGson().fromJson(JsonUtil.toJsonStr(rootObj.get("relationBlock")), relationBlockType2 );
		
		
		Map<String,List<String>> tableList = new TreeMap<String,List<String>>();
		
		// key : alias value:tableName
		Map<String,String> aliasTableName = new HashMap<String,String>();
		for(TableBlock block : tableBlockList){
			String alias = block.getAlias();
			if(! CommUtil.availiable(alias)){
				alias = CommUtil.toStandardJavaName(block.getTableName());
			}
			aliasTableName.put(alias, block.getTableName());
		}
		
		//has used cols
		Set<String> usedColsSet = new LinkedHashSet<String>();
		
		//key alias  value : useCols
		Map<String,List<String>> aliasCols = new HashMap<String,List<String>>();
		
		
		for(int i =0; i< tableBlockList.size();i++){
			TableBlock tBlock = tableBlockList.get(i);
			List<String> useCols = tBlock.getUseCols();
			String alias = tBlock.getAlias();
			if(! aliasCols.containsKey(tBlock.getAlias())){
				aliasCols.put(alias, new LinkedList<String>());
			}
			aliasCols.get(alias).addAll(useCols);
			
		}
		
		// handle the relations
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		//��table�� ���ǵ�һ��relation�ĵ�һ����Ҳ����from����ı�
		
		//handled Alias �Ѿ�������ı����
		List<String> handledAlias = new ArrayList<String>();
		
		// cols has already in select sql
		Set<String> colsProcessed = new HashSet<String>();
		
		for(int n = 0;n < relationBlockList.size();n++){
			RelationBlock rBlock = relationBlockList.get(n);
			
			String t1Alias = rBlock.getTable1Alias();
			String t1Col = rBlock.getTable1Col();
			String t2Alias = rBlock.getTable2Alias();
			String  t2Col = rBlock.getTable2Col();
			
			if(! handledAlias.contains(t1Alias)){
				
				List<String> usedCols = aliasCols.get(t1Alias);
				
				for(int k = 0;k < usedCols.size();k++){
					String col = usedCols.get(k);
					if(colsProcessed.contains(col)) continue;
					colsProcessed.add(col);
					sb.append(t1Alias + "." + col + " as " + CommUtil.toStandardJavaName(col));
					if(k != usedCols.size() - 1 ){
						sb.append(",");
					}else{
						sb.append(" ");
					}
					
				}
			}
			
			if(! handledAlias.contains(t2Alias)){
				
				List<String> usedCols = aliasCols.get(t1Alias);
				
				for(int k = 0;k < usedCols.size();k++){
					String col = usedCols.get(k);
					if(colsProcessed.contains(col)) continue;
					sb.append(t2Alias + "." + col + " as " + CommUtil.toStandardJavaName(col));
					if(k != usedCols.size() - 2 ){
						sb.append(",");
					}else{
						sb.append(" ");
					}
				}
			}
		
		}
		
		// add relation
		
		
		for(int n = 0; n < relationBlockList.size() ; n++){
			RelationBlock rBlock = relationBlockList.get(n);
			String t1Alias= rBlock.getTable1Alias();
			String t1Col = rBlock.getTable1Col();
			String t2Alias = rBlock.getTable2Alias();
			String  t2Col = rBlock.getTable2Col();
			
			String joinType = rBlock.getJoinType();
			String condition = rBlock.getCondition();
			
			List<String> toAddCols = aliasCols.get(t1Alias);
			if(n == 0){
			//	for(int i =0; i < toAddCols.size() ;i++){
					
					
				//	String asName = CommUtil.toStandardJavaName(toAddCols.get(i));
				//	String sqlStr = "${t1Alias}.${colName} as " +asName;
				//	sb.append( n != toAddCols.size()? ",":" " );
				//	sb.append(t1Alias+"."+ toAddCols.get(i) + " as " + asName ).append(n != toAddCols.size()-1? ",":" ");
					
					// add the cols into usedColsSet
				//	usedColsSet.add(toAddCols.get(i));
			//	}
				sb.append(" from " + aliasTableName.get(t1Alias));
				String joinTypeStr = CommUtil.getJoinType(joinType);
				sb.append(joinTypeStr );
				
				sb.append(aliasTableName.get(t2Alias) +" " +t2Alias+" on ");
				sb.append(t1Alias+"."+t1Col + " "+condition+" "+t2Alias+"."+t2Col);
				
				handledAlias.add(t1Alias);
				handledAlias.add(t2Alias);
			}else{
				
				if(!handledAlias.contains(t1Alias)){
					
					String joinTypeStr = CommUtil.getJoinType(joinType);
					sb.append(" "+joinTypeStr+" ");
					String t1AliasTableName = aliasTableName.get(t1Alias);
					sb.append(t1AliasTableName + " on ");
					
					sb.append(t2Alias+"."+t2Col +" " + condition +" " +t1Alias+"."+t1Col);
					
				}else if(!handledAlias.contains(t2Alias)){
					String joinTypeStr = CommUtil.getJoinType(joinType);
					sb.append(" "+joinTypeStr+" ");
					String t2AliasTableName = aliasTableName.get(t2Alias);
					sb.append(t2AliasTableName + " on ");
					
					//t1Ӧ�þ���ǰ���Ѿ����˵ı�
					sb.append(t1Alias+"."+t1Col +" " + condition +" " +t2Alias+"."+t2Col);
				}else{
					throw new RuntimeException("δ���ֱ���Ϊ"+t1Alias +" ����"+t2Alias+"�ı�");
				}
			}
		}
		return sb.toString();
	}
	
	
	//�����ļ�
	public void saveToFile(String sql,String className,String methodName,String configId,String configFileName,HttpServletRequest req)
			throws IOException, TransformerException, SAXException{
		
		// format the sql with hibernate 
		sql = SqlUtil.format(sql);
		
		String webRootDir = req.getRealPath("/");
		File sqlDir = new File(webRootDir + File.separator + "sqlXml");
		if(! sqlDir.exists()){
			//sqlDir.createNewFile();
			// if the directory to put sql files does not exsit , then create it
			sqlDir.mkdir();
		}
		
		// use the className as the sqlFile name
		File sqlXmlFile = new File(sqlDir.getPath() + File.separator + className +".xml");
		
		if( !sqlXmlFile.exists()){
		//	sqlXmlFile.createNewFile();
			// new file ,nothing exists
			Document doc = DomUtil.getDocumentBuilder().newDocument();
			
			Element rootElt = doc.createElement(NodeDefinition.NODE_ROOT_NAME);
			rootElt.setAttribute("name", className);
			doc.appendChild(rootElt);
			
			//methodNode
			Element methodNode = doc.createElement(NodeDefinition.NODE_METHOD_NAME);
			methodNode.setAttribute("name", methodName);
			rootElt.appendChild(methodNode);
			
			//sqlNode         id and configFile
			Element sqlNode = doc.createElement(NodeDefinition.NODE_SQL_NODE_NAME);
			sqlNode.setAttribute("id", configId);
			sqlNode.setAttribute("configFile", configFileName);
			methodNode.appendChild(sqlNode);
			
			// currentSql and oldSql
			Element currentSqlElt = doc.createElement(NodeDefinition.NODE_CURRENT_SQL_NAME);
			sqlNode.appendChild(currentSqlElt);
			
			CDATASection cData1 = doc.createCDATASection(sql);
			currentSqlElt.appendChild(cData1);
			
			Element oldSqlElt = doc.createElement(NodeDefinition.NODE_OLD_SQL_NAME);
			sqlNode.appendChild(oldSqlElt);
			
			CDATASection cData2 = doc.createCDATASection(""); 
			oldSqlElt.appendChild(cData2);
			
			sqlNode.appendChild(oldSqlElt);
			
			DomUtil.transformToFile(doc, sqlXmlFile);

		}else{
			
			FileInputStream fis = new FileInputStream(sqlXmlFile.getPath());
			
			
			// there would be some problem : premature end of file ,if the file is empty or have no content
			DocumentBuilder builder = DomUtil.getDocumentBuilder();
			Document doc = builder.parse(fis);
			
			Element rootElt = doc.getDocumentElement();
			// find the method node
			Node neededMethodNode = null;
			NodeList nodeList = rootElt.getElementsByTagName(NodeDefinition.NODE_METHOD_NAME);
			neededMethodNode = JdkDomUtil.getSingleNodeByAttrVal(nodeList, "name", methodName);

			//���û��
			if( neededMethodNode != null){
				
				Node neededSqlNode = null;
				NodeList childNodes = neededMethodNode.getChildNodes();
				neededSqlNode = JdkDomUtil.getSingleNodeByAttrVal(childNodes, "id", configId);
				
				if(neededSqlNode != null){
					
					//����sqlNode���޸ĸ�Node
					NodeList sqlNodeChildList = neededSqlNode.getChildNodes();
					Node currentSqlNode = JdkDomUtil.getSingleNodeByNodeName(sqlNodeChildList, NodeDefinition.NODE_CURRENT_SQL_NAME);
					Node currentSqlCDATA = JdkDomUtil.getSingleNodeByNodeType(
											currentSqlNode.getChildNodes(), 
											Node.CDATA_SECTION_NODE);
					//�������ڵ�sql
					String curSql = currentSqlCDATA.getTextContent();
					currentSqlCDATA.setTextContent(sql);
					
					Node oldSqlNode = JdkDomUtil.getSingleNodeByNodeName(sqlNodeChildList, NodeDefinition.NODE_OLD_SQL_NAME);
					Node oldSqlCDATA = JdkDomUtil.getSingleNodeByNodeType(oldSqlNode.getChildNodes(), Node.CDATA_SECTION_NODE);
					oldSqlCDATA.setTextContent(curSql);
					
					DomUtil.transformToFile(doc, sqlXmlFile);
					
				}else{
					// methodNode ���ڵ���sqlNode ������
					// ����sqlNode���Ҵ��������currentSql��oldSql�Լ���CDATA�ڵ�
					Element sqlNode = doc.createElement(NodeDefinition.NODE_SQL_NODE_NAME);
					
					Element currentSqlNode = doc.createElement(NodeDefinition.NODE_CURRENT_SQL_NAME);
					Node currentSqlNodeCDATA = doc.createCDATASection(sql);
					currentSqlNode.appendChild(currentSqlNodeCDATA);
					
					//���currentSql�ڵ�
					sqlNode.appendChild(currentSqlNode);
					
					Element oldSqlNode = doc.createElement(NodeDefinition.NODE_OLD_SQL_NAME);
					Node oldSqlCDATA = doc.createCDATASection("");		//���ǵ�һ�δ�������û��oldsql
					oldSqlNode.appendChild(oldSqlCDATA);
					
					sqlNode.appendChild(oldSqlNode);
					
					neededMethodNode.appendChild(sqlNode);
					
					DomUtil.transformToFile(doc, sqlXmlFile);
					
				}
			}else{
				//����methodNode�ڵ�
				Element methodNode = doc.createElement(NodeDefinition.NODE_METHOD_NAME);
				rootElt.appendChild(methodNode);
				
				File templateDir = new File(req.getRealPath("/sqlXml/templates"));
				File templateFile = new File(templateDir.getPath() + File.separator + "methodNodeTemplate.xml" );
				Document methodNodeDoc = DomUtil.getDocumentBuilder().parse(templateFile);
				Element methodNodeElt = methodNodeDoc.getDocumentElement();
				
				doc.appendChild(methodNodeElt );
			}
			
		}
	}
}
