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
public class CountryDAO {
    private ObservableList<String> getCountry() throws SQLException{
        ResultSet rs = null;
        try {
            String sql = "select country from country;";
            PreparedStatement state = Database.conn.prepareStatement(sql);
            rs = state.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<String> countryList = FXCollections.observableArrayList();
        if(rs != null) {
            while(rs.next()){
                countryList.add(rs.getString("country"));
            }
        }
        return countryList;
    }        
}
