/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;

/**
 *
 * @author cabre
 */
public class Libros {
    private  int isbn;
    private  String titulo;
    private  String fechapublicacion;
    private  String autor;
    private String tipo;
    private  String genero;
    private  String idioma;
    private  String descripcion;
    private  int ejemplares;

    public Libros(int isbn, String titulo, String fechapublicacion, String autor, String tipo, String genero, String idioma,  String descripcion, int ejemplares) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.fechapublicacion = fechapublicacion;
        this.autor = autor;
        this.tipo= tipo;
        this.genero = genero;
        this.idioma = idioma;
        this.descripcion = descripcion;
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

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFechapublicacion() {
        return fechapublicacion;
    }

    public void setFechapublicacion(String fechapublicacion) {
        this.fechapublicacion = fechapublicacion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(int ejemplares) {
        this.ejemplares = ejemplares;
    }




   
}
