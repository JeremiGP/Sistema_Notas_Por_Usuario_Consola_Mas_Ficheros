package service;

// Importamos las clases que vamos a usar
import model.Nota;

// Importamos las clases de java.io para poder trabajar con ficheros
import java.io.BufferedWriter;
import java.io.IOException;

// Importamos las clases de java.nio.file para poder trabajar con ficheros
import java.nio.file.DirectoryStream;
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
     * Método que devuelve todas las rutas de los archivos nota_XXX.txt ordenados
     * alfabéticamente
     * 
     * @param email Email del usuario
     * @return Lista de rutas de los archivos nota_XXX.txt ordenados alfabéticamente
     */
    private List<Path> obtenerArchivosNotas(String email) {
        List<Path> archivos = new ArrayList<>();
        Path dirUsuario = UsuarioService.CARPETA_USUARIOS.resolve(usuarioService.sanitizarEmail(email));

        if (!Files.exists(dirUsuario))
            return archivos;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirUsuario, "nota_*.txt")) {
            for (Path entry : stream) {
                archivos.add(entry);
            }

            archivos.sort((p1, p2) -> p1.getFileName().compareTo(p2.getFileName()));
        } catch (IOException e) {
            System.out.println("Error explorando la carpeta de notas.");
        }
        return archivos;
    }

    /**
     * Método que guarda una nota
     * 
     * @param email Email del usuario
     * @param nota  Nota a guardar
     */
    public void guardarNota(String email, Nota nota) {
        Path dirUsuario = UsuarioService.CARPETA_USUARIOS.resolve(usuarioService.sanitizarEmail(email));

        int id = 1;
        Path rutaNuevaNota;
        do {
            rutaNuevaNota = dirUsuario.resolve(String.format("nota_%03d.txt", id));
            id++;
        } while (Files.exists(rutaNuevaNota));

        try (BufferedWriter bw = Files.newBufferedWriter(rutaNuevaNota, StandardOpenOption.CREATE)) {
            bw.write(nota.getTitulo());
            bw.newLine();
            bw.write(nota.getContenido());
            System.out.println("Nota guardada correctamente en " + rutaNuevaNota.getFileName());
        } catch (IOException e) {
            System.out.println("Error al guardar la nota.");
        }
    }

    /**
     * Metodo para obtener la nota.
     * 
     * @param email Obtiene el Email del usuario.
     * @return Devulves las notas adjudicadas a ese email.
     */
    public List<Nota> obtenerNotas(String email) {
        List<Nota> listaNotas = new ArrayList<>();
        List<Path> archivos = obtenerArchivosNotas(email);

        for (Path archivo : archivos) {
            try {
                List<String> lineas = Files.readAllLines(archivo);
                if (!lineas.isEmpty()) {
                    String titulo = lineas.get(0);

                    StringBuilder contenido = new StringBuilder();
                    for (int i = 1; i < lineas.size(); i++) {
                        contenido.append(lineas.get(i)).append("\n");
                    }
                    listaNotas.add(new Nota(titulo, contenido.toString().trim()));
                }
            } catch (IOException e) {
                System.out.println("Error al leer la nota " + archivo.getFileName());
            }
        }
        return listaNotas;
    }

    /**
     * Metodo para eliminar notas
     * 
     * @param email      Obtiene el email del usuario.
     * @param numeroNota Obtiene el numero de la nota.
     * @return Devulve la nota eliminada.
     */
    public boolean eliminarNota(String email, int numeroNota) {
        List<Path> archivos = obtenerArchivosNotas(email);

        if (numeroNota < 1 || numeroNota > archivos.size()) {
            return false;
        }

        Path archivoABorrar = archivos.get(numeroNota - 1);

        try {
            Files.delete(archivoABorrar);
            return true;
        } catch (IOException e) {
            System.out.println("Error al borrar el fichero: " + e.getMessage());
            return false;
        }
    }
}