package com.library.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.common.Constants;
import com.library.model.Book;
import com.library.model.Category;
import com.library.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private LoanedBookService loanedBookService;
	
	public Long getTotalCount() {
		return bookRepository.count();
	}
	
	public Long getTotalLoanedBooks() {
		return bookRepository.countByStatus(Constants.BOOK_STATUS_LOANED);
	}
	
	public List<Book> getAll() {
		return bookRepository.findAll();
	}
	
	public Book get(Long id) {
		return bookRepository.findById(id).get();
	}
	
	public Book getByTag(String tag) {
		return bookRepository.findByTag(tag);
	}
	
	public List<Book> get(List<Long> ids) {
		return bookRepository.findAllById(ids);
	}
	
	public List<Book> getByCategory(Category category) {
		return bookRepository.findByCategory(category);
	}
	
	public List<Book> geAvailabletByCategory(Category category) {
		return bookRepository.findByCategoryAndStatus(category, Constants.BOOK_STATUS_AVAILABLE);
	}
	
	public Book addNew(Book book) {
		book.setCreateDate(new Date());
		book.setStatus( Constants.BOOK_STATUS_AVAILABLE );
		return bookRepository.save(book);
	}
	
	public Book save(Book book) {
		return bookRepository.save(book);
	}
	
	public void delete(Book book) {
		bookRepository.delete(book);
	}
	
	public void delete(Long id) {
		bookRepository.deleteById(id);
	}
	
	public boolean hasUsage(Book book) {
		return loanedBookService.getCountByBook(book)>0;
	}
}
