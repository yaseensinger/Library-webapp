package com.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.model.Book;
import com.library.model.LoanedBook;

@Repository
public interface LoanedBookRepository extends JpaRepository<LoanedBook, Long> {
	public Long countByBookAndReturned(Book book, Integer returned);
}
