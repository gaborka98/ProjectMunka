package Forms;

import myClass.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Elorefoglalt extends javax.swing.JFrame {
    private JTable Nev;
    private JButton visszaButton;
    private JPanel panel1;

    public Elorefoglalt(User loginUser) {
        setLocationRelativeTo(null);
        setContentPane(panel1);
        setTitle("Elore lefoglalt");
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
