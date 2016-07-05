Mastermind - Notas de Manutenção
================================

Instruções
----------

### Instalação

O jogo Mastermind é Stand-Alone e não necessita de instalação (é necessário
porém ter o JRE instalado em sua máquina).

### Execução

Para executar o jogo há duas formas:

#### 1ª forma: Explorador de arquivos

1. Abra o explorador de arquivos do seu sistema (Explorer no Windows);
2. Vá na pasta onde está localizado o arquivo `mastermind.jar`;
3. Dê um duplo clique no arquivo `mastermind.jar`.

#### 2º forma: Linha de comando

1. Abra um terminal ou prompt de comando (`cmd` no Windows);
2. Digite: `cd pasta/em/que/esta/o/mastermind/`;
3. Aperte Enter;
4. (Linux) Digite: `./__run.sh`;
4. (Windows) Digite: `__run.bat`;
5. Aperte Enter novamente.

### Solução de problemas

#### java: command not found (java: comando não encontrado)

Instale o JavaRE em sua máquina (disponível no site da oracle).

#### Falhas na conexão

Verifique se o servidor for iniciado.
Em caso negativo e você for o hoster do jogo:

1. Abra um terminal ou prompt de comando (`cmd` no Windows);
2. Execute: `cd pasta/em/que/esta/o/arquivo/do/servidor`;
3. (Linux) Execute: `./__server.sh`
3. (Windows) Execute: `__server.bat`
4. Verifique se o servidor foi iniciado corretamente e tente conectar pelo jogo
   novamente;

Caso você não seja o hoster do jogo:

1. Na janela principal do Mastermind, vá em: Jogo > Opções;
2. Insira o IP correto do servidor no campo "IP Servidor";
3. Clique em "Salvar";
4. Clique em "Fechar";
5. Tente conectar novamente.

Caso não haja sucesso nas alternativas acima, entre em contato com os
desenvolvedores.

Instruções para desenvolvedores
-------------------------------

### Diretórios

O projeto está organizado da seguinte forma:

| Diretório  | Descrição                                                      |
|------------|----------------------------------------------------------------|
| dist       | Contém os arquivos para distribuição do software a clientes    |
| mastermind | Pasta de destino dos arquivos .class                           |
| src        | Código fonte                                                   |
| vpp        | Contém arquivos de cada versão da modelagem em Visual Paradigm |

| Arquivo     | Descrição                                  |
|-------------|--------------------------------------------|
| __run.sh    | Roda o jogo (a partir dos arquivos .class) |
| __server.sh | Inicia o servidor                          |
| __build.sh  | Compila o código                           |

Log
---

### v4.1

- Da modelagem:
    - Corrida a falta de referências dos diagramas de sequência no diagrama de
      visão geral de interação (ocorrida devida à remodelagem deles);

### v4.0

- Da modelagem:
    - Corrigido desequilíbrio de responsabilidades entre o AtorJogador e as
      demais classes:
        - Tabuleiro é responsável por operar nos slots, as outras classes o
          acessam pelo `getTabuleiro()` do AtorJogador;
        - Operações nos jogadores é feita pela classe `Jogador`, sendo cada
          jogador acessível por:
            - `getJogador(ordem)` para acessar o jogador com ordem `ordem`;
            - `getJogadorAtual()` para acessar o jogador que tem a vez (com
              base no número de palpites efetuados, informação agora registrada
              no tabuleiro);
        - Removidos os enumeradores `ResultadoJogada`, `ResultadoConexao` e
          `ResultadoDesconexao` - agora suas funcionalidades são implementadas
          por meio de eventos disparados (via `ConexaoListener`);
    - Adição da interface `ConexaoListener`, que trata de servir como
      observador para os eventos relacionados aos callbacks de comunicação do
      jogo com a rede (des/conexão com êxito/falha, início de partida,
      finalização da partida com/sem erro e recebimento de jogada);
    - Adição da classe `ConexaoEvento`, que serve como descritor de cada evento
      disparado pelo AtorJogador para os ouvintes registrados como
      `ConexaoListener`;
    - Incrementados métodos para cada classe, como getters para os slots do
      tabuleiro;
    - Refatoração geral dos métodos das classes, a fim de manter consistência
      dos nomes e suas respectivas funções;
    - Modelado um diagrama de classes contendo apenas o interfaceamento com a
      rede (chamado de NetGames);
    - Refinamento do diagrama de classes "InterfaceGrafica":
        - Adicionada classe `PanelJogo`, responsável pela renderização gráfica
          do jogo;
        - Adicionada classe `JanelaConfiguracao`, responsável por permitir ao
          usuário que configure opções relativas ao Mastermind (por ora apenas
          o IP do servidor é configurável);
        - Adicionadas as referências (agregações, realizações e generalizações)
          às classes das bibliotecas `java.io`, `javax.swing` e `java.awt`
          utilizadas no projeto;
        - `InterfaceMastermind` agora realiza a interface `ConexaoListener`;
    - Remodelagem dos diagramas de sequência, agora funcionais e com as devidas
      chamadas à camada de rede (por conta de maior transparência da modelagem
      do NetGames);
    - Adicionada classe `Logger`, que não deve ser implementada em
      código-release, mas sim apenas utilizada para depuração, visto que serve
      para melhor visualização de chamadas de métodos e argumentos enviados
      (além da contagem de chamadas à própria classe);
    - Modelados os diagramas de algoritmos de métodos para os seguintes casos
      de uso:
        - Confirmar Jogada;
        - Posicionar Cor;
        - Receber Jogada;
        - Sair da Partida;
        - Selecionar Cor.
    - Modelados os diagramas de atividade que faltavam (posicionar cor e
      retirar cor);
    - Remodelados os diagramas de máquina de estados para `Jogador` e
      `AtorJogador`, adicionando-se estados importantes, retirando estados
      redundantes, corrigindo transições e readaptando por conta da remoção do
      enumerador `EstadoJogo`;
    - Adicionada a referência ao caso de uso "Selecionar Cor" no diagrama de
      visão geral de interação.

