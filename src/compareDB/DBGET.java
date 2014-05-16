package compareDB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBGET {

	public static Connection getConnection1(){
		Connection conn = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://192.168.1.123/szdai?characterEncoding=UTF-8";
			String username = "root";
			String password = "root";
			 conn = DriverManager.getConnection(url, username, password);
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void main(String[]args){
		getConnection2();
	}
	
	
	public static Connection getConnection2(){
		Connection conn = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://192.168.1.18:3306/newszdai?characterEncoding=UTF-8";
			String username = "root";
			String password = "szdai";
			 conn = DriverManager.getConnection(url, username, password);
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
}
