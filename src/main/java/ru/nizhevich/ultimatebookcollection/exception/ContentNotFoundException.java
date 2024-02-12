package ru.nizhevich.ultimatebookcollection.exception;

/**
 * Пользовательское исключение если по запросу не найдено данных.
 */

public class ContentNotFoundException extends RuntimeException{
    public ContentNotFoundException(String message) {
        super(message);
    }
}