### v3.0

- Da modelagem:
    - Incrementado o diagrama de classes, adicionando a classe
      `InterfaceMastermind`, responsável por representar a janela do jogo, e
      enumeradores `ResultadoJogada`, `ResultadoConexao` e
      `ResultadoDesconexao`;
    - Adicionado caso de uso "receber solicitação de início";
    - Correções no diagrama de visão geral de interação:
        - Adicionadas correções a respeito do tratamento de erros na conexão;
        - Reorganizados os nodos relativos à confirmação da jogada;
        - Posicionar cor agora é possível apenas quando no nodo de decisão logo
          após "Receber solicitação de início";
        - Adicionadas guardas das transições relacionadas a palpites.
    - Modelado um diagrama de sequência para cada diagrama de atividades, porém
      tratando o `AtorNetGames` como um ator cujas funcionalidades são
      levemente desconhecidas (sem muita informação relevante quanto ao
      processo de envio/recebimento das jogadas, conexão, ordenação dos
      jogadores...)
    - Modelado diagrama de sequência extra ("Tratar Jogada"), descrevendo como
      são tratadas as jogadas enquanto palpite correto/incorreto, adição das
      dicas e resposta ao usuário.

- Do levantamento de requisitos:
    - Correção na descrição da chave secreta, informando que a chave é gerada
      aleatoriamente;
    - Correção na descrição das jogadas, que serão alternadas entre os
      jogadores (apenas 1 por vez);

### v2.0

- Modelado um diagrama de atividades para os seguintes casos de uso:
    - Conectar;
    - Desconectar;
    - Confirmar jogada;
    - Receber solicitação de início;
    - Iniciar partida;
    - Sair da partida;
    - Selecionar posição;
    - Receber jogada;
    - Selecionar cor.
   Os diagramas descrevem um fluxo (alto-nível) de ações efetuadas em cada caso
   de uso;
- Há um diagrama de visão geral de interação descrevendo o fluxograma do jogo e
  como as jogadas são definidjas;
- Corrigido o levantamento de requisitos da v1.0:
    - Adicionada menção ao uso da rede;
    - Inseridas premissas de desenvolvimento não descritas anteriormente, porém
      referenciadas nos RNFs;
    - Adicionado esboço da interface gráfica;

### v1.0

- Primeira modelagem efetuada;
- Definidas operações realizáveis pela interface e o controlador de rede (casos
  de uso)
- A modelagem atual prevê as ações por parte da rede apenas nos casos de uso,
  não sendo descrito um diagrama de classes que componha tal parte do domínio
  do problema;
- Levantamento de requisitos do projeto, salvo em PDF, descrevendo
  arquitetura e linguagem utilizadas, regras do jogo, RFs e RNFs.

As classes foram organizadas da seguinte forma:

    - As cores possíveis para preencher os palpites é definida em um enumerador
      `Cor`;
    - Cada palpite é chamado de `Chave` e consiste em um conjunto de `Cor` de
      tamanho 4;
    - As dicas possíveis são definidas em um enumerador `Dica`;
    - Cada slot do tabuleiro consiste em uma `Chave` e um conjunto de 0 a 4
      `Dica`s, o que é definido na classe `SlotTabuleiro`;
    - O `Tabuleiro` consiste em um conjunto de 12 elementos do tipo
      `SlotTabuleiro`;
    - Cada jogador é representado pela classe `Jogador`, que possui seu
      respectivo apelido e um indicador de turno;
    - Cada jogada enviada/recebida por um jogador é tratada como sendo da
      classe `Jogada`, que possui o jogador que a efetuou e qual o palpite
      (`Chave`) feito;
    - Tudo é englobado em uma classe `Jogo`, que guarda consigo o `Tabuleiro`,
      os dois `Jogador`, uma `Chave` e um enumerador com os possíveis estados
      do jogo (conectando, desconectado, em andamento e finalizado).
