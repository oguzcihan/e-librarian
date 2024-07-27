package org.cihan.elibrarian.exceptions;

public class GenException extends RuntimeException {
    private final int statusCode;

    public GenException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
