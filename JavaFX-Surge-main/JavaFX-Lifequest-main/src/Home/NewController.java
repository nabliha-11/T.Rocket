/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
//import java.util.concurrent.TimeUnit;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;

/**
 *
 * @author USER
 */
public class NewController implements Initializable {

    @FXML
    private Button btnAdd;

    @FXML
    private TextField txtAct;

    @FXML
    private Label lblStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public NewController() {
        con = ConnectionUtil.conDB();
    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String username = LoginController.getUser();

    public void handleButtonAction(MouseEvent event) throws IOException, SQLException, InterruptedException {
        if (event.getSource() == btnAdd) {
            String act = txtAct.getText();
            String sql = "INSERT INTO `activity`(`username`, `activity_name`) VALUES (?, ?)";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, act);
            preparedStatement.executeUpdate();
            lblStatus.setTextFill(Color.GREEN);
            lblStatus.setText("Added Succesfully");
            close(event);
        }

    }

    public void close(MouseEvent event) {
//        Thread.currentThread.setPriority(8);
//        try {
//
//            Thread.sleep(1000);
//        } catch (InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
