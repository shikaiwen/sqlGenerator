package com.generatesql.util;

import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;

public class SqlUtil {

	
	public static Formatter formatter = new BasicFormatterImpl();
	
	public static String format(String sql){
		return formatter.format(sql);
	}
}
