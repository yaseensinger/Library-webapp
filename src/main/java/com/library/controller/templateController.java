package com.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class templateController {
	
	@RequestMapping(value = {"/", "/template"}, method = RequestMethod.GET)
	public String templatePage(Model model) {
		return "template";
	}
}