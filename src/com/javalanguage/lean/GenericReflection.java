package com.javalanguage.lean;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenericReflection {

	public static void main(String []args)throws Exception{
		//testGenericReturnTypeReflection();
		testGenericParameterTypeReflection();
	}
	
	//�������� �� �з���
	public static void testGenericReturnTypeReflection() throws Exception{
		//��������Կ���null��һ��class����void����
		Method method = GenericReflection.class.getMethod("getStringList", null);//��null���� new Class[]{null}��Ȼ����
		Type returnType = method.getGenericReturnType();
		
		//Type returnType = method.getReturnType();
		// ParameterizedType ����ָ���������������� ����Collection<String>
		//Class������Type�ӿڵ�Ψһʵ��
		if(returnType instanceof ParameterizedType){
			ParameterizedType type = (ParameterizedType)returnType;
			Type [] typeArguments = type.getActualTypeArguments();
			for(Type typeArgument : typeArguments){
				Class typeArgClass = (Class)typeArgument;
				System.out.println(typeArgClass.getName());
			}
		}
	//	System.out.println(returnType);
	}
	
	public static List<String> getStringList(){
		return new ArrayList<String>();
	}
	
	//�������з���
	public static void testGenericParameterTypeReflection()throws Exception{
		Method method = GenericReflection.class.getMethod("getList", new Class[]{List.class,Map.class});
		Type[] parameterTypes = method.getGenericParameterTypes();
		
		for(Type type : parameterTypes){
			if(type instanceof ParameterizedType){
				ParameterizedType paramType = (ParameterizedType)type;
				Type [] t = paramType.getActualTypeArguments();
				System.out.println("============================detail============================");
				for(Type tSub : t){
					Class tSubClass = (Class)tSub;
					System.out.println(tSubClass.getName());
				}
				System.out.println("=========================detail over===================");
				
			}
		}
		
	}
	public static void getList(List<String> list,Map<Integer,Map<Integer,String>> a){
		
	}
}
