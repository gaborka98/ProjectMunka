package Forms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton button1;
    private JButton backButton;
    private JPanel regForm;

    public Register() {
        setLocationRelativeTo(null);
        setContentPane(regForm);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Register");
        pack();
        setVisible(false);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Login().setVisible(true);
            }
        });
    }
}
