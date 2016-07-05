import java.io.FileReader;
import java.io.PrintWriter;
import java.awt.Window;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JanelaConfiguracao extends JDialog {
    private static final long serialVersionUID = 157004357678536666L;

    private JButton btnSalvar;
    private JTextField edtServidor;

    public JanelaConfiguracao(Window parent) {
        super(parent);

        setTitle("Configurações");
        setModal(true);

        GridLayout layout = new GridLayout(2, 2, 8, 8);
        setLayout(layout);

        JLabel lblServidor = new JLabel("IP do Servidor: ");
        edtServidor = new JTextField();
        try {
            FileReader reader = new FileReader("server.conf");
            char[] conteudo = new char[50];
            reader.read(conteudo);
            edtServidor.setText(new String(conteudo).split("\0")[0]);
            reader.close();
        } catch (java.io.IOException e) {
        }
        edtServidor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                btnSalvar.setEnabled(!edtServidor.getText().isEmpty());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                btnSalvar.setEnabled(!edtServidor.getText().isEmpty());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                btnSalvar.setEnabled(!edtServidor.getText().isEmpty());
            }
        });
        btnSalvar = new JButton("Salvar");
        btnSalvar.setEnabled(false);
        btnSalvar.addActionListener((e) -> {
            salvar();
        });
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener((e) -> {
            dispose();
        });

        add(lblServidor);
        add(edtServidor);
        add(btnSalvar);
        add(btnFechar);

        pack();

        setLocationRelativeTo(this);
    }

    private void salvar() {
        try {
            PrintWriter writer = new PrintWriter("server.conf");
            writer.print(edtServidor.getText());
            writer.close();
            btnSalvar.setEnabled(false);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Erro ao salvar configurações: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
