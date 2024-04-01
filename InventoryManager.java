import java.io.*;
import java.util.*;

public class InventoryManager {
    private List<Item> inventory;
    private static final String FILE_NAME = "Barang.csv";

    // Constructor
    public InventoryManager() {
        this.inventory = new ArrayList<>();
        loadInventoryFromFile();
    }

    // Method to load inventory from CSV file
    private void loadInventoryFromFile() {
        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            if (parts.length == 6) { // Memastikan baris memiliki semua elemen yang diharapkan
                String id = parts[0];
                String name = parts[1];
                double purchasePrice = Double.parseDouble(parts[2]);
                double sellingPrice = Double.parseDouble(parts[3]);
                int stock = Integer.parseInt(parts[4]);
                int minStock = Integer.parseInt(parts[5]);
                inventory.add(new Item(id, name, purchasePrice, sellingPrice, stock, minStock));
            } else {
                System.out.println("Invalid line format: " + line);
            }
        }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILE_NAME);
        }
    }

    // Method to save inventory to CSV file
    private void saveInventoryToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Item item : inventory) {
                writer.println(item.getId() + "," + item.getName() + "," + item.getPurchasePrice() + "," +
                        item.getSellingPrice() + "," + item.getStock() + "," + item.getMinStock());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to add an item to inventory
    public void addItem(Item item) {
        inventory.add(item);
        saveInventoryToFile();
        System.out.println("Item added successfully.");
    }

    public void removeItem(String itemId) {
        Item itemToRemove = null;
        for (Item item : inventory) {
            if (item.getId().equals(itemId)) {
                itemToRemove = item;
                break;
            }
        }
    
        if (itemToRemove != null) {
            inventory.remove(itemToRemove);
            saveInventoryToFile();
            System.out.println("Item removed successfully.");
        } else {
            System.out.println("Item not found.");
        }
    }
    

    // Method to update item details
    public void updateItem(String itemId, String name, double purchasePrice, double sellingPrice, int stock, int minStock) {
        for (Item item : inventory) {
            if (item.getId().equals(itemId)) {
                item.setName(name);
                item.setPurchasePrice(purchasePrice);
                item.setSellingPrice(sellingPrice);
                item.setStock(stock);
                item.setMinStock(minStock);
                saveInventoryToFile();
                System.out.println("Item updated successfully.");
                return;
            }
        }
        System.out.println("Item not found.");
    }
    
    // Method to search for an item by ID
    public Item searchItem(String itemId) {
        for (Item item : inventory) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null; // Jika tidak ditemukan
    }    

    // Method to generate stock report from CSV file
    public void generateStockReport() {
        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            System.out.println("=========== STOCK REPORT ===========");
            System.out.printf("%-10s %-20s %-10s\n", "ID", "Item", "Stock");
            System.out.println("-------------------------------------");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String id = parts[0];
                String name = parts[1];
                int stock = Integer.parseInt(parts[4]);
                System.out.printf("%-10s %-20s %-10s\n", id, name, stock);
            }
            System.out.println("=====================================");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILE_NAME);
        }
    }

}
