package app.biblioteca.appnotificacion;

public class Libro {
    private int isbn;
    private String titulo;
    private String autor;
    private String idioma;
    private int ejemplares;

    public Libro(int  isbn,String titulo, String autor, String idioma, int ejemplares) {
        this.isbn= isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.ejemplares = ejemplares;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public int getEjemplares() {
        return ejemplares;
    }
}
