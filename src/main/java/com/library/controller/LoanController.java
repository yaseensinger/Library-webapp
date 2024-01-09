package com.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.model.Category;
import com.library.service.CategoryService;
import com.library.service.LoanService;

@Controller
@RequestMapping(value = "/loans")
public class LoanController {

	@Autowired
	private LoanService loanService;
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute("categories")
	public List<Category> getCategories() {
		return categoryService.getAllBySort();
	}
	
	@GetMapping
	public String listLoanPage(Model model) {
		model.addAttribute("loans", loanService.getAllUnreturned());		
		return "/loans";
	}
	
	@GetMapping("/new")
	public String newLoanPage(Model model) { 
		return "/loans";
	}
}
