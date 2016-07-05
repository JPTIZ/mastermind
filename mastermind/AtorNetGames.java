import java.io.FileReader;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.Proxy;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import br.ufsc.inf.leobr.cliente.exception.JahConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoPossivelConectarException;
import br.ufsc.inf.leobr.cliente.exception.ArquivoMultiplayerException;

public class AtorNetGames implements OuvidorProxy {
    private static final long serialVersionUID = 6701991572625672625L;

    private AtorJogador atorJogador;
    private Proxy proxy;

    public AtorNetGames(AtorJogador atorJogador) {
        this.atorJogador = atorJogador;
        proxy = Proxy.getInstance();
        proxy.addOuvinte(this);
    }

    public void conectar(String apelido)
        throws JahConectadoException,
               NaoPossivelConectarException,
               ArquivoMultiplayerException
    {
        Logger.call(this, "conectar("+apelido+")");
        String servidor = "localhost";
        try {
            FileReader reader = new FileReader("server.conf");
            char[] conteudo = new char[50];
            reader.read(conteudo);
            servidor = new String(conteudo).split("\0")[0];
            reader.close();
        } catch (java.io.IOException e) {
        }
        proxy.conectar(servidor, apelido);
    }

    public void iniciarPartida() {
        Logger.call(this);
        try {
            proxy.iniciarPartida(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarJogada(Jogada jogada) {
        Logger.call(this, "enviarJogada("+jogada+")");
        try {
            proxy.enviaJogada(jogada);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void desconectar()
        throws NaoConectadoException
    {
        Logger.call(this);
        proxy.desconectar();
    }

    public String getApelido(int ordem) {
        Logger.call(this, "getApelido("+ordem+")");
        return proxy.obterNomeAdversario(ordem);
    }

    @Override
    public void finalizarPartidaComErro(String mensagem) {
        Logger.call(this);
        atorJogador.finalizarPartidaComErro(mensagem);
    }

    @Override
    public void iniciarNovaPartida(Integer posicao) {
        Logger.call(this, "iniciarNovaPartida("+posicao+")");
        atorJogador.iniciarPartida(posicao-1);
    }

    @Override
    public void receberJogada(br.ufsc.inf.leobr.cliente.Jogada jogada) {
        Logger.call(this, "receberJogada("+jogada+")");
        atorJogador.receberJogada((Jogada)jogada);
    }

    @Override
    public void tratarPartidaNaoIniciada(String mensagem) {
        Logger.call(this, "tratarPartidaNaoIniciada("+mensagem+")");
        atorJogador.disparar(
                new ConexaoEvento(ConexaoEvento.Tipo.CONECTAR_FALHA,
                new Object[] { mensagem }));
    }

    @Override
    public void tratarConexaoPerdida() {
        Logger.call(this);
    }

    @Override
    public void receberMensagem(String mensagem) {
        Logger.call(this, "receberMensagem("+mensagem+")");
    }
}
