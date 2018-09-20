package com.xuyu.spring.ext;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.xuyu.spring.annotation.ExtService;
import com.xuyu.utils.ClassUtils;

/**
 * ��дspring xml 
 * @author Administrator
 *
 */
public class ExtClassPathXmlApplicationContext {

	//spring bean ����
	private static ConcurrentHashMap<String, Class<?>> beans=null;
	//ɨ�ѵķ�Χ
	private String packageName;
	public ExtClassPathXmlApplicationContext(String packageName) throws Exception {
		this.packageName=packageName;
		beans=new ConcurrentHashMap<String, Class<?>>();
		initBean();
		
	}
	//��ʼ������
	public void initBean() throws Exception {
		//1.ʹ��Java�������ɨ������ȡ��ǰ���µ�������
		List<Class<?>> classes = ClassUtils.getClasses(packageName);
		//2.�ж������Ƿ�ע��bean��ע��
		ConcurrentHashMap<String, Class<?>> classExisAnnotation = findClassExisAnnotation(classes);
		if(classExisAnnotation==null || classExisAnnotation.isEmpty()) {
			throw new Exception("�ð���û���κ��κ������ע��");
		}
		//3.ʹ��Java������ƽ��г�ʼ��
		
	}
	public Object getBean(String beanId) throws Exception {
		if(StringUtils.isEmpty(beanId)) {
			throw new Exception("beanId��������Ϊ��");
		}
		//1.��spring������ȡbean
		Class<?>classInfo = beans.get(beanId);
		if(classInfo==null) {
			throw new Exception("class not found");
		}
		//3.ʹ�÷�����Ƴ�ʼ������
		return newInstance(classInfo);
	}
	//��ʼ������
	public Object newInstance(Class<?>classInfo) throws Exception, IllegalAccessException {
		return classInfo.newInstance();
	}
	//�ж������Ƿ����ע��
	public ConcurrentHashMap<String, Class<?>> findClassExisAnnotation(List<Class<?>> classes) {
		for (Class<?> classInfo : classes) {
			ExtService annotation = classInfo.getAnnotation(ExtService.class);
			if(annotation!=null) {
				//��ȡ��ǰ����
				String className = classInfo.getSimpleName();
				//beanIdĬ������Сд
				String beanId = toLowerCaseFirstOne(className);
				beans.put(beanId, classInfo);
				continue;
			}
		}
		return beans;
	}
	// ����ĸתСд
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}


}
