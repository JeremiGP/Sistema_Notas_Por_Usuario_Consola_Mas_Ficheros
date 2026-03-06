# · Actividad Evaluativa UT5.1: Sistema de Notas
#       Realizado por: Jeremi Gonzalez Perera
#
#       Curso: 1º DAM | Asignatura: Programación
#
#       Fecha: 6 de marzo de 2026
#
#
# · Ejecución
#       Para poder acceder a la aplicación, se debe #       ejecutar la clase Main.java ubicada en el   #       paquete app dentro de src.
#
#       Requisito: Java 17 o superior.
#
#       Persistencia: Al iniciar, el programa crea #       automáticamente el directorio data/ si no #       existe.
#
# · Organización del Proyecto (Estructura de        #   Paquetes)
#       El proyecto sigue una arquitectura de   #       servicios #para separar la lógica de #       negocio de la #interfaz #de consola:

#    |-- data/                       
#    |   |-- users.txt               
#    |   |-- usuarios/               
#    |-- src/
#    |   |-- app/
#    |   |   |-- Main.java        
#    |   |-- model/
#    |   |   |-- Usuario.java      
#    |   |   |-- Nota.java         
#    |   |-- service/
#    |   |   |-- UsuarioService.java 
#    |   |   |-- NotaService.java   
#    |   |-- utils/
#    |   |   |-- ConsolaUtils.java   
#    |-- .gitignore                  
#    |-- README.md                 
