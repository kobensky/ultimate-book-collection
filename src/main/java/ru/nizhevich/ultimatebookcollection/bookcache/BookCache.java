package ru.nizhevich.ultimatebookcollection.bookcache;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.nizhevich.ultimatebookcollection.exception.EmptyCacheException;
import ru.nizhevich.ultimatebookcollection.model.Book;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс по сути является кэшем, который инициализируется при старте приложения.
 *
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BookCache {

    private final String path;
    private File csvFile;
    private static List<Book> allBooksFromCsvFile;

    @Autowired
    public BookCache(@Value("${csv.path}") String path) {
        this.path = path;
        loadResource();
    }

    private void loadResource() {
        Resource resource = new ClassPathResource(path);
        try {
            this.csvFile = resource.getFile();
        } catch (IOException e) {
            throw new EmptyCacheException("The CSV file path is invalid. Check the path to CSV file");
        }
    }

    public List<Book> getAllBooksFromCsvFile() throws EmptyCacheException {
        if(Objects.isNull(allBooksFromCsvFile) || allBooksFromCsvFile.isEmpty()) {
            throw new EmptyCacheException("Cache have a problem");
        }
        return new ArrayList<>(allBooksFromCsvFile);
    }

    @PostConstruct
    private void init() {
        allBooksFromCsvFile = initCsvFile();
    }

    private List<Book> initCsvFile() {
        CsvToBean<Book> csvToBean = null;
        List<Book> resultBookList = null;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            csvToBean = new CsvToBeanBuilder<Book>(br)
                    .withType(Book.class)
                    .withIgnoreEmptyLine(true)
                    .withSkipLines(1)
                    .build();
            resultBookList = csvToBean.parse();
        } catch (IOException ex) {
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
}
