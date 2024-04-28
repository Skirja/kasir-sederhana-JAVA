import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Authentication auth = new Authentication();
        InventoryManager inventoryManager = new InventoryManager();
        SalesManager salesManager = new SalesManager();

        String role = null;
        int loginAttempts = 0;

        while (true) {
            if (loginAttempts >= 3) {
                System.out.println("Maximum login attempts reached. Exiting application.");
                System.exit(0);
            }

            System.out.println("\nWelcome to the Mini Market Cashier Application");
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            role = auth.authenticate(username, password);

            if (role != null) {
                System.out.println("Login successful as " + role);
                break;
            } else {
                loginAttempts++;
                System.out.println("Invalid username or password. Please try again.");
                System.out.println("Remaining login attempts: " + (3 - loginAttempts));
            }
        }

        switch (role) {
            case "OWNER":
            case "MANAGER":
                managerMenu(scanner, inventoryManager, salesManager);
                break;
            case "CASHIER":
                cashierMenu(scanner, salesManager, inventoryManager);
                break;
            default:
                System.out.println("Invalid role.");
        }

        scanner.close();
    }

    private static int getIntInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer.");
            scanner.next();
            System.out.print(prompt);
        }
        return scanner.nextInt();
    }

    private static double getDoubleInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next();
            System.out.print(prompt);
        }
        return scanner.nextDouble();
    }

    private static void managerMenu(Scanner scanner, InventoryManager inventoryManager, SalesManager salesManager) {
        clearScreen();

        int choice;
        do {
            System.out.println("\n===== MANAGER MENU =====");
            System.out.println("1. Add Item");
            System.out.println("2. Remove Item");
            System.out.println("3. Update Item");
            System.out.println("4. Sales Report");
            System.out.println("5. Stock Report");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            choice = getIntInput(scanner, "");

            switch (choice) {
                case 1:
                    addItemMenu(scanner, inventoryManager);
                    break;
                case 2:
                    removeItemMenu(scanner, inventoryManager);
                    break;
                case 3:
                    updateItemMenu(scanner, inventoryManager);
                    break;
                case 4:
                    generateSalesReportMenu(salesManager);
                    break;
                case 5:
                    generateStockReportMenu(inventoryManager);
                    break;
                case 6:
                    System.out.println("Logout successful.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 6);
    }

    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void addItemMenu(Scanner scanner, InventoryManager inventoryManager) {
        clearScreen();

        System.out.println("Menu: Add Item");
        System.out.print("Enter item ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter purchase price: ");
        double purchasePrice = getDoubleInput(scanner, "");
        System.out.print("Enter selling price: ");
        double sellingPrice = getDoubleInput(scanner, "");
        System.out.print("Enter stock: ");
        int stock = getIntInput(scanner, "");
        System.out.print("Enter minimum stock: ");
        int minStock = getIntInput(scanner, "");

        inventoryManager.addItem(new Item(id, name, purchasePrice, sellingPrice, stock, minStock));
        System.out.println("Item added successfully.");
    }

    private static void removeItemMenu(Scanner scanner, InventoryManager inventoryManager) {
        clearScreen();

        System.out.println("Menu: Remove Item");
        System.out.print("Enter item ID: ");
        String itemId = scanner.nextLine();

        inventoryManager.removeItem(itemId);
        System.out.println("Item removed successfully.");
    }

    private static void updateItemMenu(Scanner scanner, InventoryManager inventoryManager) {
        clearScreen();

        System.out.println("Menu: Update Item");
        System.out.print("Enter item ID: ");
        String itemId = scanner.nextLine();

        Item itemToUpdate = inventoryManager.searchItem(itemId);
        if (itemToUpdate != null) {
            System.out.print("Enter new item name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new purchase price: ");
            double purchasePrice = getDoubleInput(scanner, "");
            System.out.print("Enter new selling price: ");
            double sellingPrice = getDoubleInput(scanner, "");
            System.out.print("Enter new stock: ");
            int stock = getIntInput(scanner, "");
            System.out.print("Enter new minimum stock: ");
            int minStock = getIntInput(scanner, "");

            inventoryManager.updateItem(itemId, name, purchasePrice, sellingPrice, stock, minStock);
            System.out.println("Item updated successfully.");
        } else {
            System.out.println("Item not found.");
        }
    }

    private static void generateSalesReportMenu(SalesManager salesManager) {
        clearScreen();

        System.out.println("Menu: Sales Report");
        salesManager.generateSalesReport();
    }

    private static void generateStockReportMenu(InventoryManager inventoryManager) {
        clearScreen();

        System.out.println("Menu: Stock Report");
        inventoryManager.generateStockReport();
    }

    private static void cashierMenu(Scanner scanner, SalesManager salesManager, InventoryManager inventoryManager) {
        clearScreen();

        int choice;
        do {
            System.out.println("\n===== CASHIER MENU =====");
            System.out.println("1. Add Item to Cart");
            System.out.println("2. Remove Item from Cart");
            System.out.println("3. Calculate Total Price");
            System.out.println("4. Calculate Change");
            System.out.println("5. Print Receipt");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            choice = getIntInput(scanner, "");

            switch (choice) {
                case 1:
                    addItemToCartMenu(scanner, salesManager, inventoryManager);
                    break;
                case 2:
                    removeItemFromCartMenu(scanner, salesManager, inventoryManager);
                    break;
                case 3:
                    calculateTotalPriceMenu(salesManager);
                    break;
                case 4:
                    calculateChangeMenu(scanner, salesManager);
                    break;
                case 5:
                    printReceiptMenu(salesManager);
                    break;
                case 6:
                    System.out.println("Logout successful.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 6);
    }

    private static void addItemToCartMenu(Scanner scanner, SalesManager salesManager, InventoryManager inventoryManager) {
        clearScreen();

        System.out.println("Menu: Add Item to Cart");
        System.out.print("Enter item ID: ");
        String itemId = scanner.nextLine();

        Item item = inventoryManager.searchItem(itemId);
        if (item != null) {
            System.out.println("Item details:");
            System.out.println("Name: " + item.getName());
            System.out.println("Stock: " + item.getStock());

            System.out.print("Enter quantity: ");
            int quantity = getIntInput(scanner, "");

            if (quantity <= 0) {
                System.out.println("Invalid quantity.");
                return;
            }

            if (quantity > item.getStock()) {
                System.out.println("Not enough stock.");
                return;
            }

            salesManager.addItemToCart(item, quantity);
            item.setStock(item.getStock() - quantity);
            inventoryManager.updateItem(item.getId(), item.getName(), item.getPurchasePrice(), item.getSellingPrice(), item.getStock(), item.getMinStock());
            System.out.println("Item added to cart successfully.");
        } else {
            System.out.println("Item not found.");
        }
    }

    private static void removeItemFromCartMenu(Scanner scanner, SalesManager salesManager, InventoryManager inventoryManager) {
        clearScreen();

        System.out.println("Menu: Remove Item from Cart");
        System.out.print("Enter item ID: ");
        String itemId = scanner.nextLine();

        Item item = inventoryManager.searchItem(itemId);
        if (item != null) {
            System.out.println("Item details:");
            System.out.println("Name: " + item.getName());
            System.out.println("Quantity in cart: " + salesManager.getQuantityInCart(item));

            System.out.print("Enter quantity to remove: ");
            int quantityToRemove = getIntInput(scanner, "");

            if (quantityToRemove <= 0) {
                System.out.println("Invalid quantity.");
                return;
            }

            if (quantityToRemove > salesManager.getQuantityInCart(item)) {
                System.out.println("Invalid quantity.");
                return;
            }

            salesManager.removeItemFromCart(item, quantityToRemove);
            item.setStock(item.getStock() + quantityToRemove);
            inventoryManager.updateItem(item.getId(), item.getName(), item.getPurchasePrice(), item.getSellingPrice(), item.getStock(), item.getMinStock());
            System.out.println("Item removed from cart successfully.");
        } else {
            System.out.println("Item not found.");
        }
    }

    private static void calculateTotalPriceMenu(SalesManager salesManager) {
        clearScreen();

        System.out.println("Menu: Calculate Total Price");
        double totalPrice = salesManager.calculateTotalPrice();
        System.out.println("Total price: " + totalPrice);
    }

    private static void calculateChangeMenu(Scanner scanner, SalesManager salesManager) {
        clearScreen();

        System.out.println("Menu: Calculate Change");
        System.out.print("Enter payment: ");
        double payment = getDoubleInput(scanner, "");

        double change = salesManager.calculateChange(payment);
        if (Double.isNaN(change)) {
            System.out.println("Invalid input.");
        } else if (change < 0) {
            System.out.println("Insufficient payment.");
        } else {
            System.out.println("Change: " + change);
        }
    }

    private static void printReceiptMenu(SalesManager salesManager) {
        clearScreen();

        System.out.println("Menu: Print Receipt");
        salesManager.printReceipt();
        salesManager.clearCart();
    }
}
