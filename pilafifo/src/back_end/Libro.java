package back_end;

/**
 * Clase de dominio que representa un Libro.
 * Encapsula los atributos título, autor, año de publicación e ISBN.
 */
public class Libro {

    private String titulo;
    private String autor;
    private int    anio;
    private String isbn;

    /**
     * Constructor completo.
     *
     * @param titulo Título del libro (no vacío).
     * @param autor  Nombre del autor (no vacío).
     * @param anio   Año de publicación (mayor que 0).
     * @param isbn   Código ISBN (no vacío).
     */
    public Libro(String titulo, String autor, int anio, String isbn) {
        this.titulo = titulo;
        this.autor  = autor;
        this.anio   = anio;
        this.isbn   = isbn;
    }

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    // -------------------------------------------------------------------------
    // Representación textual para la visualización en la GUI
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        return "\"" + titulo + "\" — " + autor + " (" + anio + ")  ISBN: " + isbn;
    }
}
