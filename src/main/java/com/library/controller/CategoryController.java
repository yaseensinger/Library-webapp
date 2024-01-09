package com.library.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.model.Category;
import com.library.service.CategoryService;

@Controller
@RequestMapping(value = "/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
    @GetMapping
    public String showCategoriesPage(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("category", new Category());
        return "categories";
    }	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addCategoryPage(Model model) {
		model.addAttribute("category", new Category());
		return "/categories";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editCategoryPage(@PathVariable(name = "id") Long id, Model model) {
		Category category = categoryService.get(id);
		if( category != null ) {
			model.addAttribute("category", category);
			return "/categories";
		} else {
			return "redirect:/categories";
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveCategory(@Valid Category category, BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
		if( bindingResult.hasErrors() ) {
			return "/categories";
		}
		
		if( category.getId() == null ) {
			categoryService.addNew(category);
			redirectAttributes.addFlashAttribute("successMsg", "'" + category.getName() + "' is added as a new category.");
			return "redirect:/categories";
		} else {
			Category updateCategory = categoryService.save( category );
			redirectAttributes.addFlashAttribute("successMsg", "Changes for '" + category.getName() + "' are saved successfully. ");
			return "redirect:/categories/edit/"+updateCategory.getId();
		}
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public String removeCategory(@PathVariable(name = "id") Long id, Model model) {
		Category category = categoryService.get( id );
		if( category != null ) {
			if( categoryService.hasUsage(category) ) {
				model.addAttribute("categoryInUse", true);
				return showCategoriesPage(model);
			} else {
				categoryService.delete(id);
			}
		}
		return "redirect:/categories";
	}
	
}
