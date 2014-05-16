package com.learning.taglib;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class InteratorTagTest extends SimpleTagSupport{

	private String collection;
	
	private String item;
	
	@Override
	public void doTag() throws JspException, IOException {
	
		JspContext context = this.getJspContext();
		JspWriter out = context.getOut();
	
		List bookList = (List)context.getAttribute(collection);
		
		for(Object book : bookList){
			this.getJspContext().setAttribute(item, book);
			this.getJspBody().invoke(null);
		}
	}


	public String getCollection() {
		return collection;
	}
	public void setCollection(String collection) {
		this.collection = collection;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	
	
}
