package com.chen;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerServlet extends HttpServlet {

	//������¼��Ŀ������action��
	private Map<String,Object> map = new HashMap<String,Object>();
	@Override
	public void init() throws ServletException {
		//��ȡ��Ŀ�ֽ����ļ��ľ���·��
		String path = this.getServletContext().getRealPath("/WEB-INF/classes");
		//ɨ���ļ���
		try {
			scanfile(new File(path));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ɨ���ļ���
	 * @param file
	 * @throws Exception 
	 */
	private void scanfile(File file) throws Exception {
		if(file.isFile()){
			String filename = file.getName();
			if(filename.endsWith(".class")){
				String path = file.getPath();
				myClasLoader classloader = new myClasLoader(ControllerServlet.class.getClassLoader());
				Class clazz = classloader.load(path);
				Action actionannotation = (Action) clazz.getAnnotation(Action.class);
				if(actionannotation != null){
					map.put(actionannotation.value(), clazz.newInstance());
				}
			}
		}else{
			File[] files  = file.listFiles();
			for(File f : files){
				scanfile(f);
			}
		}
		
	}
	

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//��ȡuri
		String uri = request.getRequestURI();
		//��ȡ��ȡ��action������
		uri = uri.substring(request.getContextPath().length(), uri.length()-3);
		//��ȡҪ��Ӧ��action��
		Object action = map.get(uri);
		if(action == null){
			throw new RuntimeException("cannot find the action called  " +uri);
		}
		//��ȡ��������ķ���
		String methodname = request.getParameter("method");
		Method method = null;
		//ͨ�������ȡ��ָ���ķ���
		try {
			method = action.getClass().getMethod(methodname, HttpServletRequest.class,HttpServletResponse.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("can not find the method called   " + methodname);		}
		try {
			method.invoke(action, request,response);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
