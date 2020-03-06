package Forms;

import MysqlConnector.Mysqlconn;
import myClass.Device;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class List extends JFrame{
    private Mysqlconn conn = new Mysqlconn();
    String [] columns = {"Név", "Foglaltság"};

    private JPanel panel1 = new JPanel();
    private JTable table1;
    private JButton lefoglalButton;
    private JButton leadButton;


    public List() {
        DefaultTableModel model = new DefaultTableModel(columns,0);


        for (Device iter : conn.getAllDevice()) {
            model.addRow(new Object[]{iter.getNev(), iter.isFoglalt() ? "foglalt" : "szabad"});
        }

        table1.setModel(model);

        panel1.add(new JScrollPane(table1));

        setContentPane(panel1);
        setSize(500,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
        lefoglalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: updatelni a db-ben a Rented oszlopot
            }
        });
        leadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: updatelni a db-ben a Rented oszlopot

            }
        });
    }
}
