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
	
	//返回类型 中 有泛型
	public static void testGenericReturnTypeReflection() throws Exception{
		//从这里可以看出null是一种class，而void不是
		Method method = GenericReflection.class.getMethod("getStringList", null);//将null换成 new Class[]{null}竟然不行
		Type returnType = method.getGenericReturnType();
		
		//Type returnType = method.getReturnType();
		// ParameterizedType 就是指参数化的容器类型 ，即Collection<String>
		//Class好像是Type接口的唯一实现
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
	
	//参数中有泛型
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
