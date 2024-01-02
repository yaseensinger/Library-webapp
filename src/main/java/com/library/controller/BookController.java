package com.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.library.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
    // Controller logic here
}
