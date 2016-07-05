import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class PainelJogo extends JPanel implements MouseListener {
    private static final long serialVersionUID = -1648660949899700446L;

    private static final int TAMANHO_COR = 32;
    private static final int ESPACAMENTO_COR = 8;
    private static final int TAMANHO_BLOCO = TAMANHO_COR + ESPACAMENTO_COR;

    //-------------------------------------------------------------------------

    private InterfaceMastermind jogo;

    private Image fundo;
    private Image[] imagemCores;

    private Rectangle rectPaletaEsquerda;
    private Rectangle rectPaletaDireita;
    private Rectangle rectPalpite;
    private Rectangle rectBotaoEnviar;

    //-------------------------------------------------------------------------

    /**
     * Altera tamanho, cria imagens de fundo e das cores
     * @param jogo Referência do jogo atual
     **/
    public PainelJogo(InterfaceMastermind jogo) {
        super();

        this.jogo = jogo;
        setSize(InterfaceMastermind.RESOLUCAO);

        //---------------------------------------------------------------------
        try {
            fundo = ImageIO.read(new File("img/background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        imagemCores = new Image[Cor.cores.length];
        for (int i = 0; i < imagemCores.length; i++) {
            try {
                imagemCores[i] = ImageIO.read(new File("img/cor"+i+".png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //---------------------------------------------------------------------
        int tamanho = TAMANHO_BLOCO + ESPACAMENTO_COR;
        rectPaletaEsquerda = new Rectangle(
                16,
                getHeight() - (16 + 4*TAMANHO_BLOCO),
                tamanho,
                4*TAMANHO_BLOCO + ESPACAMENTO_COR);
        rectPaletaDireita = new Rectangle(rectPaletaEsquerda);
        rectPaletaDireita.translate(
                getWidth() - (32 + tamanho), 0);
        rectPalpite = new Rectangle(
                getWidth()/2 - 2*TAMANHO_BLOCO,
                getHeight() - (tamanho + ESPACAMENTO_COR),
                TAMANHO_BLOCO*4 + ESPACAMENTO_COR,
                tamanho);

        rectBotaoEnviar = new Rectangle(rectPaletaDireita);
        rectBotaoEnviar.translate(0, -(tamanho + ESPACAMENTO_COR));
        rectBotaoEnviar.setSize(tamanho, tamanho);

        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(fundo, 0, 0, this);

        Point location = rectPaletaEsquerda.getLocation();
        desenharPaleta(g, (int)location.getX(), (int)location.getY(), 1);
        location = rectPaletaDireita.getLocation();
        desenharPaleta(g, (int)location.getX(), (int)location.getY(), 5);

        Tabuleiro tabuleiro = this.jogo.getAtorJogador().getTabuleiro();

        int x = (int)rectPalpite.getX();
        int y = 16;
        desenharTabuleiro(g, tabuleiro, x, y);

        y = (int)rectPalpite.getY();
        g.drawRect(x, y, (int)rectPalpite.getWidth(), (int)rectPalpite.getHeight());
        desenharChave(
                g, tabuleiro.getPalpiteAtual(),
                x + ESPACAMENTO_COR,
                y + ESPACAMENTO_COR);

        desenharInformacoes(g);
        desenharBotoes(g);
    }

    //-------------------------------------------------------------------------
    // Desenho das bolinhas
    //-------------------------------------------------------------------------
    private void desenharPaleta(Graphics g, int x, int y, int inicio) {
        g.setColor(Color.BLACK);
        g.drawRect(x, y, TAMANHO_BLOCO + ESPACAMENTO_COR, TAMANHO_BLOCO*4 + ESPACAMENTO_COR);
        for (int i = inicio; i < inicio+4; i++) {
            g.drawImage(
                    imagemCores[i],
                    x + ESPACAMENTO_COR,
                    y + (i-inicio)*TAMANHO_BLOCO + ESPACAMENTO_COR, this);
        }
    }

    private void desenharChave(Graphics g, Chave chave, int x, int y) {
        final int espaco = TAMANHO_COR + ESPACAMENTO_COR;
        int i = 0;
        for (Cor cor : chave.getCores()) {
            g.drawImage(imagemCores[cor.indice()], x + i*espaco, y, null);
            i++;
        }
    }

    private void desenharTabuleiro(Graphics g, Tabuleiro tabuleiro, int x, int y) {
        g.setColor(Color.BLACK);

        int j = 0;
        for (Slot slot: tabuleiro.getSlots()) {
            desenharChave(
                    g, slot.getChave(),
                    x + ESPACAMENTO_COR,
                    y + (11-j)*TAMANHO_BLOCO + ESPACAMENTO_COR);
            desenharDicas(
                    g, slot.getDicas(),
                    x - (TAMANHO_BLOCO + ESPACAMENTO_COR),
                    y + (11-j)*TAMANHO_BLOCO + ESPACAMENTO_COR/2);
            j++;
        }
        g.drawRect(x, y, TAMANHO_BLOCO*4 + ESPACAMENTO_COR, TAMANHO_BLOCO*j + ESPACAMENTO_COR);
    }

    private void desenharDicas(Graphics g, ArrayList<Dica> dicas, int x, int y) {
        g.drawRect(x, y,
                36,
                TAMANHO_BLOCO - ESPACAMENTO_COR/2);
        x += 2;
        y += 2;
        int i = 0;
        for (Dica dica : dicas) {
            g.drawImage(dica.icone(), x + 17*(i%2), y + 17*(i/2), this);
            i++;
        }
    }

    //-------------------------------------------------------------------------
    // Desenhos extras
    //-------------------------------------------------------------------------

    /**
     * Desenha as informaçẽos da partida (palpite atual, vencedor, etc...)
     **/
    private void desenharInformacoes(Graphics g) {
        g.setColor(new Color(64, 28, 0));
        int x = 16 + (int)(rectPalpite.getX() + rectPalpite.getWidth());
        int y = 16;
        g.drawRect(
                x, y,
                81, 128);
        g.setColor(new Color(208, 208, 208));
        x += 4;
        y += 16;
        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        AtorJogador ator = jogo.getAtorJogador();
        g.drawString("Nº Palpite:", x, y);
        g.drawString("" + (ator.getNumeroPalpiteAtual()), x, y + 12*1);
        g.drawString("Vez de:", x, y + 12*2);
        if (ator.isEmAndamento()) {
            String apelido = ator.getJogadorAtual().getApelido();
            g.drawString("" + apelido.substring(0, Math.min(apelido.length(), 11)), x, y + 12*3);
        } else {
            g.drawString("--Ninguém--", x, y + 12*3);
        }
    }

    /**
     * Desenha botões extras (enviar palpite, etc..)
     **/
    private void desenharBotoes(Graphics g) {
        Chave chave = jogo.getAtorJogador().getTabuleiro().getPalpiteAtual();
        Color corClara;
        Color corEscura;
        if (chave.isCompleta()) {
            corClara = new Color(192, 128, 64);
            corEscura = new Color(64, 0, 0);
        } else {
            corClara = Color.LIGHT_GRAY;
            corEscura = Color.GRAY;
        }

        Rectangle rect = rectBotaoEnviar;
        g.setColor(corClara);
        g.fillRect(
                (int)rect.getX(),
                (int)rect.getY(),
                (int)rect.getWidth(), (int)rect.getWidth());
        g.setColor(corEscura);
        g.drawRect(
                (int)rect.getX(),
                (int)rect.getY(),
                (int)rect.getWidth(), (int)rect.getWidth());

        g.drawString("Enviar", (int)rect.getX() + 4, (int)rect.getY() + 29);
    }

    //-------------------------------------------------------------------------
    // Manipulação de eventos
    //-------------------------------------------------------------------------
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        Point ponto = e.getPoint();
        if (rectPaletaEsquerda.contains(e.getPoint())) {
            int indice = verificarClique(rectPaletaEsquerda, ponto, 0, TAMANHO_BLOCO);
            if (indice >= 0) {
                jogo.clickBancoDeCores(indice+1);
            }
        } else if (rectPaletaDireita.contains(e.getPoint())) {
            int indice = verificarClique(rectPaletaDireita, ponto, 0, TAMANHO_BLOCO);
            if (indice >= 0) {
                jogo.clickBancoDeCores(indice+5);
            }
        } else if (rectPalpite.contains(e.getPoint())) {
            int indice = verificarClique(rectPalpite, ponto, TAMANHO_BLOCO, 0);
            if (indice >= 0) {
                jogo.clickPalpite(indice);
            }
        } else if (rectBotaoEnviar.contains(e.getPoint())) {
            jogo.confirmarJogada();
        }
    }

    private int verificarClique(Rectangle base, Point ponto, int dx, int dy) {
        Rectangle rectTeste = new Rectangle(base.getLocation(),
                new Dimension(TAMANHO_COR, TAMANHO_COR));
        rectTeste.translate(ESPACAMENTO_COR, ESPACAMENTO_COR);
        for (int i = 0; i < 4; i++) {
            if (rectTeste.contains(ponto)) {
                return i;
            }
            rectTeste.translate(dx, dy);
        }
        return -1;
    }
}
