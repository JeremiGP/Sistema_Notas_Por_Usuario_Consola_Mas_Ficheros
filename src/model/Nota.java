package model;

public class Nota {
    private String titulo;
    private String contenido;

    public Nota(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    // Método útil para guardar en el fichero
    public String toFicheroString() {
        return titulo + ";" + contenido;
    }
}