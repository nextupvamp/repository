public class Main {
    public static void main(String[] args) {
        try {
            Driver driver = new Driver("127.0.0.1", 8080);
            // Set key1: value1
            System.out.println(driver.set("key1", "value1", 100000));
            // Get key1: value1
            System.out.println(driver.get("key1"));
            // Dump a storage
            System.out.println(driver.dump());
            // Remove key1: value1
            System.out.println(driver.remove("key1"));
            // Try to get key1: value1
            System.out.println(driver.get("key1"));
            // Load dump (returns content of file)
            System.out.println(driver.load());
            // Get key1: value1
            System.out.println(driver.get("key1"));
            // Set new entry with small ttl
            System.out.println(driver.set("key2", "value2", 1000));
            // Wait for 2 seconds
            Thread.sleep(2000);
            // Check if entry has been erased
            System.out.println(driver.get("key2"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
