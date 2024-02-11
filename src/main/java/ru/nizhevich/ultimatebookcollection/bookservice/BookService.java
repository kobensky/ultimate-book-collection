package ru.nizhevich.ultimatebookcollection.bookservice;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.nizhevich.ultimatebookcollection.model.Book;
import ru.nizhevich.ultimatebookcollection.model.ColumnConst;
import ru.nizhevich.ultimatebookcollection.model.SortingConst;
import ru.nizhevich.ultimatebookcollection.utils.SortMethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class BookService {
    @Value("${csv.path}")
    private String path;
    @Autowired
    private SortMethod sortedMethods;

    private static List<Book> allBooksFromCsvFile;

    public List<Book> getAllBooksFromCsvFile() {
        return new ArrayList<>(allBooksFromCsvFile);
    }

    @PostConstruct
    private void init() {
        allBooksFromCsvFile = initCsvFile();
    }

    private List<Book> initCsvFile() {
        CsvToBean<Book> csvToBean = null;
        try {
            csvToBean = new CsvToBeanBuilder<Book>(new BufferedReader(new FileReader(path)))
                    .withType(Book.class)
                    .withIgnoreEmptyLine(true)
                    .withSkipLines(1)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return csvToBean.parse();
    }

    public List<Book> getFilteredBooksByColumn(List<Book> books, ColumnConst column, SortingConst sort) {
        return books.stream()
                .sorted(sortedMethods.getComparator(column, sort))
                .limit(10)
                .collect(toList());
    }

    /**
     * Метод возвращает список книг отфильтрованный по году
     *
     * @param year год издания книги
     * @return список книг
     */
    public List<Book> getBooksByYear(List<Book> bookList, Integer year) {
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
