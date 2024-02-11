package ru.nizhevich.ultimatebookcollection.model;

/**
 * Константы для сортировки книг
 */
public enum ColumnConst {
    book("Название книги"),
    author("Автор книги"),
    numPages("Количество страниц"),
    publicationDate("Дата публикации"),
    rating("Рейтинг"),
    numberOfVoters("Количество проголосовавших");

    private final String value;
    ColumnConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
