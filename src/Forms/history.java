package Forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class history extends javax.swing.JFrame {
    private JPanel panel1;
    private JTable Nev;
    private JButton visszaButton;

    public history () {
        setLocationRelativeTo(null);
        setContentPane(panel1);
        setTitle("Elozmenyek");
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
