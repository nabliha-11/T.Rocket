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
import javafx.fxml.Initializable;
import utils.ConnectionUtil;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author USER
 */
public class StartController implements Initializable {

    @FXML
    private ComboBox<String> comboBoxx;
    @FXML
    private Button btnStart;
    @FXML
    private ComboBox<String> comboBoxx2;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblStatus2;
    @FXML
    private ComboBox<String> comboBoxx3;
    static int hours;
    static int minutes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        comboBoxx.setItems(FXCollections.observableArrayList(getData()));
        comboBoxx2.getItems().removeAll(comboBoxx2.getItems());
        comboBoxx2.getItems().addAll("0", "1", "2", "3", "4", "5");
        comboBoxx2.getSelectionModel().select("0");
        comboBoxx3.getItems().removeAll(comboBoxx3.getItems());
        comboBoxx3.getItems().addAll("0", "1", "5", "10", "15", "20", "30", "45", "50");
        comboBoxx3.getSelectionModel().select("5");
    }

    public StartController() {
        con = ConnectionUtil.conDB();
    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String username = LoginController.getUser();
    static String start_Act;

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

    public void setAct(String act) {
        start_Act = act;
    }

    public static String getAct() {
        return start_Act;
    }

    public void handleButtonAction(MouseEvent event) throws IOException, SQLException {
        //check if comboBoxx empty
        boolean comboEmpty = comboBoxx.getSelectionModel().isEmpty(); //check if comboBoxx empty

        //comboBoxx values or INPUT
        setAct(comboBoxx.getValue());
        String hours = comboBoxx2.getValue();
        setHours(Integer.parseInt(hours));
        String minutes = comboBoxx3.getValue();
        setMinutes(Integer.parseInt(minutes));
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        //Total time
        double work_hours = Double.parseDouble(hours) + (Double.parseDouble(minutes) / 60);
//        double d = (double) Math.round(work_hours * 1000d) / 1000d;
//        String output = df.format(work_hours);
//        
//        double d = Double.parseDouble(output);
//        System.out.println(d);
        //xp modifier
        double xp_gained = 5 * work_hours;

        String output = df.format(xp_gained);
        double d = Double.parseDouble(output);

        System.out.println(xp_gained);
        //prepared Statements to run queries on activity table
        PreparedStatement ps1;
        PreparedStatement ps2;
        PreparedStatement ps3;
        PreparedStatement ps4;
        //prepared Statements to run queries on main user talbe
        PreparedStatement ps5;
        PreparedStatement ps6;
        //result set for activity table 
        ResultSet rs1;
        ResultSet rs2;
        //result set for user table
        ResultSet rs3;
        ResultSet rs4;
        //variables
        double xp_total;
        double xp_MAIN;
        double hours_worked;

        if (event.getSource() == btnStart) {
            try {

                if (comboEmpty) {

                    lblStatus2.setText("Please select an activity");
                    lblStatus2.setTextFill(Color.RED);
                } else {
                    //FIRST: Reward for starting activity = 10xp. Updating xp for that on the activity table
                    //Also need to update the total xp of the main table

                    String sql = "SELECT `xp_Gained` FROM `activity` WHERE `username` = ? and `activity_name` = ?";
                    ps1 = con.prepareStatement(sql);
                    ps1.setString(1, username);
                    ps1.setString(2, start_Act);
                    rs1 = ps1.executeQuery();
                    rs1.next();
                    xp_total = Double.parseDouble(rs1.getString(1)) + xp_gained;
//                System.out.println(xp_gained);
                    String sql2 = "UPDATE activity SET `xp_Gained` = ? WHERE `username` =? AND `activity_name` = ?";
                    ps2 = con.prepareStatement(sql2);
                    ps2.setString(1, Double.toString(xp_total));
                    ps2.setString(2, username);
                    ps2.setString(3, start_Act);
                    ps2.executeUpdate();

                    //Have to update the main table as well
                    //First retrieving from main table, the total xp thus far and adding the new xp to it
                    String sqlMain = "SELECT total_XP FROM user WHERE users = ?";
                    ps5 = con.prepareStatement(sqlMain);
                    ps5.setString(1, username);
                    rs3 = ps5.executeQuery();
                    rs3.next();
                    xp_MAIN = Double.parseDouble(rs3.getString(1)) + xp_gained;
                    //next updating main table user
                    String sqlMainUpdate = "UPDATE user SET `total_XP` = ? WHERE users = ?";
                    ps6 = con.prepareStatement(sqlMainUpdate);
                    ps6.setString(1, Double.toString(xp_MAIN));
                    ps6.setString(2, username);
                    ps6.executeUpdate();
                    //Next the hours_Spent also needs to be updated in a similar manner

                    String sql3 = "SELECT `hours_Spent` FROM `activity` WHERE `username` = ? and `activity_name` = ?";
                    ps3 = con.prepareStatement(sql3);
                    ps3.setString(1, username);
                    ps3.setString(2, start_Act);
                    rs2 = ps3.executeQuery();
                    rs2.next();

                    hours_worked = Double.parseDouble(rs2.getString(1)) + work_hours;
                    String sql4 = "UPDATE activity SET `hours_Spent` = ? WHERE `username` =? AND `activity_name` = ?";
                    ps4 = con.prepareStatement(sql4);
                    ps4.setString(1, Double.toString(hours_worked));
                    ps4.setString(2, username);
                    ps4.setString(3, start_Act);
                    ps4.executeUpdate();

                    lblStatus2.setText("Activity Successfully Started");
                    lblStatus2.setTextFill(Color.GREEN);

                    //formatting and printing for xp gained message
//                    double d = (double)Math.round(xp_gained * 100d) / 100d;
                    lblStatus.setText("You earned +" + d + " XP!");

                    btnStart.setDisable(true);

                    Scene secondScene = new Scene(FXMLLoader.load(getClass().getResource("TimerOption.fxml")));

                    Stage stage = new Stage();
                    stage.setTitle("Surge Manager");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/sword.png")));
                    stage.setScene(secondScene);

                    stage.show();
                }
            } catch (Exception ex) {
                System.err.println("ConnectionUtil : " + ex.getMessage());
            }
        }

    }

}
