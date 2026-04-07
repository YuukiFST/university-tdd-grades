package com.grades.model;

import com.grades.exception.AlunoNaoEncontradoException;
import com.grades.exception.TurmaLotadaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Turma {

    private final String nome;
    private final int capacidadeMaxima;
    private final List<Aluno> alunos;

    public Turma(String nome, int capacidadeMaxima) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome da turma nao pode ser nulo ou vazio");
        }
        if (capacidadeMaxima <= 0) {
            throw new IllegalArgumentException("Capacidade maxima deve ser maior que zero");
        }
        this.nome = nome;
        this.capacidadeMaxima = capacidadeMaxima;
        this.alunos = new ArrayList<>();
    }

    public void adicionarAluno(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno nao pode ser nulo");
        }
        if (alunos.size() >= capacidadeMaxima) {
            throw new TurmaLotadaException(
                    "Turma '" + nome + "' atingiu a capacidade maxima de " + capacidadeMaxima + " alunos");
        }
        if (buscarAluno(aluno.getMatricula()).isPresent()) {
            throw new IllegalArgumentException(
                    "Aluno com matricula '" + aluno.getMatricula() + "' ja esta na turma");
        }
        alunos.add(aluno);
    }

    public void removerAluno(String matricula) {
        Aluno aluno = buscarAluno(matricula)
                .orElseThrow(() -> new AlunoNaoEncontradoException(
                        "Aluno com matricula '" + matricula + "' nao encontrado na turma"));
        alunos.remove(aluno);
    }

    public Optional<Aluno> buscarAluno(String matricula) {
        return alunos.stream()
                .filter(a -> a.getMatricula().equals(matricula))
                .findFirst();
    }

    public List<Aluno> getAlunos() {
        return Collections.unmodifiableList(alunos);
    }

    public String getNome() {
        return nome;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public boolean estaLotada() {
        return alunos.size() >= capacidadeMaxima;
    }
}
