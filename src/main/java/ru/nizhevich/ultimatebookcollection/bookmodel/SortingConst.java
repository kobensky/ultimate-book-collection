package ru.nizhevich.ultimatebookcollection.bookmodel;

/**
 * Константы методов сортировки
 */

public enum SortingConst {
    ASC("По возрастанию"),
    DESC("По убыванию");

    private final String value;

    SortingConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
