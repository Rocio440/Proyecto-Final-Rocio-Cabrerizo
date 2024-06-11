package controlador;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import modelo.Libros;

//CONTROLADOR PARA PRESTACIÓN DE LOS LIBROS 
public class ControladorPrestar implements Initializable {

    private ControladorEntrada controladorentrada;
    private ControladorPrestar controladorprestar;
    private ControladorTablaLibros controladortablalibros;

    //se utiliza para crear una vista filtrada de los nombres de usuarios 
    //almacenados en nombresusuarios
    // private FilteredList<String> filtro;
    private ObservableList<String> nombresusuarios;
    private FilteredList<String> filtrousuarios;

    private ObservableList<String> codigos;
    private FilteredList<String> filtroisbn;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;
    }

    public void setControladorPrestar(ControladorPrestar controladorprestar) {
        this.controladorprestar = controladorprestar;
    }

    public void setControladorTablaLibros(ControladorTablaLibros controladortablalibros) {
        this.controladortablalibros = controladortablalibros;
    }

    public void setDatoisbn(int isbn) {
        if (txtcodigo != null) {
            txtcodigo.setValue(Integer.toString(isbn));
        } else {
            System.out.println("txtcodigo aún no se ha inicializado");
        }
    }

    @FXML
    private Pane paneprincipal;

    @FXML
    private ComboBox<String> txtcodigo;

    @FXML
    private ComboBox<String> txtusuario;

    // en el boton prestar verifica que si los campos no estan realiza el metodo realizarprestamo
    //realizarprestamo se rellena con los valores del combo, pero como este es un string y solo necesito un datos de esa linea se
    // realiza .split (por medio de , )para separar las partes que necesito para el metodo 
    @FXML
    void btnprestar(ActionEvent event) {
        if (camposValidos()) {
            String combocodigo = txtcodigo.getValue();
            String[] partes = combocodigo.split(", Titulo : ");

            String[] partedni = txtusuario.getValue().split(", Nombre : ");

            if (partes.length > 0 && partedni.length > 0) {
                try {
                    int isbn = Integer.parseInt(partes[0]);
                    String dni = partedni[0];

                    realizarPrestamos(isbn, dni);
                    this.limpiarDatos();
                } catch (NumberFormatException e) {
                    System.out.println("Error");
                }
            } else {
                System.out.println("Error");
            }
        }
    }
