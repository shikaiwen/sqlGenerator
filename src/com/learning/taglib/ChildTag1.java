package com.learning.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class ChildTag1 extends BodyTagSupport{

	@Override
	public int doStartTag() throws JspException {
		System.out.println(" .doStartTag.. ");
		return BodyTag.EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		System.out.println();
		return BodyTag.EVAL_PAGE;
	}
}
