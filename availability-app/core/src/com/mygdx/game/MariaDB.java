package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.sql.*;

public class MariaDB {
    static final String host = "localhost";
    static final String port = "3306";
    static final String user = "jasminedirksen";
    static final String pass = "klkl";
    static final String url = "jdbc:mariadb://" + host + ":" + port + "/availabilityAppDB";
    public Statement stmt = null;
    String errStr = new String();
       
    public MariaDB() {
        Connection conn = null;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected database successfully...");

            stmt = conn.createStatement();

        }
        catch (SQLException sq) {
            errStr = "1 Database connection failed...";
            sq.printStackTrace();
        }
        catch (Exception e) {
            errStr = "2 Database connection failed...";
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return stmt;
    }

    public String getErrStr() {
        return errStr;
    }
}
