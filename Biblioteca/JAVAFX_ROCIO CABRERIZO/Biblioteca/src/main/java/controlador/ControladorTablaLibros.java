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
import java.util.ArrayList;
import java.util.List;
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
import java.util.function.Predicate;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import modelo.Libros;
import modelo.Prestamo;

//CONTROLADOR QUE MUESTRA LOS DATOS EN LA TABLA Y ELIMINA Y POR DONDE SE VA A LA BUSQUEDA POR MEDIO DEL BOTON
public class ControladorTablaLibros implements Initializable {

    private ControladorEntrada controladorentrada;
    private ControladorLibroNuevo controladorlibronuevo;
    private ControladorEditarLibro controladoreditarlibro;
    private ControladorTablaLibros controladortablalibros;
    private ControladorBusqueda controladorbusqueda;

    private ControladorTablausuarios controladortablausuarios;
    private Libros libro;
    private Usuario usuarios;

    private ExecutorService executor;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;
        this.setControladorTablausuarios(controladortablausuarios);
    }

    public void setControladorbusqueda(ControladorBusqueda controladorbusqueda) {
        this.controladorbusqueda = controladorbusqueda;
    }

    public void setControladorTablaLibros(ControladorTablaLibros controladortablalibros) {
        this.controladortablalibros = controladortablalibros;
    }

    public void setControladorTablausuarios(ControladorTablausuarios controladortablausuarios) {
        this.controladortablausuarios = controladortablausuarios;
    }

    ControladorPrestar controladorprestar = new ControladorPrestar(); 
