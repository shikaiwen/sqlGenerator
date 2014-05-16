package com.plsql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

import com.generatesql.project.DBUtil;

public class PlSqlTest {

	public static void main(String[] args) throws Exception{
		test1();
	}
	
	public static void test1()throws Exception{
		Connection conn = DBUtil.getConnection();
		CallableStatement stmt = conn.prepareCall("{ call powerANumber(?,?)}");
	//	CallableStatement stmt = conn.prepareCall("begin   powerANumber(?,?); end;");
		stmt.setInt(1, 10);
		stmt.registerOutParameter(2, Types.VARCHAR);
		ResultSet rs = stmt.executeQuery();
		System.out.println(stmt.getString(2));
		
	}
}
