package ru.nizhevich.ultimatebookcollection.utils;

import ru.nizhevich.ultimatebookcollection.model.Book;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class BookComparatorByDate implements Comparator<Book> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

    @Override
    public int compare(Book book1, Book book2) {
        try {
            Date date1 = dateFormat.parse(book1.getPublicationDate());
            Date date2 = dateFormat.parse(book2.getPublicationDate());
            return date1.compareTo(date2);
        } catch (ParseException e) {
            return 0;
        }
    }
}
