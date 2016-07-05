import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import java.awt.Cursor;
import java.awt.Dimension;

public class InterfaceMastermind extends JFrame implements ConexaoListener {
    public static final long serialVersionUID = 1231619309400646856L;

    public static final Dimension RESOLUCAO = new Dimension(400, 600);

    protected Cor corSelecionada;
    protected AtorJogador atorJogador;
    private JMenuItem miConectar;
    private JMenuItem miDesconectar;
    private JMenuItem miIniciarPartida;

    /**
     * Instancia AtorJogador, seta título, layout e operação padrão de
     * fechamento da janela (terminar programa), tenta alterar estilo para
     * Nimbus, seta tamanho do painel de jogo e cria componentes.
     */
    public InterfaceMastermind() {
        super();

        this.atorJogador = new AtorJogador();
        this.atorJogador.addConexaoListener(this);

        setTitle("Mastermind");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Seta estilo Nimbus (se existir)...
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                 if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.err.println("Não foi possível alterar estilo para Nimbus.");
        }

        criarComponentes();

        getContentPane().setPreferredSize(RESOLUCAO);

        pack();
        setMinimumSize(getSize());
        setMaximumSize(getSize());
    }

    /**
     * Cria menus e painel de jogo
     **/
    private void criarComponentes() {
        miConectar = new JMenuItem("Conectar");
        miConectar.addActionListener((e) -> {
            conectar();
        });
        miDesconectar = new JMenuItem("Desconectar");
        miDesconectar.addActionListener((e) -> {
            desconectar();
        });
        miIniciarPartida = new JMenuItem("IniciarPartida");
        miIniciarPartida.addActionListener((e) -> {
            iniciarPartida();
        });

        JMenuItem miOpcoes = new JMenuItem("Opções");
        miOpcoes.addActionListener((e) -> {
            new JanelaConfiguracao(this).setVisible(true);
        });
        JMenuItem miSair = new JMenuItem("Sair");
        miSair.addActionListener((e) -> {
            sair();
        });
        miConectar.setEnabled(true);
        miDesconectar.setEnabled(false);
        miIniciarPartida.setEnabled(false);

        JMenu menuJogo = new JMenu("Jogo");
        menuJogo.add(miConectar);
        menuJogo.add(miDesconectar);
        menuJogo.add(miIniciarPartida);
        menuJogo.add(miOpcoes);
        menuJogo.add(miSair);

        JMenuBar bar = new JMenuBar();
        bar.add(menuJogo);
        setJMenuBar(bar);

        PainelJogo painel = new PainelJogo(this);
        setContentPane(painel);
    }

    /**
     * Requer conexão ao atorJogador
     */
    public void conectar() {
        atorJogador.conectar(pedirNomeAoJogador());
    }

    /**
     * Requer desconexão ao atorJogador e habilita/desabilita menus conforme
     * resultado
     */
    public void desconectar() {
        atorJogador.desconectar();
    }

    public void sair() {
        if (JOptionPane.showConfirmDialog(this,
                    "Deseja mesmo sair?",
                    "Mastermind",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            dispose();
        }
    }

    public void iniciarPartida() {
        Logger.call(this);
        atorJogador.iniciarPartida();
        getContentPane().repaint();
    }

    public void clickBancoDeCores(int indice) {
        Logger.call(this, "clickBancoDeCores("+indice+")");
        if (!atorJogador.isEmAndamento()) {
            JOptionPane.showMessageDialog(this,
                    "Inicie uma partida para começar a jogar!");
            return;
        }
        if (!atorJogador.isVezUsuario()) {
            notificarVezDoOponente();
            return;
        }
        if (corSelecionada != null && corSelecionada.indice() == indice) {
            corSelecionada = null;
            setCursor(Cursor.getDefaultCursor());
            return;
        }
        corSelecionada = Cor.cores[indice];
        try {
            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new java.io.File("img/cor" + indice + ".png"));
            java.awt.Graphics2D g = img.createGraphics();
            g.setColor(java.awt.Color.WHITE);
            g.fillPolygon(new int[] {0, 3, 0}, new int[] {0, 0, 3}, 3);
            g.dispose();
            getContentPane().setCursor(
                    java.awt.Toolkit.getDefaultToolkit().createCustomCursor(
                        img, new java.awt.Point(0,0), "SelectedColorCursor"
                    )
                );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickPalpite(int indice) {
        Logger.call(this, "clickPalpite("+indice+")");
        if (corSelecionada != null) {
            posicionarCor(indice);
        } else {
            retirarCor(indice);
        }
        getContentPane().repaint();
    }

    /**
     * Ação executada quando uma jogada for enviada
     **/
    public void confirmarJogada() {
        if (!atorJogador.isVezUsuario()) {
            notificarVezDoOponente();
        }
        if (atorJogador.getTabuleiro().getPalpiteAtual().isCompleta()) {
            Jogada jogada = new Jogada(
                    atorJogador.getTabuleiro().getPalpiteAtual(),
                    atorJogador.getJogadorAtual());
            atorJogador.enviarJogada(jogada);
            getContentPane().repaint();
        }
    }

    public String pedirNomeAoJogador() {
        return JOptionPane.showInputDialog("Insira seu apelido de jogador:");
    }

    public void notificarVezDoOponente() {
        JOptionPane.showMessageDialog(this, "Vez do oponente!");
    }

    public void notificarPartidaEmAndamento() {
        JOptionPane.showMessageDialog(this, "Uma partida já está em andamento!");
    }

    public void posicionarCor(int indice) {
        atorJogador.getTabuleiro().getPalpiteAtual().posicionar(corSelecionada, indice);
        corSelecionada = null;
        getContentPane().setCursor(Cursor.getDefaultCursor());
    }

    public void retirarCor(int indice) {
        atorJogador.getTabuleiro().getPalpiteAtual().posicionar(Cor.VAZIO, indice);
    }

    public AtorJogador getAtorJogador() {
        return this.atorJogador;
    }

    /**
     * Habilita/desabilita menus e notifica usuário
     */
    @Override
    public void aoConectarComExito() {
        miConectar.setEnabled(false);
        miDesconectar.setEnabled(true);
        miIniciarPartida.setEnabled(true);
        repaint();
        JOptionPane.showMessageDialog(this, "Conectado com sucesso.");
    }

    /**
     * Notifica usuário da falha ao conectar
     * @param motivo Motivo da falha
     */
    @Override
    public void aoFalharConexao(String motivo) {
        JOptionPane.showMessageDialog(this,
                "Falha ao conectar: \n" + motivo);
    }

    /**
     * Habilita/desabilita menus e notifica usuário
     */
    @Override
    public void aoDesconectarComExito() {
        miConectar.setEnabled(true);
        miDesconectar.setEnabled(false);
        miIniciarPartida.setEnabled(false);
        JOptionPane.showMessageDialog(this,
                "Desconectado com sucesso.");
    }

    /**
     * Notifica usuário da falha ao desconectar
     * @param motivo Motivo da falha
     */
    @Override
    public void aoFalharDesconexao(String motivo) {
        JOptionPane.showMessageDialog(this,
                "Falha ao desconectar: \n" + motivo);
    }

    /**
     * Consequencia do início da partida pelo servidor
     */
    @Override
    public void aoIniciarPartida() {
        Logger.call(this);
        miIniciarPartida.setEnabled(false);
        getContentPane().repaint();
        JOptionPane.showMessageDialog(this,
                "Partida iniciada! Vez de: " +
                    atorJogador.getJogadorAtual().getApelido());
    }

    /**
     * Consequência da chegada do fim da partida (por vitória ou empate)
     */
    @Override
    public void aoFinalizarPartida(String vencedor) {
        Logger.call(this);
        miIniciarPartida.setEnabled(true);
        String str = "Fim de jogo!\nResultado: ";
        if (vencedor == null) {
            str += "Empate";
        } else {
            str += vencedor + " venceu!";
        }
        Logger.log("prompt", str);
        JOptionPane.showMessageDialog(this, str);
    }

    /**
     * Consequência de falha crítica durante o jogo
     * @param motivo Motivo da falha
     */
    @Override
    public void aoFinalizarPartidaComErro(String motivo) {
        miIniciarPartida.setEnabled(true);
        JOptionPane.showMessageDialog(this,
                "Partida finalizada com erro: \n"+motivo);
    }

    /**
     * Recebimento de uma jogada vinda do servidor
     * @param jogada Jogada realizada
     */
    @Override
    public void aoReceberJogada(Jogada jogada) {
        Logger.call(this);
        getContentPane().repaint();
    }

}
