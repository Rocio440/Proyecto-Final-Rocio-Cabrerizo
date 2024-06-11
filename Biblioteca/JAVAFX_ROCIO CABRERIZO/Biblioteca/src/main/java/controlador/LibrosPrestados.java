package controlador;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import modelo.Libros;
import modelo.Prestamo;
import modelo.Usuario;

//CONTROLADOR PARA MOSTRAR LOS LIBROS QUE SE HAN PRESTADO Y QUE NO TIENEN FECHA DDE DEVOLUCION EN LA TABLA 
public class LibrosPrestados implements Initializable {

    private ControladorTablausuarios controladortablausuarios;
    private ControladorEntrada controladorentrada;
    private ControladorTablaLibros controladortablalibros;
    private ControladorEditarLibro controladoreditarlibro;
    private ControladorDevoluciones controladordevoluciones;
    private ControladorPrestar controladorprestar;

    private Usuario usuarios;
    private Libros libros;
    private Prestamo prestamo;

    private ExecutorService executor;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;
    }

    public void setControladorTabla(ControladorTablausuarios controladortablausuarios) {
        this.controladortablausuarios = controladortablausuarios;
    }

    public void setControladorTablaLibros(ControladorTablaLibros controladortablalibros) {
        this.controladortablalibros = controladortablalibros;
    }

    public void setControladorEditarLibro(ControladorEditarLibro controladorEditarLibro) {
        this.controladoreditarlibro = controladorEditarLibro;
    }

    public void setControladorDevoluciones(ControladorDevoluciones controladordevoluciones) {
        this.controladordevoluciones = controladordevoluciones;
    }

    public void setControladorPrestar(ControladorPrestar controladorprestar) {
        this.controladorprestar = controladorprestar;
    }

    @FXML
    private TableColumn<Prestamo, Integer> columbook;

    @FXML
    private Label columusuario;

    @FXML
    private TableColumn<Prestamo, String> fentradacolum;

    @FXML
    private TableColumn<Prestamo, String> fsalidacolum;

    @FXML
    private Pane paneprincipal;

    @FXML
    private TextField txtbusqueda;

    @FXML
    private TableView<Prestamo> tablaViewPrestamos;

    public TableView<Prestamo> getTablaViewPrestamos() {
        return tablaViewPrestamos;
    }

    @FXML
    private TableColumn<Prestamo, String> columtitulo;

    @FXML
    private TableColumn<Prestamo, String> usuariocolum;

    // PARA REALIZAR LA BUSQUEDA se filtra por isbn y por titulo 
    private void filtrartabla(String textoBusqueda) {
        if (textoBusqueda.isEmpty()) {
            tablaViewPrestamos.setItems(damedatos());
        } else {
            ObservableList<Prestamo> resultados = damedatos().filtered(prestamo -> {
                Integer isbn = prestamo.getIsbn();
                String titulo = prestamo.getTitulo();
                return isbn != null && isbn.toString().contains(textoBusqueda) // Busca por ISBN
                        || titulo != null && titulo.toLowerCase().contains(textoBusqueda.toLowerCase()); // Busca por título (ignorando mayúsculas y minúsculas)
            });
            tablaViewPrestamos.setItems(resultados);
        }
    }

    public void mostrardatos() {
        CompletableFuture.runAsync(() -> {
            Platform.runLater(() -> tablaViewPrestamos.setItems(damedatos()));
        });
    }

    // para mostrar los datos de la base de datos en la tabla de la interfaz
    public ObservableList<Prestamo> damedatos() {
        ObservableList<Prestamo> listaprestamos = FXCollections.observableArrayList();
        Connection connection = controladorentrada.getConnection();
        if (connection != null) {
            String query = "SELECT * FROM prestamos WHERE fechadevo IS NULL";
            try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(query)) {
                while (rs.next()) {
                    // esto lo hago para que en la interfaz y solo en la interfaz se pueda ver la fecha
                    //que tendria de devolucion ese libro que se añade 
                    // Sumamos 15 dias a la fecha devolucion para rellenar en la interfaz pero esto no afecta a la base de datos 
                    // la colmna de fechaentrada
                    LocalDate fechasalida = LocalDate.parse(rs.getString("fechasalida"));
                    LocalDate fechadev = fechasalida.plusDays(1); // OJO CAMBIE LOS DIAS
                    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fechadevolu = fechadev.format(formato);

                    Prestamo prestamo = new Prestamo(
                            rs.getString("dniusuario"),
                            rs.getInt("isbn"),
                            rs.getString("titulo"),
                            rs.getString("fechasalida"),
                            fechadevolu);// ese dato se recoge de la variable calculada 

                    listaprestamos.add(prestamo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaprestamos;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización de la conexión y configuración de la búsqueda
        this.controladorentrada = new ControladorEntrada();
        this.controladorentrada.getConnection();
        
         //mostrar mensajes de aviso al pasar el raton 
        tablaViewPrestamos.setRowFactory(tv -> {
            TableRow<Prestamo> row = new TableRow<>();
            Tooltip tooltip = new Tooltip();
            tooltip.setText("Con doble click podra acceder a la DEVOLUCIÓN del libro" );
            Tooltip.install(row, tooltip);
            return row;
        });

        //para poner texto indicativo 
        txtbusqueda.setPromptText("BÚSQUEDA, introduzca ISBN o TITULO ");
        // bind para la realizacion de la busqueda que se active el metodo filtrado al 
        //poner texto
        txtbusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrartabla(newValue);
        });

        // Agregar Listener para el evento de doble clic
        tablaViewPrestamos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Prestamo prestamoSeleccionado = tablaViewPrestamos.getSelectionModel().getSelectedItem();
                if (prestamoSeleccionado != null) {
                    mostrarDialogo(prestamoSeleccionado);
                }
            }
        });

        // Configuración de las columnas de la tabla
        usuariocolum.setCellValueFactory(new PropertyValueFactory<>("dniusuario"));
        columbook.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        columtitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        fsalidacolum.setCellValueFactory(new PropertyValueFactory<>("fechasalida"));
        fentradacolum.setCellValueFactory(new PropertyValueFactory<>("fechadevo"));

        //CAMBIAR EL COLOR DE LO QUE SE PONE EN LA COLUMNA DE FECHA ENTRADA
        fentradacolum.setCellFactory(column -> new TableCell<Prestamo, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setTextFill(Color.RED);
                }
            }
        });

        // Inicialización y ejecución del servicio para mostrar los datos
        executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> mostrardatos());
    }

    //METODO QUE MUESTRA VENTANA PARA DEVOLVER
    private void mostrarDialogo(Prestamo prestamo) {

        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Detalles de la Devolución");

        // Crear los campos de texto para mostrar los datos
        Label a = new Label("Titulo");
        TextField txtTitulo = new TextField(prestamo.getTitulo());
        Label b = new Label("ISBN");
        TextField txtISBN = new TextField(String.valueOf(prestamo.getIsbn()));
        Label c = new Label("DNI Usuario");
        TextField txtDNI = new TextField(prestamo.getDniusuario());

        // Añadir los campos al contenido del diálogo
        dialogo.getDialogPane().setContent(new VBox(30, a, txtTitulo, b, txtISBN, c, txtDNI));

        // Añadir botones
        dialogo.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.APPLY);
        Button applyButton = (Button) dialogo.getDialogPane().lookupButton(ButtonType.APPLY);
        applyButton.setText("Devolver");

        // se maneja los metodos que funcionan en la ventana de Dialogo 
        dialogo.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.APPLY) {
                try {
                    // Realizar la devolución del libro
                    realizarDevolucion(prestamo);
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta("Error", "Error al realizar la devolución", "Se produjo un error al intentar realizar la devolución.");
                }
            }
            return null;
        });

        // Mostrar el diálogo
        dialogo.showAndWait();
    }

    // METODO para realizar la devolucion
    private void realizarDevolucion(Prestamo prestamo) {
        int isbn = prestamo.getIsbn();
        String dni = prestamo.getDniusuario();

        try {
            // Verificar si el préstamo existe en la base de datos
            if (!prestamoExiste(dni, isbn)) {
                mostrarAlerta("Error", "Préstamo no encontrado", "No se ha encontrado un préstamo activo para el usuario con DNI " + dni + " y el libro con ISBN " + isbn);
                return;
            }

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

                }
            } else {
                mostrarAlerta("Error", "Error al obtener la fecha de salida", "No se ha podido obtener la fecha de salida del préstamo.");
                return;
            }

            // Actualizar la tabla de préstamos con la fecha de devolución
            PreparedStatement st = controladorentrada.getConnection().prepareStatement(
                    "UPDATE prestamos SET fechadevo = CURRENT_DATE WHERE dniusuario = ? AND isbn = ? AND fechadevo IS NULL");

            st.setString(1, dni);
            st.setInt(2, isbn);

            int rowsUpdated = st.executeUpdate();
            st.close();

            if (rowsUpdated == 0) {
                mostrarAlerta("Error", "Préstamo no encontrado", "No se ha encontrado un préstamo activo para el usuario con ese DNI y el libro con ese ISBN");
                return;
            }

            // Incrementar el número de ejemplares disponibles del libro
            PreparedStatement stSumarEjemplar = controladorentrada.getConnection().prepareStatement(
                    "UPDATE libros SET ejemplares = ejemplares + 1 WHERE isbn = ?");

            stSumarEjemplar.setInt(1, isbn);
            stSumarEjemplar.executeUpdate();
            stSumarEjemplar.close();

            // Mostrar mensaje de confirmación
            mostrarAlerta("Confirmación", "Devolución realizada correctamente", "El libro se ha devuelto correctamente.");

            // Actualizar la tabla de préstamos
            mostrardatos();

        } catch (Exception e) {
            e.printStackTrace();
            // Mostrar el Alert en caso de error
            mostrarAlerta("Error", "Error al realizar la devolución", "Error al realizar la devolución del libro.");
        }
    }

    // METODO que recoge la fecha de devolucion si al recoger el isbn y dni tiene la fecha devo vacia 
    // recoge la fecha de salida y la transforma en la fecha en la que se devuelve
    private LocalDate obtenerFechaSalida(String dni, int isbn) {
        try {
            PreparedStatement st = controladorentrada.getConnection().prepareStatement(
                    "SELECT fechasalida FROM prestamos WHERE dniusuario = ? AND isbn = ? AND fechadevo IS NULL");
            st.setString(1, dni);
            st.setInt(2, isbn);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return LocalDate.parse(rs.getString("fechasalida"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

}
