/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
public class GroupActController implements Initializable {

    @FXML
    private TextField grp_act_name;
    @FXML
    private Button btnConfirm;
    @FXML
    private ComboBox<String> combo1;
    @FXML
    private ComboBox<String> combo2;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblXP;

    static int hours;
    static int minutes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        combo1.getItems().removeAll(combo1.getItems());
        combo1.getItems().addAll("0", "1", "2", "3", "4", "5");
        combo1.getSelectionModel().select("0");
        combo2.getItems().removeAll(combo2.getItems());
        combo2.getItems().addAll("0", "1", "5", "10", "15", "20", "30", "45", "50");
        combo2.getSelectionModel().select("5");
    }

    public GroupActController() {
        con = ConnectionUtil.conDB();
    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String username = LoginController.getUser();

    public void setHours(int h) {
        hours = h;
    }

    public static int getHours() {
        return hours;
    }

    public void setMinutes(int m) {
        minutes = m;
    }

    public static int getMinutes() {
        return minutes;
    }

    public void handleButtonAction(MouseEvent event) throws SQLException, IOException {
        String group_act = grp_act_name.getText();
        System.out.println(group_act);
        if (event.getSource() == btnConfirm) {
            if (grp_act_name.getText().trim().equals("")) {
//             System.out.println("inside emptycheck false");
                lblStatus.setText("Enter Activity Name");
                lblStatus.setTextFill(Color.RED);
            } else {

                String sql = "INSERT INTO `group_activity`(`act_name`) VALUES (?)";
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, group_act);
                preparedStatement.executeUpdate();
                sql = "SELECT LAST_INSERT_ID()";
                preparedStatement = con.prepareStatement(sql);
                resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int grp_act_id = Integer.parseInt(resultSet.getString(1));
                System.out.println(grp_act_id);
//            sql = "SELECT `act_id` WHERE ";

                String hours = combo1.getValue();
                setHours(Integer.parseInt(hours));
                String minutes = combo2.getValue();
                setMinutes(Integer.parseInt(minutes));
                DecimalFormat df = new DecimalFormat("#.###");
                df.setRoundingMode(RoundingMode.CEILING);
                //Total time
                double grp_work_hours = Double.parseDouble(hours) + (Double.parseDouble(minutes) / 60);

                double grp_xp_gained = 8 * grp_work_hours;

                String output = df.format(grp_xp_gained);
                double d = Double.parseDouble(output);

                double xp_MAIN;
//                double hours_worked;

                //For every "existing" member, first, updating group member table, then updating main table's total xp
                //First updating the Group member table info for each user
                if (GroupMemController.getM1() != null) {
                    sql = "INSERT INTO `group_members` (`username`,`group_act_id`,`grp_xp_gained`,`grp_hours_spent`) VALUES(?,?,?,?)";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, GroupMemController.getM1());
                    preparedStatement.setString(2, Integer.toString(grp_act_id));
                    preparedStatement.setString(3, Double.toString(grp_xp_gained));
                    preparedStatement.setString(4, Double.toString(grp_work_hours));
                    preparedStatement.executeUpdate();

                    //Getting total XP from main table and adding the newly gained XP
                    sql = "SELECT total_XP FROM user WHERE users = ?";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, GroupMemController.getM1());
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    xp_MAIN = Double.parseDouble(resultSet.getString(1)) + grp_xp_gained;

                    //updating the main table with the new total xp
                    sql = "UPDATE user SET `total_XP` = ? WHERE users = ?";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, Double.toString(xp_MAIN));
                    preparedStatement.setString(2, GroupMemController.getM1());
                    preparedStatement.executeUpdate();
                }

                //Doing the same for the rest of the users, if any
                if (GroupMemController.getM2() != null) {
                    sql = "INSERT INTO `group_members` (`username`,`group_act_id`,`grp_xp_gained`,`grp_hours_spent`) VALUES(?,?,?,?)";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, GroupMemController.getM2());
                    preparedStatement.setString(2, Integer.toString(grp_act_id));
                    preparedStatement.setString(3, Double.toString(grp_xp_gained));
                    preparedStatement.setString(4, Double.toString(grp_work_hours));
                    preparedStatement.executeUpdate();

                    //Getting total XP from main table and adding the newly gained XP
                    sql = "SELECT total_XP FROM user WHERE users = ?";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, GroupMemController.getM2());
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    xp_MAIN = Double.parseDouble(resultSet.getString(1)) + grp_xp_gained;

                    //updating the main table with the new total xp
                    sql = "UPDATE user SET `total_XP` = ? WHERE users = ?";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, Double.toString(xp_MAIN));
                    preparedStatement.setString(2, GroupMemController.getM2());
                    preparedStatement.executeUpdate();
                }

                if (GroupMemController.getM3() != null) {
                    sql = "INSERT INTO `group_members` (`username`,`group_act_id`,`grp_xp_gained`,`grp_hours_spent`) VALUES(?,?,?,?)";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, GroupMemController.getM3());
                    preparedStatement.setString(2, Integer.toString(grp_act_id));
                    preparedStatement.setString(3, Double.toString(grp_xp_gained));
                    preparedStatement.setString(4, Double.toString(grp_work_hours));
                    preparedStatement.executeUpdate();

                    //Getting total XP from main table and adding the newly gained XP
                    sql = "SELECT total_XP FROM user WHERE users = ?";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, GroupMemController.getM3());
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    xp_MAIN = Double.parseDouble(resultSet.getString(1)) + grp_xp_gained;

                    //updating the main table with the new total xp
                    sql = "UPDATE user SET `total_XP` = ? WHERE users = ?";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, Double.toString(xp_MAIN));
                    preparedStatement.setString(2, GroupMemController.getM3());
                    preparedStatement.executeUpdate();
                }

                if (GroupMemController.getM4() != null) {
                    sql = "INSERT INTO `group_members` (`username`,`group_act_id`,`grp_xp_gained`,`grp_hours_spent`) VALUES(?,?,?,?)";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, GroupMemController.getM4());
                    preparedStatement.setString(2, Integer.toString(grp_act_id));
                    preparedStatement.setString(3, Double.toString(grp_xp_gained));
                    preparedStatement.setString(4, Double.toString(grp_work_hours));
                    preparedStatement.executeUpdate();

                    //Getting total XP from main table and adding the newly gained XP
                    sql = "SELECT total_XP FROM user WHERE users = ?";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, GroupMemController.getM4());
                    resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    xp_MAIN = Double.parseDouble(resultSet.getString(1)) + grp_xp_gained;

                    //updating the main table with the new total xp
                    sql = "UPDATE user SET `total_XP` = ? WHERE users = ?";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, Double.toString(xp_MAIN));
                    preparedStatement.setString(2, GroupMemController.getM4());
                    preparedStatement.executeUpdate();

                }
                lblStatus.setText("Activity Successfully Started");
                lblStatus.setTextFill(Color.GREEN);

                lblXP.setText("Everyone earned +" + d + " XP!");
                btnConfirm.setDisable(true);

                Scene secondScene = new Scene(FXMLLoader.load(getClass().getResource("GroupTimerOption.fxml")));

                Stage stage = new Stage();
                stage.setScene(secondScene);
                stage.setTitle("Surge Manager");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sword.png")));

                stage.show();
            }
        }
    }
}
