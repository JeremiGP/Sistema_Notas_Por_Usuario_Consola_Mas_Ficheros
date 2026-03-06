package utils;

import java.util.Scanner;

public class ConsolaUtils {
    private static final Scanner sc = new Scanner(System.in);

    public static String leerTexto(String mensaje) {
        String texto = "";
        while (texto.isEmpty()) {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("El campo no puede estar vacío.");
            }
        }
        return texto;
    }

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