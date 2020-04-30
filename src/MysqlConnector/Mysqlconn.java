package MysqlConnector;

import myClass.Device;
import myClass.User;


import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;



public class Mysqlconn {
    private Connection conn ;

    public Mysqlconn() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.1.9:3306/projekt", "projekt", "projekt123");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean lefoglal(Device device, User user, java.util.Date dateFrom, java.util.Date dateTo) {
        int action = -2;
        java.util.Date currentDate = new java.util.Date();
        try {
            PreparedStatement lefoglal = conn.prepareStatement("UPDATE Devices SET rented=?, user_id=? WHERE device_id=?");
            lefoglal.setBoolean(1, true);
            lefoglal.setInt(2, user.getId());
            lefoglal.setInt(3, device.getIndex());

            PreparedStatement history = conn.prepareStatement("INSERT INTO Rent_list (user_id, device_id, from_date, to_date) VALUES (?,?,?,?)");
            history.setInt(1, user.getId());
            history.setInt(2, device.getIndex());
            history.setDate(3,new java.sql.Date(dateFrom.getTime()));
            history.setDate(4, new java.sql.Date(dateTo.getTime()));

            if (isSameDate(currentDate, dateFrom)) { action += lefoglal.executeUpdate(); }
            action = history.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return (action > 0);
    }

