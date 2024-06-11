package controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import modelo.Usuario;
//CONTROLADOR PARA AÑADIR NUEVO USUARIO 

public class ControladorNuevousuario implements Initializable {

    private ControladorEntrada controladorentrada;
    private ControladorTablausuarios controladortablausuarios;
    private ControladorNuevousuario controladornuevousuario;
    
    private Usuario usuarios;
    private ExecutorService executor;

    public void setControlador(ControladorEntrada controladorentrada) {
        this.controladorentrada = controladorentrada;
    }

    public void setControladorTabla(ControladorTablausuarios controladortablausuarios) {
        this.controladortablausuarios = controladortablausuarios;
    }
    
    public void setControladorNuevousuario(ControladorNuevousuario controladornuevousuario) {
        this.controladornuevousuario = controladornuevousuario;
    }

    @FXML
    private Pane paneprincipal;

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

    @FXML
    private TextField txtdni;

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
                // Crear la sentencia SQL de INSERT
                String sql = "INSERT INTO usuarios (dni, nombre, primerapellido, segundoapellido, domicilio, telefono)"
                        + " VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement statement = controladorentrada.getConnection().prepareStatement(sql);
                statement.setString(1, dni);
                statement.setString(2, nombre);
                statement.setString(3, primerApellido);
                statement.setString(4, segundoApellido);
                statement.setString(5, domicilio);
                statement.setString(6, telefono);

                statement.executeUpdate();
                statement.close();

                mostrarAlerta("Confirmación", "Datos ingresados correctamente", "Los datos se han ingresado correctamente en la base de datos");

                limpiarDatos();

                //volver al panel de tabla de usuarios
                try {
                    // Cargar el archivo FXML de la ventana de préstamos
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/usuarios.fxml"));
                    Pane ventanaPrestamos = loader.load();

                    // Limpiar el Pane y agregar la nueva ventana de préstamos
                    paneprincipal.getChildren().clear();
                    paneprincipal.getChildren().add(ventanaPrestamos);

                    // Inicializar controladoreditarlibro
                    ControladorTablausuarios controladortablausuarios = loader.getController();
                    if (controladortablausuarios != null) {
                        controladortablausuarios.setControladorTablausuarios(controladortablausuarios);

                    } else {
                        System.out.println("Error: No se pudo editar.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //METODO para validar 
    private boolean camposValidos() {  // ESTAN COMENTADOS PARA FACILITAR COMPROBACIONES 

        // Validar si el número de teléfono contiene solo dígitos
           if (!txttelefono.getText().matches("\\d+")) {
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
        } 
        return true;
    }

    // Formula para el calculo de la letra del dni 
    private char calcularLetraDNI(String numeros) {
        char[] letras = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        int numeroDni = Integer.parseInt(numeros);
        int calculoletra = numeroDni % 23;
        return letras[calculoletra];
    }

    //METODO para Alertas
    private void mostrarAlerta(String titulo, String encabezado, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    //METODO para limpiar 
    private void limpiarDatos() {
        txtdni.clear();
        txtnombre.clear();
        txtprimer.clear();
        txtsegundo.clear();
        txtdomicilio.clear();
        txttelefono.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.controladorentrada = new ControladorEntrada();
        this.controladorentrada.getConnection();
    }

}
