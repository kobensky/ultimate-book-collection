package ru.nizhevich.ultimatebookcollection.utils;

import ru.nizhevich.ultimatebookcollection.bookmodel.Book;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

/**
 * Компаратор для сравнения по Дате публикации.
 */

public class BookComparatorByDate implements Comparator<Book> {
    /**
     * Формат даты согласно формату представленному в CSV файле.
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

    /**
     * Регулярное выражение, удаляющее первое слово из строки.
     */
    private final static String DELETE_FIRST_WORD_REGEX = "^\\w+\\s";

    @Override
    public int compare(Book book1, Book book2) {
        String date1Str = deleteFirstWordIfExist(book1.getPublicationDate());
        String date2Str = deleteFirstWordIfExist(book2.getPublicationDate());

        Comparator<String> dateComparator = Comparator.comparing(dateStr -> {
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                return null;
            }
        }, Comparator.nullsFirst(Comparator.naturalOrder()));

        return dateComparator.compare(date1Str, date2Str);
    }

    /**
     * Удаляет первое слово из строки, если в строке более 3ёх слов.
     * Требуется если в строке с датой есть лишнее слово, препятствующее сравнению.
     *
     * @param date строка с датой
     * @return новая строка
     */
    public static String deleteFirstWordIfExist(String date) {
        if (date.split(" ").length > 3) {
            return  date.replaceFirst(DELETE_FIRST_WORD_REGEX, "");
        } else {
            return date;
        }
    }
}
