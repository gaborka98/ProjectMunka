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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AktualisEszkozok extends javax.swing.JFrame {

    private JTable table1;
    private JButton visszaButton;
    private JPanel akt;
    private JButton leadButton;

    private Mysqlconn con = new Mysqlconn();
    private HashMap<Device, String[]> actuallyTakenDevices;
    private String[] columns = { "ID", "Nev", "Tol", "Ig" };

    private String fromDateString;


    public AktualisEszkozok(User loggedInUser) {
        this.actuallyTakenDevices = con.getActuallyTakenDevices(loggedInUser);

        table1.setModel(new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        updateList();


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

        leadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row, column;
                row = table1.getSelectedRow();
                column = 0;

                Device rented = findById((int) table1.getValueAt(row, column));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fromDate = null;
                try {
                    fromDate = sdf.parse(fromDateString);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

                if (rented.getRenter() == loggedInUser.getId()) {
                    assert rented != null;
                    if (con.lead(rented, loggedInUser, fromDate)){
                        JOptionPane.showMessageDialog(null, "Leadás sikeresen megtörtént!");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Nem adhatod le, mert nem te foglaltad le az eszkozt!");
                    return;
                }

                actuallyTakenDevices = con.getActuallyTakenDevices(loggedInUser);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                updateList();
            }
        });
    }

    private Device findById(int id) {
        for(Device iter : actuallyTakenDevices.keySet()) {
            if (id == iter.getIndex()) {
                fromDateString = actuallyTakenDevices.get(iter)[0];
                System.out.println(fromDateString);
                return iter;
            }
        }
        return null;
    }

    private void updateList() {
        if (!actuallyTakenDevices.isEmpty()) {

            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            for (Device iter : actuallyTakenDevices.keySet()) {
                model.addRow(new Object[]{iter.getIndex(), iter.getNev(), actuallyTakenDevices.get(iter)[0], actuallyTakenDevices.get(iter)[1]});
            }
        }
    }

}
