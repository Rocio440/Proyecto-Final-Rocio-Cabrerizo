
package modelo;

/**
 *
 * @author cabre
 */
public class Usuario {
    private String dni;        
    private String nombre;
    private String primerapellido;
    private String segundoapellido;
    private String domicilio;
    private int telefono;
    private boolean sancion;
    private String cuanto;

    public Usuario(String dni, String nombre, String primerapellido, String segundoapellido, String domicilio, int telefono, boolean sancion, String cuanto) {
        this.dni = dni;
        this.nombre = nombre;
        this.primerapellido = primerapellido;
        this.segundoapellido = segundoapellido;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.sancion = sancion;
        this.cuanto=cuanto;
    }

    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerapellido() {
        return primerapellido;
    }

    public void setPrimerapellido(String primerapellido) {
        this.primerapellido = primerapellido;
    }

    public String getSegundoapellido() {
        return segundoapellido;
    }

    public void setSegundoapellido(String segundoapellido) {
        this.segundoapellido = segundoapellido;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }   

    public boolean isSancion() {
        return sancion;
    }

    public void setSancion(boolean sancion) {
        this.sancion = sancion;
    }

    public String getCuanto() {
        return cuanto;
    }

    public void setCuanto(String cuanto) {
        this.cuanto = cuanto;
    }
    
    
}
