package service;

// Importamos las clases que vamos a usar
import model.Nota;

// Importamos las clases de java.io para poder trabajar con ficheros
import java.io.BufferedWriter;
import java.io.IOException;

// Importamos las clases de java.nio.file para poder trabajar con ficheros
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// Importamos las clases de java.util para poder trabajar con listas
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el servicio de notas
 */
public class NotaService {
    private final UsuarioService usuarioService;

    /**
     * Constructor de la clase NotaService
     * 
     * @param usuarioService Servicio de usuarios
     */
    public NotaService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Método que devuelve el fichero de notas del usuario
     * 
     * @param email Email del usuario
     * @return Fichero de notas del usuario
     */
    private Path obtenerFicheroNotas(String email) {
        String emailSanitizado = usuarioService.sanitizarEmail(email);
        return UsuarioService.CARPETA_USUARIOS.resolve(emailSanitizado).resolve("notas.txt");
    }

    /**
     * Método que guarda una nota
     * 
     * @param email Email del usuario
     * @param nota  Nota a guardar
     */
    public void guardarNota(String email, Nota nota) {
        Path ficheroNotas = obtenerFicheroNotas(email);
        try (BufferedWriter bw = Files.newBufferedWriter(ficheroNotas, StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            bw.write(nota.toFicheroString());
            bw.newLine();
            System.out.println("Nota guardada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar la nota.");
        }
    }

    /**
     * Método que obtiene las notas del usuario
     * 
     * @param email Email del usuario
     * @return Lista de notas del usuario
     */
    public List<Nota> obtenerNotas(String email) {
        List<Nota> listaNotas = new ArrayList<>();
        Path ficheroNotas = obtenerFicheroNotas(email);

        if (!Files.exists(ficheroNotas))
            return listaNotas;

        try {
            List<String> lineas = Files.readAllLines(ficheroNotas);
            for (String linea : lineas) {
                String[] partes = linea.split(";");
                String titulo = partes[0];
                String contenido = partes.length > 1 ? partes[1] : "";
                listaNotas.add(new Nota(titulo, contenido));
            }
        } catch (IOException e) {
            System.out.println("Error al leer las notas.");
        }
        return listaNotas;
    }

    /**
     * Método que elimina una nota
     * 
     * @param email      Email del usuario
     * @param numeroNota Número de la nota a eliminar
     * @return true si la nota se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarNota(String email, int numeroNota) {
        Path ficheroNotas = obtenerFicheroNotas(email);
        List<Nota> notasActuales = obtenerNotas(email);

        if (numeroNota < 1 || numeroNota > notasActuales.size()) {
            return false;
        }

        notasActuales.remove(numeroNota - 1);

        try (BufferedWriter bw = Files.newBufferedWriter(ficheroNotas)) {
            for (Nota n : notasActuales) {
                bw.write(n.toFicheroString());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error al actualizar el fichero tras eliminar.");
            return false;
        }
    }
}