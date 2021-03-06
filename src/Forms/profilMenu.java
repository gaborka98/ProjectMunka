package Forms;

import myClass.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class profilMenu extends javax.swing.JFrame {
    private JButton előzményekButton;
    private JButton előreLefoglaltButton;
    private JButton aktuálisanLefoglaltEszközökButton;
    private JPanel profilFrame;
    private JButton visszaButton;

    public profilMenu(User loginUser) {
        setLocationRelativeTo(null);
        setContentPane(profilFrame);
        setTitle("Profilmenu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,300);
        setVisible(false);


        visszaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new mainMenu(loginUser).setVisible(true);
                dispose();
            }
        });
        aktuálisanLefoglaltEszközökButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AktualisEszkozok(loginUser).setVisible(true);
            }
        });
        előzményekButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new history(loginUser).setVisible(true);
            }
        });
        előreLefoglaltButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Elorefoglalt(loginUser).setVisible(true);
            }
        });
    }
}
