package service;

import model.Usuario;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class UsuarioService {
    private static final Path CARPETA_DATA = Path.of("data");
    private static final Path FICHERO_USUARIOS = CARPETA_DATA.resolve("users.txt");
    public static final Path CARPETA_USUARIOS = CARPETA_DATA.resolve("usuarios");

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

    public String sanitizarEmail(String email) {
        return email.replace("@", "_").replace(".", "_");
    }

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

    public boolean registrarUsuario(Usuario u) {
        if (existeUsuario(u.getEmail()))
            return false;

        try (BufferedWriter bw = Files.newBufferedWriter(FICHERO_USUARIOS, StandardOpenOption.APPEND)) {
            bw.write(u.getEmail() + ";" + u.getPassword());
            bw.newLine();

            Path carpetaPersonal = CARPETA_USUARIOS.resolve(sanitizarEmail(u.getEmail()));
            Files.createDirectories(carpetaPersonal);
            return true;
        } catch (IOException e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return false;
        }
    }

    public boolean iniciarSesion(String email, String password) {
        try {
            List<String> lineas = Files.readAllLines(FICHERO_USUARIOS);
            for (String linea : lineas) {
                String[] partes = linea.split(";");
                if (partes.length == 2 && partes[0].equals(email) && partes[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error en el inicio de sesión.");
        }
        return false;
    }
}