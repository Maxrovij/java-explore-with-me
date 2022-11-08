package ru.practicum.explorewithme.exceptions;

public class OperationForbiddenException extends RuntimeException {
    public OperationForbiddenException(String message) {
        super(message);
    }
}
