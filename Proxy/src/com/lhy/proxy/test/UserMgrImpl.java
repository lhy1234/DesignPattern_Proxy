package com.lhy.proxy.test;

public class UserMgrImpl implements UserMgr {
	@Override
	public void addUser() {
		System.err.println("插入到数据库user表");
		System.err.println("记录到日志表");
	}
}
