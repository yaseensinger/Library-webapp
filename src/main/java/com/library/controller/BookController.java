package com.library.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.library.model.Book;
import com.library.model.Category;
import com.library.service.BookService;
import com.library.service.CategoryService;

@Controller
@RequestMapping(value = "/books")
public class BookController {

	@Autowired
	private BookService bookService;
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute(name = "categories")
	public List<Category> categories() {
		return categoryService.getAllBySort();
	}
	    @GetMapping
    public String showBooksPage(Model model) {
        model.addAttribute("books", bookService.getAll());
        model.addAttribute("book", new Book());
        return "books";
    }	
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addBookPage(Model model) {
		model.addAttribute("book", new Book());
		return "/books";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editBookPage(@PathVariable(name = "id") Long id,  Model model) {
		Book book = bookService.get(id);
		if( book != null ) {
			model.addAttribute("book", book);
			return "/books/form";
		} else {
			return "redirect:/books";
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveBook(@Valid Book book, BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
		if( bindingResult.hasErrors() ) {
			return "/books";
		}
		
		if( book.getId() == null ) {
			if( bookService.getByTag(book.getTag()) != null ) {
				bindingResult.rejectValue("tag", "tag", "Tag already exists");
				return "/books";
			} else {
				bookService.addNew(book);
				redirectAttributes.addFlashAttribute("successMsg", "'" + book.getTitle() + "' is added as a new Book.");
				return "redirect:/books";
			}
		} else {
			Book updatedBook = bookService.save(book);
			redirectAttributes.addFlashAttribute("successMsg", "Changes for '" + book.getTitle() + "' are saved successfully. ");
			return "redirect:/books/edit/"+updatedBook.getId();
		}
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public String removeBook(@PathVariable(name = "id") Long id, Model model) {
		Book book = bookService.get( id );
		if( book != null ) {
			if( bookService.hasUsage(book) ) {
				model.addAttribute("bookInUse", true);
				return showBooksPage(model);
			} else {
				bookService.delete(id);
			}
		}
		return "redirect:/books";
	}
}
