package com.lhy.proxy;

import java.lang.reflect.Method;
public interface InvocationHandler {

	/**
	 * 代理执行的逻辑
	 * @param o 方法所属的对象
	 * @param m 要执行的方法
	 */
	public void invoke(Object o,Method m);
}
