package back_end;

/**
 * TDA Pila (LIFO) implementado mediante nodos enlazados dinámicamente.
 *
 * <p>Restricciones técnicas cumplidas:</p>
 * <ul>
 *   <li>No utiliza java.util.Stack, Deque, ArrayList, LinkedList ni arreglos.</li>
 *   <li>La estructura se construye manualmente mediante referencias entre objetos Nodo.</li>
 *   <li>La referencia {@code tope} apunta siempre al último elemento insertado.</li>
 * </ul>
 */
public class Pila {

    /** Referencia al nodo que ocupa la cima de la pila (null si está vacía). */
    private Nodo tope;

    /** Contador de elementos presentes en la pila. */
    private int  tamanio;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /** Crea una pila vacía. */
    public Pila() {
        tope     = null;
        tamanio  = 0;
    }

    // -------------------------------------------------------------------------
    // Operaciones fundamentales LIFO
    // -------------------------------------------------------------------------

    /**
     * Apilar — Push.
     * Crea un nuevo nodo que envuelve el libro recibido, lo enlaza al nodo que
     * era el tope anterior y actualiza la referencia tope hacia el nuevo nodo.
     *
     * @param libro Objeto de dominio a insertar en la cima.
     */
    public void push(Libro libro) {
        Nodo nuevoNodo = new Nodo(libro);
        nuevoNodo.setSiguiente(tope); // enlaza hacia el nodo anterior
        tope = nuevoNodo;             // el nuevo nodo pasa a ser el tope
        tamanio++;
    }

    /**
     * Desapilar — Pop.
     * Extrae y devuelve el libro situado en el tope, redirigiendo la referencia
     * tope hacia el nodo inmediatamente inferior. El nodo desvinculado queda
     * disponible para el recolector de basura de la JVM.
     *
     * @return El objeto Libro que estaba en el tope.
     * @throws PilaVaciaException si la pila no contiene elementos.
     */
    public Libro pop() {
        if (isEmpty()) {
            throw new PilaVaciaException("No se puede desapilar: la pila está vacía.");
        }
        Libro libroExtraido = tope.getDato(); // guarda el dato antes de desvincular
        Nodo  nodoAEliminar = tope;           // referencia temporal para desvinculación
        tope = tope.getSiguiente();           // tope apunta al nodo inferior
        nodoAEliminar.setSiguiente(null);     // limpia la referencia del nodo retirado
        tamanio--;
        return libroExtraido;
    }

    /**
     * Consultar tope — Peek.
     * Devuelve el libro en la cima sin alterar la estructura de nodos.
     *
     * @return El objeto Libro que ocupa el tope.
     * @throws PilaVaciaException si la pila está vacía.
     */
    public Libro peek() {
        if (isEmpty()) {
            throw new PilaVaciaException("No se puede consultar el tope: la pila está vacía.");
        }
        return tope.getDato();
    }

    /**
     * Verificar estado — isEmpty.
     *
     * @return {@code true} si la pila no contiene elementos.
     */
    public boolean isEmpty() {
        return tope == null;
    }

    /**
     * Obtener tamaño — size.
     *
     * @return Cantidad de libros apilados actualmente.
     */
    public int size() {
        return tamanio;
    }

    /**
     * Vaciar — Clear.
     * Restablece la referencia tope a null y el contador a cero.
     * Los nodos quedan sin referencias activas y serán liberados por el GC.
     */
    public void clear() {
        tope    = null;
        tamanio = 0;
    }

    // -------------------------------------------------------------------------
    // Excepción interna de dominio
    // -------------------------------------------------------------------------

    /**
     * Excepción no verificada que se lanza cuando se intenta operar sobre
     * una pila vacía.
     */
    public static class PilaVaciaException extends RuntimeException {
        public PilaVaciaException(String mensaje) {
            super(mensaje);
        }
    }
}