//    controladortablalibros.setControladorPrestar(controladorprestar);

    public void setControladorPrestar(ControladorPrestar controladorprestar) {
        this.controladorprestar = controladorprestar;
    }

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
    private ComboBox<String> combogenero;

    @FXML
    private ComboBox<String> comboidioma;

    @FXML
    private ComboBox<String> combotipo;

    private void cargardatosCombo() {
        // COMBO GENERO
        combogenero.setPromptText("Género");
        combogenero.getItems().addAll("Comedia", "Fantasía", "Histórica", "Ciencia Ficción", "Terror", "Suspense", "Infantil");

        // COMBO DESCRIPCION 
        comboidioma.setPromptText("Idioma");
        comboidioma.getItems().addAll("Español", "Inglés", "Aleman", "Chino");

        // COMBO TIPO
        combotipo.setPromptText("Tipo");
        combotipo.getItems().addAll("Audiolibro", "Electrónico", "Físico");
    }

    @FXML
    void btnnuevo(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la ventana de préstamos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/nuevo.fxml"));
            Pane ventanaPrestamos = loader.load();

            // Limpiar el Pane y agregar la nueva ventana de préstamos
            paneprincipal.getChildren().clear();
            paneprincipal.getChildren().add(ventanaPrestamos);

            // Inicializar controladoreditarlibro
            ControladorLibroNuevo controladorlibronuevo = loader.getController();
            if (controladorlibronuevo != null) {
                controladorlibronuevo.setControladorLibroNuevo(this);

            } else {
                System.out.println("Error: No se pudo obtener el controlador de edición.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnborrar(ActionEvent event) {
        Libros selecionar = tableViewLibros.getSelectionModel().getSelectedItem();
        if (selecionar != null) {
            int isbn = selecionar.getIsbn();

            Alert alertConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            alertConfirmacion.setTitle("Confirmación");
            alertConfirmacion.setHeaderText(null);
            alertConfirmacion.setContentText("¿Estás seguro de que deseas eliminar este registro?");

            ButtonType buttonOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alertConfirmacion.getButtonTypes().setAll(buttonOK, buttonCancel);

            Optional<ButtonType> resultado = alertConfirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == buttonOK) {
                try {
                    Connection connection = controladorentrada.getConnection();

                    PreparedStatement stPrestamos = connection.prepareStatement(
                            "DELETE FROM prestamos WHERE isbn = ?");
                    stPrestamos.setInt(1, isbn);
                    stPrestamos.executeUpdate();
                    stPrestamos.close();

                    PreparedStatement st = connection.prepareStatement(
                            "DELETE FROM libros WHERE isbn = ?");
                    st.setInt(1, isbn);
                    st.executeUpdate();
                    st.close();

                    // Mostrar mensaje de confirmación
                    mostrarAlerta("Confirmación", "Éxito", "El registro se ha eliminado con éxito.");

                    tableViewLibros.getItems().remove(selecionar);

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
            Alert alertAdvertencia = new Alert(Alert.AlertType.WARNING);
            alertAdvertencia.setTitle("Advertencia");
            alertAdvertencia.setHeaderText(null);
            alertAdvertencia.setContentText("Por favor, seleccione una fila para eliminar.");
            alertAdvertencia.showAndWait();
        }
    }

    public TableView<Libros> getTableViewLibros() {
        return tableViewLibros;
    }

    @FXML
    void btnlimpiar(ActionEvent event) {
        combotipo.getSelectionModel().clearSelection();
        combogenero.getSelectionModel().clearSelection();
        comboidioma.getSelectionModel().clearSelection();

        //para poner texto indicativo 
        combogenero.setPromptText("Genero");
        comboidioma.setPromptText("Idioma");
        combotipo.setPromptText("Tipo");
    }

    @FXML
    void btneditar(ActionEvent event) {
        Libros selecion = tableViewLibros.getSelectionModel().getSelectedItem();
        llamarVentanaActualizar(selecion);
    }
    // con este metodo al darle al boton de editar o usar el doble click el pane se cambiara y mostrara el
    // mismo panel que se usa para añadir nuevos libros con los datos del elemento selecionado para la edición 
    // esto se maneja en el controlador editar libro 

    private void llamarVentanaActualizar(Libros selecion) {
        if (selecion != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/editarlibro.fxml"));
                Pane ventanaPrestamos = loader.load();

                // Limpiar el Pane y agregar la nueva ventana de préstamos
                paneprincipal.getChildren().clear();
                paneprincipal.getChildren().add(ventanaPrestamos);

                // Inicializar controladoreditarlibro
                ControladorEditarLibro controladoreditar = loader.getController();
                if (controladoreditar != null) {
                    controladoreditar.setControladorTablaLibros(this);
                    controladoreditar.setDatosLibro(selecion);
                } else {
                    System.out.println("Error: No se pudo obtener el controlador de edición.");
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
    public ObservableList<Libros> damedatoslibros() {
        ObservableList<Libros> listalibros = FXCollections.observableArrayList();
        // Connection connection = controlador.getConnection();
        Connection connection = controladorentrada.getConnection();
        if (connection != null) {
            String query = "SELECT * FROM libros";
            Statement st;
            ResultSet rs;

            try {
                st = connection.createStatement();
                rs = st.executeQuery(query);
                Libros libro;
                while (rs.next()) {
                    libro = new Libros(
                            rs.getInt("isbn"),
                            rs.getString("titulo").substring(0, 1).toUpperCase() + rs.getString("titulo").substring(1).toLowerCase(),
                            rs.getString("fechapublicacion"),
                            rs.getString("autor").substring(0, 1).toUpperCase() + rs.getString("autor").substring(1).toLowerCase(),
                            rs.getString("tipo"),
                            rs.getString("genero"),
                            rs.getString("idioma"),
                            rs.getString("descripcion"),
                            rs.getInt("ejemplares"));

                    listalibros.add(libro);
                }
            } catch (SQLException e) {

            }
            return listalibros;
        }
        return null;
    }

    public void mostrardatoslibros() {
        CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> tableViewLibros.setItems(damedatoslibros()));
        });
    }
    private ObservableList<Libros> listalibros;
    private ObservableList<Usuario> listausuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.controladorentrada = new ControladorEntrada();
        this.controladorentrada.getConnection();

        // Ajustamos la tabla para que se adapte al contenido
        tableViewLibros.autosize();

        //mostrar mensajes de aviso al pasar el raton 
        tableViewLibros.setRowFactory(tv -> {
            TableRow<Libros> row = new TableRow<>();
            Tooltip tooltip = new Tooltip();
            tooltip.setText("Con doble click podra acceder al PRESTAMO del libro ");
            Tooltip.install(row, tooltip);
            return row;
        });

        // Obtenemos los datos de los libros para usarlo en el metodo de buscar por filtrado 
        listalibros = damedatoslibros(); // Inicializa listalibros

        // Llama al método filtrarTabla para aplicar el filtrado inicial
        filtrarTabla();
        cargardatosCombo();

        // se añaden listener para que se active cuando colocamos informacion en el combo 
        combogenero.setOnAction(event -> filtrarTabla());
        comboidioma.setOnAction(event -> filtrarTabla());
        combotipo.setOnAction(event -> filtrarTabla());
        //para poner texto indicativo 
        combogenero.setPromptText("Género");
        comboidioma.setPromptText("Idioma");
        combotipo.setPromptText("Tipo");

        //dejo de saltar la ventana de editar al tener lo otro  OJO
        // Con esto se consigue que al clicar dos veces en una row se abre la ventana de actualizar
        // que llamamos con el metodo llamarVentanaActualizar()
        /*
          tableViewLibros.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton. PRIMARY) { // Verificar si es un clic derecho
                Libros seleccion = tableViewLibros.getSelectionModel().getSelectedItem();
                if (seleccion != null) {
                    // Llamar al método para pasar el ISBN al controlador ControladorPrestar
                    llamarVentanaActualizar( seleccion);
                }
            }
            if (event.getButton() == MouseButton.SECONDARY) { // Verificar si es un clic derecho
                Libros seleccion = tableViewLibros.getSelectionModel().getSelectedItem();
                if (seleccion != null) {
                    // Llamar al método para pasar el ISBN al controlador ControladorPrestar
                    llamarisbn(seleccion);
                }
            }
        });*/
        // Evento de presionar la tecla Enter en la tabla PARA PRESTAMOS
        tableViewLibros.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) { // Verificar si se presionó la tecla Enter
                Libros seleccion = tableViewLibros.getSelectionModel().getSelectedItem();
                if (seleccion != null) {
                    // Llamar al método para pasar el ISBN al controlador ControladorPrestar
                    llamarisbn(seleccion);
                }
            }
        });

        // evento par ventan emergente para prestar 
        tableViewLibros.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                Libros seleccion = tableViewLibros.getSelectionModel().getSelectedItem();
                if (seleccion != null) {
                    abrirDialogo(seleccion);
                }
            }
        });

