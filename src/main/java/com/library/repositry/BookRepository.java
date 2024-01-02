package com.library.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import com.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    // Custom queries can be added here
}
