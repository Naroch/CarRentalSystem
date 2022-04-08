package connection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class conn {
    public static Connection connection() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wypozyczalnia", "root", "");
            return con;
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return null;
    }
}
