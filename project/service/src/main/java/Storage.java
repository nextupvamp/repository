import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private Map<String, Record> map;
    private final Thread cleaner;
    private File file;

    public Storage() {
        map = new HashMap<>();
        cleaner = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        removeExpired();
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Cleaner has been interrupted");
                }
            }
        };
        cleaner.start();
    }

    public String get(String key) {
        Record record = map.get(key);
        return record != null ? record.getData() : null;
    }

    public String set(String key, String value, long ttl) {
        if (key == null || value == null || key.isEmpty() || value.isEmpty()) {
            return "Value setting failure";
        }

        map.put(key, new Record(value, ttl));

        return "Value has been set successfully";
    }

    public String remove(String key) {
        Record record = map.remove(key);
        return record != null ? record.getData() : null;
    }

    public String dump() {
        file = new File(Paths.get("").toAbsolutePath().toString() + "dump.txt");

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream)) {
            oos.writeObject(map);
        } catch (Exception e) {
            throw new RuntimeException("Dumping to file error");
        }

        String body;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            body = new String(fileInputStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("File reading/writing error");
        }
        return body;
    }

    public String load() throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fileInputStream)) {
            this.map = (HashMap<String, Record>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("File reading/writing error");
        }
        return "Storage loaded successfully";
    }

    public void removeExpired() {
        var it = map.entrySet().iterator();
        while (it.hasNext()) {
            Record value = it.next().getValue();
            value.updateExpiredStatus();
            if (value.isExpired()) {
                it.remove();
            }
        }
    }
}
