package controlador;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import modelo.Prestamo;

// EN ESTE CONTROLADOR SE HACE UN UPDATE DE LA TABLA LIBROS 
public class ControladorDevoluciones implements Initializable {

    private ControladorEntrada controladorentrada;
    private ScheduledExecutorService executor;
    private Prestamo prestamo;

    private ObservableList<String> nombresusuarios;
    private FilteredList<String> filtro;

    private ObservableList<String> codigos;
    private FilteredList<String> filtroisbn;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;
    }

    @FXML
    private Pane paneprincipal;

    @FXML
    private ComboBox<String> txtcodigo;

    @FXML
    private ComboBox<String> txtusuario;
    
    
    @FXML
    void btndevolver(ActionEvent event) {
        if (camposValidos()) {
            String combocodigo = txtcodigo.getValue();
            String[] partes = combocodigo.split(", Titulo : ");

            String dni = txtusuario.getValue();

            if (partes.length > 0) {
                try {
                    int isbn = Integer.parseInt(partes[0]);

                    realizarDevolucion(isbn, dni);
                    this.limpiarDatos();
                    
                } catch (NumberFormatException e) {
                    System.out.println("Error");
                }
            } else {
                System.out.println("Error");
            }
        }
    }

   public void realizarDevolucion(int isbn, String dni) {


            try {
                // Verificar si el usuario y el libro existen en la tabla prestamos
                // asi comprobamos si el usuario y el libro estan registrados 
                if (!prestamoExiste(dni, isbn)) {
                    mostrarAlerta("Error", "Préstamo no encontrado", "No se ha encontrado un préstamo activo para el usuario con DNI " + dni + " y el libro con ISBN " + isbn);
                    return;
                }

                
                limpiarDatos();
                // PARTE de codigo para obtener la fecha de salida del préstamo 
                //PARA SABER SI TIENE PENALIZACION POR RETRASO 
                // esto se hace recogiendo con el metodo obtenerFechaSalida la fecha que tiene ese dni e isbn
                // si esta es null se recogera el Currentday y se compara la fecha en la que salio el libro
                // con la fecha en la que lo devuelve, se cacula los dias que pasan y si estos son mas de 15
                //tiene penalizacion 
                LocalDate fechaentrega = obtenerFechaSalida(dni, isbn);
                if (fechaentrega != null) {
                    // Calcular la diferencia en días
                    /*
                    LocalDate fechasalida = fechaSalida;
                    LocalDate fecha = LocalDate.now().plusDays(16);
                    Period periodo = Period.between(fechasalida, fecha);
                    int diferencia = periodo.getDays();*/

                    // utilizo esta forma mas directa que devuelve los dias 
                    long dias = fechaentrega.until(LocalDate.now(), ChronoUnit.DAYS);

                    // Si han pasado más de 16 días (15+1), mostrar alerta de penalización
                    if (dias > 1) {
                        //se le suma 10 dias a la fecha actual para mostrar cuando puede volver a sacar libros 
                        // se recoge como variable para poder sacar la fecha en el mensaje de alerta 
                        LocalDate fechaPenalizacion = LocalDate.now().plusDays(10);
                        String mensaje = "El usuario está penalizado por retraso en la devolución.\n";
                        mensaje += "Podrá volver a sacar libros a partir del día: " + fechaPenalizacion.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".";
                        mostrarAlerta("Usuario Penalizado", mensaje, "Por favor, tome las medidas adecuadas.");
                        limpiarDatos();
                    }
                } else {
                    mostrarAlerta("Error", "Error al obtener la fecha de salida", "No se ha podido obtener la fecha de salida del préstamo.");
                    return;
                }
                limpiarDatos();
                // Actualizar la tabla de préstamos con la fecha de devolución
                PreparedStatement st = controladorentrada.getConnection().prepareStatement(
                        "UPDATE prestamos SET fechadevo = CURRENT_DATE WHERE dniusuario = ? AND isbn = ? AND fechadevo IS NULL");

                st.setString(1, dni);
                st.setInt(2, isbn);

                int rowsUpdated = st.executeUpdate();
                st.close();

                if (rowsUpdated == 0) {
                    mostrarAlerta("Error", "Préstamo no encontrado", "No se ha encontrado un préstamo activo para el usuario con ese DNI  y el libro con ese ISBN ");
                    return;
                }

                // Con este codigo a la hora de la devolucion tambien cambiamos la columna ejemplares 
                //añadiendole 1 libro ya que en controlador prestamos se hace lo contrario (-1)
                PreparedStatement stSumarEjemplar = controladorentrada.getConnection().prepareStatement(
                        "UPDATE libros SET ejemplares = ejemplares + 1 WHERE isbn = ?");

                stSumarEjemplar.setInt(1, isbn);
                stSumarEjemplar.executeUpdate();
                stSumarEjemplar.close();

                // Mostrar mensaje de confirmación
                mostrarAlerta("Confirmación", "Devolución realizada correctamente", "El libro se ha devuelto correctamente.");

                // Limpiar los campos después de la devolución
                limpiarDatos();

            } catch (Exception e) {
                System.out.println(e.getMessage());

                // Mostrar el Alert en caso de error
                mostrarAlerta("Error",
                        "Error al realizar la devolución",
                        "Error al realizar la devolución del libro.");
                limpiarDatos();
            }
        
    }

    //METODO que lee/selecciona si el isbn y el dni estan en la tabla y la fecha devo es null ya que si no es null 
    //quitamos ese elemento de la tabla 
    private boolean prestamoExiste(String dni, int isbn) {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT * FROM prestamos WHERE dniusuario = ? AND isbn = ? AND fechadevo IS NULL");
            st.setString(1, dni);
            st.setInt(2, isbn);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // METODO para crear alertar 
    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private boolean camposValidos() {
        if (txtcodigo.getValue() == null || txtusuario.getValue() == null || txtusuario.getValue().isEmpty()) {
            mostrarAlerta("Error", "Campos vacíos", "Por favor, complete todos los campos");
            return false;
        }
        return true;
    }
   

    //METODO para limpar las cajas de texto cuando se han ingresado en la tabla 
    private void limpiarDatos() {
        txtcodigo.getSelectionModel().clearSelection();
        txtusuario.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.controladorentrada = new ControladorEntrada();
        this.controladorentrada.getConnection();

        nombresusuarios = FXCollections.observableArrayList();
        filtro = new FilteredList<>(nombresusuarios, p -> true);
        txtusuario.setItems(filtro);
        txtusuario.setPromptText("Seleccione el DNI del usuario");
        txtusuario.setEditable(true);

        TextField txtCombo = txtusuario.getEditor();
        txtCombo.textProperty().addListener((obs, oldValue, newValue) -> {
            final String usuarioSeleccionado = txtusuario.getSelectionModel().getSelectedItem();
            txtusuario.show();
            if (usuarioSeleccionado == null || !usuarioSeleccionado.equals(txtCombo.getText())) {
                filtro.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase()));
            }
        });
        //listener al combo de usuarios para que llame a cargarLibrosPrestados cuando se seleccione un usuario (recogiendo en el combo libro
        // solo aquellos libros que tiene el usuario seleccionado)
        txtusuario.valueProperty().addListener((obs, oldValue, newValue) -> {
        if (newValue != null) {
            cargarLibrosPrestados(newValue.split(", Nombre : ")[0]); 
        }
    });

        cargarusuarios();

        codigos = FXCollections.observableArrayList();
        filtroisbn = new FilteredList<>(codigos, p -> true);
        txtcodigo.setItems(filtroisbn);
        txtcodigo.setPromptText("Seleccione el ISBN del libro");
        txtcodigo.setEditable(true);

        TextField txtCodigoEditor = txtcodigo.getEditor();
    txtCodigoEditor.textProperty().addListener((obs, oldValue, newValue) -> {
        final String codigoSeleccionado = txtcodigo.getSelectionModel().getSelectedItem();
        txtcodigo.show();
        if (codigoSeleccionado == null || !codigoSeleccionado.toString().equals(txtCodigoEditor.getText())) {
            filtroisbn.setPredicate(item -> item.toString().contains(newValue));
        }
    });

     //   cargarISBN();
    }

    // METODO que recoge la fecha de devolucion si al recoger el isbn y dni tiene la fecha devo vacia 
    // recoge la fecha de salida y la transforma en la fecha en la que se devuelve
    private LocalDate obtenerFechaSalida(String dni, int isbn) {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT fechasalida FROM prestamos WHERE dniusuario = ? AND isbn = ? AND fechadevo IS NULL");
            st.setString(1, dni);
            st.setInt(2, isbn);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return LocalDate.parse(rs.getString("fechasalida"), DateTimeFormatter.ISO_DATE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void cargarusuarios() {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT DISTINCT dniusuario FROM prestamos WHERE fechadevo IS NULL");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                nombresusuarios.add(rs.getString("dniusuario"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*
 // METODO que por medio del QUERY recoge los libros que estan en la tabla y que estan relacionados 
 // con el usuario escogido en el combo 
    private void cargarISBN() {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT isbn, titulo FROM prestamos WHERE dniusuario = ? AND fechadevo IS NULL");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                codigos.add(Integer.toString(rs.getInt("isbn")) + ", Titulo : " + rs.getString("titulo"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
    
    //METODO que hace que solo aparezcan en el ComboBox de libros los libros que el usuario tienen en prestamo (con dniusuario=?)
    private void cargarLibrosPrestados(String dni) {
    codigos.clear(); // Limpiar los códigos anteriores
    try {
        PreparedStatement st = controladorentrada.getConnection().prepareStatement(
            "SELECT isbn, titulo FROM prestamos WHERE dniusuario = ? AND fechadevo IS NULL");
        st.setString(1, dni);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            codigos.add(Integer.toString(rs.getInt("isbn")) + ", Titulo : " + rs.getString("titulo"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
