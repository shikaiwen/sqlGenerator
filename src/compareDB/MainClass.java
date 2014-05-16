package compareDB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.generatesql.util.JsonUtil;

public class MainClass {

	//数据库1
	static Map<String,List<String>> szdaiSchema = new LinkedHashMap<String,List<String>>();
	//数据库2
	static Map<String,List<String>> szdai2Schema = new LinkedHashMap<String,List<String>>();
	
	public static void main(String[]args)throws Exception{
		System.out.println(JsonUtil.toJsonStr(test1("szdai",szdaiSchema,DBGET.getConnection1())));
//		System.out.println("\n\n");
		System.out.println(JsonUtil.toJsonStr(test1("szdai2",szdai2Schema,DBGET.getConnection2())));
		compare();
	}
	
	
	public static void compare(){
		Set<String> szdai_1KeySet = szdaiSchema.keySet();
		for(String tableName : szdai_1KeySet){
			
			//获取szdai2的表的column
			List<String> szdai_2ColumnList = szdai2Schema.get(tableName);
			if(szdai_2ColumnList == null){
				System.out.println("数据库1包含表"+tableName +" 但是数据库2没有");
				continue;
			}
			
			List<String> szdai_1ColumnList = szdaiSchema.get(tableName);
			
			// o==null ? get(i)==null : o.equals(get(i)) 分析这个
			
			//遍历szdai1的column
			for(String szdai_1Column : szdai_1ColumnList){
				if( ! szdai_2ColumnList.contains(szdai_1Column)){
					System.out.println("数据库2的表"+tableName+"有"+szdai_1Column +"但数据库1没有");
				}else{
					//从szdai2种删除列
					szdai_2ColumnList.remove(szdai_1Column);
				}
			}
			//剩下的szdai1都没有
			for(String szdai2Column : szdai_2ColumnList){
				System.out.println("数据库2的表"+tableName+"有"+szdai2Column +"但数据库1没有");
			}
			
			//从szdai2种删除这张表
			szdai2Schema.remove(tableName);
		}
		
		//剩下的表szdai1都没有
		Set<String> szdai2KeySet = szdai2Schema.keySet();
		for(String tableName : szdai2KeySet){
			System.out.println("数据库2有表"+szdai2Schema.get(tableName) + "但是数据库1没有");
		}
	}
	
	
	public static Map<String,List<String>> test1(String schemeName,Map<String,List<String>> schemeMap,Connection conn)throws Exception{
		DatabaseMetaData metaData = conn.getMetaData();
		ResultSet rs = metaData.getTables(null, schemeName, "", null);
		ResultSetMetaData rsMeta= rs.getMetaData();
		int count = rsMeta.getColumnCount();
//		for(int i=1;i<=count;i++){
//			String labelName = rsMeta.getColumnName(i);
//		//	System.out.print(labelName +"\t");
//		}
		System.out.println("\n");
		while(rs.next()){
			//获取表名称
			String tableNameUpper = rs.getString("TABLE_NAME");
			String tableNameLower = tableNameUpper.toLowerCase();
			schemeMap.put(tableNameLower, new LinkedList<String>());
			//获取列
			ResultSet columnResultset = metaData.getColumns(null, schemeName, tableNameUpper, null);
			while(columnResultset.next()){
				schemeMap.get(tableNameLower).add(columnResultset.getString("COLUMN_NAME").toLowerCase());
			}
		}
		return schemeMap;
	}
	
	static Map<Object,String> schemaRealName = new HashMap<Object,String>();
	static {
		schemaRealName.put(szdaiSchema, "数据库1");
		schemaRealName.put(szdai2Schema, "数据库2");
	}
	
	static String getDBName(Map<String,List<String>> schema){
		String name = schemaRealName.get(schema);
		return name;
	}
	
}
