package ru.nizhevich.ultimatebookcollection.utils;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.nizhevich.ultimatebookcollection.bookmodel.Book;
import ru.nizhevich.ultimatebookcollection.bookmodel.ColumnConst;
import ru.nizhevich.ultimatebookcollection.bookmodel.SortingConst;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static ru.nizhevich.ultimatebookcollection.bookmodel.ColumnConst.*;

/**
 * Бин для хранения методов сортировки по разным колонкам.
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@NoArgsConstructor
public class SortMethod {
    private static Map<ColumnConst, Comparator<Book>> SORTED_METHODS = new HashMap<>();

    @PostConstruct
    public void init(){
        SORTED_METHODS.put(book, Comparator.comparing(Book::getBook));
        SORTED_METHODS.put(author, Comparator.comparing(Book::getAuthor));
        SORTED_METHODS.put(numPages, Comparator.comparing(Book::getNumPages));
        SORTED_METHODS.put(publicationDate, new BookComparatorByDate());
        SORTED_METHODS.put(rating, Comparator.comparing(Book::getRating));
        SORTED_METHODS.put(numberOfVoters, Comparator.comparing(Book::getNumberOfVoters));
    }

    /**
     * Метод получения компаратора в зависимости от условий.
     *
     * @param column колонка по которой хотим сортировать
     * @param sort сортировка по возрастанию\убыванию
     * @return компаратор
     */
    public Comparator<Book> getComparator(ColumnConst column, SortingConst sort) {
        return sort == SortingConst.ASC ? SORTED_METHODS.get(column) : SORTED_METHODS.get(column).reversed();
    }
}
