package com.generatesql.parse;

import java.util.List;

public class TableBlock {

	private String tableName;
	private String alias;
	private List<String> useCols;
	private List<String> notUseCols;
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public List<String> getUseCols() {
		return useCols;
	}
	public void setUseCols(List<String> useCols) {
		this.useCols = useCols;
	}
	public List<String> getNotUseCols() {
		return notUseCols;
	}
	public void setNotUseCols(List<String> notUseCols) {
		this.notUseCols = notUseCols;
	}
	
	
	
	
}
