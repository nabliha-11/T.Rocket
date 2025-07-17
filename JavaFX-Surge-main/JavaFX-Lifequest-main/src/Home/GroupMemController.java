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

/**
 *
 * @author USER
 */
public class GroupMemController implements Initializable {

    @FXML
    private TextField txt_m1;
    @FXML
    private TextField txt_m2;
    @FXML
    private TextField txt_m3;
    @FXML
    private TextField txt_m4;
    @FXML
    private Button btnDone;
    @FXML
    private Label lblStatus;

    static String m1 = null;
    static String m2 = null;
    static String m3 = null;
    static String m4 = null;
    static String username = Home.LoginController.getUser();

    void setM1(String m) {
        m1 = m;
    }

    static String getM1() {
        return m1;
    }

    void setM2(String m) {
        m2 = m;
    }

    static String getM2() {
        return m2;
    }

    void setM3(String m) {
        m3 = m;
    }

    static String getM3() {
        return m3;
    }

    void setM4(String m) {
        m4 = m;
    }

    static String getM4() {
        return m4;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public GroupMemController() {
        con = ConnectionUtil.conDB();
    }

    public void handleButtonAction(MouseEvent event) throws IOException {
        String u1 = txt_m1.getText();
        String u2 = txt_m2.getText();
        String u3 = txt_m3.getText();
        String u4 = txt_m4.getText();

        if (event.getSource() == btnDone) {

//
//            System.out.println(m1);
//            System.out.println(m2);
//            System.out.println(m3);
//            System.out.println(m4);
            if (partyCheck(u1, u2, u3, u4) == "Success") {
                if (!u1.trim().equals("")) {
                    setM1(u1);
                }
                if (!u2.trim().equals("")) {
                    setM2(u2);
                }
                if (!u3.trim().equals("")) {
                    setM3(u3);
                }
                if (!u4.trim().equals("")) {
                    setM4(u4);
                }

                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
//                    stage.initStyle(StageStyle.DECORATED);
//                    stage.setMaximized(true);
                stage.close();

                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("GroupAct.fxml")));
                stage.setScene(scene);
                stage.setTitle("Surge Manager: Group Activity");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/teamwork.png")));
                stage.show();
            }
        }
    }

    Boolean selfCheck(String username1, String username2, String username3, String username4) {

        if (username1.equals(username) || username2.equals(username) || username3.equals(username) || username4.equals(username)) {
//            System.out.println("inside selfcheck false");
            return false;
        }
        return true;
    }

    Boolean emptyCheck(String username1) {
        if (username1.trim().equals("")) {
//             System.out.println("inside emptycheck false");
            return false;
        }
        return true;
    }

    Boolean checkUser(String username1, String username2, String username3, String username4) {
        String sql = "SELECT* FROM user WHERE users = ?";
        if (!username1.trim().equals("")) {
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, username1);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
//                    System.out.println("inside checkUser false");
                    return false;
                }

            } catch (Exception ex) {
                System.err.println("ConnectionUtil : " + ex.getMessage());

            }
        }
        if (!username2.trim().equals("")) {
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, username2);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    lblStatus.setTextFill(Color.TOMATO);
                    lblStatus.setText("Enter correct information");
                    return false;
                }

            } catch (Exception ex) {
                System.err.println("ConnectionUtil : " + ex.getMessage());

            }
        }
        if (!username3.trim().equals("")) {
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, username3);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    lblStatus.setTextFill(Color.TOMATO);
                    lblStatus.setText("Enter correct information");
                    return false;
                }

            } catch (Exception ex) {
                System.err.println("ConnectionUtil : " + ex.getMessage());

            }
        }
        if (!username4.trim().equals("")) {
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, username4);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    lblStatus.setTextFill(Color.TOMATO);
                    lblStatus.setText("Enter correct information");
                    return false;
                }

            } catch (Exception ex) {
                System.err.println("ConnectionUtil : " + ex.getMessage());

            }
        }
        return true;
    }

    String partyCheck(String user1, String user2, String user3, String user4) {
        if (!emptyCheck(user1) || !selfCheck(user1, user2, user3, user4) || !checkUser(user1, user2, user3, user4)) {
            lblStatus.setTextFill(Color.TOMATO);
            lblStatus.setText("Invalid User(s)");
            return "Error";
        } else {
            return "Success";
        }
    }
}
