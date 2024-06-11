/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import controlador.ControladorEntrada;
import java.awt.Dimension;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
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
import javafx.embed.swing.SwingNode;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import modelo.Libros;
import modelo.Prestamo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author cabre
 */
public class ControladorInformes implements Initializable {

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
    private Pane paneinformes;
    
    @FXML
    void btninformelibro(ActionEvent event) {
        
        try {
            InputStream inputStream = getClass().getResourceAsStream("/informes/report3.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            Map<String, Object> parametros = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, controladorentrada.getConnection());

            // Crear el JRViewer para visualizar el informe
            JRViewer jrViewer = new JRViewer(jasperPrint);
            jrViewer.setOpaque(true);
            jrViewer.setVisible(true);
            jrViewer.setPreferredSize(new Dimension((int) paneinformes.getWidth(),(int)paneinformes.getHeight()));

            SwingNode swingNode = new SwingNode();
            swingNode.setContent(jrViewer);

            // Limpiar el Pane y agregar el SwingNode
            paneinformes.getChildren().clear();
            paneinformes.getChildren().add(swingNode);
 
     
       
        } catch (JRException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error " + e.getMessage());
        }
    }

    @FXML
    void btninformeusuarios(ActionEvent event) {
       
       try {
            InputStream inputStream = getClass().getResourceAsStream("/informes/report1.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
            Map<String, Object> parametros = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, controladorentrada.getConnection());

            // Crear el JRViewer para visualizar el informe
            JRViewer jrViewer = new JRViewer(jasperPrint);
            jrViewer.setOpaque(true);
            jrViewer.setVisible(true);
            jrViewer.setPreferredSize(new Dimension((int) paneinformes.getWidth(),(int)paneinformes.getHeight()));

            SwingNode swingNode = new SwingNode();
            swingNode.setContent(jrViewer);

            // Limpiar el Pane y agregar el SwingNode
            paneinformes.getChildren().clear();
            paneinformes.getChildren().add(swingNode);
 
     
       
        } catch (JRException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error " + e.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.controladorentrada = new ControladorEntrada();
        this.controladorentrada.getConnection();
        
         
    }
}
