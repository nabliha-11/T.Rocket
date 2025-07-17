/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author USER
 */
public class GroupTimerOptionController implements Initializable {

    @FXML
    private Button btn_Y;
    @FXML
    private Button btn_N;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //To change body of generated methods, choose Tools | Templates.
    }

    public void handleButtonAction(MouseEvent event) throws IOException, SQLException {
        if (event.getSource() == btn_Y) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
//                    stage.initStyle(StageStyle.DECORATED);
//                    stage.setMaximized(true);
            stage.close();

            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("GroupTimer.fxml")));
            stage.setScene(scene);
            stage.setTitle("Surge Manager: Timer");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/hourglass.png")));
            stage.show();
        }
        if (event.getSource() == btn_N) {

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }
}
