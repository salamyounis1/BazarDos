
import java.util.concurrent.ConcurrentHashMap;

class Cache {
    private static final ConcurrentHashMap<String, String> cache_Memory = new ConcurrentHashMap<>();
 public static String get(String item) {
        return cache_Memory.get(item);
    }

    public static void put(String item, String value) {
        cache_Memory.put(item, value);
    }

    public static void invalidate(String item) {
        cache_Memory.remove(item);
    }
}