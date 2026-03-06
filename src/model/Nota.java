package model;

/**
 * Clase que representa una nota
 */
public class Nota {
    private String titulo;
    private String contenido;

    /**
     * Constructor de la clase Nota
     * 
     * @param titulo    Título de la nota
     * @param contenido Contenido de la nota
     */
    public Nota(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    /**
     * Método que devuelve el título de la nota
     * 
     * @return Título de la nota
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Método que devuelve el contenido de la nota
     * 
     * @return Contenido de la nota
     */
    public String getContenido() {
        return contenido;
    }
}