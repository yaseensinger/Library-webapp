package com.library.restcontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.common.Constants;
import com.library.model.Book;
import com.library.model.Loan;
import com.library.model.LoanedBook;
import com.library.model.Member;
import com.library.service.BookService;
import com.library.service.LoanService;
import com.library.service.LoanedBookService;
import com.library.service.MemberService;

@RestController
@RequestMapping(value = "/rest/loan")
public class LoanRestController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private LoanService loanService;
	
	@Autowired
	private LoanedBookService loanedBookservice;
	
	@RequestMapping(value="/save", method = RequestMethod.POST)
	public String save(@RequestParam Map<String, String> payload) {
		
		String memberIdStr = (String)payload.get("member");
		String[] bookIdsStr = payload.get("books").toString().split(",");
		
		Long memberId = null;
		List<Long> bookIds = new ArrayList<Long>();
		try {
			memberId = Long.parseLong( memberIdStr );
			for(int k=0 ; k<bookIdsStr.length ; k++) {
				bookIds.add( Long.parseLong(bookIdsStr[k]) );
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return "invalid number format";
		}
		
		Member member = memberService.get( memberId );
		List<Book> books = bookService.get(bookIds);
		
		Loan loan = new Loan();
		loan.setMember(member);
		loan = loanService.addNew(loan);
		
		for( int k=0 ; k<books.size() ; k++ ) {
			Book book = books.get(k);
			book.setStatus( Constants.BOOK_STATUS_LOANED );
			book = bookService.save(book);
			
			LoanedBook ib = new LoanedBook();
			ib.setBook( book );
			ib.setloan( loan );
			loanedBookservice.addNew( ib );
			
		}
		
		return "success";
	}
	
	@RequestMapping(value = "/{id}/return/all", method = RequestMethod.GET)
	public String returnAll(@PathVariable(name = "id") Long id) {
		Loan loan = loanService.get(id);
		if( loan != null ) {
			List<LoanedBook> loanedBooks = loan.getLoanedBooks();
			for( int k=0 ; k<loanedBooks.size() ; k++ ) {
				LoanedBook ib = loanedBooks.get(k);
				ib.setReturned( Constants.BOOK_RETURNED );
				loanedBookservice.save( ib );
				
				Book book = ib.getBook();
				book.setStatus( Constants.BOOK_STATUS_AVAILABLE );
				bookService.save(book);
			}
			
			loan.setReturned( Constants.BOOK_RETURNED );
			loanService.save(loan);
			
			return "successful";
		} else {
			return "unsuccessful";
		}
	}
	
	@RequestMapping(value="/{id}/return", method = RequestMethod.POST)
	public String returnSelected(@RequestParam Map<String, String> payload, @PathVariable(name = "id") Long id) {
		Loan loan = loanService.get(id);
		String[] loanedBookIds = payload.get("ids").split(",");
		if( loan != null ) {
			
			List<LoanedBook> loanedBooks = loan.getLoanedBooks();
			for( int k=0 ; k<loanedBooks.size() ; k++ ) {
				LoanedBook ib = loanedBooks.get(k);
				if( Arrays.asList(loanedBookIds).contains( ib.getId().toString() ) ) {
					ib.setReturned( Constants.BOOK_RETURNED );
					loanedBookservice.save( ib );
					
					Book book = ib.getBook();
					book.setStatus( Constants.BOOK_STATUS_AVAILABLE );
					bookService.save(book);
				}
			}
			
			return "successful";
		} else {
			return "unsuccessful";
		}
	}
	
}
