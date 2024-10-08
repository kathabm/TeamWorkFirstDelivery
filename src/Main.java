import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The {@code Main} class contains the main entry point of the program.
 * 
 * <p>It is responsible for invoking the {@code generateInfoFiles} method of the {@code GenerateInfoFiles}
 * class to create the necessary test files and generate sales reports.</p>
 */
public class Main {

    /** Path to the vendor information file. */
    private static final String SALES_MEN_FILE = "files/salesmen_info.csv";

    /** Path to the product information file. */
    private static final String PRODUCTS_FILE = "files/products.csv";

    /** Prefix for sales information files. */
    private static final String SALES_INFO_FILE = "files/sales_";

    /** Path for the product sales report file. */
    private static final String PRODUCT_SALES_REPORT_FILE = "files/product_sales.csv";

    /** Path for the vendor sales report file. */
    private static final String VENDOR_SALES_REPORT_FILE = "files/vendor_sales.csv";

    /**
     * Main method that executes the generation of test files and reports.
     * 
     * <p>Handles any {@code IOException} that may occur during the process of generating files.</p>
     */
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Menu:");
            System.out.println("1. Generate Info Files");
            System.out.println("2. Generate Reports");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    try {
                        GenerateInfoFiles.generateInfoFiles();
                        System.out.println("Files generated successfully.");
                    } catch (IOException e) {
                        System.out.println("Error generating files: " + e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        // Generate sales reports after creating necessary files
                        createVendorSalesReport(); // Creates a report of vendor sales
                        createProductSalesReport(); // Creates a report of product sales
                        System.out.println("Reports generated successfully.");
                    } catch (IOException e) {
                        System.out.println("Error generating reports: " + e.getMessage());
                    }
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    /**
     * Creates a report of vendor sales, ordered by the amount of money collected.
     * 
     * @throws IOException If an error occurs while writing the report.
     */
    private static void createVendorSalesReport() throws IOException {
        Map<String, Integer> vendorSales = new HashMap<>();
        List<String> salesMenLines = Files.readAllLines(Paths.get(SALES_MEN_FILE));

        // Iterate through each salesman to calculate total sales
        for (String salesManLine : salesMenLines) {
            String[] salesManParts = salesManLine.split(";");
            String documentNumber = salesManParts[1];
            int totalSales = 0;

            // Read sales data for the current salesman
            List<String> salesLines = Files.readAllLines(Paths.get(SALES_INFO_FILE + documentNumber + ".csv"));
            for (String salesLine : salesLines.subList(1, salesLines.size())) { // Skip header
                String[] productParts = salesLine.split(";");
                String productId = productParts[0];
                int quantitySold = Integer.parseInt(productParts[1]);
                int productPrice = getProductPrice(productId);
                totalSales += quantitySold * productPrice;
            }
            vendorSales.put(salesManParts[2] + " " + salesManParts[3], totalSales); // Full name
        }

        // Sort the sales by total amount collected
        List<Map.Entry<String, Integer>> sortedSales = new ArrayList<>(vendorSales.entrySet());
        sortedSales.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()));

        // Write the vendor sales report to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VENDOR_SALES_REPORT_FILE))) {
            for (Map.Entry<String, Integer> entry : sortedSales) {
                writer.write(entry.getKey() + ";" + entry.getValue() + "\n");
            }
        }
    }

    /**
     * Creates a report of product sales, ordered by the quantity sold.
     * 
     * @throws IOException If an error occurs while writing the report.
     */
    private static void createProductSalesReport() throws IOException {
        Map<String, Integer> productSales = new HashMap<>();
        List<String> productsLines = Files.readAllLines(Paths.get(PRODUCTS_FILE));

        // Initialize sales count for each product
        for (String productLine : productsLines) {
            String[] productParts = productLine.split(";");
            productSales.put(productParts[0], 0); // Initialize sales count
        }

        List<String> salesMenLines = Files.readAllLines(Paths.get(SALES_MEN_FILE));
        // Calculate total quantity sold for each product
        for (String salesManLine : salesMenLines) {
            String documentNumber = salesManLine.split(";")[1];
            List<String> salesLines = Files.readAllLines(Paths.get(SALES_INFO_FILE + documentNumber + ".csv"));
            for (String salesLine : salesLines.subList(1, salesLines.size())) { // Skip header
                String[] productParts = salesLine.split(";");
                int quantitySold = Integer.parseInt(productParts[1]);
                productSales.put(productParts[0], productSales.get(productParts[0]) + quantitySold);
            }
        }

        // Sort the products by quantity sold
        List<Map.Entry<String, Integer>> sortedProducts = new ArrayList<>(productSales.entrySet());
        sortedProducts.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        // Write the product sales report to a file, including product names
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_SALES_REPORT_FILE))) {
            for (Map.Entry<String, Integer> entry : sortedProducts) {
                String productId = entry.getKey();
                int price = getProductPrice(productId);
                String productName = getProductName(productId);
                writer.write(productName + ";" + price + ";" + entry.getValue() + "\n"); // Write name instead of ID
            }
        }
    }

    /**
     * Helper method to get the product price by product ID.
     * 
     * @param productId The ID of the product.
     * @return The price of the product.
     * @throws IOException If an error occurs while reading the product information.
     */
    private static int getProductPrice(String productId) throws IOException {
        List<String> productsLines = Files.readAllLines(Paths.get(PRODUCTS_FILE));
        for (String productLine : productsLines) {
            String[] productParts = productLine.split(";");
            if (productParts[0].equals(productId)) {
                return Integer.parseInt(productParts[2]);
            }
        }
        return 0;
    }

    /**
     * Helper method to get the product name by product ID.
     * 
     * @param productId The ID of the product.
     * @return The name of the product.
     * @throws IOException If an error occurs while reading the product information.
     */
    private static String getProductName(String productId) throws IOException {
        List<String> productsLines = Files.readAllLines(Paths.get(PRODUCTS_FILE));
        for (String productLine : productsLines) {
            String[] productParts = productLine.split(";");
            if (productParts[0].equals(productId)) {
                return productParts[1]; // Return the product name
            }
        }
        return "Unknown Product"; // Fallback if product not found
    }
}
