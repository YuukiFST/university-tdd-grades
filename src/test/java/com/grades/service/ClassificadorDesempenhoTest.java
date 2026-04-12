package com.grades.service;

import com.grades.exception.NotaInvalidaException;
import com.grades.model.Aluno;
import com.grades.model.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ClassificadorDesempenho")
class ClassificadorDesempenhoTest {

    private ClassificadorDesempenho classificador;

    @BeforeEach
    void setUp() {
        classificador = new ClassificadorDesempenho(new Media());
    }

    @Nested
    @DisplayName("Classificacao individual")
    class ClassificacaoIndividual {

        @Test
        @DisplayName("deve classificar como INSUFICIENTE para media abaixo de 4")
        void deveClassificarInsuficiente() {
            assertEquals("INSUFICIENTE", classificador.classificar(2.5));
        }

        @Test
        @DisplayName("deve classificar como REGULAR para media entre 4 e 5.9")
        void deveClassificarRegular() {
            assertEquals("REGULAR", classificador.classificar(5.0));
        }

        @Test
        @DisplayName("deve classificar como BOM para media entre 6 e 7.9")
        void deveClassificarBom() {
            assertEquals("BOM", classificador.classificar(7.0));
        }

        @Test
        @DisplayName("deve classificar como EXCELENTE para media entre 8 e 10")
        void deveClassificarExcelente() {
            assertEquals("EXCELENTE", classificador.classificar(9.5));
        }

        @Test
        @DisplayName("deve classificar nota zero como INSUFICIENTE")
        void deveClassificarZeroComoInsuficiente() {
            assertEquals("INSUFICIENTE", classificador.classificar(0.0));
        }

        @Test
        @DisplayName("deve classificar nota dez como EXCELENTE")
        void deveClassificarDezComoExcelente() {
            assertEquals("EXCELENTE", classificador.classificar(10.0));
        }

        @Test
        @DisplayName("deve lancar excecao para media negativa")
        void deveLancarExcecaoParaMediaNegativa() {
            assertThrows(NotaInvalidaException.class,
                    () -> classificador.classificar(-1.0));
        }

        @Test
        @DisplayName("deve lancar excecao para media acima de 10")
        void deveLancarExcecaoParaMediaAcimaDeDez() {
            assertThrows(NotaInvalidaException.class,
                    () -> classificador.classificar(11.0));
        }
    }

    @Nested
    @DisplayName("Relatorio da turma")
    class RelatorioTurma {

        @Test
        @DisplayName("deve gerar relatorio com contagem por classificacao")
        void deveGerarRelatorioComContagem() {
            Turma turma = criarTurmaComAlunos();

            Map<String, Long> relatorio = classificador.gerarRelatorio(turma);

            assertEquals(1L, relatorio.get("INSUFICIENTE"));
            assertEquals(1L, relatorio.get("REGULAR"));
            assertEquals(1L, relatorio.get("BOM"));
            assertEquals(1L, relatorio.get("EXCELENTE"));
        }

        @Test
        @DisplayName("deve lancar excecao para turma sem alunos")
        void deveLancarExcecaoParaTurmaSemAlunos() {
            Turma turma = new Turma("Vazia", 30);

            assertThrows(IllegalStateException.class,
                    () -> classificador.gerarRelatorio(turma));
        }
    }

    @Nested
    @DisplayName("Melhor aluno")
    class MelhorAluno {

        @Test
        @DisplayName("deve obter aluno com maior media da turma")
        void deveObterMelhorAluno() {
            Turma turma = criarTurmaComAlunos();

            Aluno melhor = classificador.obterMelhorAluno(turma);

            assertEquals("Diana", melhor.getNome());
        }

        @Test
        @DisplayName("deve lancar excecao para turma vazia")
        void deveLancarExcecaoParaTurmaVazia() {
            Turma turma = new Turma("Vazia", 30);

            assertThrows(IllegalStateException.class,
                    () -> classificador.obterMelhorAluno(turma));
        }
    }

    @Nested
    @DisplayName("Alunos aprovados")
    class AlunosAprovados {

        @Test
        @DisplayName("deve retornar apenas alunos com media acima do minimo")
        void deveRetornarAlunosAprovados() {
            Turma turma = criarTurmaComAlunos();

            List<Aluno> aprovados = classificador.obterAlunosAprovados(turma, 6.0);

            assertEquals(2, aprovados.size());
        }

        @Test
        @DisplayName("deve retornar lista vazia quando nenhum aluno aprovado")
        void deveRetornarListaVaziaQuandoNenhumAprovado() {
            Turma turma = criarTurmaComAlunos();

            List<Aluno> aprovados = classificador.obterAlunosAprovados(turma, 10.0);

            assertTrue(aprovados.isEmpty());
        }
    }

    /**
     * Cria turma com 4 alunos, um em cada faixa de classificacao:
     * Ana (3.0) = INSUFICIENTE
     * Bruno (5.0) = REGULAR
     * Carlos (7.0) = BOM
     * Diana (9.0) = EXCELENTE
     */
    private Turma criarTurmaComAlunos() {
        Turma turma = new Turma("Turma Teste", 30);

        Aluno ana = new Aluno("Ana", "001");
        ana.adicionarNota(3.0);

        Aluno bruno = new Aluno("Bruno", "002");
        bruno.adicionarNota(5.0);

        Aluno carlos = new Aluno("Carlos", "003");
        carlos.adicionarNota(7.0);

        Aluno diana = new Aluno("Diana", "004");
        diana.adicionarNota(9.0);

        turma.adicionarAluno(ana);
        turma.adicionarAluno(bruno);
        turma.adicionarAluno(carlos);
        turma.adicionarAluno(diana);

        return turma;
    }
}
