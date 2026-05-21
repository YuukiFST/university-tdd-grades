# Analise de beneficios do TDD

## Como o design foi aparecendo

Nao tinha planejamento de arquitetura grandioso. A gente sentou, escreveu o primeiro teste do `Aluno` e percebeu na hora: "ah, precisa validar o nome no construtor". Depois: "a lista de notas tem que ser imutavel, senao qualquer um altera de fora". Essas decisoes foram saindo dos testes, nao de um diagrama UML.

Com o `ClassificadorDesempenho` aconteceu uma coisa parecida. Na hora de testar, ficou obvio que ele precisava de um `Media` pra funcionar. Em vez de criar um `new Media()` la dentro, passamos pelo construtor. Injecao de dependencia na pratica, sem pensar muito no nome bonito do pattern.

As faixas de classificacao (INSUFICIENTE, REGULAR, BOM, EXCELENTE) ficaram definidas nos testes antes de existirem no codigo. Cada teste meio que virou um contrato: "se a media for 3.5, quero INSUFICIENTE de volta". E pronto, a especificacao tava la.

## Bugs que os testes pegaram antes

Tres coisas que teriam dado dor de cabeca sem os testes:

- No `calcularMediaPonderada`, esquecemos de checar se a soma dos pesos dava zero. Divisao por zero em producao? Ia ser bonito. O teste de caso limite forçou a validacao.
- O `removerAluno` tava comparando objeto por referencia, nao por matricula. Funcionava num cenario simples, mas quebraria em qualquer caso real. O teste com busca por matricula mostrou o furo.
- O `gerarRelatorio` so retornava as faixas que tinham alunos. Se ninguem tirava INSUFICIENTE, a chave nem aparecia no mapa. Descobrimos isso testando com turma mista.

Nenhum desses bugs era obvio olhando o codigo. Os testes acharam.

## Com TDD versus sem TDD

| Aspecto                       | Sem TDD                                       | Com TDD                                  |
| ----------------------------- | --------------------------------------------- | ---------------------------------------- |
| Quando descobre bugs          | Na hora de rodar, ou quando o professor testa | Enquanto escreve, no ciclo Red-Green     |
| Medo de mexer no codigo       | Bastante                                      | Quase nenhum, os testes seguram          |
| Como define as interfaces     | Chuta primeiro, ajusta depois                 | Sai dos testes, ja nasce testavel        |
| Documentacao de comportamento | Comentario que ninguem le                     | O teste diz o que o metodo faz           |
| Velocidade                    | Parece rapido no começo, mas paga depois      | Mais devagar no inicio, menos retrabalho |
| Cobertura                     | Onde deu vontade de testar                    | Tudo tem teste porque veio antes         |

## O que aprendemos

No começo, escrever teste antes pareceu perda de tempo. A vontade de "so codar logo" era real. Mas la pelo terceiro ciclo Red-Green-Refactor a coisa muda. Voce começa a confiar que se o teste passou, o codigo ta certo. E quando precisa mudar algo, roda os testes e sabe se quebrou ou nao.

Trocar a estrutura interna da `Turma` foi um bom exemplo. Mudamos a forma como os alunos eram armazenados, rodamos `mvn test`, tudo verde. Sem os testes, teria sido aquele processo de testar manualmente cada cenario e rezar pra nao esquecer nenhum.

Nomes de teste fazem diferença. `deveCalcularMediaPonderada` conta o que ta acontecendo. `testMedia2` nao diz nada pra ninguem. Quando voce trata os testes como documentacao, eles ficam uteis de verdade.

O esquema de pair programming funcionou bem. Um escrevia o teste, o outro fazia passar. Isso manteve o ciclo TDD rodando certinho e evitou que alguem pulasse a etapa do Red so pra ir mais rapido.

### Exemplo pratico do comparativo

Na implementacao do metodo `calcularMediaPonderada`, o teste forcou a criacao da validacao de pesos antes mesmo do calculo existir. Sem TDD, essa validacao seria adicionada apenas se alguem lembrasse ou quando um bug aparecesse em producao. O teste `deveLancarExcecaoQuandoSomaPesosZero` garantiu que o sistema trata divisao por zero desde o inicio.

Outro exemplo: o `ClassificadorDesempenho` nasceu com injecao de dependencia do `Media` porque o teste precisava passar um `Media` mockado. Sem TDD, provavelmente teriamos um `new Media()` dentro do construtor, acoplando as classes e dificultando testes futuros.
