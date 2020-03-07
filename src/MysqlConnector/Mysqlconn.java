package MysqlConnector;

import myClass.Device;

import java.sql.*;
import java.util.ArrayList;


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

    public boolean lefoglal(int index) {
        int action = -1;
        try {
            PreparedStatement lefoglal = conn.prepareStatement("UPDATE Devices SET Rented=? WHERE ID=?");
            lefoglal.setBoolean(1, true);
            lefoglal.setInt(2,index);

            action = lefoglal.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return (action > 0);
    }

    public boolean lead(int index) {
        int action = -1;
        try {
            PreparedStatement lefoglal = conn.prepareStatement("UPDATE Devices SET Rented=? WHERE ID=?");
            lefoglal.setBoolean(1, false);
            lefoglal.setInt(2,index);

            action = lefoglal.executeUpdate();

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
                devices.add(new Device(rs.getInt("ID"), rs.getString("Name"), rs.getBoolean("Rented")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return devices;
    }

    public String getPass(String username){
        String pw = null;

        try {
            PreparedStatement getpw = conn.prepareStatement("SELECT * FROM Users WHERE UserName=?");
            getpw.setString(1,username);
            ResultSet rs = getpw.executeQuery();

            while(rs.next()) {
                pw = rs.getString("Password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pw;
    }
}
