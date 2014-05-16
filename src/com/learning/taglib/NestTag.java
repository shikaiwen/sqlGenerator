package com.learning.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class NestTag extends BodyTagSupport{

	@Override
	public int doStartTag() throws JspException {
		return BodyTag.EVAL_BODY_INCLUDE;
	}
}
