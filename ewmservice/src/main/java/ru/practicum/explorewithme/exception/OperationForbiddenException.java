package ru.practicum.explorewithme.exception;

public class OperationForbiddenException extends RuntimeException {
    public OperationForbiddenException(String message) {
        super(message);
    }
}
