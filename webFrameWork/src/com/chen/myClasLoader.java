package com.chen;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class myClasLoader extends ClassLoader{

	//Ϊ�Զ��������ί�и�������
	public myClasLoader(ClassLoader c){
		super(c);
	}
	
	//��ȡ·�������class����
	public Class load(String path) throws  Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileInputStream in = new FileInputStream(path); 
		int n;
		byte[] b = new byte[1024];
		while((n=in.read(b))!=-1){
			out.write(b, 0, n);
		}
		out.close();
		byte[] bytes = out.toByteArray();
		return super.defineClass(bytes, 0, bytes.length);
	}
}
