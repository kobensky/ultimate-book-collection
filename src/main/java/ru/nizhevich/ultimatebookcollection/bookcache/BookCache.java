package ru.nizhevich.ultimatebookcollection.bookcache;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.nizhevich.ultimatebookcollection.exception.EmptyCacheException;
import ru.nizhevich.ultimatebookcollection.model.Book;

import java.io.BufferedReader;
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
@NoArgsConstructor
public class BookCache {
    @Value("${csv.path}")
    private static String path;

    private static List<Book> allBooksFromCsvFile;

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
        try {
            csvToBean = new CsvToBeanBuilder<Book>(new BufferedReader(new FileReader(path)))
                    .withType(Book.class)
                    .withIgnoreEmptyLine(true)
                    .withSkipLines(1)
                    .build();
        } catch (IOException ex) {
            throw new EmptyCacheException("The CSV file is empty or does not exists. Check the path to CSV file");
        }
        return csvToBean.parse().stream()
                .peek(obj -> {
                    if (!obj.getGenres().isEmpty()) {
                        List<String> updatedGenres = obj.getGenres().subList(1, obj.getGenres().size());
                        obj.setGenres(updatedGenres);
                    }
                }).toList();
    }
}
