// Priority.java
public enum Priority {
    URGENT,
    VIP,
    NORMAL;

    // URGENT > VIP > NORMAL
    public int compare(Priority other) {
        return other.ordinal() - this.ordinal();
    }
}