package Forms;

import myClass.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainMenu extends javax.swing.JFrame {
    private JButton profilButton;
    private JButton eszközökListázásaButton;
    private JButton kijelentkezésButton;
    private JPanel mainMenu;

    public mainMenu(User loginUser){
        setLocationRelativeTo(null);
        setContentPane(mainMenu);
        setTitle("Főmenü");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,250);
        setVisible(false);

        eszközökListázásaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new List(loginUser).setVisible(true);
                dispose();
            }
        });
        kijelentkezésButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login().setVisible(true);
                dispose();
            }
        });
        profilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new profilMenu(loginUser).setVisible(true);
                dispose();
            }
        });
    }
}
