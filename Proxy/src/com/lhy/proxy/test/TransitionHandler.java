package com.lhy.proxy.test;

import java.lang.reflect.Method;
import com.lhy.proxy.InvocationHandler;

public class TransitionHandler implements InvocationHandler{

	private Object target;
	
	public TransitionHandler(Object target) {
		this.target = target;
	}

	@Override
	public void invoke(Object o, Method m) {
		System.err.println("事务开始....");
		try {
			m.invoke(target, new Object[]{});
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("事务回滚....");
		}
		System.err.println("事务提交....");
	}
}
