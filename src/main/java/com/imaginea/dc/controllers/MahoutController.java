package com.imaginea.dc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.imaginea.dc.service.NewsArticleService;

@Controller
@RequestMapping("/mahout")
public class MahoutController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MahoutController.class);
	
	private NewsArticleService newsArticleService;

	
	@RequestMapping(value = "/ping")
	public String prepareArticlesList(Model model) {
		

		return "mahout_ping";
	}
	
	
	

	/* Getters and Setters */
	
	public void setNewsArticleService(NewsArticleService newsArticleService) {
		this.newsArticleService = newsArticleService;
	}
	
}