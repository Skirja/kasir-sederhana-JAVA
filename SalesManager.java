import java.io.*;
import java.util.*;
import java.text.*;

public class SalesManager {
    private List<Item> cart;
    private static final String FILE_NAME = "Laporan.csv";

    // Constructor
    public SalesManager() {
        this.cart = new ArrayList<>();
    }

    public void addItemToCart(Item item, int quantity) {
        // Tambahkan item ke keranjang sebanyak quantity yang diminta
        for (int i = 0; i < quantity; i++) {
            cart.add(item);
        }
        System.out.println("Item added to cart: " + item.getName());
    }

    // Method to remove an item from the cart
    public int getQuantityInCart(Item item) {
        int quantity = 0;
        for (Item cartItem : cart) {
            if (cartItem.getId().equals(item.getId())) {
                quantity++;
            }
        }
        return quantity;
    }

    public void removeItemFromCart(Item item, int quantityToRemove) {
        int removedCount = 0;
        Iterator<Item> iterator = cart.iterator();
        while (iterator.hasNext() && removedCount < quantityToRemove) {
            Item cartItem = iterator.next();
            if (cartItem.getId().equals(item.getId())) {
                iterator.remove();
                removedCount++;
            }
        }
        if (removedCount > 0) {
            System.out.println("Item removed from cart: " + item.getName());
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        Map<Item, Integer> itemCountMap = new HashMap<>();

        // Hitung jumlah setiap item di keranjang
        for (Item item : cart) {
            itemCountMap.put(item, itemCountMap.getOrDefault(item, 0) + 1);
        }

        // Tampilkan informasi item di keranjang
        System.out.println("Barang di Keranjang:");
        for (Map.Entry<Item, Integer> entry : itemCountMap.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            double price = item.getSellingPrice() * quantity;
            totalPrice += price;

            // Tampilkan item dan jumlahnya
            System.out.println("- " + item.getName() + ", Quantity: " + quantity + ", Harga: Rp." + price);
        }

        return totalPrice;
    }

    public List<Item> getCart() {
        return cart;
    }

    // Method to calculate change
    public double calculateChange(double payment) {
        double totalPrice = calculateTotalPrice();
        return payment - totalPrice;
    }

    public void printReceipt() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. No receipt generated.");
            return;
        }
    
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        System.out.println("=========== RECEIPT ===========");
        System.out.println("Date: " + formatter.format(date));
        System.out.println("-------------------------------");
        System.out.println("Items purchased:");
    
        // Membuat map untuk menyimpan jumlah setiap item yang dibeli
        Map<String, Integer> itemQuantityMap = new HashMap<>();
        double totalPrice = 0; // Total harga keseluruhan
        int totalQuantity = 0; // Total jumlah barang yang dibeli
        for (Item item : cart) {
            // Menambahkan jumlah barang dan total harga
            totalPrice += item.getSellingPrice();
            totalQuantity++;
    
            // Mengupdate jumlah setiap item
            itemQuantityMap.put(item.getName(), itemQuantityMap.getOrDefault(item.getName(), 0) + 1);
        }
    
        // Menampilkan setiap item dan jumlahnya
        for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " x Rp." + getPriceOfItem(entry.getKey()));
        }
    
        System.out.println("-------------------------------");
        System.out.println("Total Quantity: " + totalQuantity); // Tampilkan jumlah total barang yang dibeli
        System.out.println("Total Amount: Rp." + totalPrice); // Tampilkan total harga
        System.out.println("===============================");
    
        // Save sales report to CSV file
        saveSalesReportToCSV(formatter.format(date), itemQuantityMap);
    }

    private double getPriceOfItem(String itemName) {
        double price = 0;
        for (Item item : cart) {
            if (item.getName().equals(itemName)) {
                price = item.getSellingPrice();
                break;
            }
        }
        return price;
    }

    private void saveSalesReportToCSV(String date, Map<String, Integer> itemQuantityMap) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            for (Map.Entry<String, Integer> entry : itemQuantityMap.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                double price = getPriceOfItem(itemName);
                double total = price * quantity;
                writer.println(date + "," + quantity + "," + total + "," + itemName + ": " + price + ";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateSalesReport() {
        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            System.out.println("=========== SALES REPORT ===========");
            System.out.println("Date\t\tQuantity\tAmount\t\tItem");
            System.out.println("------------------------------------");
            String prevDate = "";
            String prevItem = "";
            int totalQuantity = 0;
            double totalAmount = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String date = parts[0];
                int quantity = Integer.parseInt(parts[1]);
                double amount = Double.parseDouble(parts[2]);
                String itemName = parts[3];
                
                if (!date.equals(prevDate) || !itemName.equals(prevItem)) {
                    if (!prevDate.equals("")) {
                        if (prevItem.contains(":")) {
                            System.out.printf("%s\t%d\t\tRp.%.2f\t%s\n", prevDate, totalQuantity, totalAmount, prevItem);
                        } else {
                            System.out.printf("%s\t%d\t\tRp.%.2f\t%s%.1f;\n", prevDate, totalQuantity, totalAmount, prevItem, totalAmount / totalQuantity);
                        }
                    }
                    prevDate = date;
                    prevItem = itemName;
                    totalQuantity = 0;
                    totalAmount = 0;
                }
                totalQuantity += quantity;
                totalAmount += amount;
            }
            if (!prevDate.equals("")) {
                if (prevItem.contains(":")) {
                    System.out.printf("%s\t%d\t\tRp.%.2f\t%s\n", prevDate, totalQuantity, totalAmount, prevItem);
                } else {
                    System.out.printf("%s\t%d\t\tRp.%.2f\t%s%.1f;\n", prevDate, totalQuantity, totalAmount, prevItem, totalAmount / totalQuantity);
                }
            }
            System.out.println("=====================================");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILE_NAME);
        }
    }
    
}