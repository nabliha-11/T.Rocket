/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

//import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
//import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
//import javafx.stage.Stage;
import utils.ConnectionUtil;

/**
 *
 * @author USER
 */
public class TimerController implements Initializable {

    int hours = 0;
    int minutes = 0;
    @FXML
    private Button btnStart;

    @FXML
    private Text hoursTimer;

    @FXML
    private Text minutesTimer;

    @FXML
    private Text secondsTimer;

    @FXML
    private Button btnCancel;
    Map<Integer, String> numberMap;
    Integer currSeconds;
    Thread threadCount;

    @FXML
    private Label lblStatus;
    @FXML
    private Label lblBonus;
    @FXML
    private Label lblError;

    @SuppressWarnings("deprecation")
//    @FXML
//    void unStart(ActionEvent event) {
//        
//    }

    Integer hmsToSeconds(Integer h, Integer m, Integer s) {
        Integer hToSeconds = h * 3600;
        Integer mToSeconds = m * 60;
        Integer total = hToSeconds + mToSeconds + s;
        return total;
    }

    LinkedList<Integer> secondsToHms(Integer currSeconds) {
        Integer hours = currSeconds / 3600;
        currSeconds = currSeconds % 3600;
        Integer minutes = currSeconds / 60;
        currSeconds = currSeconds % 60;
        Integer seconds = currSeconds;

        LinkedList<Integer> answer = new LinkedList<>();
        answer.add(hours);
        answer.add(minutes);
        answer.add(seconds);
        return answer;
    }

    public TimerController() {
        con = ConnectionUtil.conDB();
    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String start_Act = StartController.getAct();
    String username = LoginController.getUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        hours = StartController.getHours();

        minutes = StartController.getMinutes();

        numberMap = new TreeMap();
        for (Integer i = 0; i <= 60; i++) {
            if (0 <= i && i <= 9) {
                numberMap.put(i, "0" + i.toString());
            } else {
                numberMap.put(i, i.toString());
            }
        }
        if (hours < 10) {
            hoursTimer.setText("0" + Integer.toString(hours));
        } else {
            hoursTimer.setText(Integer.toString(hours));
        }
        if (minutes < 10) {
            minutesTimer.setText("0" + Integer.toString(minutes));
        } else {
            minutesTimer.setText(Integer.toString(minutes));
        }
        secondsTimer.setText("00");

    }

    public void handleButtonAction(MouseEvent event) throws IOException, SQLException {
        if (event.getSource() == btnStart) {
            btnStart.setDisable(true); 
            currSeconds = hmsToSeconds(hours, minutes, 00);
            try {

                startCountdown();
            } catch (Exception e2) {
                System.out.println(e2);
            }

        }
        if (event.getSource() == btnCancel) {
            if (currSeconds <= 0) {
                lblError.setText("Error. Timer Finished. Start Activity again.");
                lblError.setTextFill(Color.RED);
//                lblStatus.setTextFill(Color.GREEN);
                lblBonus.setText("");
                lblStatus.setText("");
            } else {
                btnStart.setDisable(false); 
                currSeconds = hmsToSeconds(hours, minutes, 00);
                setOutput();
                threadCount.stop();
            }
//            Node node = (Node) event.getSource();
//            Stage stage = (Stage) node.getScene().getWindow();
//            stage.close();
        }
    }

    @SuppressWarnings("Convert2Lambda")
    void startCountdown() {
        threadCount = new Thread(new Runnable() {

            @SuppressWarnings({"deprecation", "SleepWhileInLoop", "CallToThreadStopSuspendOrResumeManager"})
            @Override
            public void run() {
                //initialize the timer
//                Platform.runLater(new Runnable(){
//                    @Override
//                    public void run() {
//                        setOutput();
//                    }
//                });
//                setOutput();
//                currSeconds -= 1;
                while (true) {
                    //countdown here
                    if (Thread.interrupted()) {
                        break;
                    }
                    if (currSeconds > 0) {
                        Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        setOutput();
                    }
                });
                        currSeconds -= 1;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {

                        }
                    }
                    if (currSeconds == 0) {
                        //give XP and success message here
                        Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        setOutput();
                    }
                });
                        double xp_gained = 20;
//                        String sql2 = "UPDATE activity SET `xp_Gained` = ? WHERE `username` =? AND `activity_name` = ?";
//                        PreparedStatement ps2;
                        try {
                            //updating bonus_xp column of main table
                            String sql = "SELECT `bonus_xp` FROM `user` WHERE `users` = ?";
                            PreparedStatement ps1 = con.prepareStatement(sql);
                            ps1.setString(1, username);

                            ResultSet rs1 = ps1.executeQuery();
                            rs1.next();
                            double bonus_total_xp = Double.parseDouble(rs1.getString(1)) + xp_gained;

                            String sql2 = "UPDATE `user` SET `bonus_xp` = ? WHERE `users` = ?";
                            PreparedStatement ps2 = con.prepareStatement(sql2);
                            ps2.setString(1, Double.toString(bonus_total_xp));
                            ps2.setString(2, username);

                            ps2.executeUpdate();

                            //updating total xp column of main table
                            String sql3 = "SELECT `total_XP` FROM `user` WHERE `users` = ?";
                            PreparedStatement ps3 = con.prepareStatement(sql3);
                            ps3.setString(1, username);

                            ResultSet rs2 = ps3.executeQuery();
                            rs2.next();
                            double new_total_xp = Double.parseDouble(rs2.getString(1)) + xp_gained;

                            String sql4 = "UPDATE `user` SET `total_XP` = ? WHERE `users` = ?";
                            PreparedStatement ps4 = con.prepareStatement(sql4);
                            ps4.setString(1, Double.toString(new_total_xp));
                            ps4.setString(2, username);

                            ps4.executeUpdate();

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lblStatus.setText("Timer Complete!");
                                    lblStatus.setTextFill(Color.GREEN);

                                    lblBonus.setText("You earned " + xp_gained + " XP!");
                                    secondsTimer.setText("00");

                                }
                            });
                            currSeconds -= 1;
                            Thread.interrupted();
                        } catch (SQLException ex) {
                            Logger.getLogger(TimerController.class.getName()).log(Level.SEVERE, null, ex);
                        }

//                        setOutput();
//                        secondsTimer.setText("00");
                    }
                    if (currSeconds < 0) {
                        set_0_Output();
                    }

                }

            }
        });
        threadCount.start();
    }

    void setOutput() {
        
        LinkedList<Integer> currHms = secondsToHms(currSeconds);
        hoursTimer.setText(numberMap.get(currHms.get(0)));
        minutesTimer.setText(numberMap.get(currHms.get(1)));
        secondsTimer.setText(numberMap.get(currHms.get(2)));

    }

    void set_0_Output() {
        
        LinkedList<Integer> currHms = secondsToHms(0);
        hoursTimer.setText(numberMap.get(currHms.get(0)));
        minutesTimer.setText(numberMap.get(currHms.get(1)));
        secondsTimer.setText(numberMap.get(currHms.get(2)));

    }
}