package ru.nizhevich.ultimatebookcollection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nizhevich.ultimatebookcollection.bookservice.BookService;
import ru.nizhevich.ultimatebookcollection.model.Book;
import ru.nizhevich.ultimatebookcollection.model.ColumnConst;
import ru.nizhevich.ultimatebookcollection.model.SortingConst;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class TopTenBooksController {

    @Autowired
    private BookService bookService;

    @GetMapping("/test")
    public String test(){
        return "Test";
    }

    @GetMapping("/allBooks")
    public ResponseEntity<List<Book>> getAllBooks(){
        return new ResponseEntity<>(bookService.getAllBooksFromCsvFile(), HttpStatus.OK);
    }

    @GetMapping("/top10")
    public ResponseEntity<List<Book>> getTopTenBooks(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = true) ColumnConst column,
            @RequestParam(required = true) SortingConst sort
    ) {
        List<Book> books = bookService.getAllBooksFromCsvFile();
        if (Objects.nonNull(year)) {
            books = bookService.getBooksByYear(books, year);
        }
        books = bookService.getFilteredBooksByColumn(books, column, sort);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
