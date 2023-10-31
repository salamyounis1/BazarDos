/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ROA'A QINO
 */

import static spark.Spark.*;
import org.json.JSONObject;

public class order {
    public static void main(String[] args) {
        port(8085);
            
        get("/purchase/:ID", (req, res) -> {
            String NumItem = req.params(":ID");
            return purchase(NumItem);
        });
    }
    public static String purchase(String itemNumber) {
        return "";
    }
}