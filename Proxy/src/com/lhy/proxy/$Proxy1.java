package com.lhy.proxy;

import java.lang.reflect.Method;

public class $Proxy1 implements com.lhy.proxy.test.UserMgr {
	com.lhy.proxy.InvocationHandler h;

	public $Proxy1(InvocationHandler h) {
		this.h = h;
	}

	@Override
	public void addUser() {
		try {
			Method md = com.lhy.proxy.test.UserMgr.class.getMethod("addUser");
			h.invoke(this, md);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}