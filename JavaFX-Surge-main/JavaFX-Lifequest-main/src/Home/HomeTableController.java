/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Home;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.ConnectionUtil;

/**
 *
 * @author USER
 */
public class HomeTableController implements Initializable {

    @FXML
    private TableView<ModelTable> tbl;
    @FXML
    private TableView<ModelTable> tbl2;
    @FXML
    private TableColumn<ModelTable, String> tbl_activity;
    @FXML
    private TableColumn<ModelTable, String> tbl_xpGained;
    @FXML
    private TableColumn<ModelTable, String> tbl_Hours;
    @FXML
    private TableColumn<ModelTable, String> tbl_bonus;
    @FXML
    private TableColumn<ModelTable, String> tbl_total;

    ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
    ObservableList<ModelTable> oblist2 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        con = ConnectionUtil.conDB();
        String sql = "SELECT* FROM activity where username = ?";
        String sql2 = "SELECT total_XP,bonus_xp FROM user where users = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                
                oblist.add(new ModelTable(rs.getString("activity_name"), rs.getString("xp_Gained"), rs.getString("hours_Spent")));
//                oblist.add(new ModelTable(rs.getString("total_XP"), rs.getString("bonus_xp")));
            }
            ps = con.prepareStatement(sql2);
            ps.setString(1, username);
            rs = ps.executeQuery();

            while (rs.next()) {
//                System.out.println("inside");
//                System.out.println(rs.getString("total_XP"));
//                System.out.println(rs.getString("bonus_xp"));
                
                oblist2.add(new ModelTable(rs.getString("total_XP"), rs.getString("bonus_xp")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(HomeTableController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tbl_activity.setCellValueFactory(new PropertyValueFactory<>("Activity"));
        tbl_xpGained.setCellValueFactory(new PropertyValueFactory<>("XP_gained"));
        tbl_Hours.setCellValueFactory(new PropertyValueFactory<>("Hours_spent"));
        
        tbl_bonus.setCellValueFactory(new PropertyValueFactory<>("Bonus_xp"));
        tbl_total.setCellValueFactory(new PropertyValueFactory<>("Total_XP"));

        tbl.setItems(oblist);
        tbl2.setItems(oblist2);
    }
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String username = LoginController.getUser();
//    public void Populate() throws SQLException{
//        String sql1 = "SELECT activity_name FROM activity WHERE username = ?";
//        PreparedStatement ps1 = con.prepareStatement(sql1);
//        ps1.setString()
//    }
}
