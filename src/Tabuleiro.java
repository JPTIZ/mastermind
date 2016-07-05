import java.util.ArrayList;

public class Tabuleiro {
    protected ArrayList<Slot> slots;
    protected Chave palpiteAtual;
    protected int numeroPalpites;

    /**
     * Constrói todos os objetos com o valor padrão e seta o número de
     * palpites para 1.
     */
    public Tabuleiro() {
        slots = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            slots.add(new Slot());
        }
        palpiteAtual = new Chave();
        numeroPalpites = 1;
    }

    public ArrayList<Slot> getSlots() {
        return slots;
    }

    /**
     * Retorna um slot do tabuleiro.
     * @param indice Índice do slot a ser recebido
     * @return Slot no índice especificado
     */
    public Slot getSlot(int indice) {
        return slots.get(indice);
    }

    /**
     * Copia as cores do novo slot para o slot antigo. Não há substituição
     * da referência.
     * @param indice Índice do slot a ser substituido
     * @param slot Novo slot
     */
    public void setSlot(int indice, Slot slot) {
        Slot slotAntigo = slots.get(indice);
        Cor[] cores = slot.getChave().getCores();
        for (int i = 0; i < cores.length; i++) {
            slotAntigo.getChave().posicionar(cores[i], i);
        }
        for (Dica dica: slot.getDicas()) {
            slotAntigo.getDicas().add(dica);
        }
    }

    /**
     * @return Número de palpites efetuados até o momento.
     */
    public int getNumeroPalpites() {
        return numeroPalpites;
    }

    /**
     * Incrementa em 1 o número de palpites efetuados.
     */
    public void incrementarPalpites() {
        numeroPalpites++;
    }

    /**
     * @return Chave do palpite a ser construído pelo jogador.
     */
    public Chave getPalpiteAtual() {
        return palpiteAtual;
    }

    /**
     * Limpa todos os slots do tabuleiro (cor = VAZIO)
     */
    public void limpar() {
        for (Slot slot : slots) {
            slot.limpar();
        }
        numeroPalpites = 1;
    }
}
