package crud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //lo unico que cambio aqui es el getResorce  ../ el nombre del archivo scenebuilder
        Parent root = FXMLLoader.load(getClass().getResource("/vista/Ventana.fxml"));
        
        
                
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Biblioteca");
        stage.setResizable(false);
        stage.show();

        //scene.getStylesheets().add(getClass().getResource("/controlador/Estilos.css").toExternalForm());
        

    }

    public static void main(String[] args) {
        launch(args);
    }
}
