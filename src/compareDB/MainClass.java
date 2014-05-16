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

	//���ݿ�1
	static Map<String,List<String>> szdaiSchema = new LinkedHashMap<String,List<String>>();
	//���ݿ�2
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
			
			//��ȡszdai2�ı��column
			List<String> szdai_2ColumnList = szdai2Schema.get(tableName);
			if(szdai_2ColumnList == null){
				System.out.println("���ݿ�1������"+tableName +" �������ݿ�2û��");
				continue;
			}
			
			List<String> szdai_1ColumnList = szdaiSchema.get(tableName);
			
			// o==null ? get(i)==null : o.equals(get(i)) �������
			
			//����szdai1��column
			for(String szdai_1Column : szdai_1ColumnList){
				if( ! szdai_2ColumnList.contains(szdai_1Column)){
					System.out.println("���ݿ�2�ı�"+tableName+"��"+szdai_1Column +"�����ݿ�1û��");
				}else{
					//��szdai2��ɾ����
					szdai_2ColumnList.remove(szdai_1Column);
				}
			}
			//ʣ�µ�szdai1��û��
			for(String szdai2Column : szdai_2ColumnList){
				System.out.println("���ݿ�2�ı�"+tableName+"��"+szdai2Column +"�����ݿ�1û��");
			}
			
			//��szdai2��ɾ�����ű�
			szdai2Schema.remove(tableName);
		}
		
		//ʣ�µı�szdai1��û��
		Set<String> szdai2KeySet = szdai2Schema.keySet();
		for(String tableName : szdai2KeySet){
			System.out.println("���ݿ�2�б�"+szdai2Schema.get(tableName) + "�������ݿ�1û��");
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
			//��ȡ������
			String tableNameUpper = rs.getString("TABLE_NAME");
			String tableNameLower = tableNameUpper.toLowerCase();
			schemeMap.put(tableNameLower, new LinkedList<String>());
			//��ȡ��
			ResultSet columnResultset = metaData.getColumns(null, schemeName, tableNameUpper, null);
			while(columnResultset.next()){
				schemeMap.get(tableNameLower).add(columnResultset.getString("COLUMN_NAME").toLowerCase());
			}
		}
		return schemeMap;
	}
	
	static Map<Object,String> schemaRealName = new HashMap<Object,String>();
	static {
		schemaRealName.put(szdaiSchema, "���ݿ�1");
		schemaRealName.put(szdai2Schema, "���ݿ�2");
	}
	
	static String getDBName(Map<String,List<String>> schema){
		String name = schemaRealName.get(schema);
		return name;
	}
	
}
