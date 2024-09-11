package ru.yandex.javacource.sosnin.schedule.exceptions;

public class TaskValidationException extends RuntimeException {
    public TaskValidationException(String message) {
        super(message);
    }
}
