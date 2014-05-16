package com.generatesql.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CommUtil {

	public static boolean availiable(String str){
		if(str == null || str.trim().length() <= 0){
			return false;
		}
		return true;
	}
	
	
	private static String defaultDelimiter = "_";
	public static String toStandardJavaName(String str){
		return toStandardJavaName(str,defaultDelimiter);
	}
	
	public static String toStandardJavaName(String str,String defaultDelimiter){
		String [] parts = str.split(defaultDelimiter);
		if(parts.length == 1) return str;
		
		for(int i = 0;i<parts.length;i++){
			String part = parts[i];
			char c = part.charAt(0);
			if(i==0){
				char upperCase = Character.toLowerCase(c);
				parts[i] = part.replaceFirst(c+"", upperCase+"");
			}else{
				char lowerCase = Character.toUpperCase(c);
				parts[i] = part.replaceFirst(c+"", lowerCase+"");	//接受 Iterator的容器，数组也继承Iterator吗？待研究
			}
		}
		
		String result = String.join("", parts);	
		return  result;
	}
	
	
	//////////////////////获取连接名称///////////////
	public static String getJoinType(String type){
		
		switch (type) {
		case Const.JOIN_TYPE_LEFT:
			return " left join ";
		case Const.JOIN_TYPE_INNER:
			return " inner join ";
		case Const.JOIN_TYPE_RIGHT:
			return " right join ";
		}
		throw new RuntimeException("");
	}
	
	
	
	public static String formatStr(String format , Hashtable<String,Object> values){
		StringBuilder convFormat = new StringBuilder(format);
		Enumeration<String> keys = values.keys();
		ArrayList valueList = new ArrayList();
		int currentPos = 1;
		while(keys.hasMoreElements()){
			String key = keys.nextElement();
			String formatKey = "${"+key+"}";
			String formatPos = "%"+Integer.toString(currentPos) +"$s";
			
			int index = -1;
			while( (index = convFormat.indexOf(formatKey, index)) != -1){
				
				convFormat.replace(index, index + formatKey.length(), formatPos);
				index += formatPos.length();
				
			}
			valueList.add(values.get(key));
			++currentPos;
		}
		
		return String.format(convFormat.toString(), valueList.toArray());
	}
	
	public static void main(String[] args) {
		System.out.println(toStandardJavaName("membeid"));
	}
}
