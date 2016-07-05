public class Jogada implements br.ufsc.inf.leobr.cliente.Jogada {
    private static final long serialVersionUID = -5005537201985803047L;

    protected Slot palpite;
    protected Jogador jogador;

    public Jogada() {
    }

    public Jogada(Chave chave, Jogador jogador) {
        this.palpite = new Slot(chave);
        this.jogador = jogador;
    }

    public Slot getPalpite() {
        return palpite;
    }

    public Jogador getJogador() {
        return jogador;
    }
}
