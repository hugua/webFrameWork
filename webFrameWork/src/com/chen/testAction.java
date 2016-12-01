package com.chen;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Action("/test")
public class testAction {
	public void login(HttpServletRequest request,HttpServletResponse response){
		System.out.println("login method of the testAtion");
	}
}
