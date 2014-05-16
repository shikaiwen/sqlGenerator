package compareDB;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomPick {

	public static void test1(){
		Random rand = new Random(47);
		String [] fruits = {"apple","banana","strawberry","pear","orange","canno"};
		boolean [] picked = new boolean[fruits.length];
		List<String> list = new ArrayList<String>();
		int t ;
		do{
			t = rand.nextInt(fruits.length);
			list.add(fruits[t]);
			picked[t] = true;
		}
		while(picked[t]);
		
	}
	
	public static void main(String[] args) throws Exception{
		//accessingArray();
		seeClassType();
		//createArrayWithMarker();
		//System.out.println(int.class);
		//componentTypeOfClass();
		
	}

	public static void testReflectionArray(){
		int [] intArray = (int[])java.lang.reflect.Array.newInstance(int.class, 3);
	}
	
	public static void accessingArray(){
		int [] intArray = (int[])Array.newInstance(int.class, 3);
		Array.set(intArray, 0, 100);
		int b = intArray[0];
		System.out.println(b);
	
		System.out.println(Array.get(intArray, 0));
	}
	
	public static void seeClassType(){
		int [] aa = new int[3];
		boolean [] bool = new boolean[3];
		char [] bb = new char[3];
		short [] cc = new short[3];
		float [] dd = new float[3];
		double[] ee = new double[3];
		
		String [] ff = new String[3];
		String [][] ff2 = new String[1][];
		
		Integer [] gg = new Integer[3];
		Class [] hh = new Class[3];
		
		System.out.println(aa.getClass());
		System.out.println(bool.getClass().toString());
		System.out.println(bb.getClass().toString());
		System.out.println(cc.getClass().toString());
		System.out.println(dd.getClass().toString());
		System.out.println(ee.getClass().toString());
		
		System.out.println(ff.getClass().toString());
		System.out.println(ff2.getClass().toString());
		
		System.out.println(gg.getClass().toString());
		System.out.println(hh.getClass().toString());
	}
	
	public static void createArrayWithMarker() throws Exception{
		Class intClass = Class.forName("[I");
		System.out.println(intClass.isArray());
	//	System.out.println(a.length);
	//	int [] aa = (int[])Array.newInstance(intClass, 0);
	//	System.out.println(aa.length);
	}
	
	public static void componentTypeOfClass(){
		Class intClass = int.class;
		Class intArrClass = new int[4][].getClass().getComponentType();
		System.out.println(intClass.getComponentType());
		System.out.println(intArrClass);
	}
}
