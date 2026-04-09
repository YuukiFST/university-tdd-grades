package com.grades.service;

import com.grades.model.Aluno;
import com.grades.model.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Media")
class MediaTest {

    private Media media;

    @BeforeEach
    void setUp() {
        media = new Media();
    }

    @Nested
    @DisplayName("Media simples")
    class MediaSimples {

        @Test
        @DisplayName("deve calcular media aritmetica das notas")
        void deveCalcularMediaAritmetica() {
            Aluno aluno = criarAlunoComNotas("Ana", "001", 7.0, 8.0, 9.0);

            double resultado = media.calcularMedia(aluno);

            assertEquals(8.0, resultado, 0.001);
        }

        @Test
        @DisplayName("deve calcular media com nota unica")
        void deveCalcularMediaComNotaUnica() {
            Aluno aluno = criarAlunoComNotas("Ana", "001", 6.5);

            assertEquals(6.5, media.calcularMedia(aluno), 0.001);
        }

        @Test
        @DisplayName("deve lancar excecao quando aluno sem notas")
        void deveLancarExcecaoQuandoSemNotas() {
            Aluno aluno = new Aluno("Ana", "001");

            assertThrows(IllegalStateException.class,
                    () -> media.calcularMedia(aluno));
        }

        @Test
        @DisplayName("deve lancar excecao quando aluno for nulo")
        void deveLancarExcecaoQuandoAlunoNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> media.calcularMedia(null));
        }
    }

    @Nested
    @DisplayName("Media ponderada")
    class MediaPonderada {

        @Test
        @DisplayName("deve calcular media ponderada corretamente")
        void deveCalcularMediaPonderada() {
            Aluno aluno = criarAlunoComNotas("Ana", "001", 6.0, 8.0, 10.0);
            double[] pesos = { 2, 3, 5 };

            double resultado = media.calcularMediaPonderada(aluno, pesos);

            // (6*2 + 8*3 + 10*5) / (2+3+5) = (12+24+50)/10 = 8.6
            assertEquals(8.6, resultado, 0.001);
        }

        @Test
        @DisplayName("deve lancar excecao quando pesos diferem das notas")
        void deveLancarExcecaoQuandoPesosDiferemDasNotas() {
            Aluno aluno = criarAlunoComNotas("Ana", "001", 7.0, 8.0);
            double[] pesos = { 1, 2, 3 };

            assertThrows(IllegalArgumentException.class,
                    () -> media.calcularMediaPonderada(aluno, pesos));
        }

        @Test
        @DisplayName("deve lancar excecao quando soma dos pesos for zero")
        void deveLancarExcecaoQuandoSomaPesosZero() {
            Aluno aluno = criarAlunoComNotas("Ana", "001", 7.0);
            double[] pesos = { 0 };

            assertThrows(IllegalArgumentException.class,
                    () -> media.calcularMediaPonderada(aluno, pesos));
        }
    }

    @Nested
    @DisplayName("Media da turma")
    class MediaTurma {

        @Test
        @DisplayName("deve calcular media geral da turma")
        void deveCalcularMediaGeralDaTurma() {
            Turma turma = new Turma("Turma A", 30);

            Aluno a1 = criarAlunoComNotas("Ana", "001", 8.0, 8.0);
            Aluno a2 = criarAlunoComNotas("Bruno", "002", 6.0, 6.0);
            turma.adicionarAluno(a1);
            turma.adicionarAluno(a2);

            // Media a1=8.0, Media a2=6.0 → Media turma = 7.0
            assertEquals(7.0, media.calcularMediaTurma(turma), 0.001);
        }

        @Test
        @DisplayName("deve lancar excecao quando turma sem alunos")
        void deveLancarExcecaoQuandoTurmaSemAlunos() {
            Turma turma = new Turma("Turma Vazia", 30);

            assertThrows(IllegalStateException.class,
                    () -> media.calcularMediaTurma(turma));
        }
    }

    @Nested
    @DisplayName("Aprovacao")
    class Aprovacao {

        @Test
        @DisplayName("deve considerar aluno aprovado com media acima do minimo")
        void deveConsiderarAlunoAprovado() {
            Aluno aluno = criarAlunoComNotas("Ana", "001", 7.0, 8.0);

            assertTrue(media.alunoAprovado(aluno, 6.0));
        }

        @Test
        @DisplayName("deve considerar aluno reprovado com media abaixo do minimo")
        void deveConsiderarAlunoReprovado() {
            Aluno aluno = criarAlunoComNotas("Ana", "001", 3.0, 4.0);

            assertFalse(media.alunoAprovado(aluno, 6.0));
        }

        @Test
        @DisplayName("deve considerar aluno aprovado com media igual ao minimo")
        void deveConsiderarAprovadoComMediaIgualAoMinimo() {
            Aluno aluno = criarAlunoComNotas("Ana", "001", 6.0, 6.0);

            assertTrue(media.alunoAprovado(aluno, 6.0));
        }
    }

    private Aluno criarAlunoComNotas(String nome, String matricula, double... notas) {
        Aluno aluno = new Aluno(nome, matricula);
        for (double nota : notas) {
            aluno.adicionarNota(nota);
        }
        return aluno;
    }
}
