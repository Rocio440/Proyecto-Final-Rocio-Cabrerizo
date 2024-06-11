package controlador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author cabre
 */
//METODO DONDE SE REALIZA LA CONEXION CON LA BASE DE DATOS, Y DA ACCESO A LA VENTANA PARA NAVEGAR POR LAS
//DIFERENTES OPCIONES 
public class ControladorEntrada implements Initializable {

    Connection conexion;
    private ControladorNuevousuario controladornuevousuario;
    private ControladorTablausuarios controladortablausuarios;
    
    private ControladorTablaLibros controladortablalibros;
    private ControladorLibroNuevo controladorlibronuevo;
    
    private ControladorPrestar controladorprestar;


    private static String CADENA_CONEXION;
    private static String USUARIO;
    private static String CLAVE;
// Con esto se lee desde un archivo de texto la contraseña , el usuario y la clave de conexion 
    static {
        try (BufferedReader br = new BufferedReader(new FileReader("archivo.txt"))) {
            CADENA_CONEXION = br.readLine();
            USUARIO = br.readLine();
            CLAVE = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //PARA CONECTAR LA BASE DE DATOS
    public void executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection(CADENA_CONEXION, USUARIO, CLAVE);
            return conn;

        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ha ocurrido un error de conexión");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

    @FXML
    private Pane paneVentana;

    @FXML
    private Label lblDia;

    @FXML
    private Pane panecentral;

    public Pane getPanecentral() {
        return panecentral;
    }
    
    
//Abrimos por medio de los botones las ventanas que estan ancladas a paneles 
    @FXML
    void btndevoluciones(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/devoluciones.fxml"));
            Pane ventanaPrestamos = loader.load();

            panecentral.getChildren().clear();
            panecentral.getChildren().add(ventanaPrestamos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnlibros(ActionEvent event) {
        try {
         
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/libros.fxml"));
            Pane ventanaPrestamos = loader.load();
            
            //aqui cargo tambien el controlador de usuarios ya que lo voy a necesitar tambien para
            //realizar los prestamos directamente desde este panel 
            controladortablalibros= loader.getController();
            System.out.println(controladortablalibros);
            
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/vista/usuarios.fxml"));
            Pane novalgopana= loader2.load();
            controladortablausuarios= loader2.getController();
            System.out.println(controladortablausuarios);
            controladortablalibros.setControladorTablausuarios(controladortablausuarios);

            panecentral.getChildren().clear();
            panecentral.getChildren().add(ventanaPrestamos);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnprestamos(ActionEvent event) {
        
        try {
          
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/prestamos.fxml"));
            Pane ventanaPrestamos = loader.load();

            panecentral.getChildren().clear();
            panecentral.getChildren().add(ventanaPrestamos);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    @FXML
    void btnprincipal(ActionEvent event) {

        try {
    
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/principal.fxml"));
            Pane ventanaPrincipal = loader.load();

      
            panecentral.getChildren().clear();
            panecentral.getChildren().add(ventanaPrincipal);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void btnlibrosprestados(ActionEvent event) {
        try {
          
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/prestados.fxml"));
            Pane ventanaPrestamos = loader.load();

            panecentral.getChildren().clear();
            panecentral.getChildren().add(ventanaPrestamos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void btnusuarios(ActionEvent event) {
        try {
         
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/usuarios.fxml"));
            Pane ventanaPrestamos = loader.load();

        
            panecentral.getChildren().clear();
            panecentral.getChildren().add(ventanaPrestamos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void btnnuevousuario(ActionEvent event) {
        try {
      
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/nuevousuario.fxml"));
            Pane ventanaPrincipal = loader.load();

            panecentral.getChildren().clear();
            panecentral.getChildren().add(ventanaPrincipal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
     @FXML
    void btninformes(ActionEvent event) {
        try {
           
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/panelinformes.fxml"));
            Pane ventanaPrincipal = loader.load();

            panecentral.getChildren().clear();
            panecentral.getChildren().add(ventanaPrincipal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarVentanaPrincipal() {
        try {
           
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/principal.fxml"));
            Pane ventanaPrincipal = loader.load();

            panecentral.getChildren().clear();
            panecentral.getChildren().add(ventanaPrincipal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // METODO para poner el en label la fecha del CurrentDay
    private void iniciardordedia() {
        LocalDate dia = LocalDate.now();
        int year = dia.getYear();
        int hoy = dia.getDayOfMonth();
        int diaSemana = dia.getDayOfWeek().getValue();//con esto cogemos el valor para ponerlo en la lista
        String[] diasSemana = {"Lunes", "Martes", 
            "Miércoles", "Jueves",
            "Viernes", "Sábado", "Domingo"};

        int mes = dia.getMonthValue();//cogems valor numerico para decir posicion en la lista
        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        lblDia.setText(" Hoy es " + diasSemana[diaSemana - 1] + " " + hoy + " de " + meses[mes - 1] + " de " + year);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarVentanaPrincipal();
        iniciardordedia();
        

        this.conexion = this.getConnection();
    }

}
