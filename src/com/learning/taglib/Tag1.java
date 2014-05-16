package com.learning.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

public class Tag1 implements Tag , BodyTag{

	/**
	 * ���õ�˳��
	 * ���doStartTag ���� BodyTag.EVAL_BODY_BUFFERED
	 * doStartTag()-> doAfterBody() if(����BodyTag.EVAL_BODY_AGAIN)  ��������doStartTag
	 *  							else if(����BodyTag.SKIP_BODY) ����doEndTag()
	 * 
	 */
	
	private int loop;
	
	public Tag1() {
		System.out.println("Tag1 created ...");
		System.out.println("EVAL_BODY INCLUDE" +Tag.EVAL_BODY_INCLUDE);
		System.out.println("EVAL_PAGE"+Tag.EVAL_PAGE);
		System.out.println("SKIP_BODY"+Tag.SKIP_BODY);
		System.out.println("SKIP_PAGE" + Tag.SKIP_PAGE);
	}
	
	@Override
	public int doEndTag() throws JspException {
		System.out.println("doEndTag...");
		JspWriter out = pageContext.getOut();
		String str = bodyContent.getString();
		try {
			out.write(str.toUpperCase());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return BodyTag.EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		return BodyTag.EVAL_BODY_BUFFERED;
//		if(loop > 0){
//			return  BodyTag.EVAL_BODY_INCLUDE;
//		}else{
//			return  BodyTag.SKIP_BODY;
//		}
//		System.out.println("doStartTag ...");
//		System.out.println(loop);
//		return BodyTag.EVAL_BODY_INCLUDE;
	}

	@Override
	public Tag getParent() {
		System.out.println("getParent ..");
		return null;
	}

	@Override
	public void release() {
		System.out.println("release invoked ...");
	}

	PageContext pageContext ;
	@Override
	public void setPageContext(PageContext context) {
		this.pageContext = context;
		
	}
	
	@Override
	public void setParent(Tag tag) {
		System.out.println(tag);
	}
//////////////////////////////////////////////////////////////Tag�ӿڷ���

	@Override
	public int doAfterBody() throws JspException {//����IterationTag.EVAL_BODY_AGAIN�ͻᷴ������ѭ��
		
//		if(loop > 0){
//			loop --;
//			return BodyTag.EVAL_BODY_AGAIN;
//		}else{
//			return BodyTag.SKIP_BODY;
//		}
		
		String str = bodyContent.getString();
		System.out.println(str);
		return Tag.SKIP_BODY;
//		try{
//			System.out.println("doAfterBody...");

//			bodyContent.write(str);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//
//		return Tag.SKIP_BODY;
	}
	
	@Override
	public void doInitBody() throws JspException {
		System.out.println("doInitBody ....");
	}
	
	BodyContent bodyContent ;
	@Override
	public void setBodyContent(BodyContent bodyContent) {
		System.out.println("bodyContent .."+bodyContent.getString());
		this.bodyContent = bodyContent;
		System.out.println("bodyContent .."+bodyContent.getString());
	}

	public int getLoop() {
		return loop;
	}

	public void setLoop(int loop) {
		this.loop = loop;
	}
	
	
	
	
}
