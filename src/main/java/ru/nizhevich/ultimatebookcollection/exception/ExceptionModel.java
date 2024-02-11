package ru.nizhevich.ultimatebookcollection.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionModel {
    private String systemMessage;
    private String userMessage;
}
