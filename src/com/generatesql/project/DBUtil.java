package com.generatesql.project;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {



	public static void main(String[] args) {
		//System.out.println(getConnection());
		DBUtil.getMySqlConnection();
	}

	public static Connection getConnection(){
		Connection conn = null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url="jdbc:oracle:thin:@localhost:1521:orcl";
			String username = "drp";
			String password = "drp";
			 conn = DriverManager.getConnection(url, username, password);
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	public static Connection getMySqlConnection(){
		Connection conn = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
	//		String url = "jdbc:mysql://localhost:3306/szdai";
			String url = "jdbc:mysql://192.168.1.123:3306/szdai";
			conn = DriverManager.getConnection(url, "root", "root");
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
}
