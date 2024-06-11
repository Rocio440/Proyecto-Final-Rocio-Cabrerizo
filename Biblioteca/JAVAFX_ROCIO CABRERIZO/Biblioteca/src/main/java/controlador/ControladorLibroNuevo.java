package controlador;

import java.io.IOException;
import java.sql.SQLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import modelo.Libros;

//CONTROLADOR PARA INTRODUCIR LIBROS NUEVOS 
public class ControladorLibroNuevo implements Initializable {

    private ControladorEntrada controladorentrada;
    private Libros libro;
    private ControladorTablaLibros controladortablalibros;
    private ControladorLibroNuevo controladorlibronuevo;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;
    }

    public void setControladorTablaLibro(ControladorLibroNuevo controladorlibronuevo) {
        this.controladorlibronuevo = controladorlibronuevo;
    }

    public void setControladorLibroNuevo(ControladorTablaLibros controladortablalibros) {
        this.controladortablalibros = controladortablalibros;
    }

    @FXML
    private ComboBox<String> combotipo;

    @FXML
    private ComboBox<String> combodescripcion;

    @FXML
    private ComboBox<String> combogenero;

    @FXML
    private Pane paneprincipal;

    @FXML
    private TextField txtautor;

    @FXML
    private TextField txtejemplares;

    @FXML
    private DatePicker txtfecha;

     @FXML
    private ComboBox<String> txtidioma;

    @FXML
    private TextField txtisbn;

    @FXML
    private TextField txttitulo;

    @FXML
    void btnsubir(ActionEvent event) {
        try {
            // Verificar si todos los campos están completos y validaciones 
            if (!camposValidos()) {
                // Mostrar Alert si no todos los datos están completos
                mostrarAlerta("Error", "Por favor, complete todos los campos.");
                return;
            }

            int isbn = Integer.parseInt(txtisbn.getText());
            String titulo = txttitulo.getText();
            String fecha = txtfecha.getValue().toString();
            String autor = txtautor.getText();
            String tipo = combotipo.getValue();
            String genero = combogenero.getValue();
            String idioma = txtidioma.getValue();
            String descripcion = combodescripcion.getValue();
            int ejemplares = Integer.parseInt(txtejemplares.getText());

            // Verificar si el libro ya existe en la base de datos
            PreparedStatement ps = controladorentrada.getConnection().prepareStatement(
                    "SELECT * FROM libros WHERE isbn = ?");
            ps.setInt(1, isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Si el libro ya existe, actualizar el número de ejemplares, sumandole a los ya existentes
                //los que añadimos en el cuadro de ejemplares
                int ejemplaresActuales = rs.getInt("ejemplares");
                ejemplares += ejemplaresActuales;
                //hago UPDATE por los ejemplares para añadir si ese libro ya esta metido 
                PreparedStatement stUpdate = controladorentrada.getConnection().prepareStatement(
                        "UPDATE libros SET ejemplares = ? WHERE isbn = ?");
                stUpdate.setInt(1, ejemplares);
                stUpdate.setInt(2, isbn);
                stUpdate.executeUpdate();
                stUpdate.close();
            } else {
                // Si el libro no existe, se hace un INSERT 
                PreparedStatement stInsert = controladorentrada.getConnection().prepareStatement(
                        "INSERT INTO libros(isbn, titulo, fechapublicacion, autor, tipo, genero, idioma, descripcion, ejemplares) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                stInsert.setInt(1, isbn);
                stInsert.setString(2, titulo);
                stInsert.setString(3, fecha);
                stInsert.setString(4, autor);
                stInsert.setString(5, tipo);
                stInsert.setString(6, genero);
                stInsert.setString(7, idioma);
                stInsert.setString(8, descripcion);
                stInsert.setInt(9, ejemplares);
                stInsert.executeUpdate();
                stInsert.close();
            }

            // Mostrar Alert después de la inserción o actualización exitosa
            mostrarAlerta("Confirmación", "Datos Ingresados correctamente");

            // Limpiar los campos después de mostrar el mensaje de éxito
            limpiarCampos();

            //volver al panel de tabla de libros 
            try {
                // Cargar el archivo FXML de la ventana de préstamos
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/libros.fxml"));
                Pane ventanaPrestamos = loader.load();

                // Limpiar el Pane y agregar la nueva ventana de préstamos
                paneprincipal.getChildren().clear();
                paneprincipal.getChildren().add(ventanaPrestamos);

                // Inicializar controladoreditarlibro
                ControladorTablaLibros controladortablalibros = loader.getController();
                if (controladortablalibros != null) {
                    controladortablalibros.setControladorTablaLibros(controladortablalibros);

                } else {
                    System.out.println("Error: No se pudo editar.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Para actualizar la tabla de libros
            //  controladortablalibros.mostrardatoslibros();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Método para validar 
    private boolean camposValidos() {
        // si todos los campos están completos
        if (txtisbn.getText().isEmpty() || txttitulo.getText().isEmpty()
                || txtfecha.getValue() == null || txtautor.getText().isEmpty()
                || combotipo.getValue() == null
                || combogenero.getValue() == null || txtidioma.getValue() == null
                || combodescripcion.getValue() == null || txtejemplares.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios");
            return false;
        }
        // Validar si el isbn contiene solo dígitos
        if (!txtisbn.getText().matches("\\d+")) {
            mostrarAlerta("Formato incorrecto", "Ingrese solo números en el campo de ISBN");
            return false;
        }

        // Validar si el ejemplares solo son numeros
        if (!txtejemplares.getText().matches("\\d+")) {
            mostrarAlerta("Formato incorrecto", "Ingrese solo números en el campo Ejemplares");
            return false;
        }
        return true;
    }

    // METODO para cargar los los combo 
    public void cargardatosCombo() {
        // COMBO GENERO
        combogenero.setPromptText("Selecciona un Género");
        combogenero.getItems().addAll("Comedia", "Fantasía", "Terror", "Suspense", "Infantil");

        // COMBO DESCRIPCION 
        combodescripcion.setPromptText("Selecciona una Descripción");
        combodescripcion.getItems().addAll("Para niños en edad preadolescente (10-12 años)", "Para adolescentes (13-18 años)", "Para jóvenes adultos (18-25 años)", "Para adultos jóvenes ( <25 años)");

        // COMBO TIPO
        combotipo.setPromptText("Selecciona un Tipo");
        combotipo.getItems().addAll("Audiolibro", "Electronico", "Fisico");
        
        // COMBO IDIOMA
        txtidioma.setPromptText("Selecciona un Idioma");
        txtidioma.getItems().addAll("Inglés", "Español", "Alemán", "Chino", "Francés");
    }

    //METODO para limpiar la ventana de los datos añadidos 
    private void limpiarCampos() {
        txtisbn.clear();
        txttitulo.clear();
        txtfecha.setValue(null);
        txtautor.clear();
        combotipo.getSelectionModel().clearSelection();
        combogenero.getSelectionModel().clearSelection();
        txtidioma.getSelectionModel().clearSelection();
        combodescripcion.getSelectionModel().clearSelection();
        txtejemplares.clear();
        
    }

    //METODO para crear alertas 
    private void mostrarAlerta(String titulo, String contenido) {
        // Mostrar un diálogo de alerta con el mensaje especificado
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar la conexión con la base de datos
        this.controladorentrada = new ControladorEntrada();
        this.controladorentrada.getConnection();

        // 
        cargardatosCombo();

        //AGREGO UN LISTENER PARA EL CAMPO ISBN PARA DETECTAR UN ISBN QUE SEA IGUAL 
        // de tal manera que si meto un isbn que ya tengo me apareceran todos los datos
        // de ese libro salvo los ejemplares que añadire manualmente para sumarselos
        // a los que ya hay segun lo que ponga 
        txtisbn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    int isbn = Integer.parseInt(newValue);
                    // Consultar la base de datos para obtener los datos del libro con el ISBN proporcionado
                    PreparedStatement ps = controladorentrada.getConnection().prepareStatement(
                            "SELECT * FROM libros WHERE isbn = ?");
                    ps.setInt(1, isbn);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        // Si se encuentra un libro con el ISBN proporcionado, llenar los campos con los datos
                        txttitulo.setText(rs.getString("titulo"));
                        txtfecha.setValue(rs.getDate("fechapublicacion").toLocalDate());
                        txtautor.setText(rs.getString("autor"));
                        combotipo.setValue(rs.getString("tipo"));
                        combogenero.setValue(rs.getString("genero"));
                        txtidioma.setValue(rs.getString("idioma"));
                        combodescripcion.setValue(rs.getString("descripcion"));

                    } else {
                        // Si no se encuentra un libro con el ISBN proporcionado, limpiar los campos
                        //no uso el mmetodo porque me daraia error ya que en el metodo coge el isbn y lo limpia
                        // y de esa manera no podria añadir nunca 
                        txttitulo.clear();
                        txtfecha.setValue(null);
                        txtautor.clear();
                        combotipo.getSelectionModel().clearSelection();
                        combogenero.getSelectionModel().clearSelection();
                        txtidioma.getSelectionModel().clearSelection();
                        combodescripcion.getSelectionModel().clearSelection();
                        txtejemplares.clear();
                        

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
