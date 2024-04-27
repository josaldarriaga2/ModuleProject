package paquete;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    // Método principal para realizar las tareas de creación de los archivos de reportes
    public static void main(String[] args) {
        // Generar los archivos de ventas para cada vendedor y el archivo de reporte de productos
        boolean ventasGenerated = generateVentasFiles("vendedores.txt");
        boolean reporteGenerated = generateReporteProductos("vendedores.txt");
        
        // Mostrar mensaje de finalización exitosa o mensaje de error
        if (ventasGenerated && reporteGenerated) {
            System.out.println("Archivos de ventas y reporte de productos generados exitosamente.");
        } else {
            System.out.println("Error al generar los archivos de ventas y reporte de productos.");
        }
    }

    // Método para generar los archivos de ventas para cada vendedor
    private static boolean generateVentasFiles(String vendedoresFile) {
        try {
            FileReader fileReader = new FileReader(vendedoresFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                String tipoDocumento = parts[0];
                long numeroDocumento = Long.parseLong(parts[1]);
                String nombres = parts[2];
                // Generar archivo de ventas para el vendedor
                generateVentasFile(tipoDocumento, numeroDocumento, nombres);
            }
            bufferedReader.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para generar el archivo de ventas para un vendedor específico
    private static void generateVentasFile(String tipoDocumento, long numeroDocumento, String nombres) {
        try {
            FileWriter writer = new FileWriter(tipoDocumento + "_" + numeroDocumento + "_Ventas.txt");
            Random random = new Random();
            // Generar información de ventas para el vendedor
            for (int i = 1; i <= 6; i++) { // Seleccionar aleatoriamente 6 productos
                int productoID = random.nextInt(20) + 1; // ID de producto entre 1 y 20
                int cantidad = random.nextInt(10) + 1; // Cantidad entre 1 y 10
                // Escribir en el archivo
                writer.write(productoID + ";" + cantidad + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para generar el archivo de reporte de productos
    private static boolean generateReporteProductos(String vendedoresFile) {
        try {
            FileReader fileReader = new FileReader(vendedoresFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Map<Integer, Integer> cantidadPorProducto = new HashMap<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                String ventasFilename = parts[0] + "_" + parts[1] + "_Ventas.txt";
                FileReader ventasFileReader = new FileReader(ventasFilename);
                BufferedReader ventasBufferedReader = new BufferedReader(ventasFileReader);
                String ventaLine;
                while ((ventaLine = ventasBufferedReader.readLine()) != null) {
                    String[] ventaParts = ventaLine.split(";");
                    int productoID = Integer.parseInt(ventaParts[0]);
                    int cantidad = Integer.parseInt(ventaParts[1]);
                    // Actualizar la cantidad total del producto
                    cantidadPorProducto.put(productoID, cantidadPorProducto.getOrDefault(productoID, 0) + cantidad);
                }
                ventasBufferedReader.close();
            }
            bufferedReader.close();

            // Generar el archivo de reporte de productos
            FileWriter writer = new FileWriter("reporte_productos.txt");
            for (Map.Entry<Integer, Integer> entry : cantidadPorProducto.entrySet()) {
                int productoID = entry.getKey();
                int cantidadTotal = entry.getValue();
                // Escribir en el archivo
                writer.write(productoID + ";" + cantidadTotal + "\n");
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
