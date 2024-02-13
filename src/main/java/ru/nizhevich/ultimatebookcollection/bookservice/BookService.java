package ru.nizhevich.ultimatebookcollection.bookservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nizhevich.ultimatebookcollection.bookcache.BookCache;
import ru.nizhevich.ultimatebookcollection.bookmodel.Book;
import ru.nizhevich.ultimatebookcollection.bookmodel.ColumnConst;
import ru.nizhevich.ultimatebookcollection.bookmodel.SortingConst;
import ru.nizhevich.ultimatebookcollection.exception.ContentNotFoundException;
import ru.nizhevich.ultimatebookcollection.utils.BookComparatorByDate;
import ru.nizhevich.ultimatebookcollection.utils.SortMethod;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Сервис для обработки запросов на фильтрацию книг из кэша.
 *
 * @see BookCache
 */

@Slf4j
@Service
public class BookService {

    /**
     * Параметр для установки количества выдаваемых книг.
     * По умолчанию имеем ввиду Топ10 книг.
     */
    private final static int TOP_BOOKS_COUNT = 10;

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
     * @return список книг
     */
    public List<Book> getListOfBookFromCache() {
        return bookCache.getListOfBooksFromCsvFile();
    }

    /**
     * Получаем карту (название - книга) всех книг из кэша.
     *
     * @return карта книг
     */
    public Map<String, Book> geMapOfBooksFromCache() {
        return bookCache.getMapOfBooksFromCsvFile();
    }

    /**
     * Метод получения книг из списка по заданным условиям фильтрации и сортировки.
     *
     * @param year   необязательный параметр, год издания книги
     * @param column обязательный параметр, колонка для фильтрации
     * @param sort   обязательный параметр, сортировка по возрастанию или убыванию
     * @return список книг
     */
    public List<Book> getTopBooks(Integer year, ColumnConst column, SortingConst sort) throws ContentNotFoundException {
        List<Book> books = getListOfBookFromCache();
        if (Objects.nonNull(year)) {
            books = getBooksByYear(books, year);
        }
        books = getFilteredBooksByColumn(books, column, sort);
        if (Objects.isNull(books) || books.isEmpty()) {
            log.error("По заданным параметрам нет книг");
            throw new ContentNotFoundException("Book list is empty");
        }
        return books;
    }

    /**
     * Метод получения книг из списка по заданным условиям фильтрации и сортировки,
     * без учёта года издания книги.
     *
     * @param books  список книг для сортировки
     * @param column колонка по которой нужно сортировать
     * @param sort   метод сортировки по возрастанию или убыванию
     * @return список книг
     */
    public List<Book> getFilteredBooksByColumn(List<Book> books, ColumnConst column, SortingConst sort) {
        return books.stream()
                .sorted(sortedMethods.getComparator(column, sort))
                .limit(TOP_BOOKS_COUNT)
                .toList();
    }

    /**
     * Метод возвращает список книг отфильтрованный по году.
     *
     * @param year год издания книги
     * @return список книг
     */
    private List<Book> getBooksByYear(List<Book> bookList, Integer year) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
        return bookList.stream()
                .filter(book -> {
                    try {
                        String dateStr = BookComparatorByDate.deleteFirstWordIfExist(book.getPublicationDate());
                        LocalDate date = LocalDate.parse(dateStr, formatter);
                        return date.getYear() == year;
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .toList();
    }

    /**
     * Метод ищет книгу по названию и сортирует книги.
     *
     * @param bookName название книги
     * @param sort метод сортировки
     * @return список книг
     */
    public List<Book> getBookByName(String bookName, SortingConst sort) throws ContentNotFoundException {
        Map<String, Book> bookMap = geMapOfBooksFromCache();
        List<Book> foundBooks = bookMap.entrySet().stream()
                .filter(entry -> entry.getKey().toLowerCase().startsWith(bookName.toLowerCase()))
                .map(Map.Entry::getValue)
                .sorted(sortedMethods.getComparator(ColumnConst.book, sort))
                .toList();

        if (!foundBooks.isEmpty()) {
            return foundBooks;
        } else {
            log.error("Список книг с названием: " + bookName + " пуст");
            throw new ContentNotFoundException("Book list is empty");
        }
    }
}
