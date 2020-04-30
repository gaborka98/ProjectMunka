package Forms;

import MysqlConnector.Mysqlconn;
import myClass.Device;
import myClass.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RentDetail extends JFrame {
    private JPanel panel1;
    private JTextField fromDate;
    private JTextField toDate;
    private JButton lefoglalButton;
    private JButton mégseButton;

    private Mysqlconn conn = new Mysqlconn();

    public Date FromDateToLead = new Date();


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
                Date startDate = new Date();
                Date endDate = new Date();
                if (!fromDate.getText().isEmpty()) {
                    try {
                        startDate = format.parse(fromDate.getText());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Kezdo datumot kotelezo megadni!");
                    return;
                }

                if (toDate.getText().isEmpty()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startDate);
                    cal.add(Calendar.DAY_OF_MONTH,selectedDevice.getMaxRent());
                    endDate = cal.getTime();
                } else {
                    try {
                        endDate = format.parse(toDate.getText());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                }

                LocalDate FromDateLocal, ToDateLocal;
                FromDateLocal = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                ToDateLocal = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long daysBetween = ChronoUnit.DAYS.between(FromDateLocal, ToDateLocal);

                if (daysBetween > selectedDevice.getMaxRent()) {
                    JOptionPane.showMessageDialog(null, "Ennyi napra nem lehet lefoglalni\nMax nap amire lefoglalhatod ezt az eszkozt: " + selectedDevice.getMaxRent());
                    return;
                }

                Calendar afterOneYear = Calendar.getInstance();
                afterOneYear.setTime(today);
                afterOneYear.add(Calendar.YEAR,1);

                if (afterOneYear.getTime().before(startDate)) {
                    JOptionPane.showMessageDialog(null, "Nem foglalhatsz le egy evre elore!");
                    return;
                }

                if (!checkForwardRentIsFree(selectedDevice, startDate, endDate)) {
                    JOptionPane.showMessageDialog(null, "A kivalasztott intervallumon az eszkoz mar foglalt.");
                    return;
                }

                conn.lefoglal(selectedDevice, loginUser, startDate, endDate);
                FromDateToLead = startDate;
                setVisible(false);
            }
        });
        mégseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public boolean checkForwardRentIsFree(Device device, Date fromDate, Date toDate) {
        ArrayList<Date[]> forwarding = conn.getFromToDate(device);

        for ( Date[] fromto : forwarding) {
            if (fromto[0].before(fromDate)  && fromDate.before(fromto[1])){
                return false;
            }
            if (fromto[0].before(toDate) && toDate.before(fromto[1])) {
                return false;
            }
            if (fromDate.equals(fromto[0]) || toDate.equals(fromto[0]) || toDate.equals(fromto[1])) return false;
        }
        return true;
    }
}
