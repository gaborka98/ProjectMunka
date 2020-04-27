package Forms;

import MysqlConnector.Mysqlconn;
import myClass.Device;
import myClass.User;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AktualisEszkozok extends javax.swing.JFrame {

    private JTable table;
    private JButton visszaButton;
    private JPanel akt;
    private JButton leadButton;

    private Mysqlconn con = new Mysqlconn();
    private User user;
    private HashMap<Device, String[]> actuallyTakenDevices;


    public AktualisEszkozok(User loggedInUser) {
        this.user = loggedInUser;
        this.actuallyTakenDevices = con.getActuallyTakenDevices(user);


        setLocationRelativeTo(null);
        setContentPane(akt);
        setTitle("Aktualis eszkozok");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setVisible(false);

        updateList();

        visszaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private void updateList() {
        if (!actuallyTakenDevices.isEmpty()) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            for (Device iter : actuallyTakenDevices.keySet()) {
                model.addRow(new Object[]{iter.getIndex(), iter.getNev(), actuallyTakenDevices.get(iter)[0], actuallyTakenDevices.get(iter)[1]});
            }
        }
    }

}
