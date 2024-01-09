package com.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.model.Loan;
import com.library.model.Member;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
	public List<Loan> findByReturned(Integer returned);
	public Long countByMemberAndReturned(Member member, Integer returned);
}
