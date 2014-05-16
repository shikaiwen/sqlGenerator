package com.generatesql.packageutil;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generatesql.util.JsonUtil;
@Controller
public class PackageController {

	// src folder 
	public  List<String> srcList = Arrays.asList("src");
	
	public void getPackageData(){
		
	}
	
	public static void main(String[] args) throws Exception{

		Class clazz = Class.forName("java.lang.String[]");
		System.out.println(clazz);
	//	System.out.println(aa.g);
	}
	
	// save the package info and java files in this package
	static Map<String,Map<String,File>> packageFileMap = new HashMap<String,Map<String,File>>();
	
	
	@RequestMapping(value="packageController/getPackageInfo")
	public  void getPackageInfo(HttpServletRequest req ,HttpServletResponse response){
		
		String src = "src";
		String projectPath = req.getRealPath("/");
	//	String projectPath = "F:/myprojects/sqlGen1.3/WebContent";
		
		File webContentDir = new File(projectPath);
		File projectDir = webContentDir.getParentFile();
		
		File srcFolder = new File(projectDir.getPath() + File.separator + src);
		
		List<File> srcJavaFiles = this.getFilesInFolder(srcFolder);
		
		//default包的文件处理
		Map<String,File> defaultPackageFile = new HashMap<String,File>();
		for(File file : srcJavaFiles){
			String name = file.getName().substring(0, file.getName().indexOf(".java"));
			defaultPackageFile.put(name, file);
		}
		
		packageFileMap.put("default", defaultPackageFile);
		
		//其他包的文件处理
		File [] packageFolders = srcFolder.listFiles();
		for(File file : packageFolders){
			
			if(file.isDirectory()){
				handle(file);
			}
		}
		
		System.out.println(JsonUtil.toJsonStr(packageFileMap));
		String treeNodeStr = this.wrapToZTreeData(packageFileMap);
		
		try{
			response.getWriter().write(treeNodeStr);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//递归处理
	public  void handle(File file){
		
		if( file.isDirectory() ){
			
			// get java files
			File [] files = file.listFiles();

			for(File f : files){
				if ( f.isDirectory() ){
					handle(f);
					
				}else if(f.getName().endsWith(".java")){
					//将此java文件放入map中					
					String packageName = this.getPackageName(f);
					if(! packageFileMap.containsKey(packageName)){
						packageFileMap.put( packageName, new HashMap<String,File> () );
					}
					
					packageFileMap.get(packageName).put( f.getName(), f );
				}
			}
			
		}
	}
	
	public  String getPackageName(File file){
		
		for(String srcFolder : srcList){
			
			String filePath = file.getPath();
			String packageDirStr = filePath.substring( filePath.indexOf(srcFolder) + srcFolder.length() + 1);
			
			packageDirStr = packageDirStr.replace(file.getName(), "");
			packageDirStr = packageDirStr.substring(0, packageDirStr.length()-1);
			packageDirStr = packageDirStr.replace(File.separator, ".");
			if( !packageDirStr.equals("") && !(packageDirStr == null) && !(packageDirStr.length() == 0) ){
				return packageDirStr;	
			}
		}
		System.out.println("获取包名称失败....文件名:"+file.getPath());
		return null;
		
	}
	
	
	public  List<File> getFilesInFolder(File fileFolder){
	
		File [] files = fileFolder.listFiles(new FileFilter(){
			public boolean accept(File pathname) {
				if(pathname.getName() != null && pathname.getName().endsWith(".java") ){
					return true;
				}
				return false;
			}
		});
		if(files == null) return null;
		return Arrays.asList(files);
	}

	//将数据包装成ztree的树形结构
	public  String wrapToZTreeData(Map<String,Map<String,File>> data){
		
		List<Map> ztreeObj = new LinkedList<Map>();
		
		Set<Entry<String,Map<String,File>>> entrySet = data.entrySet();
		
		List rootEltList = new LinkedList();
		
		for(Entry<String,Map<String,File>> entry : entrySet){
			
			//元素的即对象
			Map eltMap = new HashMap();
			
			String packageName = entry.getKey();
			eltMap.put("name", packageName);
			eltMap.put("isParent", true);
			List childrenList = CommUtil.getList();
			eltMap.put("children", childrenList);
			
			Map<String,File> javaFileInPackage = entry.getValue();
			for(String str : javaFileInPackage.keySet()){
			//	这一层是java文件，加上属性type=java
				Map childMap = CommUtil.getHashMap();
				childMap.put("name", str);	//节点的name是java文件全称 aa.java
				childMap.put("id",str.replace(".java", ""));   //id为不带后缀的名称
				
				childMap.put("isParent", true);
				childMap.put("type", "java");
				childMap.put("packageName", packageName);
				
				File file = javaFileInPackage.get(str);
				childMap.put("path", file.getPath());
				
				childrenList.add(childMap);
			}
			
			
			 //添加到主List中
			rootEltList.add(eltMap);
		}
		
		return JsonUtil.toJsonStr(rootEltList);
	}
	
	
	@RequestMapping(value="packageController/getSubNodes")
	public void getSubNodes(HttpServletRequest req,HttpServletResponse resp)throws Exception{
		
		
		
		String type = req.getParameter("type");
		String name = req.getParameter("name");
		String path = req.getParameter("path");
		String packageName = req.getParameter("packageName");

		if("java".equals(type)){
			handleJavaType(req,resp);
		}else if("method".equals(type)){
			handleMethodType(req,resp);
		}
		

	}
	
	public void handleMethodType(HttpServletRequest req ,HttpServletResponse resp) throws Exception{
		String canonicalClassName = req.getParameter("canonicalClassName");
		String methodName = req.getParameter("id");
		
		final String queryPrefix = canonicalClassName + "." +methodName;
		File jsonFileDir = new File(req.getRealPath("/jsonConfigFiles"));
		
		File [] resultFiles = jsonFileDir.listFiles(new FileFilter(){
			 @Override
			public boolean accept(File pathname) {
				if(pathname.isDirectory()) return false;
				boolean needed = pathname.getName().startsWith(queryPrefix);
				if(needed){
					return  true;
				}
				return false;
			}
		});
		
		List jsonFileList = new ArrayList();
		
		
		for(File jsonFile : resultFiles){
			Map map = new HashMap();
			map.put("name", jsonFile.getName());
			map.put("id", jsonFile.getName());
			map.put("type", "jsonFile");
			jsonFileList.add(map);
		}
		resp.getWriter().write(JsonUtil.toJsonStr(jsonFileList));
	}
	
	
	public void handleJavaType(HttpServletRequest req,HttpServletResponse resp)throws Exception{
		
		
		String type = req.getParameter("type");
		String name = req.getParameter("name");
		String path = req.getParameter("path");
		String packageName = req.getParameter("packageName");
		
		System.out.println("type:"+ type + "  name:"+ name +" packageName:"+packageName);
		
		String classFileName = packageName + "." + name;
		classFileName = classFileName.substring(0, classFileName.indexOf(".java"));
		
		try{
			Class clazz = Class.forName(classFileName);
			
			Method [] methods = clazz.getDeclaredMethods();
			
			List childrenList = CommUtil.getList();
			
			for(Method method : methods){
				
				String methodName = method.getName();
				Class[] paramTypes = method.getParameterTypes();
				Parameter [] params = method.getParameters();
				
				//节点名称  methodName(type1,type2)
				Map childMap = CommUtil.getHashMap();
				
				StringBuilder sb = new StringBuilder();
				sb.append("( ");
				for(int i =0; i < paramTypes.length ; i++){
					sb.append(paramTypes[i].getSimpleName())
					.append(" " + params[i].getName())
					.append( i == paramTypes.length-1 ? "":",");
				}
				sb.append(" )");
				
				childMap.put("name", methodName + sb.toString() );
				//将类信息放到属性中
				childMap.put("canonicalClassName",clazz.getCanonicalName() );
				//方法信息，确保能找到具体方法 ,方法之间用;号分隔
				StringBuilder paramTypeSb = new StringBuilder(); 
				for(int i =0;i<paramTypes.length;i++){
					paramTypeSb.append(paramTypes[i].getCanonicalName()).append(i == paramTypes.length -1 ? "":";");
				}
				childMap.put("paramTypeArrStr", paramTypeSb.toString());
				childMap.put("id",method.getName());
				childMap.put("isParent", true);
				childMap.put("type", "method");
				childrenList.add(childMap);
			}
			
			resp.getWriter().write(JsonUtil.toJsonStr(childrenList));
			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping(value="packageController/getJsonFile")
	public void getJsonFile(HttpServletRequest req ,HttpServletResponse response)throws Exception{
		String fileName = req.getParameter("file");
		File fi = new File(req.getRealPath("/jsonConfigFiles") + File.separator + fileName);
		
		FileInputStream fis = new FileInputStream(fi.getPath());
		
		byte [] bytesLength = new byte[(int)fi.length()];
		fis.read(bytesLength);
		//response.getWriter().write(bytesLength);
		response.getOutputStream().write(bytesLength);
	}
	
	
	//更新json文件内容
	@RequestMapping(value="packageController/updateJson")
	public void updateJson(HttpServletRequest req,HttpServletResponse response)throws Exception{
		String fileName = req.getParameter("fileName");
		String jsonStr = req.getParameter("jsonStr");
		FileOutputStream fos = new  FileOutputStream(req.getRealPath("/jsonConfigFiles") + File.separator + fileName);
		
		byte [] bytes = jsonStr.getBytes();
		fos.write(bytes);
		fos.flush();
		fos.close();
	}
}