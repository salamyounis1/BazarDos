

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
    private static final int Number_of_REPLICAS = 2;

    public static void main(String[] args) {
        port(8087);

        // Assume replicas for order and catalog services
        order[] order_Replication = new order[Number_of_REPLICAS];
        CatalogService[] catalog_Replication = new CatalogService[Number_of_REPLICAS];

        // Initialize replicas
        for (int i = 0; i <2; i++) {
            order_Replication[i] = new order();
            catalog_Replication[i] = new CatalogService();
        }

        // Set up caching
        Cache cache = new Cache();    
        
        
        
        

        get("/search/:topic", (req, res) -> {
            String T = req.params(":topic");

            // Check cache first
            String cheak_cache = cache.get("search-" + T);
            if (cheak_cache != null) {
                return cheak_cache;
            }

            // Forward the request to a replica using a load balancing algorithm
            int replicaIndex = loadBalancingAlgorithm(T, Number_of_REPLICAS);
            String result = catalog_Replication[replicaIndex].searchbytopic(T);

            // Update cache
            cache.put("search-" + T, result);

            return result;
        });

        put("/purchase/:ID", (req, res) -> {
            String itNum = req.params(":ID");

            // Forward the request to a replica using a load balancing algorithm
            int replicaIndex = loadBalancingAlgorithm(itNum, Number_of_REPLICAS);
            String result = order_Replication[replicaIndex].purchase(itNum);

            // Invalidate cache for purchased item
            cache.invalidate("search-" + itNum);

            return result;
        });

        get("/info/:ID", (req, res) -> {
            String itNum = req.params(":ID");

            // Check cache first
            String cheak_cache = cache.get("info-" + itNum);
            if (cheak_cache != null) {
                return cheak_cache;
            } 

            // Forward the request to a replica using a load balancing algorithm
            int replicaIndex = loadBalancingAlgorithm(itNum, Number_of_REPLICAS);
            String result = catalog_Replication[replicaIndex].infoID(itNum);

            // Update cache
            cache.put("info-" + itNum, result);

            return result;
        });
    } 

    // Load balancing algorithm (simplified round-robin)
    private static int loadBalancingAlgorithm(String item, int number_of_Replicas) {
        return item.hashCode() % number_of_Replicas;
    }
}
