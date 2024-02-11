package ru.nizhevich.ultimatebookcollection.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

/**
 * Конвертер из String в Long.
 * Для OpenCSV.
 */
public class LongConverter extends AbstractBeanField<Long, String> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException {
        if (value.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

