package com.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.common.Constants;
import com.library.model.Book;
import com.library.model.LoanedBook;
import com.library.repository.LoanedBookRepository;

@Service
public class LoanedBookService {

	@Autowired
	private LoanedBookRepository loanedBookRepository;
	
	public List<LoanedBook> getAll() {
		return loanedBookRepository.findAll();
	}
	
	public LoanedBook get(Long id) {
		return loanedBookRepository.findById(id).get();
	}
	
	public Long getCountByBook(Book book) {
		return loanedBookRepository.countByBookAndReturned(book, Constants.BOOK_NOT_RETURNED);
	}
	
	public LoanedBook save(LoanedBook loanedBook) {
		return loanedBookRepository.save(loanedBook);
	}
	
	public LoanedBook addNew(LoanedBook loanedBook) {
		loanedBook.setReturned( Constants.BOOK_NOT_RETURNED );
		return loanedBookRepository.save(loanedBook);
	}

}
