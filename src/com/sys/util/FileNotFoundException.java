package com.sys.util;

public class FileNotFoundException extends RuntimeException{

	public static final String DEFAULT_MSG = "�����ļ�����configLocationδ�ҵ������ļ�";
	public FileNotFoundException(String str) {
		
		super(str);
	}
	
	public FileNotFoundException(){
		super(DEFAULT_MSG);
	}
	
	
}
