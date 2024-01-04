package com.library.service;

import com.library.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book getBookById(Long id);
    void addBook(Book book);
    // Add other book-related methods...
}