///////////////////////////////////////////////////////////////////////////////////////////////////
        // Obtenemos los datos de los libros
        ObservableList<Libros> listalibros = damedatoslibros();

        // Configuramos las columnas para que muestren los datos correspondientes
        isbncolum.setCellValueFactory(new PropertyValueFactory<Libros, Integer>("isbn"));
        titulocolum.setCellValueFactory(new PropertyValueFactory<Libros, String>("titulo"));
        fechapublicacioncolum.setCellValueFactory(new PropertyValueFactory<Libros, String>("fechapublicacion"));
        autorcolum.setCellValueFactory(new PropertyValueFactory<Libros, String>("autor"));
        columtipo.setCellValueFactory(new PropertyValueFactory<Libros, String>("tipo"));
        generocolum.setCellValueFactory(new PropertyValueFactory<Libros, String>("genero"));
        idiomacolum.setCellValueFactory(new PropertyValueFactory<Libros, String>("idioma"));
        descripcioncolum.setCellValueFactory(new PropertyValueFactory<Libros, String>("descripcion"));
        ejemplarescolum.setCellValueFactory(new PropertyValueFactory<Libros, Integer>("ejemplares"));

        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> mostrardatoslibros());

    }

    //METODO para filtrar tabla segun se ponga en los combo PARA BUSQUEDA
    private void filtrarTabla() {
        String genero = combogenero.getValue();
        String idioma = comboidioma.getValue();
        String tipo = combotipo.getValue();

        if (genero == null && idioma == null && tipo == null) {
            // Si no se ha seleccionado nada en los combose muestran todos los datos de la tabla 
            tableViewLibros.setItems(listalibros);
        } else {
            ObservableList<Libros> lista = listalibros.filtered(libro -> {
                boolean generotabla = genero == null || genero.isEmpty() || libro.getGenero().equalsIgnoreCase(genero);
                boolean idiomatabla = idioma == null || idioma.isEmpty() || libro.getIdioma().equalsIgnoreCase(idioma);
                boolean tipotabla = tipo == null || tipo.isEmpty() || libro.getTipo().equalsIgnoreCase(tipo);

                return generotabla && idiomatabla && tipotabla;
            });

            tableViewLibros.setItems(lista);
        }
    }
