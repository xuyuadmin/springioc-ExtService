package com.xuyu.spring.ext;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.xuyu.spring.annotation.ExtService;
import com.xuyu.utils.ClassUtils;

/**
 * 手写spring xml 
 * @author Administrator
 *
 */
public class ExtClassPathXmlApplicationContext {

	//spring bean 容器
	private static ConcurrentHashMap<String, Class<?>> beans=null;
	//扫把的范围
	private String packageName;
	public ExtClassPathXmlApplicationContext(String packageName) throws Exception {
		this.packageName=packageName;
		beans=new ConcurrentHashMap<String, Class<?>>();
		initBean();
		
	}
	//初始化对象
	public void initBean() throws Exception {
		//1.使用Java反射机制扫包，获取当前包下的所有类
		List<Class<?>> classes = ClassUtils.getClasses(packageName);
		//2.判断类上是否注入bean的注解
		ConcurrentHashMap<String, Class<?>> classExisAnnotation = findClassExisAnnotation(classes);
		if(classExisAnnotation==null || classExisAnnotation.isEmpty()) {
			throw new Exception("该包下没有任何任何类加上注解");
		}
		//3.使用Java反射机制进行初始化
		
	}
	public Object getBean(String beanId) throws Exception {
		if(StringUtils.isEmpty(beanId)) {
			throw new Exception("beanId参数不能为空");
		}
		//1.从spring容器获取bean
		Class<?>classInfo = beans.get(beanId);
		if(classInfo==null) {
			throw new Exception("class not found");
		}
		//3.使用反射机制初始化对象
		return newInstance(classInfo);
	}
	//初始化对象
	public Object newInstance(Class<?>classInfo) throws Exception, IllegalAccessException {
		return classInfo.newInstance();
	}
	//判断类上是否存在注解
	public ConcurrentHashMap<String, Class<?>> findClassExisAnnotation(List<Class<?>> classes) {
		for (Class<?> classInfo : classes) {
			ExtService annotation = classInfo.getAnnotation(ExtService.class);
			if(annotation!=null) {
				//获取当前类名
				String className = classInfo.getSimpleName();
				//beanId默认类名小写
				String beanId = toLowerCaseFirstOne(className);
				beans.put(beanId, classInfo);
				continue;
			}
		}
		return beans;
	}
	// 首字母转小写
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}


}
