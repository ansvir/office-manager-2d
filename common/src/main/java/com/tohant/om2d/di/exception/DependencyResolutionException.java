package com.tohant.om2d.di.exception;

public class DependencyResolutionException extends RuntimeException {
    public DependencyResolutionException(String message, Throwable exception) {
        super(message, exception);
    }

}
