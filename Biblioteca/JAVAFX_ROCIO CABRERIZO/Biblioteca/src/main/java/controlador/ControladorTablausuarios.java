package controlador;

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
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;
import modelo.Libros;
import modelo.Prestamo;

//CONTROLADOR PARA MOSTRAR LOS DATOS DDE LA TABLA USUARIOS, ELIMAR Y BUSCAR POR MEDIO DE FILTRADO 
public class ControladorTablausuarios implements Initializable {
    private ControladorTablausuarios controladortablausuarios;
    private ControladorTablaLibros controladortablalibros;
    private ControladorEntrada controladorentrada;
    private ControladorEditar controladoreditar;
    private ControladorNuevousuario controladornuevousuario;
    private LibrosPrestados librosprestados;
    private ControladorPrestar controladorprestar;
    private Usuario usuario;
   

    private ExecutorService executor;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;
    }
    
    public void setControladorTablausuarios(ControladorTablausuarios controladortablausuarios) {
        this.controladortablausuarios = controladortablausuarios;
    }
    
    public void setControladorTablaLibros(ControladorTablaLibros controladortablalibros) {
        this.controladortablalibros = controladortablalibros;
    }
    
    public void setControladorPrestar(ControladorPrestar controladorprestar) {
        this.controladorprestar = controladorprestar;
    }
    
    

    @FXML
    private TableView<Usuario> TableViewUsuarios;

    @FXML
    private Button btnnuevo;

    @FXML
    private TableColumn<Usuario, String> columdni;

    @FXML
    private TableColumn<Usuario, String> columdomicilio;

    @FXML
    private TableColumn<Usuario, String> columnombre;

    @FXML
    private TableColumn<Usuario, String> columprime;

    @FXML
    private TableColumn<Usuario, String> columsegundo;

    @FXML
    private TableColumn<Usuario, Integer> columtelefono;

    @FXML
    private Label columusuario;

    @FXML
    private Pane paneprincipal;

    @FXML
    private TextField txtbusqueda;
    
    @FXML
    void btnnuevo(ActionEvent event) {
        
        try {
            // Cargar el archivo FXML de la ventana de préstamos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/nuevousuario.fxml"));
            Pane ventanaPrestamos = loader.load();

            // Limpiar el Pane y agregar la nueva ventana de préstamos
            paneprincipal.getChildren().clear();
            paneprincipal.getChildren().add(ventanaPrestamos);

            // Inicializar controladoreditarlibro
            ControladorNuevousuario controladornuevousuario = loader.getController();
            if (controladornuevousuario != null) {
                controladornuevousuario.setControladorNuevousuario(controladornuevousuario);

            } else {
                System.out.println("Error: No se pudo obtener el controlador de edición.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    void btnborrar(ActionEvent event) {
        Usuario selecionar = TableViewUsuarios.getSelectionModel().getSelectedItem();
        if (selecionar != null) {
            String dni = selecionar.getDni();
            // al selecionar el dato saltara un alert para asegurar la confirmacion 
            Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmacion.setTitle("Confirmación");
            alertConfirmacion.setHeaderText(null);
            alertConfirmacion.setContentText("¿Estás seguro de que deseas eliminar este registro?");

            // en el cuadro de alert se insertan unos botones para confirmar 
            ButtonType buttonOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alertConfirmacion.getButtonTypes().setAll(buttonOK, buttonCancel);

            Optional<ButtonType> resultado = alertConfirmacion.showAndWait();
            // procesar la respuesta del usuario
            if (resultado.isPresent() && resultado.get() == buttonOK) {
                try {
                    Connection connection = controladorentrada.getConnection();
                    
                    PreparedStatement stPrestamos = connection.prepareStatement(
                            "DELETE FROM prestamos WHERE dniusuario = ?");
                    stPrestamos.setString(1, dni);
                    stPrestamos.executeUpdate();
                    stPrestamos.close();
                    
                    PreparedStatement st = connection.prepareStatement(
                            "DELETE FROM usuarios WHERE dni = ?  ");
                    st.setString(1, dni);
                    st.executeUpdate();
                    st.close();

                    // Mostrar un mensaje de borrado exitoso
                    Alert alertExito = new Alert(Alert.AlertType.INFORMATION);
                    alertExito.setTitle("Éxito");
                    alertExito.setHeaderText(null);
                    alertExito.setContentText("El registro se ha eliminado con éxito.");
                    alertExito.showAndWait();

                    TableViewUsuarios.getItems().remove(selecionar);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    // Mostrar el Alert en caso de error
                    Alert alertError = new Alert(Alert.AlertType.ERROR);
                    alertError.setTitle("Error");
                    alertError.setHeaderText(null);
                    alertError.setContentText("Error al intentar eliminar datos de la base de datos");
                    alertError.showAndWait();
                }
            }
        } else {
            // ocurre si le damos al boton de elimiar y no hemos seleccionado nada 
            Alert alertAdvertencia = new Alert(Alert.AlertType.WARNING);
            alertAdvertencia.setTitle("Advertencia");
            alertAdvertencia.setHeaderText(null);
            alertAdvertencia.setContentText("Por favor, seleccione una fila para eliminar.");
            alertAdvertencia.showAndWait();
        }
    }

    public TableView<Usuario> getTableViewUsuarios() {
        return TableViewUsuarios;
    }

    @FXML
    void btneditar(ActionEvent event) {
        Usuario selecion = TableViewUsuarios.getSelectionModel().getSelectedItem();
        llamarVentanaActualizar(selecion);
    }

    /* METODO QUE SE USA PARA LLAMAR A LA VENTANA PARA ACUTUALIZAR
    SE LLAMA CON DOBLE CLICK Y CON EL BOTON ACTUALIZAR */
    private void llamarVentanaActualizar(Usuario selecion) {
        if (selecion != null) {
            try {
                // Cargar el archivo FXML de la ventana de préstamos
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/editarusuario.fxml"));
                Pane ventanaPrestamos = loader.load();

                // Limpiar el Pane y agregar la nueva ventana de préstamos
                paneprincipal.getChildren().clear();
                paneprincipal.getChildren().add(ventanaPrestamos);

                // Inicializar controladoreditarlibro
                ControladorEditar controladoreditar = loader.getController();
                if (controladoreditar != null) {
                    controladoreditar.setControladorTabla(this);
                    controladoreditar.setDatos(selecion);
                } else {
                    System.out.println("Error: No se pudo editar.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona una fila para actualizar.");
            alert.showAndWait();
        }
    }

    // para mostrar los datos de la base de datos en la tabla de la interfaz
    public ObservableList<Usuario> damedatos() {
        ObservableList<Usuario> listausuarios = FXCollections.observableArrayList();
        // Connection connection = controlador.getConnection();
        Connection connection = controladorentrada.getConnection();
        if (connection != null) {
            String query = "SELECT * FROM usuarios";
            Statement st;
            ResultSet rs;

            try {
                st = connection.createStatement();
                rs = st.executeQuery(query);
                Usuario usuario;
                while (rs.next()) {
                    usuario = new Usuario(
                            rs.getString("dni").toUpperCase(),
                            rs.getString("nombre").substring(0,1).toUpperCase()+rs.getString("nombre").substring(1).toLowerCase(),
                            rs.getString("primerapellido").substring(0,1).toUpperCase()+rs.getString("primerapellido").substring(1).toLowerCase(),
                            rs.getString("segundoapellido").substring(0,1).toUpperCase()+rs.getString("segundoapellido").substring(1).toLowerCase(),
                            rs.getString("domicilio").substring(0,1).toUpperCase()+rs.getString("domicilio").substring(1).toLowerCase(),
                            rs.getInt("telefono"),
                            rs.getBoolean("sancion"),
                            rs.getString("cuanto"));

                    listausuarios.add(usuario);
                }
            } catch (SQLException e) {

            }
            return listausuarios;
        }
        return null;
    }

    public void mostrardatos() {
        CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> TableViewUsuarios.setItems(damedatos()));
        });
    }

    // PARA REALIZAR LA BUSQUEDA FILTRANDO
    private void filtrartabla(String dni) {
        if (dni.isEmpty()) {
            TableViewUsuarios.setItems(damedatos());
        } else {
            ObservableList<Usuario> resultados = damedatos().filtered(usuario -> {
                String dniusuario = usuario.getDni();
                return dniusuario != null && dniusuario.contains(dni);
            });
            TableViewUsuarios.setItems(resultados);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.controladorentrada = new ControladorEntrada(); 
        this.controladorentrada.getConnection(); 

        //mostrar mensajes de aviso al pasar el raton (Tooltip) 
        TableViewUsuarios.setRowFactory(tv -> {
            TableRow<Usuario> row = new TableRow<>();
            Tooltip tooltip = new Tooltip();
            tooltip.setText("Con doble click podra EDITAR el usuario ");
            Tooltip.install(row, tooltip);
            return row;
        });
         
        //para poner texto indicativo en el cuadro de texto busqueda 
        txtbusqueda.setPromptText("BÚSQUEDA, introduzca DNI del Usuario");
        // este bind hace que cada vez que se introduce texto en el cuadro de texto de busqueda
        // se activa el metodo qur filtra la tabla 
        txtbusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrartabla(newValue);
        });

        // Accion de doble click sobre filas
        TableViewUsuarios.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Usuario selecionar = TableViewUsuarios.getSelectionModel().getSelectedItem();
                llamarVentanaActualizar(selecionar);
            }
        });

        //con esto ajustamos la tabla a lo que se escribe dentro
        TableViewUsuarios.autosize();

        ObservableList<Usuario> lista = damedatos();

        columdni.setCellValueFactory(new PropertyValueFactory<Usuario, String>("dni"));
        columnombre.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nombre"));
        columprime.setCellValueFactory(new PropertyValueFactory<Usuario, String>("primerapellido"));
        columsegundo.setCellValueFactory(new PropertyValueFactory<Usuario, String>("segundoapellido"));
        columdomicilio.setCellValueFactory(new PropertyValueFactory<Usuario, String>("domicilio"));
        columtelefono.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("telefono"));

        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> mostrardatos());

        // lanzar con ExecutorService
        // mostrardatos();
    }

}
