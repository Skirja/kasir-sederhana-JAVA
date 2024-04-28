public class Item {
    private String id;
    private String name;
    private double purchasePrice;
    private double sellingPrice;
    private int stock;
    private int minStock;
    private int quantity;

    // Constructor
    public Item(String id, String name, double purchasePrice, double sellingPrice, int stock, int minStock) {
        this.id = id;
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
        this.stock = stock;
        this.minStock = minStock;
    }

    // Getters and setters
    public String getId() {
        return id;

