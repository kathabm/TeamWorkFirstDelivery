import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.List;
import java.io.File;

/**
 * The {@code GenerateInfoFiles} class is responsible for generating test files,
 * including files with seller information, product details, and sales data.
 * 
 * <p>
 * This class provides methods to create seller and product information files,
 * as well as generate sales files for each seller.
 * </p>
 * 
 * <p>
 * A directory named "files" is used to store these files. The directory is
 * cleaned at the beginning of execution to ensure new files are generated.
 * </p>
 */
public class GenerateInfoFiles {

	/** Array of possible vendor names. */
	private static final String[] NAMES = { "Juan", "Maria", "Carlos", "Ana", "Luis", "Isabel" };

	/** Array of possible vendor last names. */
	private static final String[] LAST_NAMES = { "Perez", "Gomez", "Rodriguez", "Martinez", "Fernandez", "Lopez" };

	/** Array of possible product names. */
	private static final String[] PRODUCT_NAMES = { "Laptop", "Smartphone", "Tablet", "Headphones", "Monitor",
			"Speaker", "Keyboard", "Camera", "Printer", "Mouse" };

	/** Path to the seller information file. */
	private static final String SALES_MEN_FILE = "files/salesmen_info.csv";

	/** Path to the product information file. */
	private static final String PRODUCTS_FILE = "files/products.csv";

	/** Prefix for sales information files. */
	private static final String SALES_INFO_FILE = "files/sales_";

	/**
	 * Main method that executes the generation of test files.
	 * 
	 * <p>
	 * Handles any {@code IOException} that may occur during the process of
	 * generating files.
	 * </p>
	 * 
	 */
	public static void main(String[] args) {
		// Create an instance of Random to generate random numbers
		Random rand = new Random();
		// Generate a random number of sellers between 1 and 10
		int qtySellers = rand.nextInt(10) + 1;
		// Define the number of products
		int qtyProducts = 9;

		try {
			// Delete all files in the "files" directory
			deleteAllFilesInDirectory();
			// Create a file with information about the sellers
			createSalesManInfoFile(qtySellers);
			// Create a file with information about the products
			createProductsFile(qtyProducts);
			// Reads all lines from the file specified by SALES_MEN_FILE and stores them in
			// a List of Strings
			List<String> salesMenLines = Files.readAllLines(Paths.get(SALES_MEN_FILE));
			// Iterate over each line representing a seller
			for (String salesManLine : salesMenLines) {
				// Split the line into parts using ";" as the delimiter
				String[] salesManParts = salesManLine.split(";");
				// Parse the document number from the split parts
				long documentNumber = Long.parseLong(salesManParts[1]);
				// Extract the seller's name
				String name = salesManParts[2];
				// Define a random sales value
				int randomSales = 100;
				// Create a file with example sales data for the seller
				createSalesMenFile(randomSales, name, documentNumber);
			}
			// Print a success log
			System.out.println("Files generated successfully.");
		} catch (IOException e) {
			// Print an error log if file operations fail
			System.out.println("Error generating files: " + e.getMessage());
		}
	}

	/**
	 * Creates a csv file with sellers information.
	 * 
	 * <p> The file contains one line per seller in the following format: </p>
	 * <ul>
	 * <li>Document type (CC); Document number; First name; Last name.</li>
	 * </ul>
	 * 
	 * @param salesmanCount Number of sellers to generate.
	 * @throws IOException If an error occurs while writing the file.
	 */
	public static void createSalesManInfoFile(int salesmanCount) throws IOException {
		Random rand = new Random();
		// Open a BufferedWriter to write the sellers information to the CSV file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_MEN_FILE))) {
			// Loop through each salesman to write their information to the file
			for (int i = 0; i < salesmanCount; i++) {
				// Retrieve the name from the NAMES array
				String name = NAMES[i];
				// Select a random last name from the LAST_NAMES array
				String lastName = LAST_NAMES[rand.nextInt(LAST_NAMES.length)];
				// Generate a random value for the id
				long id = rand.nextInt(100000000) + 1000000000;
				// Write the sellers information to the CSV file
				writer.write("CC;" + id + ";" + name + ";" + lastName + "\n");
			}
		}
	}

	/**
	 * Creates a csv file with product information.
	 * 
	 * <p>The file contains one line per product with the following format: </p>
	 * <ul>
	 * <li>Product ID; Product name; Product price.</li>
	 * </ul>
	 * 
	 * @param productsCount Number of products to generate.
	 * @throws IOException If an error occurs while writing the file.
	 */
	public static void createProductsFile(int productsCount) throws IOException {
		Random rand = new Random();
		// Open a BufferedWriter to write the product information to the CSV file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE))) {
			// Loop through each product to write their information to the file
			for (int i = 0; i <= productsCount; i++) {
				// Generate a random value for the product
				int productValue = rand.nextInt(900000) + 2000000;
				// Retrieve the product from the PRODUCT_NAMES array
				String product = PRODUCT_NAMES[i];
				// Write the product information to the CSV file
				writer.write(i + 1 + ";" + product + ";" + productValue + "\n");
			}
		}
	}

	/**
	 * Creates csv sales files for each seller.
	 * 
	 * <p> Each file contains the sales information for a specific seller with the following format: </p>
	 * <ul>
	 * <li>Document type; Document number.</li>
	 * </ul>
	 * <p> Followed by one line per product sold, with the following format: </p>
	 * <ul>
	 * <li>Product ID; Quantity sold.</li>
	 * </ul>
	 * 
	 * @param randomSalesCount The maximum value (exclusive) for the random quantity of products sold.
	 * @param name             The name of the salesman, used in the filename.
	 * @param id               The unique ID of the salesman, used in the filename and for identification.
	 * @throws IOException If an error occurs while writing the file.
	 */
	private static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
		Random random = new Random();
		// Read all lines from the products file
		List<String> productsLines = Files.readAllLines(Paths.get(PRODUCTS_FILE));
		// Construct the filename using the salesman's ID and name
		String fileName = SALES_INFO_FILE + id + "_" + name + ".csv";
		// Open a BufferedWriter to write the sales information to the CSV file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			// Write the salesman's ID as the first line of the CSV file
			writer.write("CC;" + id + "\n");
			// Iterate over each line from the products file
			for (String productLine : productsLines) {
				// Split the product line into parts
				String[] productParts = productLine.split(";");
				String productId = productParts[0];
				// Generate a random quantity sold for the product
				int quantitySold = random.nextInt(randomSalesCount);
				// Write the product ID and quantity sold to the CSV file
				writer.write(productId + ";" + quantitySold + "\n");
			}
		} 
	}

	/**
	 * Deletes all files in the "files" directory.
	 * 
	 * @throws IOException If the directory does not exist, is not a directory, or
	 *                     an error occurs accessing files.
	 */
	private static void deleteAllFilesInDirectory() throws IOException {
		File directory = new File("files");
		if (!directory.exists()) {
			throw new IOException("The directory does not exist.");
		}
		if (!directory.isDirectory()) {
			throw new IOException("The specified path is not a directory.");
		}
		// List all files in the directory
		File[] files = directory.listFiles();
		if (files == null) {
			throw new IOException("Error accessing the directory's files or the directory is empty.");
		}
		// Iterate over each file and delete it
		for (File file : files) {
			file.delete();
		}
	}
}
