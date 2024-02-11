package ru.nizhevich.ultimatebookcollection.model;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import lombok.*;
import ru.nizhevich.ultimatebookcollection.utils.DoubleConverter;
import ru.nizhevich.ultimatebookcollection.utils.LongConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель книги из CSV файла.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    /**
     * Строка в CSV файле содержащая жанры может быть представлена как список подстрок.
     * Это регулярное выражение для разделения строки на подстроки.
     * Делит по [ , ' ] и пробелу
     */
    private final static String SPLIT_GENRES_STRING = "[\\[',\\]\\s]+|'";

    @CsvCustomBindByPosition(position = 0, converter = LongConverter.class)
    private Long id;

    @CsvBindByPosition(position = 2)
    private String book;

    @CsvBindByPosition(position = 3)
    private String series;

    @CsvCustomBindByPosition(position = 4, converter = LongConverter.class)
    private Long releaseNumber;

    @CsvBindByPosition(position = 5)
    private String author;

    @CsvBindByPosition(position = 8)
    private String description;

    @CsvCustomBindByPosition(position = 9, converter = LongConverter.class)
    private Long numPages;

    @CsvBindByPosition(position = 10)
    private String format;

    @CsvBindAndSplitByPosition(
            position = 11,
            elementType = String.class,
            splitOn = SPLIT_GENRES_STRING,
            collectionType = ArrayList.class
    )
    private List<String> genres;

    @CsvBindByPosition(position = 12)
    private String publicationDate;

    @CsvCustomBindByPosition(position = 13, converter = DoubleConverter.class)
    private Double rating;

    @CsvCustomBindByPosition(position = 14, converter = DoubleConverter.class)
    private Double numberOfVoters;
}
