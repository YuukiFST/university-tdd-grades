# Sistema de Notas - TDD

Projeto da disciplina de Teste de Software. Um sistema de notas feito em Java seguindo TDD do começo ao fim.

## Integrantes

- Fausto Yuuki
- Rafael Pereira

## O que faz

Gerencia notas de alunos organizados em turmas. Da pra registrar notas, calcular media simples e ponderada, classificar desempenho (Insuficiente, Regular, Bom, Excelente) e gerar relatorio da turma inteira.

## Tecnologias

- Java 21
- JUnit 5 (Jupiter)
- Maven

## Rodando os testes

```bash
mvn test
```

Esperado: 53 testes, 0 falhas.

## Estrutura do projeto

```
src/
├── main/java/com/grades/
│   ├── model/
│   │   ├── Aluno.java              # Dados do aluno + validacao de notas
│   │   └── Turma.java              # Agrupamento com limite de vagas
│   ├── service/
│   │   ├── Media.java              # Calculos de media e aprovacao
│   │   └── ClassificadorDesempenho.java  # Faixas de desempenho e relatorios
│   └── exception/
│       ├── NotaInvalidaException.java
│       ├── AlunoNaoEncontradoException.java
│       └── TurmaLotadaException.java
└── test/java/com/grades/
    ├── model/
    │   ├── AlunoTest.java          # 13 testes
    │   └── TurmaTest.java          # 12 testes
    └── service/
        ├── MediaTest.java          # 12 testes
        └── ClassificadorDesempenhoTest.java  # 16 testes
docs/
├── requisitos.md
└── analise-beneficios.md
```

## Numeros

| O que              | Quanto |
| ------------------ | ------ |
| Classes de dominio | 4      |
| Exceptions         | 3      |
| Metodos testaveis  | 17     |
| Testes escritos    | 53     |
| Testes passando    | 53     |
| Falhas             | 0      |
| Regras de negocio  | 5      |

## Como aplicamos o TDD

Pra cada funcionalidade, seguimos o ciclo:

1. Red - Escrevemos o teste descrevendo o que esperavamos. Rodamos, ele falha porque o codigo ainda nao existe.
2. Green - Fazemos o minimo pra o teste passar. Sem over-engineering.
3. Refactor - Melhoramos a estrutura sem quebrar nada. Testes continuam verdes.

## Decisoes de design

- Cada classe faz uma coisa so. Aluno guarda dados, Media calcula, Classificador classifica.
- `ClassificadorDesempenho` recebe `Media` no construtor em vez de criar internamente. Isso facilita teste e troca.
- `getNotas()` e `getAlunos()` devolvem listas imutaveis pra ninguem alterar de fora.
- Dados invalidos sao barrados no construtor, nao depois.
