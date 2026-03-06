package service;

import model.Nota;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class NotaService {
    private final UsuarioService usuarioService;

    public NotaService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private Path obtenerFicheroNotas(String email) {
        String emailSanitizado = usuarioService.sanitizarEmail(email);
        return UsuarioService.CARPETA_USUARIOS.resolve(emailSanitizado).resolve("notas.txt");
    }

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