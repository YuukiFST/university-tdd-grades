package com.grades.service;

import com.grades.model.Aluno;
import com.grades.model.Turma;

import java.util.List;

public class Media {

    public double calcularMedia(Aluno aluno) {
        validarAlunoComNotas(aluno);

        List<Double> notas = aluno.getNotas();
        double soma = notas.stream().mapToDouble(Double::doubleValue).sum();
        return soma / notas.size();
    }

    public double calcularMediaPonderada(Aluno aluno, double[] pesos) {
        validarAlunoComNotas(aluno);

        List<Double> notas = aluno.getNotas();
        if (pesos.length != notas.size()) {
            throw new IllegalArgumentException(
                    "Quantidade de pesos (" + pesos.length + ") difere da quantidade de notas (" + notas.size() + ")");
        }

        double somaPonderada = 0;
        double somaPesos = 0;
        for (int i = 0; i < notas.size(); i++) {
            somaPonderada += notas.get(i) * pesos[i];
            somaPesos += pesos[i];
        }

        if (somaPesos == 0) {
            throw new IllegalArgumentException("Soma dos pesos nao pode ser zero");
        }
        return somaPonderada / somaPesos;
    }

    public double calcularMediaTurma(Turma turma) {
        List<Aluno> alunos = turma.getAlunos();
        if (alunos.isEmpty()) {
            throw new IllegalStateException("Turma nao possui alunos");
        }

        List<Aluno> alunosComNotas = alunos.stream()
                .filter(Aluno::possuiNotas)
                .toList();

        if (alunosComNotas.isEmpty()) {
            throw new IllegalStateException("Nenhum aluno da turma possui notas");
        }

        double somaMedias = alunosComNotas.stream()
                .mapToDouble(this::calcularMedia)
                .sum();

        return somaMedias / alunosComNotas.size();
    }

    public boolean alunoAprovado(Aluno aluno, double notaMinima) {
        return calcularMedia(aluno) >= notaMinima;
    }

    private void validarAlunoComNotas(Aluno aluno) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno nao pode ser nulo");
        }
        if (!aluno.possuiNotas()) {
            throw new IllegalStateException(
                    "Aluno '" + aluno.getNome() + "' nao possui notas para calcular media");
        }
    }
}
