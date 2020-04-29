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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class List extends JFrame{
    private Mysqlconn conn = new Mysqlconn();
    private ArrayList<Device> allDevice = conn.getAllDevice();
    String [] columns = {"ID", "Név", "Foglaltság"};

    private JPanel panel1;
    private JTable table1;
    private JButton lefoglalButton;
    private JButton leadButton;
    private JButton visszaButton;
    private JCheckBox színesMódCheckBox;
    private JButton frissítésButton;

    private Date fromDate = new Date();

    public List(User loggedIn) {
        table1.setModel(new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        updateList();

        setContentPane(panel1);
        setTitle("Eszkozok listazasa, Jog:" + loggedIn.getRank());
        setSize(600,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);

        lefoglalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row, column;
                row = table1.getSelectedRow();
                column = 0;

                Device rented = findById((int) table1.getValueAt(row, column));

                RentDetail asd = new RentDetail(rented, loggedIn);
                asd.setVisible(true);

                fromDate = asd.FromDateToLead;

                updateList();
                allDevice = conn.getAllDevice();
            }
        });
        leadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row, column;
                row = table1.getSelectedRow();
                column = 0;

                Device rented = findById((int) table1.getValueAt(row, column));

                if (rented.getRenter() == loggedIn.getId()) {
                    assert rented != null;
                    if (conn.lead(rented, loggedIn, fromDate)){
                        JOptionPane.showMessageDialog(null, "Leadás sikeresen megtörtént!");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Nem adhatod le, mert nem te foglaltad le az eszkozt!");
                    return;
                }

                updateList();
                allDevice = conn.getAllDevice();

            }
        });
        visszaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new mainMenu(loggedIn).setVisible(true);
                dispose();
            }
        });
        színesMódCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateList();
            }
        });
        frissítésButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateList();
            }
        });
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

    private Device findById(int id) {
        for(Device iter : allDevice) {
            if (id == iter.getIndex()) { return iter; }
        }
        return null;
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