//METODO que nos lleva a la ventana de prestamos y se llama con tecla ENTER

    private void llamarisbn(Libros selecion) {
        if (selecion != null) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/prestamos.fxml"));
                Pane ventanaPrestamos = loader.load();

                // Limpiar el Pane y agregar la nueva ventana de préstamos
                paneprincipal.getChildren().clear();
                paneprincipal.getChildren().add(ventanaPrestamos);

                // Inicializar controladoreditarlibro
                ControladorPrestar controladorprestar = loader.getController();

                if (controladorprestar != null) {
                    // Pasar el ISBN al controlador de Prestar
                    controladorprestar.setDatoisbn(selecion.getIsbn());
                } else {
                    System.out.println("Error: No se pudo obtener el controlador de Prestar.");
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

// METODO para abrir ventana emergente para prestar directamente desde la tabla 
    private List<Pair<String, String>> damenombresusuarios() {
        List<Pair<String, String>> nombresusuarios = new ArrayList<>();
        ObservableList<Usuario> usuarios = controladortablausuarios.damedatos();

        for (Usuario usuario : usuarios) {
            String nombreCompleto = usuario.getNombre() + " " + usuario.getPrimerapellido();
            String dni = usuario.getDni();
            nombresusuarios.add(new Pair<>(nombreCompleto, dni));
        }

        return nombresusuarios;
    }

    private void abrirDialogo(Libros libro) {
        Dialog<Pair<String, String>> dialogo = new Dialog<>();
        dialogo.setTitle("Préstamo");

        Label a = new Label("Título");
        TextField txtTitulo = new TextField(libro.getTitulo());
        Label b = new Label("ISBN");
        TextField txtISBN = new TextField(String.valueOf(libro.getIsbn()));
        Label c = new Label("DNI Usuario");

        List<Pair<String, String>> nombresUsuarios = damenombresusuarios();
        ObservableList<String> observableNombresUsuarios = FXCollections.observableArrayList();

        for (Pair<String, String> pair : nombresUsuarios) {
            observableNombresUsuarios.add(pair.getKey() + " con DNI: " + pair.getValue());
        }

        FilteredList<String> filteredItems = new FilteredList<>(observableNombresUsuarios, p -> true);

        ComboBox<String> combodni = new ComboBox<>(filteredItems);
        combodni.setPromptText("Seleccione el DNI del usuario");
        combodni.setEditable(true);

        // El editor permite personalizar lo que se escribe en el combo
        TextField txtCombo = combodni.getEditor();

        // Agregar un listener a la propiedad del texto para que cuando cambie podamos hacer algo en concreto
        txtCombo.textProperty().addListener((obs, oldValue, newValue) -> {
            final String usuarioSeleccionado = combodni.getSelectionModel().getSelectedItem();

            // Mostrar todos los valores al empezar a escribir
            combodni.show();

            // Esto es para evitar que el filtro se active cuando un ítem es seleccionado
            if (usuarioSeleccionado == null || !usuarioSeleccionado.equals(txtCombo.getText())) {
                filteredItems.setPredicate(item -> {
                    if (item.toLowerCase().contains(newValue.toLowerCase())) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }
        });

        dialogo.getDialogPane().setContent(new VBox(30, a, txtTitulo, b, txtISBN, c, combodni));
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);
        Button applyButton = (Button) dialogo.getDialogPane().lookupButton(ButtonType.APPLY);
        applyButton.setText("Prestar");

        // se maneja los métodos que funcionan en la ventana de diálogo 
        dialogo.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.APPLY) {
                return new Pair<>(txtISBN.getText(), combodni.getValue());
            }
            return null;
        });

        Optional<Pair<String, String>> resultado = dialogo.showAndWait();

        resultado.ifPresent(criterio -> {
            String isbn = criterio.getKey();
            String titulo = libro.getTitulo();
            String seleccionado = criterio.getValue();
            // Para solo poder recoger el dato que necesito de la cadena de valor que muestra el combo
            // Dividir la cadena en partes utilizando " con DNI: " como separador
            String[] partes = seleccionado.split(" con DNI: ");
            if (partes.length == 2) {
                String dni = partes[1]; // El DNI debería estar en la segunda parte
                realizarPrestamos(Integer.parseInt(isbn), dni, titulo);
                this.mostrardatoslibros();
            } else {
                // Manejar el caso donde no se puede extraer el DNI correctamente
                mostrarAlerta("Error", "Formato de usuario incorrecto", "No se pudo extraer el DNI del usuario seleccionado.");
            }
        });

    }

    public void realizarPrestamos(int isbn, String dni, String titulo) {
        try {

            if (librorepetido(dni, isbn)) {
                mostrarAlerta("Error", "Libro no disponible", "No se puede realizar el prestamo ");
                return;
            }

            // se pone esta validacion tambien ya que si no hay ejemplares del libro tampoco puede prestarse
            // y saldria un erro 
            // esto se hace recogiendo el numero atribuido a ese libro (isbn) que esta en la tabla libros
            int ejemplaresDisponibles = numerolibros(isbn);
            if (ejemplaresDisponibles <= 0) {
                mostrarAlerta("Error", "No hay ejemplares disponibles", "No hay ejemplares del libro en este momento.");

                return;
            }

            // Se introduce los datos en la tabla y recoge el mismo dia al añadir el usuario y el isbn
            PreparedStatement st = controladorentrada.getConnection().prepareStatement(
                    "INSERT INTO prestamos (dniusuario, isbn, titulo,  fechasalida) VALUES (?, ?, ?, CURRENT_DATE)");

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

        } catch (Exception e) {
            System.out.println(e.getMessage());

            // Mostrar el Alert en caso de error
            mostrarAlerta("Error", "Error al insertar datos", "Error al insertar datos en la tabla de préstamos.");
        }
    }

    // METODO para saber los ejemplares que tiene un libro 
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

    //METODO para hacer alertas 
    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        // Mostrar un diálogo de alerta con el mensaje especificado
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

}
