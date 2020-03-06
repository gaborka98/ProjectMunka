package Forms;

import MysqlConnector.Mysqlconn;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Login extends javax.swing.JFrame {
    private Mysqlconn connection = new Mysqlconn();

    private JTextField usrTextField;
    private JPasswordField passTextField;
    private JButton loginButton;
    private JPanel loginForm;
    private JButton registerButton;

    public Login() {

        setLocationRelativeTo(null);
        setContentPane(loginForm);
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginProcess();
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Register().setVisible(true);
            }
        });

        // Ha entert nyom a jelszo mezőben is belép
        passTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginProcess();
                }
            }
        });
    }

    private void loginProcess(){
        String username = usrTextField.getText();
        String pw = passTextField.getText();
        String dbPw = connection.getPass(username);

        if (!pw.equals(dbPw)) {
            JOptionPane.showMessageDialog(null, "Login failed...");
            return;
        }
        setVisible(false);
        new List().setVisible(true);
    }

}