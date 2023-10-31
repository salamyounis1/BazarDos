/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ROA'A QINO
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static spark.Spark.*;
import org.json.JSONObject;

public class order {
    private static Connection conData; // Define the connection as a class-level instance variable

    public static void main(String[] args) {
        port(8085);

        conData = connectToDatabase(); // Assign the connection

        get("/purchase/:ID", (req, res) -> {
            String itemNumber = req.params(":ID");
            return purchase(itemNumber);
        });
    }
    public static String purchase(String itemNumber) {
        return "";
    }
    public static Connection connectToDatabase() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:sqliteDB.db");
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}