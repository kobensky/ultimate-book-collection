package ru.nizhevich.ultimatebookcollection.exception;

/**
 * Пользовательское исключение если возникают проблемы с кэшем
 */
public class EmptyCacheException extends RuntimeException{
    public EmptyCacheException(String message) {
        super(message);
    }
}
