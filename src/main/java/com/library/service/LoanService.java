package com.library.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.common.Constants;
import com.library.model.Loan;
import com.library.model.Member;
import com.library.repository.LoanRepository;

@Service
public class LoanService {

	@Autowired
	private LoanRepository loanRepository;
	
	public List<Loan> getAll() {
		return loanRepository.findAll();
	}
	
	public Loan get(Long id) {
		return loanRepository.findById(id).get();
	}
	
	public List<Loan> getAllUnreturned() {
		return loanRepository.findByReturned( Constants.BOOK_NOT_RETURNED );
	}
	
	public Loan addNew(Loan loan) {
		loan.setloanDate( new Date() );
		loan.setReturned( Constants.BOOK_NOT_RETURNED );
		return loanRepository.save(loan);
	}
	
	public Loan save(Loan loan) {
		return loanRepository.save(loan);
	}
	
	public Long getCountByMember(Member member) {
		return loanRepository.countByMemberAndReturned(member, Constants.BOOK_NOT_RETURNED);
	}
}
