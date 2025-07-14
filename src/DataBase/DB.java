
package DataBase;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    
    static String driver = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/rex";
    static String username = "root";
    static String password = "ah.zak277";
    static Connection con;
        
    public static Connection getConnection()
    {
        if (con == null)
        {
            try {
                Class.forName(driver);
                con = DriverManager.getConnection(url,username,password);
            } catch (SQLException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }
        return con;
    }
}
