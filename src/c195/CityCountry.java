/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author A Suzuki
 */
public class CityCountry {
    private int cityId;
    private String city;
    private int countryId;
    private String country;
    
    public static ObservableList<String> createCityCountry() throws SQLException{
        ObservableList<String> ret = FXCollections.observableArrayList();
        String sql = "select ci.cityId, ci.city, co.countryId, "
                + "co.country from city as ci "
                + "inner join country as co "
                + "where ci.countryId = co.countryId; ";
        PreparedStatement state = Database.conn.prepareStatement(sql);
        ResultSet rs = state.executeQuery();
        while(rs.next()){
            ret.add(rs.getInt("cityId") + ":" + rs.getString("city") + " - " + 
                    rs.getInt("countryId") + ":" + rs.getString("country"));
        }
        return ret;
    }
}
