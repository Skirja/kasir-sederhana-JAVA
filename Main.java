import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Authentication auth = new Authentication();
        InventoryManager inventoryManager = new InventoryManager();
        SalesManager salesManager = new SalesManager();
        
        String role = null;
        int loginAttempts = 0;

        // Main loop
        while (true) {
            if (loginAttempts >= 3) { // Batas maksimum percobaan login
                System.out.println("Batas maksimum percobaan login telah tercapai. Aplikasi akan keluar.");
                System.exit(0); // Keluar dari program jika batas login tercapai
            }

            System.out.println("\nSelamat datang di Aplikasi Kasir Minimarket");
            System.out.print("Masukkan username: ");
            String username = scanner.nextLine();
            System.out.print("Masukkan password: ");
            String password = scanner.nextLine();
    
            // Authentication
            role = auth.authenticate(username, password);
    
            if (role != null) {
                System.out.println("Login berhasil sebagai " + role);
                break; // Keluar dari loop setelah berhasil login
            } else {
                loginAttempts++; // Tambahkan jumlah percobaan login
                System.out.println("Username atau password salah. Silakan coba lagi.");
                System.out.println("Sisa percobaan login: " + (3 - loginAttempts)); // Tampilkan sisa percobaan login
            }
        }
    
        // Main menu based on user role
        switch (role) {
            case "OWNER":
            case "MANAGER":
                managerMenu(scanner, inventoryManager, salesManager);
                break;
            case "KASIR":
                cashierMenu(scanner, salesManager, inventoryManager);
                break;
            default:
                System.out.println("Peran tidak valid.");
        }
    
        scanner.close();
    }        

    // Manager menu
    private static void managerMenu(Scanner scanner, InventoryManager inventoryManager, SalesManager salesManager) {
        int choice;
        do {
            System.out.println("\n===== MANAGER MENU =====");
            System.out.println("1. Tambah Barang");
            System.out.println("2. Hapus Barang");
            System.out.println("3. Update Barang");
            System.out.println("4. Laporan Penjualan");
            System.out.println("5. Laporan Stok");
            System.out.println("6. Logout");
            System.out.print("Pilihan Anda: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    // Tambah Barang
                    addItemMenu(scanner, inventoryManager);
                    break;
                case 2:
                    // Hapus Barang
                    removeItemMenu(scanner, inventoryManager);
                    break;
                case 3:
                    // Update Barang
                    updateItemMenu(scanner, inventoryManager);
                    break;
                case 4:
                    // Laporan Penjualan
                    generateSalesReportMenu(salesManager);
                    break;
                case 5:
                    // Laporan Stok
                    generateStockReportMenu(inventoryManager);
                    break;
                case 6:
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 6);
    }

    // Manager menu options
    private static void addItemMenu(Scanner scanner, InventoryManager inventoryManager) {
        // Implementasi untuk menambahkan barang ke inventaris
        System.out.println("Menu: Tambah Barang");
        System.out.print("Masukkan ID barang: ");
        String id = scanner.nextLine();
        System.out.print("Masukkan nama barang: ");
        String name = scanner.nextLine();
        System.out.print("Masukkan harga beli: ");
        double purchasePrice = scanner.nextDouble();
        System.out.print("Masukkan harga jual: ");
        double sellingPrice = scanner.nextDouble();
        System.out.print("Masukkan stok: ");
        int stock = scanner.nextInt();
        System.out.print("Masukkan batas minimum stok: ");
        int minStock = scanner.nextInt();
        inventoryManager.addItem(new Item(id, name, purchasePrice, sellingPrice, stock, minStock));
    }
    
    private static void removeItemMenu(Scanner scanner, InventoryManager inventoryManager) {
        // Implementasi untuk menghapus barang dari inventaris
        System.out.println("Menu: Hapus Barang");
        System.out.print("Masukkan ID barang yang akan dihapus: ");
        String itemId = scanner.nextLine();
        inventoryManager.removeItem(itemId);
    }
    
    private static void updateItemMenu(Scanner scanner, InventoryManager inventoryManager) {
        // Implementasi untuk mengupdate detail barang
        System.out.println("Menu: Update Barang");
        System.out.print("Masukkan ID barang yang akan diupdate: ");
        String itemId = scanner.nextLine();
        Item itemToUpdate = inventoryManager.searchItem(itemId);
        if (itemToUpdate != null) {
            System.out.print("Masukkan nama barang baru: ");
            String name = scanner.nextLine();
            System.out.print("Masukkan harga beli baru: ");
            double purchasePrice = scanner.nextDouble();
            System.out.print("Masukkan harga jual baru: ");
            double sellingPrice = scanner.nextDouble();
            System.out.print("Masukkan stok baru: ");
            int stock = scanner.nextInt();
            System.out.print("Masukkan batas minimum stok baru: ");
            int minStock = scanner.nextInt();
            inventoryManager.updateItem(itemId, name, purchasePrice, sellingPrice, stock, minStock);
        } else {
            System.out.println("Barang tidak ditemukan.");
        }
    }
    
    private static void generateSalesReportMenu(SalesManager salesManager) {
        // Implementasi untuk menampilkan laporan penjualan dalam bentuk tabel
        System.out.println("Menu: Laporan Penjualan");
        salesManager.generateSalesReport();
    }
    
    private static void generateStockReportMenu(InventoryManager inventoryManager) {
        // Implementasi untuk menampilkan laporan stok barang dari file CSV
        System.out.println("Menu: Laporan Stok");
        inventoryManager.generateStockReport();
    }

    // Cashier menu
    private static void cashierMenu(Scanner scanner, SalesManager salesManager, InventoryManager inventoryManager) {
        int choice;
        do {
            System.out.println("\n===== CASHIER MENU =====");
            System.out.println("1. Tambah Barang ke Keranjang");
            System.out.println("2. Hapus Barang dari Keranjang");
            System.out.println("3. Hitung Total Harga");
            System.out.println("4. Hitung Kembalian");
            System.out.println("5. Cetak Struk");
            System.out.println("6. Logout");
            System.out.print("Pilihan Anda: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    // Tambah Barang ke Keranjang
                    addItemToCartMenu(scanner, salesManager, inventoryManager);
                    break;
                case 2:
                    // Hapus Barang dari Keranjang
                    removeItemFromCartMenu(scanner, salesManager, inventoryManager);
                    break;
                case 3:
                    // Hitung Total Harga
                    calculateTotalPriceMenu(salesManager);
                    break;
                case 4:
                    // Hitung Kembalian
                    calculateChangeMenu(scanner, salesManager);
                    break;
                case 5:
                    // Cetak Struk
                    printReceiptMenu(salesManager);
                    break;
                case 6:
                    System.out.println("Logout berhasil.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 6);
    }

    private static void addItemToCartMenu(Scanner scanner, SalesManager salesManager, InventoryManager inventoryManager) {
        // Implementasi untuk menambahkan barang ke keranjang saat transaksi
        System.out.println("Menu: Tambah Barang ke Keranjang");
    
        // Masukkan ID barang
        System.out.print("Masukkan ID barang: ");
        String itemId = scanner.nextLine();
    
        // Cari barang berdasarkan ID
        Item item = inventoryManager.searchItem(itemId);
        if (item != null) {
            // Barang ditemukan, tampilkan informasi barang

            System.out.println("=================================");
            System.out.println("---Informasi Barang---");
            System.out.println("Nama: " + item.getName());
            System.out.println("Stok: " + item.getStock());
            System.out.println("=================================");
    
            // Masukkan jumlah yang akan dibeli
            System.out.print("Masukkan jumlah yang akan dibeli: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // consume newline
    
            // Cek ketersediaan stok
            if (quantity <= 0) {
                System.out.println("Jumlah yang dimasukkan harus lebih besar dari 0.");
                return;
            }
    
            if (quantity > item.getStock()) {
                System.out.println("Stok tidak mencukupi.");
                return;
            }
    
            // Barang ditambahkan ke keranjang
            salesManager.addItemToCart(item, quantity);
    
            // Update stok barang
            item.setStock(item.getStock() - quantity);
            inventoryManager.updateItem(item.getId(), item.getName(), item.getPurchasePrice(), item.getSellingPrice(), item.getStock(), item.getMinStock());
            System.out.println("Stok barang telah berhasil diperbarui.");
        } else {
            System.out.println("Barang dengan ID tersebut tidak ditemukan.");
        }
    }

    private static void removeItemFromCartMenu(Scanner scanner, SalesManager salesManager, InventoryManager inventoryManager) {
        // Implementasi untuk menghapus barang dari keranjang
        System.out.println("Menu: Hapus Barang dari Keranjang");
        System.out.print("Masukkan ID barang yang akan dihapus dari keranjang: ");
        String itemId = scanner.nextLine();
        Item item = inventoryManager.searchItem(itemId);
        if (item != null) {
            // Tampilkan informasi barang yang akan dihapus
            System.out.println("Informasi Barang yang akan dihapus:");
            System.out.println("Nama: " + item.getName());
            System.out.println("Sisa Barang: " + salesManager.getQuantityInCart(item));
            System.out.print("Masukkan jumlah barang yang akan dihapus: ");
            int quantityToRemove = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (quantityToRemove <= 0) {
                System.out.println("Jumlah yang dimasukkan harus lebih besar dari 0.");
                return;
            }
            if (quantityToRemove > salesManager.getQuantityInCart(item)) {
                System.out.println("Jumlah barang yang dimasukkan melebihi jumlah barang di keranjang.");
                return;
            }
            // Hapus barang dari keranjang
            salesManager.removeItemFromCart(item, quantityToRemove);
            // Tambahkan kembali stok barang yang dihapus dari keranjang
            item.setStock(item.getStock() + quantityToRemove);
            inventoryManager.updateItem(item.getId(), item.getName(), item.getPurchasePrice(), item.getSellingPrice(), item.getStock(), item.getMinStock());
            System.out.println("Stok barang telah berhasil diperbarui.");
        } else {
            System.out.println("Barang dengan ID tersebut tidak ditemukan.");
        }
    }
    
    private static void calculateTotalPriceMenu(SalesManager salesManager) {
        // Implementasi untuk menghitung total harga
        System.out.println("Menu: Hitung Total Harga");
        double totalPrice = salesManager.calculateTotalPrice();
        System.out.println("Total harga: Rp." + totalPrice);
    }
    
    

    private static void calculateChangeMenu(Scanner scanner, SalesManager salesManager) {
        // Implementasi untuk menghitung kembalian
        System.out.println("Menu: Hitung Kembalian");
        System.out.print("Masukkan jumlah uang pembayaran: Rp.");
        double payment = scanner.nextDouble();
        double change = salesManager.calculateChange(payment);
        if (change < 0) {
            System.out.println("Jumlah uang pembayaran tidak mencukupi.");
        } else {
            System.out.println("Kembalian: Rp." + change);
        }
    }

    private static void printReceiptMenu(SalesManager salesManager) {
        // Implementasi untuk mencetak struk
        System.out.println("Menu: Cetak Struk");
        salesManager.printReceipt();
    }

}
