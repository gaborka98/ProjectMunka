package Forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AktualisEszkozok extends javax.swing.JFrame {
    private JTable table;
    private JButton visszaButton;
    private JPanel akt;
    private JButton leadButton;

    public AktualisEszkozok() {
        setLocationRelativeTo(null);
        setContentPane(akt);
        setTitle("Aktualis eszkozok");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,300);
        setVisible(false);

        visszaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