//7METODO que realiza el prestamo recogiendo los datos e insertandolos en la tabla prestamos
    public void realizarPrestamos(int isbn, String dni) {
        //VALIDACIONES
        try {
            if (librorepetido(dni, isbn)) {
                mostrarAlerta("Error", "Libro ya en prestamo", "El usuario ya tiene ese libro en prestamo. \n"
                        + "No se puede realizar el prestamo hasta que devuelva el otro ejemplar ");
                return;
            }
            // se pone esto porque solo se puede prestar a usuarios ya existendes, si no saldra una alerta
            // se hace llamando al metodo usuarioExiste que buscca por medio de sentencia en la tabla 
            if (!usuarioExiste(dni)) {
                mostrarAlerta("Error", "Usuario no encontrado", "El usuario con ese DNI, no existe en la base de datos");
                return;
            }
            // se pone esto porque solo se puede prestar libros ya existendes 
            // se hace llamando al metodo isbnxiste que buscca por medio de sentencia en la tabla 
            if (!isbnExiste(isbn)) {
                mostrarAlerta("Error", "Libro no encontrado", "El libro con ese ISBN no existe en la base de datos.");
                this.limpiarDatos();
                return;
            }
            // se pone esta validacion tambien ya que si no hay ejemplares del libro tampoco puede prestarse
            // y saldria un erro 
            // esto se hace recogiendo el numero atribuidos a ese libro (isbn) que esta en la tabla libros
            int ejemplaresDisponibles = numerolibros(isbn);
            if (ejemplaresDisponibles <= 0) {
                mostrarAlerta("Error", "No hay ejemplares disponibles", "No hay ejemplares del libro en este momento.");
                limpiarDatos();
                return;
            }
            String titulo = obtenerTituloPorISBN(isbn);  
            // Se introduce los datos en la tabla y recoge el mismo dia al añadir el usuario y el isbn
            PreparedStatement st = controladorentrada.getConnection().prepareStatement( // LA FECHASALIDA ES LA FECHA DE PRESTAMO
                    "INSERT INTO prestamos (dniusuario, isbn, titulo, fechasalida) VALUES (?, ?, ?, CURRENT_DATE)");

            st.setString(1, dni);
            st.setInt(2, isbn);
            st.setString(3, titulo);

            st.executeUpdate();
            st.close();

            // Restar un ejemplar de la tabla de libros
            PreparedStatement stRestarEjemplar = controladorentrada.getConnection().prepareStatement(
                    "UPDATE libros SET ejemplares = ejemplares - 1 WHERE isbn = ?");

            stRestarEjemplar.setInt(1, isbn);
            stRestarEjemplar.executeUpdate();
            stRestarEjemplar.close();

            // Mostrar mensaje de confirmación
            mostrarAlerta("Confirmación", "Datos ingresados correctamente", "Los datos se han ingresado correctamente.");

            // Limpiar los campos después de ingresar los datos
            limpiarDatos();

        } catch (Exception e) {
            System.out.println(e.getMessage());

            // Mostrar el Alert en caso de error
            mostrarAlerta("Error", "Error al insertar datos", "Error al insertar datos en la tabla de préstamos.");
        }
    }

    //METODO para saber los ejemplares que tiene un libro 
    private int numerolibros(int isbn) throws SQLException {
        PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT ejemplares FROM libros WHERE isbn = ?");
        st.setInt(1, isbn);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            return rs.getInt("ejemplares");
        } else {
            return 0;
        }
    }

    // METODO para obtener el titulo de ese isbn de la tabla libros para ponerlo en la tabla de prestamos 
    // al ponerle el titulo en la tabla puedo luego hacer una busqueda por titulo tambien 
    private String obtenerTituloPorISBN(int isbn) {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT titulo FROM libros WHERE isbn = ?");
            st.setInt(1, isbn);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("titulo");
            } else {
                return ""; // Devuelve una cadena vacía si no se encuentra el título
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ""; // En caso de error, también devuelve una cadena vacía
        }
    }

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

    //METODO para buscar el dni en la tabla por SQL y saber si el usuario existe o no (para la validacion )
    private boolean usuarioExiste(String dni) {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT * FROM usuarios WHERE dni = ?");
            st.setString(1, dni);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //METODO para buscar el isbn en la tabla por SQL
    private boolean isbnExiste(int isbn) { // tambien para la validacion 
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT * FROM libros WHERE isbn = ?");
            st.setInt(1, isbn);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void limpiarDatos() {
        txtcodigo.getSelectionModel().clearSelection();
        txtusuario.getSelectionModel().clearSelection();
    }
//cargamos los datos
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.controladorentrada = new ControladorEntrada();
        this.controladorentrada.getConnection();

        nombresusuarios = FXCollections.observableArrayList();
        filtrousuarios = new FilteredList<>(nombresusuarios, p -> true);
        txtusuario.setItems(filtrousuarios);
        txtusuario.setPromptText("Seleccione el DNI del usuario");
        txtusuario.setEditable(true);

        TextField txtCombo = txtusuario.getEditor();
        txtCombo.textProperty().addListener((obs, oldValue, newValue) -> {
            final String usuarioSeleccionado = txtusuario.getSelectionModel().getSelectedItem();
            txtusuario.show();
            if (usuarioSeleccionado == null || !usuarioSeleccionado.equals(txtCombo.getText())) {
                filtrousuarios.setPredicate(item -> item.toLowerCase().contains(newValue.toLowerCase()));
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

        cargarISBN();
    }

    private void cargarusuarios() {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT dni, nombre FROM usuarios");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                nombresusuarios.add(rs.getString("dni") + ", Nombre : " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarISBN() {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement("SELECT isbn ,titulo FROM libros where ejemplares > 0");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                codigos.add(Integer.toString(rs.getInt("isbn")) + ", Titulo : " + rs.getString("titulo"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //METODO para saber si el usuario ya tiene en prestamo ese libro 
    private boolean librorepetido(String dni, int isbn) {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement(
                    "SELECT * FROM prestamos WHERE dniusuario = ? AND isbn = ? AND fechadevo IS NULL");
            st.setString(1, dni);
            st.setInt(2, isbn);
            ResultSet rs = st.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }

    }
}
