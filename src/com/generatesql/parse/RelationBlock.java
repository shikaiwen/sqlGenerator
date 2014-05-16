package com.generatesql.parse;

public class RelationBlock {

    private String table1Alias ;
	private String table1Col;
	private String table2Alias;
	private String table2Col;
	private String joinType;
	private String  condition;
	public String getTable1Alias() {
		return table1Alias;
	}
	public void setTable1Alias(String table1Alias) {
		this.table1Alias = table1Alias;
	}
	public String getTable1Col() {
		return table1Col;
	}
	public void setTable1Col(String table1Col) {
		this.table1Col = table1Col;
	}
	public String getTable2Alias() {
		return table2Alias;
	}
	public void setTable2Alias(String table2Alias) {
		this.table2Alias = table2Alias;
	}
	public String getTable2Col() {
		return table2Col;
	}
	public void setTable2Col(String table2Col) {
		this.table2Col = table2Col;
	}
	public String getJoinType() {
		return joinType;
	}
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	
	
}
