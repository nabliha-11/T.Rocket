/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import javafx.stage.Modality;

/**
 *
 * @author USER
 */
public class HomeController implements Initializable {

    @FXML
    private Button btnStart;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnDel;
    @FXML
    private Button btnTable;
    @FXML
    private Button btnGrp;
    @FXML
    private Button btnMap;
//    @FXML
//    private Button btnGrp;
//    @FXML
//    private Button btnSet;
    @FXML
    private Button btnOut;
    String username;
    @FXML
    private Label lblUsername;

    public HomeController() {
        con = ConnectionUtil.conDB();
//        username = LoginController.getUser();

        System.out.println("Welcome " + username);
    }

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lblUsername.setText(LoginController.getUser());

    }

    public void handleButtonAction(MouseEvent event) throws IOException, SQLException {
        Parent root;
        if (event.getSource() == btnStart) {
//            Node node = (Node) event.getSource();
//            Stage stage = (Stage) node.getScene().getWindow();
//            stage.close();

            Scene secondScene = new Scene(FXMLLoader.load(getClass().getResource("Start.fxml")));

            Stage stage = new Stage();
            stage.setScene(secondScene);
//            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner();
            stage.show();
            stage.setTitle("Surge Manager: Start Activity");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/project.png")));
        }

        if (event.getSource() == btnNew) {
            Node node = (Node) event.getSource();
//            Stage stage = (Stage) node.getScene().getWindow();
//            stage.close();

//            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("New.fxml")));
//            stage.setScene(scene);
//            stage.show();
            Scene secondScene = new Scene(FXMLLoader.load(getClass().getResource("New.fxml")));
            
            Stage stage = new Stage();
            stage.setScene(secondScene);
            stage.setTitle("Surge Manager: New Activity");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/marker.png")));
            stage.show();
        }

        if (event.getSource() == btnDel) {
            Node node = (Node) event.getSource();
//            Stage stage = (Stage) node.getScene().getWindow();
//            stage.close();

            Scene secondScene = new Scene(FXMLLoader.load(getClass().getResource("Del.fxml")));

            Stage stage = new Stage();
            stage.setScene(secondScene);
            stage.setTitle("Surge Manager: Delete Activity");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/disable.png")));
            stage.show();
        }

//        if (event.getSource() == btnGrp) {
//        }
//
//        if (event.getSource() == btnSet) {
//        }
        if (event.getSource() == btnOut) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Login.fxml")));
            stage.setScene(scene);
            stage.setTitle("Surge Manager");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sword.png")));
            stage.show();
        }
        if (event.getSource() == btnTable) {
            Scene secondScene = new Scene(FXMLLoader.load(getClass().getResource("HomeTable.fxml")));

            Stage stage = new Stage();
            stage.setScene(secondScene);
            stage.setTitle("Surge Manager: Activity View");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sword.png")));
            stage.show();
        }
        if (event.getSource() == btnGrp) {
            Node node = (Node) event.getSource();
//            Stage stage = (Stage) node.getScene().getWindow();
//            stage.close();

//            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("New.fxml")));
//            stage.setScene(scene);
//            stage.show();
            Scene secondScene = new Scene(FXMLLoader.load(getClass().getResource("GroupMem.fxml")));

            Stage stage = new Stage();
            stage.setScene(secondScene);
            stage.setTitle("Surge Manager: Group Activity");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/teamwork.png")));
            stage.show();
        }
        if (event.getSource() == btnMap){
            String url = "F:\\JavaFX-Surge-main\\JavaFX-LifeQuest-main\\src\\googlemap.html";
            File htmlFile = new File(url);
            Desktop.getDesktop().browse(htmlFile.toURI());
        }
    }

}
