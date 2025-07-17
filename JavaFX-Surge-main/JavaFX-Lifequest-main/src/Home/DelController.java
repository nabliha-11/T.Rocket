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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import utils.ConnectionUtil;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author USER
 */
public class DelController implements Initializable {

    @FXML
    private Button btn_Confirm;
    @FXML
    private ComboBox<String> comboBoxx;
    @FXML
    private Label lblStatus;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        comboBoxx.setItems(FXCollections.observableArrayList(getData()));

    }

    public DelController() {
        con = ConnectionUtil.conDB();
    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String username = LoginController.getUser();

    private List<String> getData() {

        // Define the data you will be returning, in this case, a List of Strings for the ComboBox
        List<String> options = new ArrayList<>();

        try {

            String query = "SELECT `activity_name` FROM activity WHERE `username` = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                options.add(set.getString("activity_name"));
            }

            statement.close();
            set.close();

            // Return the List
            return options;

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void handleButtonAction(MouseEvent event) throws IOException, SQLException {
        String del_Act = comboBoxx.getValue();
        System.out.println(del_Act);
        if (event.getSource() == btn_Confirm) {
            String sql = "DELETE FROM activity WHERE username = ? and activity_name = ?";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, del_Act);
                preparedStatement.executeUpdate();
                lblStatus.setTextFill(Color.GREEN);
                lblStatus.setText("Deleted Succesfully");
            } catch (Exception ex) {
                System.err.println("ConnectionUtil : " + ex.getMessage());
                
            }
        }
    }
}
