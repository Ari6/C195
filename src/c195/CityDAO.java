/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195;

import Controller.CustomerDetailController;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author A Suzuki
 */
public class CityDAO {
    private ObservableList<String> getCity() throws SQLException{
        ResultSet rs = null;
        try {
            String sql = "select city from city;";
            PreparedStatement state = Database.conn.prepareStatement(sql);
            rs = state.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<String> cityList = FXCollections.observableArrayList();
        while(rs.next()){
            cityList.add(rs.getString("city"));
        }
        return cityList;
    }
    
}
