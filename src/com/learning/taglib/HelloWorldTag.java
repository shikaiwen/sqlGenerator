package com.learning.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class HelloWorldTag extends SimpleTagSupport{

	private String name;
	@Override
	public void doTag() throws JspException, IOException {
		JspContext jspContext = this.getJspContext();
		JspWriter out = jspContext.getOut();
		System.out.println("name is :"+ name );
		out.write("HEllo Tags");
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
