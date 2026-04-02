package com.grades.exception;

public class NotaInvalidaException extends RuntimeException {

    public NotaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
