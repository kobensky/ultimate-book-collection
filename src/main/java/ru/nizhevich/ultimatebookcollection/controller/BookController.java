package ru.nizhevich.ultimatebookcollection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nizhevich.ultimatebookcollection.bookservice.BookService;
import ru.nizhevich.ultimatebookcollection.bookmodel.Book;
import ru.nizhevich.ultimatebookcollection.bookmodel.ColumnConst;
import ru.nizhevich.ultimatebookcollection.bookmodel.SortingConst;

import java.util.List;

/**
 * Эндпоинты для работы со списком книг.
 */

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/allBooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.getListOfBookFromCache(), HttpStatus.OK);
    }

    @GetMapping("/top10")
    public ResponseEntity<List<Book>> getTopTenBooks(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = true) ColumnConst column,
            @RequestParam(required = true) SortingConst sort
    ) {
        return new ResponseEntity<>(bookService.getTopBooks(year, column, sort), HttpStatus.OK);
    }

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getBooksByName(
            @RequestParam(required = true) String bookName,
            @RequestParam(required = true) SortingConst sort
    ) {
        return new ResponseEntity<>(bookService.getBookByName(bookName, sort), HttpStatus.OK);
    }
}
