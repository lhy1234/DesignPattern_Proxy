package com.lhy.proxy.test;

import com.lhy.proxy.InvocationHandler;
import com.lhy.proxy.Proxy;

public class Client {
	public static void main(String[] args) throws Exception{
		UserMgr userMgr = new UserMgrImpl();
		InvocationHandler h = new TransitionHandler(userMgr);
		UserMgr proxy = (UserMgr)Proxy.newProxyInstance(UserMgr.class, h);
		proxy.addUser();
	}
}
