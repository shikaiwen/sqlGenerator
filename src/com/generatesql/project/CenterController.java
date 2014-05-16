package com.generatesql.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CenterController {

	public static final int  DEFAULT_COLUMN_PER_ROW = 4;
	@RequestMapping(value="centerController/getTableInfo")
	public void getTableInfo(HttpServletRequest request,HttpServletResponse response){
		String tableName = request.getParameter("tableName");
		String resultStr = Test1.getTableInfo(tableName.toUpperCase());
		System.out.println(resultStr);
		try{
			response.getWriter().write(resultStr);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	
	
	//处理从客户端 前台发送过来的Json 数据
	@RequestMapping(value="centerController/handleJson")
	public void handleJson(HttpServletRequest req,HttpServletResponse  response)throws Exception{
		
		String jsonStr = req.getParameter("jsonStr");
		String jsonFileName = req.getParameter("jsonFileName");
//		Type type = new TypeToken<Map<String,Object>>(){}.getType();
//		Map<String,Object> jsonObj = JsonUtil.getGson().fromJson(jsonStr, type);
//		
//		
//		for(String key : jsonObj.keySet()){
//			System.out.println("key:"+ key +"   value:"+ jsonObj.get(key));
//		}
		
		String methodName = req.getParameter("id");// method name
		String canonicalClassName = req.getParameter("canonicalClassName");
		
		String fileName = canonicalClassName + "."+ methodName +"."+ jsonFileName+".json"; 
		//文件名称temp.json
		String realPath = req.getSession().getServletContext().getRealPath("/");
		FileOutputStream fos = new FileOutputStream(realPath +File.separator +"jsonConfigFiles"+File.separator + fileName);
		
		byte [] bytes = jsonStr.getBytes("utf-8");
		fos.write(bytes);
		fos.flush();
		fos.close();
		
		//返回 文件 名
		response.getWriter().write(fileName);
	}
	
	//查看保存的json文件
	@RequestMapping(value="centerController/toView")
	public ModelAndView toView(HttpServletRequest req,HttpServletResponse response) throws Exception{
		String fileName = req.getParameter("fileName");
		ModelAndView mav = new ModelAndView("viewPage.jsp");
		
		File file = new File(req.getRealPath("/jsonConfigFiles") + File.separator + fileName);
		
		FileInputStream fis = new FileInputStream(file.getPath());
		
		byte [] bytes = new byte[(int)file.length()];
		
		fis.read(bytes);
		String jsonStr = new String(bytes);
		mav.addObject("fileName", fileName);
		mav.addObject("jsonStr",jsonStr);
		return mav;
	}
	
	//还原sql
	@RequestMapping(value="sqlGen/recoverSql")
	public void recoverSql(HttpServletRequest req,HttpServletResponse response) throws Exception{
		FileInputStream fis = new FileInputStream(req.getRealPath("/")+File.separator + "temp.json");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fis,"utf-8"));
		String jsonStr = br.readLine();
		br.close();
		response.setContentType("contentType:text/html ; charset:utf-8");
		response.getWriter().write(jsonStr);
	}
	
	
	@RequestMapping(value="sqlGen/getSql")
	public void getSql(HttpServletRequest request,HttpServletResponse response){
		String s = request.getParameter("columnPerRow");
		
		System.out.println(s);
		String columnPerRowStr = (String)request.getParameter("columnPerRow");
		int columnPerRow = DEFAULT_COLUMN_PER_ROW;
		if( ! isEmptyOrNull(columnPerRowStr)) columnPerRow = Integer.parseInt(columnPerRowStr);

		
		String columnStr = (String)request.getParameter("columnStr");
		String tableName = (String)request.getParameter("tableName");
		String alias = (String)request.getParameter("alias");
		
		String[] columnArray = columnStr.split(",");
		StringBuffer resultStr = new StringBuffer();
		boolean hasAlias = isEmptyOrNull(alias)? false:true;
		
		int count = 0;
		for(int i = 0; i < columnArray.length; i ++){
			if(i == 0){
				resultStr.append("sql.append( \"select \");\n");
			}
			count ++;
			if(count ==1){
				
				if(hasAlias){
					resultStr.append("sql.append(\""+alias+"."+columnArray[i]);
					resultStr.append(" as "+toStandardJavaName(columnArray[i]) );
				}else{
					resultStr.append("sql.append(\""+columnArray[i]);
				}
				resultStr.append("\"");	
				
			}else{
				
				if(hasAlias){
					resultStr.append(","+alias+ "."+columnArray[i]);
					resultStr.append(" as "+ toStandardJavaName(columnArray[i]));
				}else{
					resultStr.append(","+columnArray[i]);
				}
				resultStr.append("\"");
			}
			if(count == columnPerRow || i == columnArray.length -1){
				if(i != columnArray.length -1) resultStr.append(",");
				resultStr.append(");");
				resultStr.append("\n");
				count = 0;
			}
			
		}
		resultStr.append("sql.append(\"from "+ tableName +" "+ alias+"\");");
		resultStr.append("\n");
		System.out.println(resultStr.toString());
		try{
			response.getWriter().write(resultStr.toString());
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static String toStandardJavaName(String str){
		
		StringBuffer sb = new StringBuffer();
		String[] arr = str.split("_");
		for(int i = 0; i < arr.length; i++){
			if(i == 0 ){
				sb.append(arr[i]);
			}else{
				char [] charArr = arr[i].toCharArray();
				charArr [0] = (charArr[0] + "").toUpperCase().charAt(0);
				sb.append(String.valueOf(charArr));
			}
		}
		return sb.toString();
	}
	
	@RequestMapping(value="sqlGen/processXml")
	public void processXml(HttpServletRequest request){
		String xmlData = request.getParameter("xmlData");
		XmlParse.parseFromStr(xmlData);
		System.out.println(xmlData);
	}
	
	
	
	public boolean isEmptyOrNull(String str){
		
		if(null == str) return true;
		if("".equals(str)) return true;
		if("null".equals(str)) return true;
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println((String)null);
	}
	
}
