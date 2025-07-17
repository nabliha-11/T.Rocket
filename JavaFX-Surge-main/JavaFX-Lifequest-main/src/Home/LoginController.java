package Home;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import javafx.stage.StageStyle;
import utils.ConnectionUtil;

/**
 *
 * @author USER
 */
public class LoginController implements Initializable {

    @FXML
    private Label lbl_close;
    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignup;
    @FXML
    private Button btnSignin;
    @FXML
    private Button btnForgot;
//    @FXML
//    private Button btnSignup;

    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == lbl_close) {
            System.exit(0);

        }
        if (event.getSource() == btnSignin) {
            //login Stuff
            if (logIn().equals("Success")) {
                try {

                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
//                    stage.initStyle(StageStyle.DECORATED);
//                    stage.setMaximized(true);
                    stage.close();

                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Home.fxml")));
                    stage.setTitle("Surge Manager: Home");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sword.png")));
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.print(ex.getMessage());
                }
            }
        }
        if (event.getSource() == btnSignup) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();

                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Signup.fxml")));
                stage.setScene(scene);
                stage.setTitle("Surge Manager");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sword.png")));
                stage.show();
            } catch (IOException ex) {
                System.err.print(ex.getMessage());
            }

        }
        if (event.getSource() == btnForgot) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();

                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("Forgot.fxml")));
                stage.setScene(scene);
                stage.setTitle("Surge Manager");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sword.png")));
                stage.show();
            } catch (IOException ex) {
                System.err.print(ex.getMessage());
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public LoginController() {
        con = ConnectionUtil.conDB();
    }

    // ----
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    static String username;

    public void setUser(String s) {
        username = s;
    }

    public static String getUser() {
        return username;
    }
//    setUser(txtUsername.getText());
    //using String to check for status

    private String logIn() {
        String email = txtUsername.getText();
        String password = txtPassword.getText();
        setUser(email);

        //query
        String sql = "SELECT* FROM user WHERE users = ? and password = md5(?)";

        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                lblErrors.setTextFill(Color.TOMATO);
                lblErrors.setText("Enter correct information");
                System.err.println("Wrong Logins --///");
                return "Error";
            } else {
                lblErrors.setTextFill(Color.GREEN);
                lblErrors.setText("Login Succesful...Redirecting..");
                System.out.println("Succesful Login!");
                return "Success";
            }

        } catch (Exception ex) {
            System.err.println("ConnectionUtil : " + ex.getMessage());
            return "Exception";
        }

    }
    // ADD LIB

//    private void showDialog(String info, String header, String title) {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setContentText(info);
//        alert.setHeaderText(header);
//        alert.showAndWait();
//
//    }
}
