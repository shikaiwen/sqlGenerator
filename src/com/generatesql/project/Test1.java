package com.generatesql.project;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.generatesql.util.JsonUtil;

public class Test1 {

	static Connection connection = DBUtil.getMySqlConnection();;
	
	public static void main(String[] args) throws Exception{
		getTableInfo("t_user");
	}
	
	
	public static String getTableInfo(String tableName){

		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		try{
			DatabaseMetaData dbmd = connection.getMetaData();
			ResultSet rs = dbmd.getColumns(null, "DRP", tableName.toUpperCase(), null);
			while(rs.next()){
				Map<String,String> map = new TreeMap<String,String>();
				map.put("columnName", rs.getString("COLUMN_NAME").toLowerCase());
				map.put("typeName", rs.getString("TYPE_NAME").toLowerCase());
				map.put("remarks", rs.getString("REMARKS") == null?"":rs.getString("REMARKS"));
				resultList.add(map);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		System.out.println(resultList.size());
		String resultStr = JsonUtil.toJsonStr(resultList);
		System.out.println(resultStr);
		return resultStr;
	}
}
