package Forms;

import MysqlConnector.Mysqlconn;
import myClass.Device;
import myClass.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Elorefoglalt extends javax.swing.JFrame {
    private JTable table1;
    private JButton visszaButton;
    private JPanel panel1;
    private JButton visszamond;

    private Mysqlconn con = new Mysqlconn();
    private User user;
    private HashMap<Device, String[]> forwardRent;
    private String[] columns = { "ID", "Nev", "Tol", "Ig" };

    public Elorefoglalt(User loginUser) {
        this.user = loginUser;
        this.forwardRent = con.getFutureRents(user);

        table1.setModel(new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        updateList();


        setLocationRelativeTo(null);
        setContentPane(panel1);
        setTitle("Elore lefoglalt");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,300);
        setVisible(false);




        visszaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        visszamond.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row, column;
                row = table1.getSelectedRow();
                column = 0;

                String fromDate = (String) table1.getValueAt(row,2);
                String toDate = (String) table1.getValueAt(row,3);

                Device rented = findById((int) table1.getValueAt(row, column));

                if (con.visszamond(rented, user, fromDate, toDate)){
                    JOptionPane.showMessageDialog(null, "Visszamondas megtortent.");
                } else {
                    JOptionPane.showMessageDialog(null, "Visszamondasnal valami hiba tortent!");
                }
                updateList();
            }
        });
    }
    private void updateList() {
        if (!forwardRent.isEmpty()) {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);

            for (Device iter : forwardRent.keySet()) {
                model.addRow(new Object[]{iter.getIndex(), iter.getNev(), forwardRent.get(iter)[0], forwardRent.get(iter)[1]});
            }
        }
    }

    private Device findById(int id) {
        for(Device iter : forwardRent.keySet()) {
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
