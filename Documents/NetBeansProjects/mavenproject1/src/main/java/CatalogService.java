/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author twitter
 */


import static spark.Spark.*;
import org.json.JSONObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;

public class CatalogService {
    public static void main(String[] args) {
        
        port(8086); Connection connection = DBcon();

        get("/search/:topic", (reqs, res) -> {
            String topic = reqs.params(":topic");
            
            return searchbytopic(topic);
        });

        get("/info/:ID", (reqs, res) -> {
            
            String itemNumber = reqs.params(":ID");
            
            return infoID(itemNumber);
        });
    }

   public static String searchbytopic(String topic) {
        
         Connection connection = DBcon();
         
        JSONArray result = new JSONArray();
        try {
            String query = "SELECT * FROM catalog WHERE topic = ?";
            
            PreparedStatement prestatement = connection.prepareStatement(query);
            
            prestatement.setString(1, topic);
            ResultSet Set = prestatement.executeQuery();

            while (Set.next()) {
                JSONObject entry = new JSONObject();
                entry.put("ID", Set.getString("ID"));
                entry.put("Hstock", Set.getInt("Hstock"));
                entry.put("cost", Set.getDouble("cost"));
                entry.put("title", Set.getString("title2"));
                entry.put("topic", Set.getString("topic"));
                result.put(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return result.toString();
    }


    public static String infoID(String itemNumber) {
         Connection connection = DBcon();
         
        JSONObject result = new JSONObject();
        
        try {
            String query = "SELECT * FROM catalog WHERE ID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, itemNumber);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
              JSONObject entry = new JSONObject();
                entry.put("ID", resultSet.getString("ID"));
                entry.put("Hstock", resultSet.getInt("Hstock"));
                entry.put("cost", resultSet.getDouble("cost"));
                entry.put("title", resultSet.getString("title2"));
                entry.put("topic", resultSet.getString("topic"));
                result.put(resultSet.getString("ID"), entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

   private static Connection DBcon() {
    Connection connection = null;
    
    try {
        Class.forName("org.sqlite.JDBC");
        
        connection = DriverManager.getConnection("jdbc:sqlite:sqliteDB.db");
        
        
        if (connection != null) {
            System.out.println("Connected to the database.");
        }
    } catch (ClassNotFoundException e) {
        System.err.println("SQLite JDBC driver not found.");
        e.printStackTrace();
    } catch (SQLException e) {
        System.err.println("Error connecting to the database.");
      
    }
    return connection;
}

}
