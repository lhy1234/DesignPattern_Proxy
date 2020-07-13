package com.lhy.proxy;

public class Test {

	public static void main(String[] args) throws Exception {
		InvocationHandler h = new TimeHandler(new Tank());
		// 站在使用者的角度，动态代理，Proxy产生一个代理类的对象，你根本看不到这个代理类的名字
		Moveable m = (Moveable) Proxy.newProxyInstance(Moveable.class, h);
		m.move();
	
	}

}
