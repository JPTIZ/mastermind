import java.util.ArrayList;

public class Slot implements java.io.Serializable {
    private static final long serialVersionUID = -8603895643707279244L;

    protected Chave chave;
    protected volatile ArrayList<Dica> dicas;

    public Slot() {
        this(new Chave());
    }

    public Slot(Chave chave) {
        this.chave = chave;
        this.dicas = new ArrayList<>();
    }

    public Chave getChave() {
        return this.chave;
    }

    public ArrayList<Dica> getDicas() {
        return this.dicas;
    }

    public void addDica(Dica dica) {
        dicas.add(dica);
    }

    public void limpar() {
        dicas.clear();
        for (int i = 0; i < 4; i++) {
            chave.posicionar(Cor.VAZIO, i);
        }
    }
}
