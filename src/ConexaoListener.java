public interface ConexaoListener {
    public void aoConectarComExito();
    public void aoFalharConexao(String motivo);
    public void aoDesconectarComExito();
    public void aoFalharDesconexao(String motivo);
    public void aoIniciarPartida();
    public void aoFinalizarPartida(String vencedor);
    public void aoFinalizarPartidaComErro(String motivo);
    public void aoReceberJogada(Jogada jogada);
}
