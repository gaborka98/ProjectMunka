package Forms;

import MysqlConnector.Mysqlconn;
import myClass.Device;
import myClass.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RentDetail extends JFrame {
    private JPanel panel1;
    private JTextField fromDate;
    private JTextField toDate;
    private JButton lefoglalButton;
    private JButton mégseButton;

    private Mysqlconn conn = new Mysqlconn();


    public RentDetail(Device selectedDevice, User loginUser) {
        setLocationRelativeTo(null);
        setContentPane(panel1);
        setTitle(selectedDevice.getNev() + " lefoglalásának részletei");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600,200);
        setVisible(false);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        fromDate.setText(format.format(today));

        lefoglalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: Input ellenőrzés dátumokra


                assert selectedDevice != null;
                if (conn.lefoglal(selectedDevice, loginUser)){
                    JOptionPane.showMessageDialog(null, "Lefoglalás sikeresen megtörtént!");
                }
                dispose();
            }
        });
        mégseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
