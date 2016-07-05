public enum Cor {
    VAZIO(0),
    AZUL(1),
    AMARELO(2),
    VERMELHO(3),
    VERDE(4),
    BRANCO(5),
    MARROM(6),
    ROXO(7),
    PRETO(8);

    public static final Cor[] cores = {
        VAZIO, AZUL, AMARELO, VERMELHO, VERDE, BRANCO, MARROM, ROXO, PRETO
    };

    private int indice_;
    Cor(int indice_) {
        this.indice_ = indice_;
    }

    public int indice() {
        return this.indice_;
    }
}
