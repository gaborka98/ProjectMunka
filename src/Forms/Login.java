package Forms;

import MysqlConnector.Mysqlconn;
import myClass.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Login extends javax.swing.JFrame {
    private Mysqlconn connection = new Mysqlconn();

    private JTextField usrTextField;
    private JPasswordField passTextField;
    private JButton loginButton;
    private JPanel loginForm;

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

    private String encryptStringSha1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(text.getBytes());
            BigInteger no = new BigInteger(1,hash);
            String hashedtext = no.toString(16);
            while (hashedtext.length() < 32) hashedtext = "0" + hashedtext;
            return hashedtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    private void loginProcess(){
        String username = usrTextField.getText();
        String pw = passTextField.getText();
        User loginUser = connection.getUser(username);

        System.out.println(pw + "\t" + username + "\t" + loginUser.getPassword());
        System.out.println(encryptStringSha1(username + ":" + pw));

        if (!encryptStringSha1(username + ":" + pw).equals(loginUser.getPassword())) {
            JOptionPane.showMessageDialog(null, "Login failed...");
            return;
        }
        setVisible(false);
        new List(loginUser).setVisible(true);
    }

}