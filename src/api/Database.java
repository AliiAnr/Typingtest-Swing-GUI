package api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection database;

    public Database () throws ClassNotFoundException, SQLException {
        String SUrl, SUsername, SPassword;

        SUrl = "jdbc:mysql://localhost:3306/fastfingers";
        SUsername = "root";
        SPassword = "";

        Class.forName("com.mysql.cj.jdbc.Driver");
        database = DriverManager.getConnection(SUrl, SUsername, SPassword);
    }
}
