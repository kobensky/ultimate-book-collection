package ru.nizhevich.ultimatebookcollection.bookservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.nizhevich.ultimatebookcollection.bookcache.BookCache;
import ru.nizhevich.ultimatebookcollection.model.Book;
import ru.nizhevich.ultimatebookcollection.model.ColumnConst;
import ru.nizhevich.ultimatebookcollection.model.SortingConst;
import ru.nizhevich.ultimatebookcollection.utils.SortMethod;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Сервис для обработки запросов на фильтрацию книг из кэша.
 *
 * @see BookCache
 */

@Service
public class BookService {

    private final SortMethod sortedMethods;

    private final BookCache bookCache;

    @Autowired
    public BookService(SortMethod sortMethod, BookCache bookCache) {
        this.bookCache = bookCache;
        this.sortedMethods = sortMethod;
    }

    /**
     * Получаем список всех книг из кэша.
     *
     * @return
     */
    public List<Book> getAllBooksFromCache() {
        return bookCache.getAllBooksFromCsvFile();
    }

    /**
     * Метод получения первых 10 книг из списка по заданным условиям фильтрации и сортировки.
     *
     * @param year   необязательный параметр, год издания книги
     * @param column обязательный параметр, колонка для фильтрации
     * @param sort   обязательный параметр, сортировка по возрастанию или убыванию
     * @return список книг
     */
    public ResponseEntity<List<Book>> getTopTenBooks(Integer year, ColumnConst column, SortingConst sort) {
        List<Book> books = getAllBooksFromCache();
        if (Objects.nonNull(year)) {
            books = getBooksByYear(books, year);
        }
        books = getFilteredBooksByColumn(books, column, sort);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Метод получения первых 10 книг из списка по заданным условиям фильтрации и сортировки,
     * без учёта года издания книги.
     *
     * @param books список книг для сортировки
     * @param column колонка по которой нужно сортировать
     * @param sort метод сортировки по возрастанию или убыванию
     * @return список книг
     */
    public List<Book> getFilteredBooksByColumn(List<Book> books, ColumnConst column, SortingConst sort) {
        return books.stream()
                .sorted(sortedMethods.getComparator(column, sort))
                .limit(10)
                .collect(toList());
    }

    /**
     * Метод возвращает список книг отфильтрованный по году.
     *
     * @param year год издания книги
     * @return список книг
     */
    private List<Book> getBooksByYear(List<Book> bookList, Integer year) {
        // тут потенциальный баг, который я уже исправил в BookComparatorByDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        return bookList.stream()
                .filter(book -> {
                    try {
                        LocalDate date = LocalDate.parse(book.getPublicationDate(), formatter);
                        return date.getYear() == year;
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }
}
