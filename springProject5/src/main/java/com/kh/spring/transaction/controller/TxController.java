package com.kh.spring.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.spring.transaction.model.service.TestServiceImpl;

@Controller
public class TxController {
	
	@Autowired
	private TestServiceImpl service;
	
	@RequestMapping("insertProduct")
	public String insertProduct() {
		
		service.test01();
		
		return "redirect:/";
	}
	
	
}
