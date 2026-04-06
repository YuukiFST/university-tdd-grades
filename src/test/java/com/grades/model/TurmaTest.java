package com.grades.model;

import com.grades.exception.AlunoNaoEncontradoException;
import com.grades.exception.TurmaLotadaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Turma")
class TurmaTest {

    private Turma turma;

    @BeforeEach
    void setUp() {
        turma = new Turma("Engenharia de Software", 3);
    }

    @Nested
    @DisplayName("Criacao")
    class Criacao {

        @Test
        @DisplayName("deve criar turma com nome e capacidade validos")
        void deveCriarTurmaComDadosValidos() {
            assertEquals("Engenharia de Software", turma.getNome());
            assertEquals(3, turma.getCapacidadeMaxima());
            assertTrue(turma.getAlunos().isEmpty());
        }

        @Test
        @DisplayName("deve lancar excecao quando capacidade for zero")
        void deveLancarExcecaoQuandoCapacidadeZero() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Turma("Turma A", 0));
        }

        @Test
        @DisplayName("deve lancar excecao quando capacidade for negativa")
        void deveLancarExcecaoQuandoCapacidadeNegativa() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Turma("Turma A", -5));
        }

        @Test
        @DisplayName("deve lancar excecao quando nome for nulo")
        void deveLancarExcecaoQuandoNomeNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> new Turma(null, 30));
        }
    }

    @Nested
    @DisplayName("Adicionar aluno")
    class AdicionarAluno {

        @Test
        @DisplayName("deve adicionar aluno na turma")
        void deveAdicionarAlunoNaTurma() {
            Aluno aluno = new Aluno("Carlos", "2024001");
            turma.adicionarAluno(aluno);

            assertEquals(1, turma.getAlunos().size());
        }

        @Test
        @DisplayName("deve lancar excecao quando turma estiver lotada")
        void deveLancarExcecaoQuandoTurmaLotada() {
            turma.adicionarAluno(new Aluno("A", "001"));
            turma.adicionarAluno(new Aluno("B", "002"));
            turma.adicionarAluno(new Aluno("C", "003"));

            assertThrows(TurmaLotadaException.class,
                    () -> turma.adicionarAluno(new Aluno("D", "004")));
        }

        @Test
        @DisplayName("deve lancar excecao ao adicionar aluno com matricula duplicada")
        void deveLancarExcecaoAoAdicionarMatriculaDuplicada() {
            turma.adicionarAluno(new Aluno("Carlos", "2024001"));

            assertThrows(IllegalArgumentException.class,
                    () -> turma.adicionarAluno(new Aluno("Outro Carlos", "2024001")));
        }

        @Test
        @DisplayName("deve lancar excecao ao adicionar aluno nulo")
        void deveLancarExcecaoAoAdicionarAlunoNulo() {
            assertThrows(IllegalArgumentException.class,
                    () -> turma.adicionarAluno(null));
        }
    }

    @Nested
    @DisplayName("Remover aluno")
    class RemoverAluno {

        @Test
        @DisplayName("deve remover aluno existente")
        void deveRemoverAlunoExistente() {
            turma.adicionarAluno(new Aluno("Carlos", "2024001"));
            turma.removerAluno("2024001");

            assertTrue(turma.getAlunos().isEmpty());
        }

        @Test
        @DisplayName("deve lancar excecao ao remover aluno inexistente")
        void deveLancarExcecaoAoRemoverAlunoInexistente() {
            assertThrows(AlunoNaoEncontradoException.class,
                    () -> turma.removerAluno("999"));
        }
    }

    @Nested
    @DisplayName("Buscar aluno")
    class BuscarAluno {

        @Test
        @DisplayName("deve encontrar aluno por matricula")
        void deveEncontrarAlunoPorMatricula() {
            turma.adicionarAluno(new Aluno("Carlos", "2024001"));

            Optional<Aluno> resultado = turma.buscarAluno("2024001");

            assertTrue(resultado.isPresent());
            assertEquals("Carlos", resultado.get().getNome());
        }

        @Test
        @DisplayName("deve retornar vazio quando aluno nao encontrado")
        void deveRetornarVazioQuandoNaoEncontrado() {
            Optional<Aluno> resultado = turma.buscarAluno("999");

            assertFalse(resultado.isPresent());
        }
    }

    @Test
    @DisplayName("deve retornar lista imutavel de alunos")
    void deveRetornarListaImutavelDeAlunos() {
        assertThrows(UnsupportedOperationException.class,
                () -> turma.getAlunos().add(new Aluno("Hack", "000")));
    }

    @Test
    @DisplayName("deve verificar se turma esta lotada")
    void deveVerificarSeTurmaEstaLotada() {
        assertFalse(turma.estaLotada());

        turma.adicionarAluno(new Aluno("A", "001"));
        turma.adicionarAluno(new Aluno("B", "002"));
        turma.adicionarAluno(new Aluno("C", "003"));

        assertTrue(turma.estaLotada());
    }
}
