package com.eugepavia.todolist.exception;

public record ErrorResponse(
        int status,
        String message
) {
}
