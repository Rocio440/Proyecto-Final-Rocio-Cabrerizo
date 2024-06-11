package controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modelo.Usuario;
//CONTROLADOR QUE EDITAR LOS DATOS DE LOS USUARIOS QUE SE ENCUENTRA EN LA TABLA USUARIOS 
public class ControladorEditar implements Initializable {

    private ControladorTablausuarios controladortablausuarios;
    private ControladorEntrada controladorentrada;

    private Usuario usuarios;
    private ExecutorService executor;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;
    }

    public void setControladorTabla(ControladorTablausuarios controladortablausuarios) {
        this.controladortablausuarios = controladortablausuarios;
    }

    @FXML
    private Pane paneprincipal;

    @FXML
    private TextField txtdni;

    @FXML
    private TextField txtdomicilio;

    @FXML
    private TextField txtnombre;

    @FXML
    private TextField txtprimer;

    @FXML
    private TextField txtsegundo;

    @FXML
    private TextField txttelefono;

    //para que aparezcan los mismo datos en la ventana que se abre para editar 
    public void setDatos(Usuario usuario) {
        txtdni.setText(usuario.getDni());
        txtnombre.setText(usuario.getNombre());
        txtprimer.setText(usuario.getPrimerapellido());
        txtsegundo.setText(usuario.getSegundoapellido());
        txtdomicilio.setText(usuario.getDomicilio());
        txttelefono.setText(String.valueOf(usuario.getTelefono()));

    }

    @FXML
    void btnregistrar(ActionEvent event) {
        if (camposValidos()) {
            String dni = txtdni.getText().toUpperCase();
            String nombre = txtnombre.getText();
            String primerApellido = txtprimer.getText();
            String segundoApellido = txtsegundo.getText();
            String domicilio = txtdomicilio.getText();
            String telefono = txttelefono.getText();

            try {

                // Se crea la sentencia SQL para settear los datos 
                String sql = "UPDATE usuarios SET  nombre = ?, "
                        + "primerapellido = ?, segundoapellido = ? ,"
                        + " domicilio = ?, telefono = ? WHERE dni = ?";

                PreparedStatement statement = controladorentrada.getConnection().prepareStatement(sql);

                statement.setString(1, nombre);
                statement.setString(2, primerApellido);
                statement.setString(3, segundoApellido);
                statement.setString(4, domicilio);
                statement.setString(5, telefono);
                statement.setString(6, dni);

                statement.executeUpdate();
                statement.close();

                //  tablausuarios.mostrardatos(); 
                // tablausuarios.damedatos();
                controladortablausuarios.mostrardatos();

                mostrarAlerta("Confirmación", "Datos ingresados correctamente", "Los datos se han editado correctamente.");

                // volver al panel de la tabla libros despues de editar
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/usuarios.fxml"));
                    Pane ventanaPrestamos = loader.load();

                    // Limpiar el Pane y agregar la nueva ventana de préstamos
                    paneprincipal.getChildren().clear();
                    paneprincipal.getChildren().add(ventanaPrestamos);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    // VALIDACIONES 
    private boolean camposValidos() {  // ESTAN COMENTADOS PARA FACILITAR COMPROBACIONES 

        // Validar si el número de teléfono contiene solo dígitos
        /*   if (!txttelefono.getText().matches("\\d+")) {
            mostrarAlerta("Error", "Formato incorrecto", "Ingrese solo números en el campo de teléfono");
            return false;
        }
        // Valida que el numero de digitos del telefono sea 9 
        if (txttelefono.getText().length() != 9) {
            mostrarAlerta("Error", "Teléfono Inválido", "El teléfono debe tener exactamente 9 dígitos.");
            return false;
        }
        // Valida que tenga 8 digitos el dni y una letra al final 
        if (!txtdni.getText().matches("\\d{8}[a-zA-Z]")) {
            mostrarAlerta("Error", "DNI inválido", "El DNI debe tener 8 números seguidos de una letra");
            return false;
        // Calculo de la letra del dni 
        } else {
            char letraDni = txtdni.getText().charAt(8);
            char letraCalculada = calcularLetraDNI(txtdni.getText().substring(0, 8));
            String letraDniMayuscula = String.valueOf(letraDni).toUpperCase();
            if (!letraDniMayuscula.equals(String.valueOf(letraCalculada))) {
                mostrarAlerta("Error", "Letra incorrecta", "La letra proporcionada en el DNI no es válida");
                return false;
            }
        } */
        return true;
    }

    //METODO para validar la letra del dni la cual se calcula con una formula 
    private char calcularLetraDNI(String numeros) {
        char[] letras = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        int numeroDni = Integer.parseInt(numeros);
        int calculoletra = numeroDni % 23;
        return letras[calculoletra];
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.controladorentrada = new ControladorEntrada();
        this.controladorentrada.getConnection(); // Establecer la conexión con la base de datos
    }
}
