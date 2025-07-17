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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;

public class ForgotController implements Initializable {

    @FXML
    private TextField txtUsername;
//    @FXML
//    private TextField txtEmail;
    @FXML
    private TextField txtNewPassword;
    @FXML
    private TextField txtNewPasswordConfirm;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnBack;
    @FXML
    private Label lblStatus;

    public ForgotController() {
        con = ConnectionUtil.conDB();
    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public void handleButtonAction(MouseEvent event) throws IOException, SQLException {

        if (event.getSource() == btnReset) {
            String username = txtUsername.getText();
            String newpassword = txtNewPassword.getText();
            String newpasswordConfirm = txtNewPasswordConfirm.getText();

            if (!newpasswordConfirm.equals(newpassword)) {

                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Passwords do not match");

            } else if (!userConfirm(username)) {

                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("User does not exist.");

            } else if (queryCheck(username, newpassword)) {

                lblStatus.setTextFill(Color.TOMATO);
                lblStatus.setText("Old password. Please give another password.");

            } else {
                try {
                    String query1 = "UPDATE `user` SET `password`= md5(?) WHERE `users` = ?";
                    PreparedStatement ps = con.prepareStatement(query1);
                    ps.setString(1, newpassword);
                    ps.setString(2, username);
                    ps.executeUpdate();

                    String query2 = "INSERT INTO `login`(`username`,`pass`) VALUES(?,?)";
                    ps = con.prepareStatement(query2);
                    ps.setString(1, username);
                    ps.setString(2, newpassword);
                    if (ps.executeUpdate() > 0) {

                        lblStatus.setTextFill(Color.GREEN);
                        lblStatus.setText("Password Succesfully Updated!");
                    }

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        if (event.getSource() == btnBack) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();

                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Login.fxml")));
                stage.setScene(scene);
                stage.setTitle("Surge Manager");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sword.png")));
                stage.show();
            } catch (IOException ex) {
                System.err.print(ex.getMessage());
            }
        }
    }

    public boolean queryCheck(String username, String newpass) {

        boolean check = false;
        PreparedStatement ps;
        ResultSet rs;
        String q = "SELECT `pass` FROM `login` WHERE `username` = ?";
        try {

            ps = con.prepareStatement(q);
            ps.setString(1, username);

            rs = ps.executeQuery();

            while (rs.next()) {

                String oldpass = rs.getString("pass");
                if (newpass.equals(oldpass)) {
                    check = true;
                    return check;
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Exception");
        }

        return check;
    }

    public boolean userConfirm(String username) {

        PreparedStatement ps;
        ResultSet rs;
        boolean userExist = true;
        String query = "SELECT users FROM `user` WHERE `users`= ?";

        try {

            ps = con.prepareStatement(query);
            ps.setString(1, username);

            rs = ps.executeQuery();
            if (!rs.next()) {

                userExist = false; //correct info
                return userExist;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return userExist;

    }
}
