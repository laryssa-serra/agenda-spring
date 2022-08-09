package br.com.agenda.model.entities;

public class InvalidFormatException extends RuntimeException {
    public InvalidFormatException(String s) {
        super(s);
    }
}
