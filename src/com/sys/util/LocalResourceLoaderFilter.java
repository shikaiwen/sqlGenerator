package com.sys.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LocalResourceLoaderFilter implements Filter{

	public static int buffSize = 200;
	
	Properties configProperties = new Properties();
	
	public void doFilter(ServletRequest req, ServletResponse resp,FilterChain chain) throws IOException, ServletException {
			
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String path = uri.replace(contextPath, "");
		int index = uri.indexOf("/", contextPath.length()+1);
		String mappingPrefix = uri.substring(contextPath.length()+1, index);
		//这里将/号也去掉了
		String toResolvePath = uri.substring(contextPath.length() + 2 + mappingPrefix.length());
		System.out.println(toResolvePath);
		String [] finalArray = toResolvePath.split("/");
		String key = finalArray[0];
		
		StringBuffer relativePath = new StringBuffer();
		for( int i = 1 ;i< finalArray.length;i++){
			relativePath.append(finalArray[i]).append(i!=finalArray.length-1?"/":"");
		}
		
		String prePath = configProperties.getProperty(key);
		
		if(prePath == null) throw new FileNotFoundException(key+"无法找到映射,请检查配置文件.....");
		
		if(!prePath.endsWith("/")){
			prePath = prePath + "/";
		}
		String diskPath = prePath + relativePath;
		InputStream is = new FileInputStream(diskPath);
		try{
			OutputStream os = response.getOutputStream();
			byte[] buff = new byte[buffSize];
			int num = 0;
			while( (num = is.read(buff)) > 0){
				System.out.println(num);
				os.write(buff, 0, num);
			}
			os.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void init(FilterConfig config) throws ServletException {
		String filePath = config.getInitParameter("configLocation");
		filePath = filePath.replaceAll(" ", "");
		if("".equals(filePath)){
			throw new FileNotFoundException(this.getClass().getCanonicalName()+"的configLocation属性未赋值");
		}
		String realFilePath = filePath;
		InputStream is = null;
		if(filePath.contains(":")){
			String[] devideFilePath = filePath.split(":");
			if(! devideFilePath[0].equalsIgnoreCase("CLASSPATH")){
				throw new PathFormatException("未知前缀 "+devideFilePath[0]);
			}
			realFilePath = devideFilePath[1];
			 is = Thread.currentThread().getContextClassLoader().getResourceAsStream(realFilePath);
		}else{
			is = config.getServletContext().getResourceAsStream(realFilePath);
		}
		try {
			configProperties.load(is);
			System.out.println(configProperties.getProperty("bossjs"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	public void destroy() {
		
	}
}
