package Forms;

import javax.swing.*;

public class AktualisEszkozok extends JFrame {
    private JTable Nev;
    private JButton visszaButton;
    private JPanel akt;

    private void createUIComponents() {
        // TODO: place custom component creation code here

        //setLocationRelativeTo(null);
        setContentPane(akt);
        setTitle("Aktualis");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,300);
        setVisible(false);
    }
}
