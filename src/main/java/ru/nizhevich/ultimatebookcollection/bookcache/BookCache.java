package ru.nizhevich.ultimatebookcollection.bookcache;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.nizhevich.ultimatebookcollection.exception.EmptyCacheException;
import ru.nizhevich.ultimatebookcollection.bookmodel.Book;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс по сути является кэшем книг, который инициализируется при старте приложения.
 *
 */

@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BookCache {

    private final String path;
    private InputStream csvInputStream;
    private static List<Book> ALL_BOOKS_FROM_FILE;
    private static Map<String, Book> ALL_BOOKS_BY_NAME;

    @Autowired
    public BookCache(@Value("${csv.path}") String path) {
        this.path = path;
    }

    @PostConstruct
    public void init() {
        loadResource();
        ALL_BOOKS_FROM_FILE = initCsvFile();
        ALL_BOOKS_BY_NAME = groupBooksByName(ALL_BOOKS_FROM_FILE);
    }

    private void loadResource() {
        Resource resource = new ClassPathResource(path);
        try {
            this.csvInputStream = resource.getInputStream();
        } catch (IOException e) {
            log.error("Ошибка при загрузке файла CSV: " + e.getMessage());
            throw new EmptyCacheException("The CSV file path is invalid. Check the path to CSV file");
        }
    }

    public List<Book> getListOfBooksFromCsvFile() throws EmptyCacheException {
        if(Objects.isNull(ALL_BOOKS_FROM_FILE) || ALL_BOOKS_FROM_FILE.isEmpty()) {
            log.error("Не удалось получить список книг из CSV файла");
            throw new EmptyCacheException("Cache have a problem");
        }
        return new ArrayList<>(ALL_BOOKS_FROM_FILE);
    }

    public Map<String, Book> getMapOfBooksFromCsvFile() throws EmptyCacheException {
        if(Objects.isNull(ALL_BOOKS_BY_NAME) || ALL_BOOKS_BY_NAME.isEmpty()) {
            log.error("Не удалось получить список книг из CSV файла");
            throw new EmptyCacheException("Cache have a problem");
        }
        return new HashMap<>(ALL_BOOKS_BY_NAME);
    }

    private List<Book> initCsvFile() {
        CsvToBean<Book> csvToBean = null;
        List<Book> resultBookList = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvInputStream))) {
            csvToBean = new CsvToBeanBuilder<Book>(br)
                    .withType(Book.class)
                    .withIgnoreEmptyLine(true)
                    .withSkipLines(1)
                    .build();
            resultBookList = csvToBean.parse();
        } catch (IOException ex) {
            log.error("Проблема с инициализацией CSV файла");
            throw new EmptyCacheException("The CSV file is empty or does not exists. Check the path to CSV file");
        }
        return resultBookList.stream()
                .peek(obj -> {
                    if (!obj.getGenres().isEmpty()) {
                        List<String> updatedGenres = obj.getGenres().subList(1, obj.getGenres().size());
                        obj.setGenres(updatedGenres);
                    }
                }).toList();
    }

    private Map<String, Book> groupBooksByName(List<Book> books) {
        return books.stream()
                .filter(book -> book.getBook() != null)
                .collect(Collectors
                        .toMap(Book::getBook, book -> book, (existing, replacement) -> existing));
    }
}