    public ArrayList<java.util.Date[]> getFromToDate(Device device) {
        ArrayList<java.util.Date[]> fromTo = new ArrayList<>();
        try{
            PreparedStatement dateto = conn.prepareStatement("SELECT from_date, to_date FROM Rent_list WHERE device_id=?");
            dateto.setInt(1, device.getIndex());
            ResultSet rs = dateto.executeQuery();

            while(rs.next()) {
                java.util.Date[] tmp = new java.util.Date[2];
                tmp[0] = new java.util.Date(rs.getDate("from_date").getTime());
                tmp[1] = new java.util.Date(rs.getDate("to_date").getTime());
                fromTo.add(tmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fromTo;
    }

    public boolean eszkozTorlese(Device selected) {
        int toReturn = -1;
        try {
            PreparedStatement delete = conn.prepareStatement("DELETE FROM Devices WHERE device_id=?");
            delete.setInt(1,selected.getIndex());
            toReturn = delete.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn == 1 ? true : false;
    }

    public boolean eszkozHozzaadasa(String pname, int maxDay) {
        int toReturn = -1;
        try {
            PreparedStatement insert = conn.prepareStatement("INSERT INTO Devices (`name`, max_days) VALUES (?,?)");
            insert.setString(1,pname);
            insert.setInt(2,maxDay);
            toReturn = insert.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn == 1 ? true : false;
    }

    public boolean lead(Device device, User user, java.util.Date from) {
        int action = -2;
        try {
            PreparedStatement lefoglal = conn.prepareStatement("UPDATE Devices SET rented=? user_id=? WHERE device_id=?");
            lefoglal.setBoolean(1, false);
            lefoglal.setInt(2,-1);
            lefoglal.setInt(3,device.getIndex());

            Calendar cal = Calendar.getInstance();
            PreparedStatement history = conn.prepareStatement("UPDATE Rent_list SET to_date=? WHERE device_id=? AND user_id=? AND from_date=?");
            history.setDate(1, new Date(cal.getTimeInMillis()));
            history.setInt(2, device.getIndex());
            history.setInt(3, user.getId());
            history.setDate(4, new Date(from.getTime()));

            action = lefoglal.executeUpdate() + history.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return (action > 0);
    }

    public ArrayList<Device> getAllDevice() {

        ArrayList<Device> devices = new ArrayList<>();


        try {
            PreparedStatement getAll = conn.prepareStatement("SELECT * FROM Devices");
            ResultSet rs = getAll.executeQuery();

            while (rs.next()) {
                devices.add(new Device(rs.getInt("device_id"), rs.getString("name"), rs.getBoolean("rented"),rs.getInt("max_days"), rs.getInt("user_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

    public User getUser(String username) {
        User toReturn = null;
        try {
            PreparedStatement getUser = conn.prepareStatement("SELECT * FROM Users WHERE user_name=?");
            getUser.setString(1, username);
            ResultSet rs = getUser.executeQuery();

            while(rs.next()) {
                toReturn = new User(rs.getString("real_name"), rs.getString("rank"), rs.getString("user_name"), rs.getString("email"), rs.getString("password"), rs.getInt("user_id") );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public HashMap<Device, String[]> getHistoryDevices(User loggedINUser) {
        HashMap<Device, String[]> history = new HashMap<>();

        try {
            PreparedStatement getHistory = conn.prepareStatement("SELECT Devices.`name`,Devices.rented, Devices.max_days, Devices.device_id, Rent_list.from_date, Rent_list.to_date, Rent_list.user_id FROM Devices INNER JOIN Rent_list ON Devices.device_id = Rent_list.device_id WHERE Rent_list.user_id = ? AND Rent_list.to_date <= CURRENT_DATE()");
            getHistory.setInt(1, loggedINUser.getId());
            ResultSet rs = getHistory.executeQuery();

            while(rs.next()) {
                String[] temp = {rs.getString("from_date"), rs.getString("to_date")};
                history.put(new Device(rs.getInt("device_id"), rs.getString("name"), rs.getBoolean("rented"), rs.getInt("max_days"), rs.getInt("user_id")), temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return history;
    }

    public HashMap<Device, String[]> getActuallyTakenDevices(User loggedINUser) {
        HashMap<Device, String[]> actuallyTaken = new HashMap<>();

        try {
            PreparedStatement getActuallyTakenDevices = conn.prepareStatement("SELECT Devices.`name`,Devices.rented, Devices.max_days, Devices.device_id, Rent_list.from_date, Rent_list.to_date, Rent_list.user_id FROM Devices INNER JOIN Rent_list ON Devices.device_id = Rent_list.device_id WHERE Rent_list.user_id = ? AND Rent_list.from_date <= CURRENT_DATE() AND Rent_list.to_date > CURRENT_DATE()");
            getActuallyTakenDevices.setInt(1, loggedINUser.getId());
            ResultSet rs = getActuallyTakenDevices.executeQuery();

            while(rs.next()) {
                String[] temp = {rs.getString("from_date"), rs.getString("to_date")};
                actuallyTaken.put(new Device(rs.getInt("device_id"), rs.getString("name"), rs.getBoolean("rented"), rs.getInt("max_days"), rs.getInt("user_id")), temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return actuallyTaken;
    }

    public boolean visszamond(Device device, User loginUser, String fromDate, String toDate) {
        int toReturn = -1;

        try {
            SimpleDateFormat sdp = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date fromtemp = sdp.parse(fromDate);
            java.util.Date totemp = sdp.parse(toDate);

            PreparedStatement visszamond = conn.prepareStatement("DELETE FROM Rent_list WHERE device_id=? AND user_id=? AND from_date=? AND to_date=?");
            visszamond.setInt(1,device.getIndex());
            visszamond.setInt(2,loginUser.getId());
            visszamond.setDate(3, new Date(fromtemp.getTime()));
            visszamond.setDate(4, new Date(totemp.getTime()));

            toReturn = visszamond.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return toReturn == 1 ? true : false;
    }

    public HashMap<Device, String[]> getFutureRents(User loggedINUser) {
        HashMap<Device, String[]> futureRents = new HashMap<>();

        try {
            PreparedStatement getFutureTakenDevices = conn.prepareStatement("SELECT Devices.`name`,Devices.rented, Devices.max_days, Devices.device_id, Rent_list.from_date, Rent_list.to_date, Rent_list.user_id FROM Devices INNER JOIN Rent_list ON Devices.device_id = Rent_list.device_id WHERE Rent_list.user_id = ? AND Rent_list.from_date > CURRENT_DATE() ");
            getFutureTakenDevices.setInt(1, loggedINUser.getId());
            ResultSet rs = getFutureTakenDevices.executeQuery();

            while(rs.next()) {
                String[] temp = {rs.getString("from_date"), rs.getString("to_date")};
                futureRents.put(new Device(rs.getInt("device_id"), rs.getString("name"), rs.getBoolean("rented"), rs.getInt("max_days"), rs.getInt("user_id")), temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return futureRents;
    }

    private boolean isSameDate(java.util.Date current, java.util.Date from) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(current);
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0);
        currentCalendar.set(Calendar.MINUTE, 0);
        currentCalendar.set(Calendar.SECOND, 0);

        java.util.Date comperaCurrent = currentCalendar.getTime();

        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(current);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);

        java.util.Date compareFrom = fromCalendar.getTime();

        return comperaCurrent.equals(compareFrom);
    }
}
