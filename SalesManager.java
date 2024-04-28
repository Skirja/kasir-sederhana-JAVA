import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SalesManager class handles the shopping cart and related operations.
 */
public class SalesManager {
    private List<Item> cart;
    private static final String FILE_NAME = "Laporan.csv";

    // Constructor
    public SalesManager() {
        this.cart = new ArrayList<>();
    }

    // ... Rest of the methods ...

    /**
     * Generates the sales report from the CSV file.
     */
    public void generateSalesReport() {
        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            System.out.println("=============================== SALES REPORT =======================================");
            System.out.println("Date\t\t\tQuantity\tAmount\t\tMargin\t\tItem");
            System.out.println("------------------------------------------------------------------------------------");

            SalesReportData prevData = null;
            int totalQuantity = 0;
            double totalAmount = 0;
            double totalMargin = 0;

            while (scanner.hasNextLine()) {
                SalesReportData data = parseSalesReportLine(scanner.nextLine());
                if (prevData == null || !prevData.date.equals(data.date) || !prevData.itemName.equals(data.itemName)) {
                    printSalesReportData(prevData, totalQuantity, totalAmount, totalMargin);
                    prevData = data;
                    totalQuantity = 0;
                    totalAmount = 0;
                    totalMargin = 0;
                }
                totalQuantity += data.quantity;
                totalAmount += data.amount;
                totalMargin += data.margin;
            }

            printSalesReportData(prevData, totalQuantity, totalAmount, totalMargin);
            System.out.println("====================================================================================");
            System.out.println("Total Margin Keuntungan: Rp." + totalMargin); // Menampilkan total margin keuntungan
            System.out.println("Total Pendapatan: Rp." + totalAmount); // Menampilkan total pendapatan
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + FILE_NAME);
        }
    }

    /**
     * Parses a line from the CSV file into SalesReportData.
     *
     * @param line The line to parse.
     * @return SalesReportData object.
     */
    private SalesReportData parseSalesReportLine(String line) {
        String[] parts = line.split(",");
        String date = parts[0];
        int quantity = Integer.parseInt(parts[1]);
        double amount = Double.parseDouble(parts[2]);
        double margin = Double.parseDouble(parts[3]);
        String itemName = parts[4];

        return new SalesReportData(date, quantity, amount, margin, itemName);
    }

    /**
     * Prints the sales report data for a specific time period and item.
     *
     * @param data     SalesReportData object.
     * @param quantity Total quantity for the time period.
     * @param amount   Total amount for the time period.
     * @param margin   Total margin for the time period.
     */
    private void printSalesReportData(SalesReportData data, int quantity, double amount, double margin) {
        if (data != null) {
            System.out.printf("%s\t\s\s\s%d\t\tRp.%.2f\tRp.%.2f\t%s\n", data.date, data.quantity, data.amount, data.margin, data.itemName);
        }
    }

    /**
     * SalesReportData class holds the data for a single line in the sales report.
     */
    private static class SalesReportData {
        String date;
        int quantity;
        double amount;
        double margin;
        String itemName;

        public SalesReportData(String date, int quantity, double amount, double margin, String itemName) {
            this.date = date;
            this.quantity = quantity;
            this.amount = amount;
            this.margin = margin;
            this.itemName = itemName;
        }
    }
}
