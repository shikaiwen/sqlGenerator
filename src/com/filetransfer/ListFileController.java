package com.filetransfer;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.generatesql.util.JsonUtil;

@Controller
public class ListFileController {

	public static void main(String[] args) throws Exception{

		String path = "H:\\all_temp\\施纯峰"; 
		File f = new File(path);
		FileInputStream fis = new FileInputStream(f);
		
		byte [] b = new byte[(int)f.length()];
		fis.read(b);
	}
	
	
	@RequestMapping(value="listFileController/listRootFile.htm")
	public static void listRootFile(HttpServletRequest req,HttpServletResponse resp)throws Exception{
		
		File[] rootFiles = File.listRoots();
		StringBuffer sb = new StringBuffer();
//		Map<String,Object> rootMap = getTreeStrModel();
		List rootList = new ArrayList();
		for(File f : rootFiles){
			String name = f.getPath();
			Map m = getTreeStrModel();
			m.put("name", name);
			m.put("isParent", true);
			m.put("path", f.getPath());
			rootList.add(m);
		}
		resp.setCharacterEncoding("utf-8");
		String treeStr = JsonUtil.toJsonStr(rootList);
		System.out.println(treeStr);
		resp.getWriter().write(treeStr);
	}
	
	
	@RequestMapping(value="listFileController/getSubNodes.htm")
	public void listSubNode(HttpServletRequest req,HttpServletResponse resp)throws Exception{
		String path = req.getParameter("path");
		
		File rootFile = new File(path);
		if(rootFile == null) throw new RuntimeException(path +"不存在");
		
		List root = new LinkedList();
		File [] subFiles = rootFile.listFiles();
		if(subFiles != null){
			for(File f : subFiles){
				Map m = getTreeStrModel();
				m.put("name", f.getName());
				if(f.isDirectory()){
					m.put("isParent", true);
				}
				m.put("path", f.getPath());
				
				root.add(m);
			}
		}
		String treeStr = JsonUtil.toJsonStr(root);
		resp.getWriter().write(treeStr);
	}
	
	
	
	public static StringBuffer getSubFileTreeStr(File f,StringBuffer sb){
		File [] files = f.listFiles();
		if(files != null){
			for(int i = 0; i < files.length ; i++){
				File file = files[i];
				String name = file.getName();
				if(file.isDirectory()){
					sb.append("{\"name\":"+"\""+name+"\",\"children\":[");
					getSubFileTreeStr(file, sb);
					sb.append("]}");
				}else{
					sb.append("{\"name\":" + "\""+name+"\"}");
				}
				sb.append(  (i== files.length -1) ? "":"," );
			}
		}

		return sb;
	}
	
	
	@RequestMapping(value="listFileController/downloadFile.htm")
	public void downloadFile(HttpServletRequest req,HttpServletResponse resp) throws Exception{
	//	req.setCharacterEncoding("UTF-8");;
		String path = req.getParameter("path");
		String realPath = new String(path.getBytes("iso-8859-1"),"utf-8");
		System.out.println ( new String(path.getBytes("iso-8859-1") , "GBK"));
		System.out.println(realPath);
		File file = new File(realPath);
		if(!file.exists()) throw new RuntimeException(path + "文件不存在");
		
		
		byte [] fileBytes = new byte[(int)file.length()];
		FileInputStream fis = new FileInputStream(file.getPath());
		
		fis.read(fileBytes);
//		resp.setContentType("application/abcdx;charset=UTF-8");
		String fileName = file.getName();
		String asciiName = URLEncoder.encode(fileName);
		String utf8Name = URLEncoder.encode(fileName, "utf-8");
		resp.addHeader("Content-Disposition", "attachment; filename="+"5Lit5Zu9LnppcA=="+"; filename*=utf8''"+"5Lit5Zu9LnppcA=="+"" );
		
		resp.getOutputStream().write(fileBytes);
		
		//如果是目录，则先压缩然后再下载
		if(file.isDirectory()){
			
		}
		

	}
	public static Map<String,Object> getTreeStrModel(){
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("children", new ArrayList());
		return model;
	}
}
