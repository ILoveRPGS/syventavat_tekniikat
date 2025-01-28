public class Category {
    private String primary;
    private String secondary;

    public Category(String primary, String secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public String getPrimary() {
        return this.primary;
    }

    public String getSecondary() {
        return this.secondary;
    }
}