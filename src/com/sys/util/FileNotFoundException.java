package com.sys.util;

public class FileNotFoundException extends RuntimeException{

	public static final String DEFAULT_MSG = "配置文件属性configLocation未找到配置文件";
	public FileNotFoundException(String str) {
		
		super(str);
	}
	
	public FileNotFoundException(){
		super(DEFAULT_MSG);
	}
	
	
}
