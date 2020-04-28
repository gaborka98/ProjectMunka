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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class history extends javax.swing.JFrame {
    private JPanel panel1;
    private JTable table;
    private JButton visszaButton;

    private Mysqlconn con = new Mysqlconn();
    private User user;
    private HashMap<Device, String[]> historyDevice;
    private String[] columns = { "ID", "Nev", "Tol", "Ig" };

    public history (User loggedInUser) {
        this.user = loggedInUser;
        this.historyDevice = con.getHistoryDevices(user);

        setLocationRelativeTo(null);
        setContentPane(panel1);
        setTitle("Elozmenyek");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400,300);
        setVisible(false);

        table.setModel(new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        updateList();

        visszaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    }

    private void updateList() {
        if (!historyDevice.isEmpty()) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            for (Device iter : historyDevice.keySet()) {
                model.addRow(new Object[]{iter.getIndex(), iter.getNev(), historyDevice.get(iter)[0], historyDevice.get(iter)[1]});
            }
        }
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
