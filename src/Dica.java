import java.io.File;
import java.awt.Image;
import javax.imageio.ImageIO;

public enum Dica {
    POSICAO_CORRETA("dica_posicao_correta.png"),
    POSICAO_INCORRETA("dica_posicao_incorreta.png");

    private Image icone;
    Dica(String arquivoIcone) {
        try {
            this.icone = ImageIO.read(new File("img/"+arquivoIcone));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Image icone() {
        return icone;
    }
}
