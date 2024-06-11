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
public class Prestamo {
     private String dniusuario;
     private int isbn;
     private String titulo;
     private String fechasalida;
     private String fechadevo;

    public Prestamo(String dniusuario, int isbn, String titulo, String fechasalida, String fechadevo) {
        this.dniusuario = dniusuario;
        this.isbn = isbn;
        this.titulo = titulo;
        this.fechasalida = fechasalida;
        this.fechadevo = fechadevo;
    }

    public String getDniusuario() {
        return dniusuario;
    }

    public void setDniusuario(String dniusuario) {
        this.dniusuario = dniusuario;
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

    public String getFechasalida() {
        return fechasalida;
    }

    public void setFechasalida(String fechasalida) {
        this.fechasalida = fechasalida;
    }

    public String getFechadevo() {
        return fechadevo;
    }

    public void setFechadevo(String fechadevo) {
        this.fechadevo = fechadevo;
    }
     
     

}