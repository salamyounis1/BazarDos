

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

public class FrontendService {
    public static void main(String[] args) {
       port(8087);
            get("/search/:topic", (req, res) -> {
      
        });

       

        put("/purchase/:ID", (req, res) -> {
           

        });
         get("/info/:ID", (req, res) -> {
         
        });
       

       
    }
}
