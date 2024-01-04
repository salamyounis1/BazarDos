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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import static spark.Spark.*;
import org.json.JSONObject;

public class order {
    private static Connection conData; // Define the connection as a class-level instance variable

    public static void main(String[] args) {
        port(8085);

        conData = connectToDatabase(); // Assign the connection

        get("/purchase/:ID", (req, res) -> {
            String itNum = req.params(":ID");
            return purchase(itNum);
        });
    }
    public static String purchase(String itNum) {
         JSONObject result = new JSONObject();
         long startTime = System.nanoTime();
        try {
            String HstockQuery = "SELECT Hstock FROM catalog WHERE ID = ?";
            PreparedStatement HstockStat = conData.prepareStatement(HstockQuery);
            HstockStat.setString(1, itNum);
            ResultSet HstockResSet = HstockStat.executeQuery();

            if (HstockResSet.next()) {
                int Hstock = HstockResSet.getInt("Hstock");
                if (Hstock > 0) {
                    Hstock--;
                    String updateQuery = "UPDATE catalog SET Hstock = ? WHERE ID = ?";
                    PreparedStatement updateStat = conData.prepareStatement(updateQuery);
                    updateStat.setInt(1, Hstock);
                    updateStat.setString(2, itNum);
                    updateStat.executeUpdate();

                    result.put("status", "The Item purchased successfully.");
                } else {
                    result.put("status", "The Item is out of stock.");
                }
            } else {
                result.put("status", "Item not found .");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
         // Record the end time
        long endTime = System.nanoTime();

        // Calculate the time
        long Time_response = endTime - startTime;

        // Convert time to milliseconds (optional)
        long TimeInMilliseconds = Time_response / 1_000_000;

        // Print the time
        System.out.println("Purchase Time: " + TimeInMilliseconds + " milliseconds");

        invalidateCache(itNum); // Invalidate cache after a purchase
        return result.toString();
    
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
     public static void invalidateCache(String itemNumber) {
        Cache.invalidate("order-" + itemNumber);
     }
}
