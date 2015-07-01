/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courseworkbd.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class BDConnectionHandler {

    private static BDConnectionHandler instance;
    private Connection conn;
    private String connectStr = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};"
            + "DBQ=D:\\CourseWorkDB.accdb";

    public static BDConnectionHandler getInstance() {
        if (instance == null) {
            instance = new BDConnectionHandler();
        }
        return instance;
    }

    public boolean initConnection() {
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            conn = DriverManager.getConnection(connectStr);
            if (conn == null) {
                System.err.println("No connection");
                return false;
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(BDConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BDConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
