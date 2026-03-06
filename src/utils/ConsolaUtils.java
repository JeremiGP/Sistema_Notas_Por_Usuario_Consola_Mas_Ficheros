package utils;

// Importamos la clase Scanner para poder leer datos de la consola
import java.util.Scanner;

/**
 * Clase que representa la utilidad de la consola
 */
public class ConsolaUtils {
    // Creamos un objeto Scanner para poder leer datos de la consola
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Método que lee un texto de la consola
     * 
     * @param mensaje Mensaje a mostrar en la consola
     * @return Texto leído de la consola
     */
    public static String leerTexto(String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("  [!] El campo no puede estar vacío. Inténtalo de nuevo.");
            }
        }
        return texto;
    }

    /**
     * Método que lee un texto multilinea de la consola
     * 
     * @param mensaje Mensaje a mostrar en la consola
     * @return Texto leído de la consola
     */
    public static String leerTextoMultilinea(String mensaje) {
        System.out.println(mensaje);
        System.out.println("  (Nota: Escribe 'FIN' en una nueva línea para terminar y guardar)");
        System.out.println("  -------------------------------------------");
        StringBuilder sb = new StringBuilder();
        while (true) {
            System.out.print("  | "); // Guía visual para el bloque de texto
            String linea = sc.nextLine();
            if (linea.trim().equalsIgnoreCase("FIN")) {
                break;
            }
            sb.append(linea).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    /**
     * Método que lee un entero de la consola
     * 
     * @param mensaje Mensaje a mostrar en la consola
     * @return Entero leído de la consola
     */
    public static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        try {
            int numero = sc.nextInt();
            sc.nextLine();
            return numero;
        } catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }
}