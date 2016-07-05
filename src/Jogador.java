public class Jogador implements java.io.Serializable {
    private static final long serialVersionUID = -5005537201985803047L;

    protected String apelido;
    protected boolean vencedor;

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getApelido() {
        return apelido;
    }

    public void setVencedor(boolean flag) {
        vencedor = flag;
    }

    public boolean isVencedor() {
        return vencedor;
    }
}
