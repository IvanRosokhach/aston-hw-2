package ru.aston.exception;

public class CustomSqlException extends RuntimeException {

    public CustomSqlException(String message) {
        super(message);
    }

}
