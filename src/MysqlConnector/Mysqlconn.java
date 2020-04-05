package MysqlConnector;

import myClass.Device;
import myClass.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;


public class Mysqlconn {
    private Connection conn ;

    public Mysqlconn() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://gaborka98.mooo.com:3306/projekt", "projekt", "projekt123");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean lefoglal(Device device, User user) {
        int action = -2;
        try {
            Calendar cal = Calendar.getInstance();
            // Date rentDate = new Date();
            PreparedStatement lefoglal = conn.prepareStatement("UPDATE Devices SET rented=? WHERE device_id=?");
            lefoglal.setBoolean(1, true);
            lefoglal.setInt(2, device.getIndex());

            PreparedStatement history = conn.prepareStatement("INSERT INTO History (user_id, device_id, from_date, to_date) VALUES (?,?,?,?)");
            history.setInt(1, user.getId());
            history.setInt(2, device.getIndex());
            history.setDate(3,new java.sql.Date(cal.getTimeInMillis()));
            // add to date automaticalli
            cal.add(Calendar.DATE, device.getMaxRent());
            history.setDate(4, new java.sql.Date(cal.getTimeInMillis()));

            action = lefoglal.executeUpdate() + history.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return (action > 0);
    }

    public boolean lead(Device device, User user) {
        int action = -2;
        try {
            PreparedStatement lefoglal = conn.prepareStatement("UPDATE Devices SET rented=? WHERE device_id=?");
            lefoglal.setBoolean(1, false);
            lefoglal.setInt(2,device.getIndex());

            Calendar cal = Calendar.getInstance();
            PreparedStatement history = conn.prepareStatement("UPDATE History SET to_date=? WHERE device_id=? AND user_id=?");
            history.setDate(1, new java.sql.Date(cal.getTimeInMillis()));
            history.setInt(2, device.getIndex());
            history.setInt(3, user.getId());

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
                devices.add(new Device(rs.getInt("device_id"), rs.getString("name"), rs.getBoolean("rented"),rs.getInt("max_days")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }
/*
    public String getPass(String username){
        String pw = null;

        try {
            PreparedStatement getpw = conn.prepareStatement("SELECT * FROM Users WHERE user_name=?");
            getpw.setString(1,username);
            ResultSet rs = getpw.executeQuery();

            while(rs.next()) {
                pw = rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pw;
    }
*/
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
}
