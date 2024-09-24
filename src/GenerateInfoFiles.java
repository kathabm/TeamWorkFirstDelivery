import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * The {@code GenerateInfoFiles} class is responsible for generating test files,
 * including files with vendor information, product details, and sales data.
 * 
 * <p>This class provides methods to create vendor and product information files,
 * as well as generate sales files for each vendor.</p>
 * 
 * <p>A directory named "files" is used to store these files. The directory is cleaned
 * at the beginning of execution to ensure new files are generated.</p>
 */
public class GenerateInfoFiles {
    
    /** Array of possible vendor names. */
    private static final String[] NAMES = { "Juan", "Maria", "Carlos", "Ana", "Luis", "Isabel" };

    /** Array of possible vendor last names. */
    private static final String[] LAST_NAMES = { "Perez", "Gomez", "Rodriguez", "Martinez", "Fernandez", "Lopez" };

    /** Array of possible product names. */
    private static final String[] PRODUCT_NAMES = { "Laptop", "Smartphone", "Tablet", "Headphones", "Monitor",
            "Speaker", "Keyboard", "Camera", "Printer", "Mouse" };

    /** Path to the vendor information file. */
    private static final String SALES_MEN_FILE = "files/salesmen_info.csv";

    /** Path to the product information file. */
    private static final String PRODUCTS_FILE = "files/products.csv";

    /** Prefix for sales information files. */
    private static final String SALES_INFO_FILE = "files/sales_";


    /**
     * Generates all necessary files:
     * <ul>
     *     <li>Deletes existing files in the "files" directory.</li>
     *     <li>Creates a file with vendor information.</li>
     *     <li>Creates a file with product information.</li>
     *     <li>Generates sales files for each vendor.</li>
     *     <li>Creates vendor and product sales reports.</li>
     * </ul>
     * 
     * @throws IOException If an error occurs while writing or deleting files.
     */
    public static void generateInfoFiles() throws IOException {
        Random random = new Random();
        int qtySellers = random.nextInt(10) + 1; // Random number of sellers between 1 and 10
        int qtyProducts = PRODUCT_NAMES.length; // Set the quantity of products to the length of PRODUCT_NAMES

        createFilesDirectory(); // Create the directory if it does not exist
        deleteAllFilesInDirectory(); // Deletes all files in the "files" directory
        createSalesManInfoFile(qtySellers); // Creates the file with vendor information
        createProductsFile(qtyProducts); // Creates the file with product information
        createSalesMenFile(); // Creates files with sales data

    }

    /**
     * Creates the "files" directory if it does not already exist.
     */
    private static void createFilesDirectory() {
        File directory = new File("files");
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory
        }
    }

    /**
     * Creates a file with vendor information.
     * 
     * <p>The file contains one line per vendor with the following format:</p>
     * <ul>
     *     <li>Document type (CC);</li>
     *     <li>Document number;</li>
     *     <li>First name;</li>
     *     <li>Last name.</li>
     * </ul>
     * 
     * @param qtySellers Number of vendors to generate.
     * @throws IOException If an error occurs while writing the file.
     */
    public static void createSalesManInfoFile(int qtySellers) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_MEN_FILE))) {
            Random random = new Random();
            for (int i = 0; i < qtySellers; i++) {
                String name = NAMES[i];
                String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                writer.write("CC;" + (random.nextInt(90000) + 10000) + ";" + name + ";" + lastName + "\n");
            }
        }
    }

    /**
     * Creates a file with product information.
     * 
     * <p>The file contains one line per product with the following format:</p>
     * <ul>
     *     <li>Product ID;</li>
     *     <li>Product name;</li>
     *     <li>Product price.</li>
     * </ul>
     * 
     * @param qtyProducts Number of products to generate.
     * @throws IOException If an error occurs while writing the file.
     */
    public static void createProductsFile(int qtyProducts) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE))) {
            Random random = new Random();
            for (int i = 0; i < qtyProducts; i++) { // Changed to i < qtyProducts
                String product = PRODUCT_NAMES[i];
                writer.write(i + 1 + ";" + product + ";" + (random.nextInt(900000) + 2000000) + "\n");
            }
        }
    }

    /**
     * Creates sales files for each vendor.
     * 
     * <p>Each file contains the sales information for a specific vendor with the following format:</p>
     * <ul>
     *     <li>Document type;</li>
     *     <li>Document number.</li>
     * </ul>
     * <p>Followed by one line per product sold, with the following format:</p>
     * <ul>
     *     <li>Product ID;</li>
     *     <li>Quantity sold.</li>
     * </ul>
     * 
     * @throws IOException If an error occurs while writing the file.
     */
    private static void createSalesMenFile() throws IOException {
        Random random = new Random();
        List<String> salesMenLines = Files.readAllLines(Paths.get(SALES_MEN_FILE));
        List<String> productsLines = Files.readAllLines(Paths.get(PRODUCTS_FILE));

        for (String salesManLine : salesMenLines) {
            String[] salesManParts = salesManLine.split(";");
            String documentType = salesManParts[0];
            String documentNumber = salesManParts[1];
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(SALES_INFO_FILE + documentNumber + ".csv"))) {
                writer.write(documentType + ";" + documentNumber + "\n");

                for (String productLine : productsLines) {
                    String[] productParts = productLine.split(";");
                    String productId = productParts[0];
                    int quantitySold = random.nextInt(10); // Random quantity between 0 and 9
                    writer.write(productId + ";" + quantitySold + "\n");
                }
            }
        }
    }

    /**
     * Deletes all files in the "files" directory.
     * 
     * @throws IOException If the directory does not exist, is not a directory, or an error occurs accessing files.
     */
    private static void deleteAllFilesInDirectory() throws IOException {
        File directory = new File("files");

        if (!directory.exists()) {
            throw new IOException("The directory does not exist.");
        }
        if (!directory.isDirectory()) {
            throw new IOException("The specified path is not a directory.");
        }

        File[] files = directory.listFiles();
        
        if (files == null) {
            throw new IOException("Error accessing the directory's files or the directory is empty.");
        }

        for (File file : files) {
            file.delete();
        }
    }
    
}