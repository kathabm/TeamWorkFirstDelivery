import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.List;
import java.io.File;

public class GenerateInfoFiles {

	private static final String[] NAMES = { "Juan", "Maria", "Carlos", "Ana", "Luis", "Isabel" };
	private static final String[] LAST_NAMES = { "Perez", "Gomez", "Rodriguez", "Martinez", "Fernandez", "Lopez" };
	private static final String[] PRODUCT_NAMES = { "Laptop", "Smartphone", "Tablet", "Headphones", "Monitor",
			"Speaker", "Keyboard", "Camera", "Printer", "Mouse" };
	private static final String SALES_MEN_FILE = "files/salesmen_info.csv";
	private static final String PRODUCTS_FILE = "files/products.csv";
	private static final String SALES_INFO_FILE = "files/sales_";

	/**
	 * Genera archivos de prueba.
	 */
	public static void main(String[] args) {
		Random rand = new Random();
		int qtySellers = rand.nextInt(10) + 1; // Random quantity between 1 and 10
		int qtyProducts = 9;
		try {
			deleteAllFilesInDirectory(); // elimina todos los archivos en files
			createSalesManInfoFile(qtySellers); // Genera archivo con información de vendedores
			createProductsFile(qtyProducts); // Genera archivo con información de productos
			createSalesMenFile(); // Genera archivo con ventas de ejemplo
			System.out.println("Files generated successfully.");
		} catch (IOException e) {
			System.out.println("Error generating files: " + e.getMessage());
		}
	}

	/**
	 * Crea un archivo con información de vendedores.
	 *
	 * @param qtySellers Número de vendedores a generar.
	 * @throws IOException Si ocurre un error al escribir el archivo.
	 */
	public static void createSalesManInfoFile(int qtySellers) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_MEN_FILE));
		Random rand = new Random();
		for (int i = 0; i < qtySellers; i++) {
			String name = NAMES[i];
			String lastName = LAST_NAMES[rand.nextInt(LAST_NAMES.length)];
			writer.write("CC;" + (rand.nextInt(90000) + 10000) + ";" + name + ";" + lastName + "\n");
		}
		writer.close();
	}

	/**
	 * Crea un archivo con información pseudoaleatoria de productos.
	 *
	 * @param qtyProducts Número de productos a generar.
	 * @throws IOException Si ocurre un error al escribir el archivo.
	 */
	public static void createProductsFile(int qtyProducts) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE));
		Random rand = new Random();
		for (int i = 0; i <= qtyProducts; i++) {
			String product = PRODUCT_NAMES[i];
			writer.write(i + 1 + ";" + product + ";" + (rand.nextInt(900000) + 2000000) + "\n");
		}
		writer.close();
	}
	/**
	 * Crea un archivo de ventas de prueba para un vendedor.
	 *
	 * @throws IOException Si ocurre un error al escribir el archivo.
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
				writer.close(); // Separate file for each sales person
			}
		}
	}
	/**
     * Elimina todos los archivos en el directorio especificado.
     * 
     * @throws IOException si el directorio no existe o no es un directorio
     */
	private static void deleteAllFilesInDirectory() throws IOException {
        File directory = new File("files");

        // Verifica si el directorio existe y es realmente un directorio
        if (!directory.exists()) {
            throw new IOException("El directorio no existe.");
        }
        if (!directory.isDirectory()) {
            throw new IOException("La ruta especificada no es un directorio.");
        }

        // Obtiene la lista de archivos en el directorio
        File[] files = directory.listFiles();
        
        // Verifica si la lista de archivos es null (en caso de errores o directorio vacío)
        if (files == null) {
            throw new IOException("Error al acceder a los archivos del directorio o el directorio está vacío.");
        }

        // Elimina cada archivo en el directorio
        for (File file : files) {
            //  Elimina el archivo del directorio
            file.delete();
        }
    }
}
