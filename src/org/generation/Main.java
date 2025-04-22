package org.generation;

import java.util.*;
import java.util.concurrent.*;

public class Main {
    // Diccionario ampliado con pares palabra en español y su traducción en inglés
    private static final String[][] PALABRAS = {
            {"hola", "hello"}, {"adios", "goodbye"}, {"casa", "house"},
            {"perro", "dog"}, {"gato", "cat"}, {"arbol", "tree"},
            {"rojo", "red"}, {"azul", "blue"}, {"amarillo", "yellow"},
            {"comida", "food"}, {"agua", "water"}, {"fuego", "fire"},
            {"tierra", "earth"}, {"cielo", "sky"}, {"sol", "sun"},
            {"luna", "moon"}, {"hombre", "man"}, {"mujer", "woman"},
            {"tiempo", "time"}, {"numero", "number"}, {"libro", "book"},
            {"escuela", "school"}, {"amigo", "friend"}, {"familia", "family"},
            {"trabajo", "work"}, {"ciudad", "city"}, {"carro", "car"},
            {"puerta", "door"}, {"ventana", "window"}, {"flor", "flower"}
    };

    public static void main(String[] args) {
        // Crear HashMap para almacenar el diccionario Español-Inglés
        HashMap<String, String> diccionario = new HashMap<>();
        // Scanner para leer entrada del usuario
        Scanner scanner = new Scanner(System.in);
        // Random para operaciones aleatorias (aunque no se usa directamente aquí)
        Random random = new Random();

        // Cargar las palabras del arreglo al HashMap
        for (String[] par : PALABRAS) {
            diccionario.put(par[0], par[1]);
        }

        // Mostrar menú para seleccionar dificultad
        System.out.println("=== Diccionario Español-Inglés ===");
        System.out.println("Selecciona dificultad:");
        System.out.println("1. Fácil (5 palabras)");
        System.out.println("2. Medio (7 palabras)");
        System.out.println("3. Difícil (10 palabras)");
        System.out.print("Ingresa opción (1-3): ");

        int dificultad = 1; // Valor por defecto
        int numPalabras = 5; // Número de palabras a preguntar según dificultad

        // Validar entrada del usuario para dificultad
        while (true) {
            try {
                dificultad = Integer.parseInt(scanner.nextLine());
                if (dificultad < 1 || dificultad > 3) {
                    System.out.print("Opción inválida. Intenta de nuevo (1-3): ");
                } else {
                    break; // Entrada válida, salir del ciclo
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingresa un número (1-3): ");
            }
        }

        // Asignar número de palabras según dificultad seleccionada
        switch (dificultad) {
            case 1:
                numPalabras = 5;
                break;
            case 2:
                numPalabras = 7;
                break;
            case 3:
                numPalabras = 10;
                break;
        }

        // Obtener lista de todas las palabras en español
        List<String> palabrasEspañol = new ArrayList<>(diccionario.keySet());
        // Mezclar la lista para selección aleatoria
        Collections.shuffle(palabrasEspañol);
        // Seleccionar la cantidad de palabras según dificultad
        List<String> seleccionadas = palabrasEspañol.subList(0, numPalabras);

        // Variables para contar respuestas correctas, incorrectas y puntuación
        int correctas = 0;
        int incorrectas = 0;
        int puntuacion = 0;
        final int PUNTOS_POR_ACIERTO = 10; // Puntos por cada respuesta correcta
        final int TIEMPO_MAX_SEGUNDOS = 10; // Tiempo límite para responder cada palabra

        System.out.println("\n¡Traduce las siguientes palabras al inglés!");
        System.out.println("(Tienes " + TIEMPO_MAX_SEGUNDOS + " segundos para cada palabra)\n");

        // Ciclo para pedir traducción de cada palabra seleccionada
        for (String palabra : seleccionadas) {
            System.out.println("Traduce: '" + palabra + "'");

            // Leer respuesta del usuario con límite de tiempo
            String respuesta = leerConTiempo(scanner, TIEMPO_MAX_SEGUNDOS);

            if (respuesta == null) {
                // Si se agotó el tiempo, cuenta como incorrecta y muestra respuesta correcta
                System.out.println("Tiempo agotado! La respuesta correcta es: " + diccionario.get(palabra));
                incorrectas++;
            } else {
                // Normalizar respuesta para comparación (minúsculas y sin espacios)
                respuesta = respuesta.toLowerCase().trim();
                if (respuesta.equals(diccionario.get(palabra))) {
                    System.out.println("✓ ¡Correcto!");
                    correctas++;
                    puntuacion += PUNTOS_POR_ACIERTO;
                } else {
                    System.out.println("✗ Incorrecto. La respuesta correcta es: " + diccionario.get(palabra));
                    incorrectas++;
                }
            }
            System.out.println(); // Línea en blanco para separar preguntas
        }

        // Mostrar resultados finales
        System.out.println("=== RESULTADOS ===");
        System.out.println("Respuestas correctas: " + correctas);
        System.out.println("Respuestas incorrectas: " + incorrectas);
        System.out.println("Puntuación total: " + puntuacion + " puntos");

        // Cerrar el scanner para liberar recursos
        scanner.close();
    }

    /**
     * Lee una línea de entrada del usuario con un tiempo límite.
     * Si el usuario no responde en el tiempo, devuelve null.
     */

    private static String leerConTiempo(Scanner scanner, int segundos) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(scanner::nextLine);

        try {
            // Espera la entrada hasta el tiempo límite
            return future.get(segundos, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // Si se agota el tiempo, cancela la tarea y retorna null
            future.cancel(true);
            return null;
        } catch (Exception e) {
            // En caso de otras excepciones, también retorna null
            return null;
        } finally {
            // Apaga el executor para liberar recursos
            executor.shutdownNow();
        }
    }
}