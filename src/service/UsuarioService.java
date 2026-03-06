package service;

// Importamos las clases que vamos a usar
import model.Usuario;

// Importamos las clases de java.io para poder trabajar con ficheros
import java.io.BufferedWriter;
import java.io.IOException;

// Importamos las clases de java.nio.file para poder trabajar con ficheros
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
// Importamos las clases de java.util para poder trabajar con listas
import java.util.List;

/**
 * Clase que representa el servicio de usuarios
 */
public class UsuarioService {
    private static final Path CARPETA_DATA = Path.of("data");
    private static final Path FICHERO_USUARIOS = CARPETA_DATA.resolve("users.txt");
    public static final Path CARPETA_USUARIOS = CARPETA_DATA.resolve("usuarios");

    /**
     * Método que inicializa el sistema
     */
    public void inicializarSistema() {
        try {
            Files.createDirectories(CARPETA_USUARIOS);
            if (!Files.exists(FICHERO_USUARIOS)) {
                Files.createFile(FICHERO_USUARIOS);
            }
        } catch (IOException e) {
            System.out.println("Error inicializando sistema: " + e.getMessage());
        }
    }

    /**
     * Método que sanitiza el email
     * 
     * @param email Email a sanitizar
     * @return Email sanitizado
     */
    public String sanitizarEmail(String email) {
        return email.replace("@", "_").replace(".", "_");
    }

    /**
     * Método que hashea una contraseña
     * 
     * @param password Contraseña a hashear
     * @return Contraseña hasheada
     */
    private String hashSHA256(String password) {
        try {
            // Instanciamos el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            // Convertimos los bytes en texto hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error crítico de seguridad: Algoritmo no encontrado.");
            return null;
        }
    }

    /**
     * Método que comprueba si existe un usuario
     * 
     * @param email Email del usuario
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean existeUsuario(String email) {
        try {
            List<String> lineas = Files.readAllLines(FICHERO_USUARIOS);
            for (String linea : lineas) {
                if (linea.split(";")[0].equals(email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo usuarios.");
        }
        return false;
    }

    /**
     * Método que registra un usuario
     * 
     * @param u Usuario a registrar
     * @return true si el usuario se registró correctamente, false en caso contrario
     */
    public boolean registrarUsuario(Usuario u) {
        if (existeUsuario(u.getEmail()))
            return false;

        String passHash = hashSHA256(u.getPassword());
        if (passHash == null)
            return false;

        try (BufferedWriter bw = Files.newBufferedWriter(FICHERO_USUARIOS, StandardOpenOption.APPEND)) {
            bw.write(u.getEmail() + ";" + passHash);
            bw.newLine();

            Path carpetaPersonal = CARPETA_USUARIOS.resolve(sanitizarEmail(u.getEmail()));
            Files.createDirectories(carpetaPersonal);
            return true;
        } catch (IOException e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método que inicia sesión
     * 
     * @param email    Email del usuario
     * @param password Contraseña del usuario
     * @return true si el usuario inició sesión correctamente, false en caso
     *         contrario
     */
    public boolean iniciarSesion(String email, String password) {
        String passHashInput = hashSHA256(password);

        try {
            List<String> lineas = Files.readAllLines(FICHERO_USUARIOS);
            for (String linea : lineas) {
                String[] partes = linea.split(";");
                if (partes.length == 2 && partes[0].equals(email) && partes[1].equals(passHashInput)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error en el inicio de sesión.");
        }
        return false;
    }
}