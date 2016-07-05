public class ConexaoEvento {
    public Tipo tipo;
    public Object[] args;

    public ConexaoEvento(Tipo tipo, Object[] args) {
        this.tipo = tipo;
        this.args = args;
    }

    public ConexaoEvento(Tipo tipo) {
        this(tipo, null);
    }

    public enum Tipo {
        CONECTAR_EXITO,
        CONECTAR_FALHA,
        DESCONECTAR_EXITO,
        DESCONECTAR_FALHA,
        INICIAR_PARTIDA,
        FINALIZAR_PARTIDA,
        FINALIZAR_PARTIDA_COM_ERRO,
        RECEBER_JOGADA,
    }
}
