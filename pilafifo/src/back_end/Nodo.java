package back_end;

/**
 * Nodo de la estructura dinámica de la Pila.
 * Contiene la referencia al objeto de dominio (Libro) y la referencia
 * al nodo inmediatamente inferior en la pila.
 */
public class Nodo {

    /** Dato almacenado en este nodo. */
    private Libro dato;

    /** Referencia al nodo que se encontraba en el tope antes de este. */
    private Nodo siguiente;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Crea un nodo que almacena el libro recibido.
     * La referencia siguiente queda en null hasta que la Pila la asigne.
     *
     * @param dato Objeto Libro a almacenar.
     */
    public Nodo(Libro dato) {
        this.dato      = dato;
        this.siguiente = null;
    }

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public Libro getDato() {
        return dato;
    }

    public void setDato(Libro dato) {
        this.dato = dato;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
