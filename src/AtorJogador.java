import java.util.ArrayList;

public class AtorJogador {
    protected Chave chave;
    protected Tabuleiro tabuleiro;
    protected Jogador[] jogadores;
    protected AtorNetGames atorNetGames;
    protected int ordemUsuario;
    protected boolean conectado;
    protected boolean emAndamento;
    protected ArrayList<ConexaoListener> listenersConexao;

    public AtorJogador() {
        tabuleiro = new Tabuleiro();
        jogadores = new Jogador[] {
            new Jogador(), new Jogador()
        };
        atorNetGames = new AtorNetGames(this);
        listenersConexao = new ArrayList<>();
    }

    public boolean isEmAndamento() {
        return emAndamento;
    }

    public boolean isConectado() {
        return this.conectado;
    }

    public boolean isVezUsuario() {
        return tabuleiro.getNumeroPalpites() % 2 == ordemUsuario;
    }

    public int getNumeroPalpiteAtual() {
        return tabuleiro.getNumeroPalpites();
    }

    public String getVencedor() {
        for (Jogador jogador: jogadores) {
            if (jogador.isVencedor()) {
                return jogador.getApelido();
            }
        }
        return null;
    }

    public Jogador getJogador(int ordem) {
        return jogadores[ordem];
    }

    public Jogador getJogadorAtual() {
        return getJogador(getNumeroPalpiteAtual() % 2);
    }

    public Tabuleiro getTabuleiro() {
        return this.tabuleiro;
    }

    public void addConexaoListener(ConexaoListener listener) {
        listenersConexao.add(listener);
    }

    public void disparar(ConexaoEvento evento) {
        for (ConexaoListener listener: listenersConexao) {
            switch (evento.tipo) {
                case CONECTAR_EXITO:
                    listener.aoConectarComExito();
                    break;
                case CONECTAR_FALHA:
                    listener.aoFalharConexao(evento.args[0].toString());
                    break;
                case DESCONECTAR_EXITO:
                    listener.aoDesconectarComExito();
                    break;
                case DESCONECTAR_FALHA:
                    listener.aoFalharDesconexao(evento.args[0].toString());
                    break;
                case INICIAR_PARTIDA:
                    listener.aoIniciarPartida();
                    break;
                case FINALIZAR_PARTIDA:
                    listener.aoFinalizarPartida((String)evento.args[0]);
                    break;
                case FINALIZAR_PARTIDA_COM_ERRO:
                    listener.aoFinalizarPartidaComErro(""+evento.args[0]);
                    break;
                case RECEBER_JOGADA:
                    listener.aoReceberJogada((Jogada)evento.args[0]);
            }
        }
    }

    public void conectar(String apelido) {
        Logger.call(AtorJogador.this);
        ConexaoEvento evento;
        try {
            atorNetGames.conectar(apelido);
            evento = new ConexaoEvento(ConexaoEvento.Tipo.CONECTAR_EXITO);
        } catch (Exception e) {
            evento = new ConexaoEvento(ConexaoEvento.Tipo.CONECTAR_FALHA,
                                       new Object[] { e.getMessage() });
        }
        disparar(evento);
    }

    public void desconectar() {
        Logger.call(this);
        ConexaoEvento evento;
        try {
            atorNetGames.desconectar();
            evento = new ConexaoEvento(ConexaoEvento.Tipo.DESCONECTAR_EXITO);
        } catch (Exception e) {
            evento = new ConexaoEvento(ConexaoEvento.Tipo.DESCONECTAR_FALHA,
                                       new Object[] { e.getMessage() });
        }
        disparar(evento);
    }

    public void iniciarPartida() {
        atorNetGames.iniciarPartida();
    }

    public void iniciarPartida(int ordem) {
        Logger.call(this, "iniciarPartida("+ordem+")");

        ordemUsuario = ordem;
        if (ordem == 1) {
            chave = Chave.gerar();
            enviarJogada(new Jogada(chave, null));
        }
        emAndamento = true;
        tabuleiro.limpar();

        jogadores[ordem].setApelido(atorNetGames.getApelido(ordem+1));
        jogadores[1-ordem].setApelido(atorNetGames.getApelido(2-ordem));
        for (Jogador jogador: jogadores) {
            jogador.setVencedor(false);
        }

        disparar(new ConexaoEvento(ConexaoEvento.Tipo.INICIAR_PARTIDA));
    }

    public void finalizarPartida() {
        Logger.call(this);
        chave = null;
        emAndamento = false;
        disparar(new ConexaoEvento(ConexaoEvento.Tipo.FINALIZAR_PARTIDA,
                    new Object[] { getVencedor() }));
    }

    public void finalizarPartidaComErro(String mensagem) {
        Logger.call(this);
        chave = null;
        emAndamento = false;
        disparar(new ConexaoEvento(ConexaoEvento.Tipo.FINALIZAR_PARTIDA_COM_ERRO,
                 new Object[] { mensagem }));
    }

    public void enviarJogada(Jogada jogada) {
        Logger.call(this);
        atorNetGames.enviarJogada(jogada);
        if (jogada.getJogador() != null) {
            tratarJogada(jogada);
        }
    }

    public void receberJogada(Jogada jogada) {
        Logger.call(this);
        if (chave == null) {
            chave = jogada.getPalpite().getChave();
            Logger.log("Chave (RECEBIDA)", chave);
            return;
        }
        tratarJogada(jogada);
        disparar(
                new ConexaoEvento(
                    ConexaoEvento.Tipo.RECEBER_JOGADA, new Object[] {jogada}
                    ));
    }

    public void tratarJogada(Jogada jogada) {
        Logger.call(this);
        Slot palpite = jogada.getPalpite();
        boolean correta = palpite.getChave().igual(chave);
        if (correta) {
            getJogadorAtual().setVencedor(true);
            finalizarPartida();
        } else {
            boolean[] verificados = {false,false,false,false};
            for (int j = 0; j < 4; j++) {
                Cor cor = palpite.getChave().getCor(j);
                for (int i = 0; i < 4; i++) {
                    if (verificados[i]) continue;
                    Cor corChave = chave.getCor(i);
                    if (corChave == cor) {
                        if (i != j) {
                            palpite.addDica(Dica.POSICAO_INCORRETA);
                        } else {
                            palpite.addDica(Dica.POSICAO_CORRETA);
                        }
                        verificados[i] = true;
                    }
                }
            }
        }
        getTabuleiro().setSlot(getNumeroPalpiteAtual()-1, palpite);
        proximoPalpite();
        getTabuleiro().getPalpiteAtual().esvaziar();
    }

    public void proximoPalpite() {
        if (this.getNumeroPalpiteAtual() >= 12) {
            finalizarPartida();
            return;
        }
        tabuleiro.incrementarPalpites();
    }
}
