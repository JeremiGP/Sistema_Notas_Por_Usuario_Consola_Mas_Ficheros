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
            System.out.println("\n=============================================");
            System.out.println("|             SISTEMA DE NOTAS              |");
            System.out.println("=============================================");
            System.out.println("|  [1] Registrarse                          |");
            System.out.println("|  [2] Iniciar sesión                       |");
            System.out.println("|  [0] Salir                                |");
            System.out.println("=============================================");
            opcion = ConsolaUtils.leerEntero(">> Elige una opción: ");

            switch (opcion) {
                case 1:
                    registrar();
                    break;
                case 2:
                    login();
                    break;
                case 0:
                    System.out.println("\n[INFO] Cerrando el sistema. ¡Hasta pronto!\n");
                    break;
                default:
                    System.out.println("\n[ERROR] Opción incorrecta. Por favor, intenta de nuevo.");
            }
        } while (opcion != 0);
    }

    /**
     * Método que permite al usuario registrarse
     */
    private static void registrar() {
        System.out.println("\n---------------------------------------------");
        System.out.println("                  REGISTRO                   ");
        System.out.println("---------------------------------------------");
        String email = ConsolaUtils.leerTexto(">> Email/Usuario: ");
        String pass = ConsolaUtils.leerTexto(">> Contraseña: ");

        Usuario nuevoUsuario = new Usuario(email, pass);
        if (us.registrarUsuario(nuevoUsuario)) {
            System.out.println("[EXITO] ¡Usuario registrado con éxito!");
        } else {
            System.out.println("[ERROR] El usuario ya existe o hubo un error en el registro.");
        }
    }

    /**
     * Método que permite al usuario iniciar sesión
     */
    private static void login() {
        System.out.println("\n---------------------------------------------");
        System.out.println("                INICIAR SESION               ");
        System.out.println("---------------------------------------------");
        String email = ConsolaUtils.leerTexto(">> Email/Usuario: ");
        String pass = ConsolaUtils.leerTexto(">> Contraseña: ");

        if (us.iniciarSesion(email, pass)) {
            System.out.println("[EXITO] ¡Bienvenido/a, " + email + "!");
            menuUsuario(email);
        } else {
            System.out.println("[ERROR] Credenciales incorrectas.");
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
            System.out.println("\n=============================================");
            System.out.println("|                 TUS NOTAS                 |");
            System.out.println("=============================================");
            System.out.println("|  [1] Crear nota                           |");
            System.out.println("|  [2] Listar notas                         |");
            System.out.println("|  [3] Ver nota por número                  |");
            System.out.println("|  [4] Eliminar nota (por número)           |");
            System.out.println("|  [0] Cerrar sesión                        |");
            System.out.println("=============================================");
            opcion = ConsolaUtils.leerEntero(">> Elige una opción: ");

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
                    System.out.println("\n[INFO] Sesión cerrada correctamente.");
                    break;
                default:
                    System.out.println("\n[ERROR] Opción incorrecta. Por favor, intenta de nuevo.");
            }
        } while (opcion != 0);
    }

    /**
     * Método que permite al usuario crear una nota
     * 
     * @param email Email del usuario logueado
     */
    private static void crearNotaMenu(String email) {
        System.out.println("\n---------------------------------------------");
        System.out.println("                 NUEVA NOTA                  ");
        System.out.println("---------------------------------------------");
        String titulo = ConsolaUtils.leerTexto(">> Título: ");
        String contenido = ConsolaUtils.leerTextoMultilinea(">> Contenido");

        Nota nueva = new Nota(titulo, contenido);
        System.out.println();
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
            System.out.println("\n[INFO] Actualmente no tienes notas guardadas.");
            return;
        }

        System.out.println("\n---------------------------------------------");
        System.out.println("               LISTA DE NOTAS                ");
        System.out.println("---------------------------------------------");
        for (int i = 0; i < misNotas.size(); i++) {
            System.out.printf("%d. %s\n", (i + 1), misNotas.get(i).getTitulo());
        }
        System.out.println("---------------------------------------------");
    }

    /**
     * Método que permite al usuario ver una nota
     * 
     * @param email Email del usuario logueado
     */
    private static void verNotaMenu(String email) {
        List<Nota> misNotas = ns.obtenerNotas(email);
        if (misNotas.isEmpty()) {
            System.out.println("\n[INFO] Actualmente no tienes notas guardadas.");
            return;
        }

        System.out.println();
        int numero = ConsolaUtils.leerEntero(">> Introduce el número de la nota a consultar: ");
        if (numero > 0 && numero <= misNotas.size()) {
            Nota n = misNotas.get(numero - 1);
            System.out.println("\n=============================================");
            System.out.println(" TITULO: " + n.getTitulo());
            System.out.println("---------------------------------------------");
            System.out.println(n.getContenido());
            System.out.println("=============================================");
        } else {
            System.out.println("\n[ERROR] Número de nota inválido.");
        }
    }

    /**
     * Método que permite al usuario eliminar una nota
     * 
     * @param email Email del usuario logueado
     */
    private static void eliminarNotaMenu(String email) {
        System.out.println();
        int numero = ConsolaUtils.leerEntero(">> Número de la nota a eliminar: ");
        if (ns.eliminarNota(email, numero)) {
            System.out.println("\n[EXITO] Nota eliminada correctamente.");
        } else {
            System.out.println("\n[ERROR] No se pudo eliminar la nota. Comprueba que el número sea correcto.");
        }
    }
}