
package controlador;

import controlador.ControladorEntrada;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modelo.Usuario;
import java.util.concurrent.ExecutorService;
import javafx.scene.control.ComboBox;
import modelo.Libros;

/**
 *
 * @author cabre
 */
public class ControladorBusqueda implements Initializable {

    private ControladorEntrada controladorentrada;
    private ControladorLibroNuevo controladorlibronuevo;
    private ControladorEditarLibro controladoreditarlibro;
    //   private ControladorEditarLibro controladoreditarlibro;
    private ControladorBusqueda controladorbusqueda;
    private ControladorTablaLibros controladortablalibros;
    private Libros libro;

    private ExecutorService executor;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;

    }

    public void setControladorbusqueda(ControladorBusqueda controladorbusqueda) {
        this.controladorbusqueda = controladorbusqueda;
    }

    public void setControladorTablaLibros(ControladorTablaLibros controladortablalibros) {
        this.controladortablalibros = controladortablalibros;
    }

    @FXML
    private ComboBox<String> combogenero;

    @FXML
    private ComboBox<String> comboidioma;

    @FXML
    private ComboBox<String> combotipo;

    @FXML
    private TableView<Libros> tableViewLibros;

    @FXML
    private TableColumn<Libros, String> columtipo;

    @FXML
    private TableColumn<Libros, String> autorcolum;

    @FXML
    private Label labellibros;

    @FXML
    private TableColumn<Libros, String> descripcioncolum;

    @FXML
    private TableColumn<Libros, Integer> ejemplarescolum;

    @FXML
    private TableColumn<Libros, String> generocolum;

    @FXML
    private TableColumn<Libros, String> idiomacolum;

    @FXML
    private TableColumn<Libros, Integer> isbncolum;

    @FXML
    private Pane paneprincipal;

    @FXML
    private TableColumn<Libros, String> fechapublicacioncolum;

    @FXML
    private TableColumn<Libros, String> titulocolum;

    @FXML
    void btncerrar(ActionEvent event) {
        // para cerrar la ventana en la que nos encontramos
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void cargardatosCombo() {
        // COMBO GENERO
        combogenero.setPromptText("Género");
        combogenero.getItems().addAll("  ","Comedia", "Fantasía", "Terror", "Suspense", "Infantil");

        // COMBO DESCRIPCION 
        comboidioma.setPromptText("Idioma");
        comboidioma.getItems().addAll("  ","Español", "Ingles", "Aleman", "Chino");

        // COMBO TIPO
        combotipo.setPromptText("Tipo");
        combotipo.getItems().addAll("  ","Audiolibro", "Electronico", "Fisico");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.controladorentrada = new ControladorEntrada();
        this.controladorentrada.getConnection();

        cargardatosCombo();

        // Ajustamos la tabla para que se adapte al contenido
        tableViewLibros.autosize();
      // Configura las columnas de la tabla

        // Llama al método mostrardatoslibros() de ControladorTablaLibros para cargar los datos en la tabla
        if (controladortablalibros != null) { // Verifica si controladortablalibros está inicializado
            controladortablalibros.mostrardatoslibros();
        }
    }
    


}
