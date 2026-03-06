package app;

// Importamos las clases que vamos a usar
import model.Nota;
import model.Usuario;
import service.NotaService;
import service.UsuarioService;
import utils.ConsolaUtils;

// Importamos la clase java.util.List para poder usarla
import java.util.List;

/**
 * Clase principal que contiene el método main y los métodos para ejecutar el
 * programa
 */
public class Main {
    // Instanciamos nuestros servicios
    private static final UsuarioService us = new UsuarioService();
    private static final NotaService ns = new NotaService(us);

    /**
     * Método principal que se ejecuta al iniciar el programa
     * 
     * @param args Argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        us.inicializarSistema();
        menuPrincipal();
    }

    /**
     * Método que muestra el menú principal y permite al usuario elegir una opción
     */
    private static void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n=================================");
            System.out.println("        SISTEMA DE NOTAS         ");
            System.out.println("=================================");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesión");
            System.out.println("0. Salir");
            opcion = ConsolaUtils.leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1:
                    registrar();
                    break;
                case 2:
                    login();
                    break;
                case 0:
                    System.out.println("¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);
    }

    /**
     * Método que permite al usuario registrarse
     */
    private static void registrar() {
        System.out.println("\n--- REGISTRO ---");
        String email = ConsolaUtils.leerTexto("Email: ");
        String pass = ConsolaUtils.leerTexto("Contraseña: ");

        Usuario nuevoUsuario = new Usuario(email, pass);
        if (us.registrarUsuario(nuevoUsuario)) {
            System.out.println("¡Usuario registrado con éxito!");
        } else {
            System.out.println("El usuario ya existe o hubo un error.");
        }
    }

    /**
     * Método que permite al usuario iniciar sesión
     */
    private static void login() {
        System.out.println("\n--- LOGIN ---");
        String email = ConsolaUtils.leerTexto("Email: ");
        String pass = ConsolaUtils.leerTexto("Contraseña: ");

        if (us.iniciarSesion(email, pass)) {
            System.out.println("¡Bienvenido, " + email + "!");
            menuUsuario(email);
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    /**
     * Método que muestra el menú del usuario y permite al usuario elegir una opción
     * 
     * @param emailLogueado Email del usuario logueado
     */
    private static void menuUsuario(String emailLogueado) {
        int opcion;
        do {
            System.out.println("\n---------------------------------");
            System.out.println("          TUS NOTAS         ");
            System.out.println("---------------------------------");
            System.out.println("1. Crear nota");
            System.out.println("2. Listar notas");
            System.out.println("3. Ver nota por número");
            System.out.println("4. Eliminar nota (por número)");
            System.out.println("0. Cerrar sesión");
            opcion = ConsolaUtils.leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1:
                    crearNotaMenu(emailLogueado);
                    break;
                case 2:
                    listarNotasMenu(emailLogueado);
                    break;
                case 3:
                    verNotaMenu(emailLogueado);
                    break;
                case 4:
                    eliminarNotaMenu(emailLogueado);
                    break;
                case 0:
                    System.out.println("Sesión cerrada.");
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);
    }

    /**
     * Método que permite al usuario crear una nota
     * 
     * @param email Email del usuario logueado
     */
    private static void crearNotaMenu(String email) {
        System.out.println("\n--- ✍️ NUEVA NOTA ---");
        String titulo = ConsolaUtils.leerTexto("Título: ");
        String contenido = ConsolaUtils.leerTextoMultilinea("Contenido");

        Nota nueva = new Nota(titulo, contenido);
        ns.guardarNota(email, nueva);
    }

    /**
     * Método que permite al usuario listar sus notas
     * 
     * @param email Email del usuario logueado
     */
    private static void listarNotasMenu(String email) {
        List<Nota> misNotas = ns.obtenerNotas(email);
        if (misNotas.isEmpty()) {
            System.out.println("No tienes notas.");
            return;
        }

        System.out.println("\n--- LISTA DE NOTAS ---");
        for (int i = 0; i < misNotas.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), misNotas.get(i).getTitulo());
        }
    }

    /**
     * Método que permite al usuario ver una nota
     * 
     * @param email Email del usuario logueado
     */
    private static void verNotaMenu(String email) {
        List<Nota> misNotas = ns.obtenerNotas(email);
        if (misNotas.isEmpty()) {
            System.out.println("No tienes notas.");
            return;
        }

        int numero = ConsolaUtils.leerEntero("Introduce el número de la nota: ");
        if (numero > 0 && numero <= misNotas.size()) {
            Nota n = misNotas.get(numero - 1);
            System.out.println("\n=================================");
            System.out.println("Título: " + n.getTitulo());
            System.out.println("Texto:  " + n.getContenido());
            System.out.println("=================================");
        } else {
            System.out.println("Número de nota inválido.");
        }
    }

    /**
     * Método que permite al usuario eliminar una nota
     * 
     * @param email Email del usuario logueado
     */
    private static void eliminarNotaMenu(String email) {
        int numero = ConsolaUtils.leerEntero("Número de la nota a eliminar: ");
        if (ns.eliminarNota(email, numero)) {
            System.out.println("Nota eliminada.");
        } else {
            System.out.println("Error al eliminar. Revisa el número.");
        }
    }
}