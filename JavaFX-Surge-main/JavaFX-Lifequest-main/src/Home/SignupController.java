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
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;

public class SignupController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private TextField txtPasswordConfirm;
    @FXML
    private TextField txtRegion;
    @FXML
    private TextField txtDob;
    @FXML
    private TextField txtSex;
    @FXML
    private TextField txtEmail;
    @FXML
    private Label lblStatus;
    @FXML
    private Button Signup;
    @FXML
    private Button btnBack;
    @FXML
    private ComboBox<String> cBoxx_sex;
    @FXML
    private ComboBox<String> cBoxx_region;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cBoxx_sex.getItems().removeAll(cBoxx_sex.getItems());
        cBoxx_sex.getItems().addAll("M", "F");
//        cBoxx_sex.getSelectionModel().select("0");
        cBoxx_region.getItems().removeAll(cBoxx_region.getItems());
        cBoxx_region.getItems().addAll("Albania","Algeria","Bangladesh","Bhutan","Canada","Chile","China","Denmark","Estonia","Finland","Germany","India","Japan","United Kingom","United States of America","Venezuela");
        cBoxx_region.getSelectionModel().select("Albania");
    }

    public SignupController() {
        con = ConnectionUtil.conDB();
    }

    // ----
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    static String xp;

    public void setXP(String xp) {
        this.xp = xp;
    }

    public static String getXP() {
        return xp;
    }

    public boolean userDuplicate(String username) {
        PreparedStatement ps;
        ResultSet rs;
        boolean checkUser = false;
        String query = "SELECT* FROM `user` WHERE `users`=?";

        try {
            ps = con.prepareStatement(query);
            ps.setString(1, username);

            rs = ps.executeQuery();

            if (rs.next()) {
                checkUser = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return checkUser;
    }

    public void handleButtonAction(MouseEvent event) throws SQLException, IOException {
        boolean comboEmpty = cBoxx_sex.getSelectionModel().isEmpty();
        
        if (event.getSource() == Signup) {

            String username = txtUsername.getText();
            String password = txtPassword.getText();
            String passwordConfirm = txtPasswordConfirm.getText();
            String region = cBoxx_region.getValue();
            String dob = txtDob.getText();
            String sex = cBoxx_sex.getValue();
            String email = txtEmail.getText();

            if (userDuplicate(username) == true) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Username is taken. Please try another one.");
            } else if (!passwordConfirm.equals(password)) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Passwords do not match");
            } else if (comboEmpty) {
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Please select sex");
            } else if (!(Pattern.matches("^[a-zA-Z0-9]+[@]{1}+[a-zA-Z0-9]+[.]{1}+[a-zA-Z0-9]+$", email))){
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Please enter a valid email address");
            } else if(password.length() < 6){
                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Password too short");
            } else {
                try {
                    String sql1 = "INSERT INTO `user`(`users`, `password`, `region`, `DOB`, `sex`, `email`, `total_XP`, `Level`) VALUES (?,`md5`(?),?,?,?,?, 0, 0)";
                    preparedStatement = con.prepareStatement(sql1);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    preparedStatement.setString(3, region);
                    preparedStatement.setString(4, dob);
                    preparedStatement.setString(5, sex);
                    preparedStatement.setString(6, email);
                    preparedStatement.executeUpdate(); //updating my main user table

                    String sql2 = "INSERT INTO `login`(`username`,`pass`) VALUES (?,?)";
                    preparedStatement = con.prepareStatement(sql2);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    preparedStatement.executeUpdate(); //updating the separate login table

                    lblStatus.setTextFill(Color.GREEN);
                    lblStatus.setText("Added Successfully");
                } catch (SQLException ex) {
//                    System.out.println(ex.getMessage());
//                    lblStatus.setTextFill(Color.TOMATO);
//                    lblStatus.setText("Exception");

                }
            }
        }
        if (event.getSource() == btnBack) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
//                    stage.initStyle(StageStyle.DECORATED);
//                    stage.setMaximized(true);
            stage.close();

            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Login.fxml")));
            stage.setScene(scene);
            stage.show();
        }

    }

}
