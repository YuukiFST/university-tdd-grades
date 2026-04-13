package com.grades.service;

import com.grades.exception.NotaInvalidaException;
import com.grades.model.Aluno;
import com.grades.model.Turma;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClassificadorDesempenho {

    private static final double LIMITE_INSUFICIENTE = 4.0;
    private static final double LIMITE_REGULAR = 6.0;
    private static final double LIMITE_BOM = 8.0;

    private final Media media;

    public ClassificadorDesempenho(Media media) {
        this.media = media;
    }

    public String classificar(double mediaAluno) {
        if (mediaAluno < 0.0 || mediaAluno > 10.0) {
            throw new NotaInvalidaException("Media deve estar entre 0.0 e 10.0. Valor recebido: " + mediaAluno);
        }

        if (mediaAluno < LIMITE_INSUFICIENTE) {
            return "INSUFICIENTE";
        }
        if (mediaAluno < LIMITE_REGULAR) {
            return "REGULAR";
        }
        if (mediaAluno < LIMITE_BOM) {
            return "BOM";
        }
        return "EXCELENTE";
    }

    public Map<String, Long> gerarRelatorio(Turma turma) {
        validarTurmaComAlunos(turma);

        Map<String, Long> relatorio = new LinkedHashMap<>();
        relatorio.put("INSUFICIENTE", 0L);
        relatorio.put("REGULAR", 0L);
        relatorio.put("BOM", 0L);
        relatorio.put("EXCELENTE", 0L);

        turma.getAlunos().stream()
                .filter(Aluno::possuiNotas)
                .forEach(aluno -> {
                    double mediaAluno = media.calcularMedia(aluno);
                    String classificacao = classificar(mediaAluno);
                    relatorio.merge(classificacao, 1L, Long::sum);
                });

        return relatorio;
    }

    public Aluno obterMelhorAluno(Turma turma) {
        validarTurmaComAlunos(turma);

        return turma.getAlunos().stream()
                .filter(Aluno::possuiNotas)
                .max(Comparator.comparingDouble(media::calcularMedia))
                .orElseThrow(() -> new IllegalStateException("Nenhum aluno com notas na turma"));
    }

    public List<Aluno> obterAlunosAprovados(Turma turma, double notaMinima) {
        validarTurmaComAlunos(turma);

        return turma.getAlunos().stream()
                .filter(Aluno::possuiNotas)
                .filter(aluno -> media.alunoAprovado(aluno, notaMinima))
                .toList();
    }

    private void validarTurmaComAlunos(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma nao pode ser nula");
        }
        if (turma.getAlunos().isEmpty()) {
            throw new IllegalStateException("Turma nao possui alunos");
        }
    }
}
