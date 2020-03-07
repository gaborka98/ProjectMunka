package Forms;

import MysqlConnector.Mysqlconn;
import myClass.Device;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class List extends JFrame{
    private Mysqlconn conn = new Mysqlconn();
    String [] columns = {"ID", "Név", "Foglaltság"};

    private JPanel panel1;
    private JTable table1;
    private JButton lefoglalButton;
    private JButton leadButton;
    private JButton button1;


    public List() {
        table1.setModel(new DefaultTableModel(columns,0));

        updateList();

        setContentPane(panel1);
        setTitle("List devices");
        setSize(400,300);
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
                // TODO: updatelni a db-ben a Rented oszlopot

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
    }

    private void updateList() {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);


        for (Device iter : conn.getAllDevice()) {
            model.addRow(new Object[]{iter.getIndex(), iter.getNev(), iter.isFoglalt() ? "foglalt" : "szabad"});
        }

        table1.setModel(model);

    }
}
