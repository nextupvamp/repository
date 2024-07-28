import java.io.Serializable;

public class Record implements Serializable {
    public static final long DEFAULT_TTL = 256_000;

    private String data;
    private long ttl;
    private boolean expired;

    public Record(String data) {
        this.data = data;
        ttl = DEFAULT_TTL + System.currentTimeMillis();
        expired = false;
    }

    public Record(String data, long ttl) {
        this.data = data;
        this.ttl = ttl + System.currentTimeMillis();
        expired = false;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setTTL(long ttl) {
        this.ttl = ttl + System.currentTimeMillis();
    }

    public long getTTL() {
        return ttl;
    }

    public void updateExpiredStatus() {
        if (ttl < System.currentTimeMillis()) {
            expired = true;
        }
    }

    public boolean isExpired() {
        return expired;
    }
}
