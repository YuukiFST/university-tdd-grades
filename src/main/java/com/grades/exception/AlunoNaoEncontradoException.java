package com.grades.exception;

public class AlunoNaoEncontradoException extends RuntimeException {

    public AlunoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
