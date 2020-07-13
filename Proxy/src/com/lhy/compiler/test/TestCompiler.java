package com.lhy.compiler.test;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.lhy.proxy.Moveable;
import com.lhy.proxy.Tank;

public class TestCompiler {

	public static void main(String[] args) throws Exception{
		String rt = "\r\n";
		String src = 
		"package com.lhy.proxy;"+ rt +

		"public class TankTimeProxy implements Moveable{"+rt +

		"	Moveable m;"+rt +
			
		"	public TankTimeProxy(Moveable m) {"+rt +
		"		super();"+rt +
		"		this.m = m;"+rt +
		"	}"+rt +

		"	@Override" +rt +
		"	public void move() {" +rt +
				//计算方法运行了多长时间
		"		long start = System.currentTimeMillis();" +rt +
		"		System.out.println(\"start:\"+start);" +rt +
		"		m.move();"+rt +
		"		long end = System.currentTimeMillis();"+rt +
		"		System.out.println(\"time:\"+(end-start));"+rt +
		"	}"+rt +
		"}";
		
		//1，生成代理类
		String fileName = System.getProperty("user.dir")
							+"/src/com/lhy/proxy/TankTimeProxy.java";//获取项目根路径
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		fw.write(src);
		fw.flush();
		fw.close();
		
		//2，将生成的类进行编译成class文件
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();//拿到系统默认的编译器（其实就是javac）
		StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);//诊断监听器；语言；编码
		Iterable units =  fileMgr.getJavaFileObjects(fileName);
		CompilationTask task = compiler.getTask(null, fileMgr, null, null, null, units);
		task.call();
		fileMgr.close();
		
		//3，将class load到内存 
		URL[] urls = new URL[]{new URL("file:/"+ System.getProperty("user.dir")+"/src")};
		URLClassLoader urlClassLoader = new URLClassLoader(urls);
		Class clazz = urlClassLoader.loadClass("com.lhy.proxy.TankTimeProxy");
		//System.out.println(clazz);
		//4，，创建一个对象
		//不能用 clazz.newInstance();创建对象因为它会调用空构造方法
		Constructor<Moveable> constructor = clazz.getConstructor(Moveable.class);//获取某个类型参数的构造器
		Moveable m = constructor.newInstance(new Tank());//
		m.move();
	
	}
}
