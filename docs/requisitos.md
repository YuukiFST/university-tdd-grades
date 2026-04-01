# Documento de requisitos - Sistema de Notas

## Contexto

Precisavamos de um sistema pra gerenciar notas de alunos dentro de turmas. A ideia era simples: cadastrar alunos, jogar as notas deles la dentro, calcular medias e ver quem passou ou nao. Alem disso, queriamos poder classificar cada aluno em faixas de desempenho (Insuficiente, Regular, Bom, Excelente) e gerar um relatorio consolidado por turma.

## Classes e responsabilidades

### Aluno

Guarda os dados de um estudante: nome, matricula e notas. Nao aceita nome ou matricula vazia. Toda nota adicionada tem que estar entre 0 e 10, senao ele barra.

### Turma

Junta alunos num grupo com limite de vagas. Da pra adicionar, remover e buscar alunos por matricula. Se a turma lota, nao entra mais ninguem. Matricula repetida tambem nao rola.

### Media

Faz as contas. Calcula media simples, media ponderada (com pesos pra cada nota), media geral de uma turma inteira e diz se o aluno passou ou nao com base numa nota de corte.

### ClassificadorDesempenho

Pega a media de um aluno e enquadra numa faixa. Tambem gera relatorio da turma (quantos alunos em cada faixa), acha o melhor aluno e filtra quem foi aprovado.

## Metodos por classe

### Aluno

| Metodo        | Assinatura                             | O que faz                                                            |
| ------------- | -------------------------------------- | -------------------------------------------------------------------- |
| Construtor    | `Aluno(String nome, String matricula)` | Cria o aluno, valida que nome e matricula nao sao nulos nem vazios   |
| adicionarNota | `void adicionarNota(double nota)`      | Registra nota de 0.0 a 10.0. Fora disso, lanca NotaInvalidaException |
| getNotas      | `List<Double> getNotas()`              | Devolve copia das notas (imutavel)                                   |
| possuiNotas   | `boolean possuiNotas()`                | Diz se tem pelo menos uma nota                                       |

### Turma

| Metodo         | Assinatura                                      | O que faz                                                                                            |
| -------------- | ----------------------------------------------- | ---------------------------------------------------------------------------------------------------- |
| Construtor     | `Turma(String nome, int capacidadeMaxima)`      | Cria turma, capacidade tem que ser > 0                                                               |
| adicionarAluno | `void adicionarAluno(Aluno aluno)`              | Coloca aluno na turma. TurmaLotadaException se lotou, IllegalArgumentException se matricula repetida |
| removerAluno   | `void removerAluno(String matricula)`           | Tira o aluno pela matricula. AlunoNaoEncontradoException se nao achar                                |
| buscarAluno    | `Optional<Aluno> buscarAluno(String matricula)` | Procura aluno pela matricula                                                                         |
| estaLotada     | `boolean estaLotada()`                          | Diz se ja bateu o limite de vagas                                                                    |

### Media

| Metodo                 | Assinatura                                                   | O que faz                                                               |
| ---------------------- | ------------------------------------------------------------ | ----------------------------------------------------------------------- |
| calcularMedia          | `double calcularMedia(Aluno aluno)`                          | Media aritmetica. IllegalStateException se nao tem notas                |
| calcularMediaPonderada | `double calcularMediaPonderada(Aluno aluno, double[] pesos)` | Media com pesos. O array de pesos tem que bater com o tamanho das notas |
| calcularMediaTurma     | `double calcularMediaTurma(Turma turma)`                     | Pega a media de cada aluno e faz a media das medias                     |
| alunoAprovado          | `boolean alunoAprovado(Aluno aluno, double notaMinima)`      | true se a media do aluno >= nota de corte                               |

### ClassificadorDesempenho

| Metodo               | Assinatura                                                         | O que faz                                                            |
| -------------------- | ------------------------------------------------------------------ | -------------------------------------------------------------------- |
| classificar          | `String classificar(double media)`                                 | INSUFICIENTE (0-3.9), REGULAR (4-5.9), BOM (6-7.9), EXCELENTE (8-10) |
| gerarRelatorio       | `Map<String, Long> gerarRelatorio(Turma turma)`                    | Conta quantos alunos caem em cada faixa                              |
| obterMelhorAluno     | `Aluno obterMelhorAluno(Turma turma)`                              | Devolve o aluno com a maior media                                    |
| obterAlunosAprovados | `List<Aluno> obterAlunosAprovados(Turma turma, double notaMinima)` | Lista dos alunos com media >= nota de corte                          |

## Regras de negocio

1. Nota tem que ficar entre 0.0 e 10.0. Fora disso, `NotaInvalidaException`.
2. Turma tem limite. Passou da capacidade, `TurmaLotadaException`.
3. Dois alunos com a mesma matricula na mesma turma? Nao pode.
4. Sem notas, sem media. Tentar calcular media de aluno zerado da `IllegalStateException`.
5. Na media ponderada, o numero de pesos tem que ser igual ao numero de notas.

## Cenarios de excecao por classe

| Classe                  | O que da errado                                                   |
| ----------------------- | ----------------------------------------------------------------- |
| Aluno                   | Nota fora de 0-10 dispara `NotaInvalidaException`                 |
| Turma                   | Turma lotada dispara `TurmaLotadaException`                       |
| Media                   | Calcular media sem nota nenhuma dispara `IllegalStateException`   |
| ClassificadorDesempenho | Pedir melhor aluno de turma vazia dispara `IllegalStateException` |
