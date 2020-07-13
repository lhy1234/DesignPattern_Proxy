package com.lhy.compiler.test;

import java.lang.reflect.Method;

import com.lhy.proxy.Moveable;

public class Test2 {

	public static void main(String[] args) {
//		Method[] methods = Moveable.class.getMethods();
//		for(Method m : methods){
//			System.err.println(m.getName());//move
//		}
		
		int rs = m();
		System.out.println(rs);

	}
	
	public  static int m(){
		try {
			int a=1/0;
			return 100;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			System.out.println("xxxx");
		}
		return 1000;
	}

}
