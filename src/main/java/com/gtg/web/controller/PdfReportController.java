package com.gtg.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PdfReportController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showItems() {
	//	log.info("showItems()- start");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("shop");
		return modelAndView;
	}

	
}
