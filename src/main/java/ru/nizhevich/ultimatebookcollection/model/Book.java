package ru.nizhevich.ultimatebookcollection.model;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import lombok.*;
import ru.nizhevich.ultimatebookcollection.utils.DoubleConverter;
import ru.nizhevich.ultimatebookcollection.utils.LongConverter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

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
            splitOn = "[\\[',\\]\\s]+|'",
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
