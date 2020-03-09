package Forms;

import MysqlConnector.Mysqlconn;
import myClass.Device;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class List extends JFrame{
    private Mysqlconn conn = new Mysqlconn();
    String [] columns = {"ID", "Név", "Foglaltság", "Foglalás dátuma"};

    private JPanel panel1;
    private JTable table1;
    private JButton lefoglalButton;
    private JButton leadButton;
    private JButton visszaButton;
    private JCheckBox színesMódCheckBox;


    public List() {
        table1.setModel(new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        updateList();

        setContentPane(panel1);
        setTitle("List devices");
        setSize(600,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);

        lefoglalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row, column;
                row = table1.getSelectedRow();
                column = 0;

                int selectedId = (int) table1.getValueAt(row, column);

                if (conn.lefoglal(selectedId)){
                    JOptionPane.showMessageDialog(null, "Lefoglalás sikeresen megtörtént!");
                }

                updateList();

            }
        });
        leadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row, column;
                row = table1.getSelectedRow();
                column = 0;

                int selectedId = (int) table1.getValueAt(row, column);

                if (conn.lead(selectedId)){
                    JOptionPane.showMessageDialog(null, "Leadás sikeresen megtörtént!");
                }

                updateList();

            }
        });
        visszaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Login().setVisible(true);
            }
        });
        színesMódCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateList();
            }
        });
    }

    private void updateList() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);


        for (Device iter : conn.getAllDevice()) {
            model.addRow(new Object[]{iter.getIndex(), iter.getNev(), iter.isFoglalt() ? "foglalt" : "szabad", iter.getRentDate()});
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
}
