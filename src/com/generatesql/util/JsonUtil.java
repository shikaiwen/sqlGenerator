package com.generatesql.util;

import com.google.gson.Gson;

public class JsonUtil {

	
	static Gson gson = new Gson();
	
	public static Gson getGson(){
		return gson;
	}
	
	public static String toJsonStr(Object obj){
		return JsonUtil.getGson().toJson(obj);
	}
}
