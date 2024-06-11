/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modelo.Libros;
import modelo.Usuario;

/**
 *
 * @author cabre
 */
//CONTROLADOR PARA EDITAR LOS DATOS DE LOS LIBROS DE LA TABLA LIBROS 
public class ControladorEditarLibro implements Initializable {

    private ControladorEntrada controladorentrada;
    private ControladorTablaLibros controladortablalibros;
    private ControladorEditarLibro controladoreditarlibro;
    private ControladorLibroNuevo controladorlibronuevo;

    private Libros libros;
    private ExecutorService executor;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;
    }

    public void setControladorTablaLibros(ControladorTablaLibros controladortablalibros) {
        this.controladortablalibros = controladortablalibros;
    }

    public void setControladorEditarLibro(ControladorEditarLibro controladorEditarLibro) {
        this.controladoreditarlibro = controladorEditarLibro;
    }

    public void setControladorLibroNuevo(ControladorTablaLibros controladortablalibros) {
        this.controladortablalibros = controladortablalibros;
    }

    @FXML
    private ComboBox<String> combodescripcion;

    @FXML
    private ComboBox<String> combogenero;

    @FXML
    private ComboBox<String> combotipo;

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

    //Se recogen los datos para mostrarlos en la ventana que se abre para editar 
    public void setDatosLibro(Libros libro) {
        txtisbn.setText(String.valueOf(libro.getIsbn()));
        txttitulo.setText(libro.getTitulo());
        txtfecha.setValue(LocalDate.parse(libro.getFechapublicacion()));
        txtautor.setText(libro.getAutor());
        combotipo.setValue(libro.getGenero());
        combogenero.setValue(libro.getGenero());
        txtidioma.setValue(libro.getIdioma());
        combodescripcion.setValue(libro.getDescripcion());
        txtejemplares.setText(String.valueOf(libro.getEjemplares()));

    }

    //METODO para rellenar los combobox 
    // este metodo luego se añade en Intialize para que los datos se cargen al abrir la pagina 
    private void cargardatosCombo() {
        // COMBO GENERO
        combogenero.setPromptText("Género");
        combogenero.getItems().addAll("Comedia", "Fantasía", "Histórica", "Ciencia Ficción", "Terror", "Suspense", "Infantil");

        // COMBO DESCRIPCION 
        combodescripcion.setPromptText("Selecciona una Descripción");
        combodescripcion.getItems().addAll("Para niños en edad preadolescente (10-12 años)", "Para adolescentes (13-18 años)", "Para jóvenes adultos (18-25 años)", "Para adultos jóvenes ( <25 años)");

        // COMBO TIPO
        combotipo.setPromptText("Tipo");
        combotipo.getItems().addAll("Audiolibro", "Electrónico", "Físico");
        
        // COMBO IDIOMA
        txtidioma.setPromptText("Selecciona un Idioma");
        txtidioma.getItems().addAll("Inglés", "Español", "Alemán", "Chino", "Francés");

    }

    @FXML
    void btnsubir(ActionEvent event) {

        if (camposValidos()) {
            int isbn = Integer.parseInt(txtisbn.getText());
            String titulo = txttitulo.getText();
            if (!titulo.isEmpty()) {
                titulo = titulo.substring(0, 1).toUpperCase() + titulo.substring(1).toLowerCase();
            }
            String fecha = txtfecha.getValue().toString();
            String autor = txtautor.getText();
            if (!autor.isEmpty()) {
                autor = autor.substring(0, 1).toUpperCase() + autor.substring(1).toLowerCase();
            }
            String tipo = combotipo.getValue();
            String genero = combogenero.getValue();
            String idioma = txtidioma.getValue();
           /* if (!idioma.isEmpty()) {
                idioma = idioma.substring(0, 1).toUpperCase() + idioma.substring(1).toLowerCase();
            }*/
            String descripcion = combodescripcion.getValue();
            int ejemplares = Integer.parseInt(txtejemplares.getText());

            try {

                // Sentencia SQL para settear los datos 
                String sql = "UPDATE libros SET titulo = ?, fechapublicacion = ?,"
                        + " autor = ?, tipo = ?, genero = ?, idioma = ?,"
                        + " descripcion = ?, ejemplares = ? WHERE isbn = ?";
                PreparedStatement statement = controladorentrada.getConnection().prepareStatement(sql);

                statement.setString(1, titulo);
                statement.setString(2, fecha);
                statement.setString(3, autor);
                statement.setString(4, tipo);
                statement.setString(5, genero);
                statement.setString(6, idioma);
                statement.setString(7, descripcion);
                statement.setInt(8, ejemplares);
                statement.setInt(9, isbn);

                statement.executeUpdate();
                statement.close();

                controladortablalibros.mostrardatoslibros();
                // Mostrar Alert después de la actualización exitosa
                mostrarAlerta("Confirmación", "Datos actualizados correctamente");

                // volver al panel de la tabla libros despues de editar
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/libros.fxml"));
                    Pane ventanaPrestamos = loader.load();

                    // Limpiar el Pane y agregar la nueva ventana de libros
                    paneprincipal.getChildren().clear();
                    paneprincipal.getChildren().add(ventanaPrestamos);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (SQLException e) {
                mostrarAlerta("Error", "Error al actualizar los datos del libro en la base de datos.");
                System.out.println(e.getMessage());
            }
        }
    }

    // METODO para validar los campos 
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
        return true;
    }

    //METODO para realizar alertas
    private void mostrarAlerta(String titulo, String contenido) {
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
        cargardatosCombo();

    }

}
