package model;

/**
 * Clase que representa un usuario
 */
public class Usuario {
    private String email;
    private String password;

    /**
     * Constructor de la clase Usuario
     * 
     * @param email    Email del usuario
     * @param password Contraseña del usuario
     */
    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Método que devuelve el email del usuario
     * 
     * @return Email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Método que devuelve la contraseña del usuario
     * 
     * @return Password del usuario
     */
    public String getPassword() {
        return password;
    }
}