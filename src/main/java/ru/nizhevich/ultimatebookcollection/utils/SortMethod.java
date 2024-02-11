package ru.nizhevich.ultimatebookcollection.utils;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.nizhevich.ultimatebookcollection.model.Book;
import ru.nizhevich.ultimatebookcollection.model.ColumnConst;
import ru.nizhevich.ultimatebookcollection.model.SortingConst;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static ru.nizhevich.ultimatebookcollection.model.ColumnConst.*;

/**
 * Бин для хранения методов сортировки по разным колонкам.
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@NoArgsConstructor
public class SortMethod {
    private static Map<ColumnConst, Comparator<Book>> sortedMethods = new HashMap<>();

    @PostConstruct
    public void init(){
        sortedMethods.put(book, Comparator.comparing(Book::getBook));
        sortedMethods.put(author, Comparator.comparing(Book::getAuthor));
        sortedMethods.put(numPages, Comparator.comparing(Book::getNumPages));
        sortedMethods.put(publicationDate, new BookComparatorByDate());
        sortedMethods.put(rating, Comparator.comparing(Book::getRating));
        sortedMethods.put(numberOfVoters, Comparator.comparing(Book::getNumberOfVoters));
    }

    /**
     * Метод получения компаратора в зависимости от условий.
     *
     * @param column колонка по которой хотим сортировать
     * @param sort сортировка по возрастанию\убыванию
     * @return компаратор
     */
    public Comparator<Book> getComparator(ColumnConst column, SortingConst sort) {
        return sort == SortingConst.ASC ? sortedMethods.get(column) : sortedMethods.get(column).reversed();
    }
}
