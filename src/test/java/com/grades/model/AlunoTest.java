package com.grades.model;

import com.grades.exception.NotaInvalidaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Aluno")
class AlunoTest {

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        aluno = new Aluno("Maria Silva", "2024001");
    }

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("deve criar aluno com nome e matricula validos")
        void deveCriarAlunoComDadosValidos() {
            assertEquals("Maria Silva", aluno.getNome());
            assertEquals("2024001", aluno.getMatricula());
            assertTrue(aluno.getNotas().isEmpty());
        }

        @Test
        @DisplayName("deve lancar excecao quando nome for nulo")
        void deveLancarExcecaoQuandoNomeNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Aluno(null, "2024001"));
        }

        @Test
        @DisplayName("deve lancar excecao quando nome for vazio")
        void deveLancarExcecaoQuandoNomeVazio() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Aluno("", "2024001"));
        }

        @Test
        @DisplayName("deve lancar excecao quando matricula for nula")
        void deveLancarExcecaoQuandoMatriculaNula() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Aluno("Maria", null));
        }

        @Test
        @DisplayName("deve lancar excecao quando matricula for vazia")
        void deveLancarExcecaoQuandoMatriculaVazia() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Aluno("Maria", "  "));
        }
    }

    @Nested
    @DisplayName("Notas")
    class Notas {

        @Test
        @DisplayName("deve adicionar nota valida")
        void deveAdicionarNotaValida() {
            aluno.adicionarNota(8.5);

            assertEquals(1, aluno.getNotas().size());
            assertEquals(8.5, aluno.getNotas().get(0), 0.001);
        }

        @Test
        @DisplayName("deve aceitar nota zero")
        void deveAceitarNotaZero() {
            aluno.adicionarNota(0.0);

            assertEquals(0.0, aluno.getNotas().get(0), 0.001);
        }

        @Test
        @DisplayName("deve aceitar nota dez")
        void deveAceitarNotaDez() {
            aluno.adicionarNota(10.0);

            assertEquals(10.0, aluno.getNotas().get(0), 0.001);
        }

        @Test
        @DisplayName("deve lancar excecao para nota negativa")
        void deveLancarExcecaoParaNotaNegativa() {
            assertThrows(NotaInvalidaException.class,
                    () -> aluno.adicionarNota(-1.0));
        }

        @Test
        @DisplayName("deve lancar excecao para nota acima de dez")
        void deveLancarExcecaoParaNotaAcimaDeDez() {
            assertThrows(NotaInvalidaException.class,
                    () -> aluno.adicionarNota(10.1));
        }

        @Test
        @DisplayName("deve retornar lista imutavel de notas")
        void deveRetornarListaImutavelDeNotas() {
            aluno.adicionarNota(7.0);

            assertThrows(UnsupportedOperationException.class,
                    () -> aluno.getNotas().add(5.0));
        }

        @Test
        @DisplayName("deve adicionar multiplas notas")
        void deveAdicionarMultiplasNotas() {
            aluno.adicionarNota(7.0);
            aluno.adicionarNota(8.0);
            aluno.adicionarNota(9.0);

            assertEquals(3, aluno.getNotas().size());
        }

        @Test
        @DisplayName("deve verificar se aluno possui notas")
        void deveVerificarSeAlunoPossuiNotas() {
            assertFalse(aluno.possuiNotas());

            aluno.adicionarNota(5.0);
            assertTrue(aluno.possuiNotas());
        }
    }
}
