package com.grades.model;

import com.grades.exception.NotaInvalidaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno {

    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 10.0;

    private final String nome;
    private final String matricula;
    private final List<Double> notas;

    public Aluno(String nome, String matricula) {
        validarCampoObrigatorio(nome, "Nome");
        validarCampoObrigatorio(matricula, "Matricula");
        this.nome = nome;
        this.matricula = matricula;
        this.notas = new ArrayList<>();
    }

    public void adicionarNota(double nota) {
        if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new NotaInvalidaException(
                    "Nota deve estar entre " + NOTA_MINIMA + " e " + NOTA_MAXIMA + ". Valor recebido: " + nota);
        }
        notas.add(nota);
    }

    public List<Double> getNotas() {
        return Collections.unmodifiableList(notas);
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public boolean possuiNotas() {
        return !notas.isEmpty();
    }

    private void validarCampoObrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " nao pode ser nulo ou vazio");
        }
    }
}
