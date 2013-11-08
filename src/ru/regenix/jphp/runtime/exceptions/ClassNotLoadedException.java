package ru.regenix.jphp.runtime.exceptions;

public class ClassNotLoadedException extends RuntimeException {

    public ClassNotLoadedException(String name) {
        super(String.format("Class '%s' not loaded", name));
    }
}
