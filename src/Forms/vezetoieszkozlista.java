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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class vezetoieszkozlista extends javax.swing.JFrame {
    private Mysqlconn conn = new Mysqlconn();
    private ArrayList<Device> allDevice = conn.getAllDevice();
    String [] columns = {"ID", "Név", "Foglaltság"};

    private JTable table1;
    private JButton leadButton;
    private JButton lefoglalButton;
    private JButton frissítésButton;
    private JButton visszaButton;
    private JCheckBox színesMódCheckBox;
    private JPanel bossDevicesList;

    public vezetoieszkozlista(User loggedInUser) {
        table1.setModel(new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        updateList();

        setLocationRelativeTo(null);
        setContentPane(bossDevicesList);
        setTitle("Főmenü");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,250);
        setVisible(false);

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

                assert rented != null;
                if (conn.lead(rented, loggedInUser)){
                    JOptionPane.showMessageDialog(null, "Leadás sikeresen megtörtént!");
                }

                updateList();

            }
        });
        lefoglalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row, column;
                row = table1.getSelectedRow();
                column = 0;

                Device rented = findById((int) table1.getValueAt(row, column));

                new RentDetail(rented, loggedInUser).setVisible(true);

                updateList();
            }
        });
    }

    private Device findById(int id) {
        for(Device iter : allDevice) {
            if (id == iter.getIndex()) { return iter; }
        }
        return null;
    }

    private void updateList() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);


        for (Device iter : conn.getAllDevice()) {
            model.addRow(new Object[]{iter.getIndex(), iter.getNev(), iter.isFoglalt() ? "foglalt" : "szabad"});
        }

        if (színesMódCheckBox.isSelected()) {
            table1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    String status = (String)table.getModel().getValueAt(row,2);

                    if (status.equals("foglalt")) { c.setBackground(Color.RED); c.setForeground(Color.BLACK); }
                    else if (status.equals("szabad")) { c.setBackground(Color.GREEN); c.setForeground(Color.BLACK);}
                    if (isSelected) { c.setBackground(Color.GRAY); c.setForeground(Color.WHITE); }
                    return c;
                }
            });
        } else if (!színesMódCheckBox.isSelected()) {
            table1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
        }

        table1.setModel(model);
    }
    private class dateLabelFromatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
    }
}
