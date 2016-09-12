package com.demo.ssh.view;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

public class ModelAndView extends org.springframework.web.servlet.ModelAndView{
	
	@Autowired
	HttpServletRequest  request;

	/**
	 * 
	 */
	public ModelAndView() {
		super();
	}
	
}
