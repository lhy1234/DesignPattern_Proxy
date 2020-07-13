package com.lhy.proxy;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * 产生代理的类
 * @author dev
 *
 */
public class Proxy {
	/**
	 * 
	 * @param interfaces 代理实现的接口
	 * @param h 代理处理逻辑
	 * @return
	 * @throws Exception
	 */
	public static Object newProxyInstance(Class interfaces,InvocationHandler h) throws Exception{//动态传入接口，其实jdk可以传多个接口
		//换行字符串
		String rt = "\r\n";
		String methodStr = "";
		//反射拿到接口的所有的方法
		Method[] methods = interfaces.getMethods();
		for(Method m : methods){
			methodStr += "@Override"+rt +
					"public void "+ m.getName()+ "() {"+
					"	try{"+rt+
					"	Method md = "+ interfaces.getName()+".class.getMethod(\""+m.getName()+"\");"+rt+	
					"	h.invoke(this,md);"+rt+ //this->代理对象
						"	}catch(Exception e){e.printStackTrace();}"+
					"}";
		}
		
		//只要能动态的 编译这段代码，就能动态的产生代理类！类的名字无所谓
		//动态编译的技术：JDK6 Compiler API，CGLib（用到了ASM） ，ASM
		//（CGLib、ASM不用源码来编译，能直接生成二进制文件，因为java的二进制文件格式是公开的）
		//Spring内部，如果是实现接口就是用的JDK本身的API产生代理，否则就用CGLib
		
		String src = 
		"package com.lhy.proxy;"+ rt +
		"import java.lang.reflect.Method;"+rt+
		"public class $Proxy1 implements "+ interfaces.getName() +"{"+rt +

		"	com.lhy.proxy.InvocationHandler h;"+rt+
		"	public $Proxy1(InvocationHandler h) {"+rt +
		"		this.h = h;"+rt +
		"	}"+rt +

		 methodStr +
		"}";
		
		//1，生成代理类
		String fileName = System.getProperty("user.dir")
							+"/src/com/lhy/proxy/$Proxy1.java";//获取项目根路径
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
		Class clazz = urlClassLoader.loadClass("com.lhy.proxy.$Proxy1");
	
		//4，，创建一个对象
		//不能用 clazz.newInstance();创建对象因为它会调用空构造方法
		Constructor constructor = clazz.getConstructor(InvocationHandler.class);//获取某个类型参数的构造器
		Object obj = constructor.newInstance(h);//
		
		return obj;
	} 
}
