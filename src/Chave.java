public class Chave implements java.io.Serializable {
    private static final long serialVersionUID = 6787258572557873318L;

    protected Cor[] cores;

    public Chave() {
        this.cores = new Cor[4];
        for (int i = 0; i < cores.length; i++) {
            cores[i] = Cor.VAZIO;
        }
    }

    public boolean igual(Chave chave) {
        int i = 0;
        for (Cor cor : cores) {
            if (cor != chave.getCor(i)) {
                return false;
            }
            i++;
        }
        return true;
    }

    public boolean isOcupado(int indice) {
        return cores[indice] != Cor.VAZIO;
    }

    public boolean isCompleta() {
        for (Cor cor: cores) {
            if (cor == Cor.VAZIO) return false;
        }
        return true;
    }

    public void posicionar(Cor cor, int indice) {
        cores[indice] = cor;
    }

    public void esvaziar(int indice) {
        cores[indice] = Cor.VAZIO;
    }

    public void esvaziar() {
        for (int i = 0; i < 4; i++) {
            esvaziar(i);
        }
    }

    public Cor getCor(int indice) {
        return cores[indice];
    }

    public Cor[] getCores() {
        return this.cores;
    }

    public static Chave gerar() {
        // TODO
        Chave chave = new Chave();
        for (int i = 0; i < 4; i++) {
            int indice = (int)(Math.random() * (Cor.cores.length-1)) + 1;
            chave.posicionar(Cor.cores[indice], i);
        }
        Logger.log("Chave secreta (GERADA)", chave);
        return chave;
    }

    @Override
    public String toString() {
        String str = "[";
        for (int i = 0; i < 4; i++) {
            str += cores[i]+(i==3?"":", ");
        }
        str += "]";
        return str;
    }
}
