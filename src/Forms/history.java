package Forms;

import javax.swing.*;

public class history extends JFrame {
    private JPanel panel1;
    private JTextArea eszköznyilvántartóRendszerTextArea;
    private JTextArea előzményekTextArea;
    private JButton visszaButton;
    private JTable Nev;

    private void createUIComponents() {
        // TODO: place custom component creation code here

        //setLocationRelativeTo(null);
        setContentPane(panel1);
        setTitle("Elozmenyek");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,300);
        setVisible(false);


    }
}
