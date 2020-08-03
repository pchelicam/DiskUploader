package ru.pchelicam.tools.du;

/**
 * Exceptions that can appear in application
 */
public class DiskException extends RuntimeException {
    public DiskException(String message) {
        super(message);
    }
    public DiskException(String message, Throwable t) {
        super(message, t);
    }
}
