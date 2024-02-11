package ru.nizhevich.ultimatebookcollection.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class DoubleConverter extends AbstractBeanField<Double, String> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException {
        if (value.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
