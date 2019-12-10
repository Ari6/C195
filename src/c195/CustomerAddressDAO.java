/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author A Suzuki
 */
public class CustomerAddressDAO {
    
    public static ObservableList<CustomerAddress> getAllCustomerAddress() throws SQLException {
        ResultSet rs = null;
        ObservableList<CustomerAddress> customerAddress = 
                FXCollections.observableArrayList();
        String sql = "select cus.customerId, cus.customerName, cus.addressId, "
                + "ad.address," +
                "ad.address2, ci.cityId, ci.city, co.countryId, "
                + "co.country, ad.phone, ad.postalCode " +
                "from ((customer as cus " +
                "inner join address as ad on cus.addressId = ad.addressId )" +
                "inner join city as ci on ad.cityId = ci.cityId) " +
                "inner join country as co on ci.countryId = co.countryId "
                + "order by customerId;";
        try {
            PreparedStatement statement = Database.conn.prepareStatement(sql);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        }
        while(rs.next()){
            customerAddress.add(new CustomerAddress(
                    rs.getInt("customerId"),
                    rs.getString("customerName"),
                    rs.getInt("addressId"),
                    rs.getString("address"),
                    rs.getString("address2"),
                    rs.getInt("cityId"),
                    rs.getString("city"),
                    rs.getInt("countryId"),
                    rs.getString("country"),
                    rs.getString("phone"),
                    rs.getString("postalCode")

                    ));
        }
        return customerAddress;
    }
    public static CustomerAddress getCustomerAddress(int id) throws SQLException {
        ResultSet rs = null;
        CustomerAddress customerAddress = null;
        String sql = "select cus.customerId, cus.customerName, ad.addressId, ad.address," +
        "ad.address2, ci.cityId, ci.city, co.countryId, co.country, ad.phone, ad.postalCode " +
        "from ((customer as cus " +
        "inner join address as ad on cus.addressId = ad.addressId )" +
        "inner join city as ci on ad.cityId = ci.cityId) " +
        "inner join country as co on ci.countryId = co.countryId " + 
        "where cus.customerId = ?;";
        try {
            PreparedStatement statement = Database.conn.prepareStatement(sql);
            statement.setInt(1, id);
            rs = statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        }
        if(rs.next()){
            customerAddress = new CustomerAddress(
                    rs.getInt("customerId"),
                    rs.getString("customerName"),
                    rs.getInt("addressId"),
                    rs.getString("address"),
                    rs.getString("address2"),
                    rs.getInt("cityId"),
                    rs.getString("city"),
                    rs.getInt("countryId"),
                    rs.getString("country"),
                    rs.getString("phone"),
                    rs.getString("postalCode")
                    );
        }
        return customerAddress;
    }
    public void addCustomerAddress(CustomerAddress ca) throws SQLException{
        //Insert data to address table
        String sql = "Insert into address("
            + "address, address2, cityId, postalCode, phone, "
            + "createDate, createdBy, lastUpdate, lastUpdateBy)"
            + "values(?,?,?,?,?, utc_timestamp(),?,utc_timestamp(),?); ";
        PreparedStatement state = Database.conn.prepareStatement(sql);
        state.setString(1, ca.getAddress());
        state.setString(2, ca.getAddress2());
        state.setInt   (3, ca.getCityId());
        state.setString(4, ca.getPostalCode());
        state.setString(5, ca.getPhone());
        state.setString(6, Session.getLoginName());
        state.setString(7, Session.getLoginName());
        state.executeUpdate();
        
        sql = "Insert into customer("
            + "customerName, addressId, active, createDate, createdBy, "
            + "lastUpdate, lastUpdateBy) values (?,last_insert_id(),1,utc_timestamp(),?,utc_timestamp(),?);";                    
        state = Database.conn.prepareStatement(sql);
        state.setString(1, ca.getName());
        state.setString(2, Session.getLoginName());
        state.setString(3, Session.getLoginName());
        state.executeUpdate();
    }
    
    public void updateCustomerAddress(CustomerAddress ca) throws SQLException{
        String sql = "update address set " +
                "address = ?, address2 = ?, cityId = ?, postalCode = ?, " +
                "phone = ?, lastUpdate = utc_timestamp(), lastUpdateBy = ? " +
                "where addressId = ?;";
        PreparedStatement state = Database.conn.prepareStatement(sql);
        state.setString(1, ca.getAddress());
        state.setString(2, ca.getAddress2());
        state.setInt(3, ca.getCityId());
        state.setString(4, ca.getPostalCode());
        state.setString(5, ca.getPhone());
        state.setString(6, Session.getLoginName());
        state.setInt(7, ca.getAddressId());
        state.executeUpdate();
        sql = "update customer set " + 
                "customerName = ?, lastUpdate = utc_timestamp(), lastUpdateBy = ? " +
                "where customerId = ?;";
        state = Database.conn.prepareStatement(sql);
        state.setString(1, ca.getName());
        state.setString(2, Session.getLoginName());
        state.setInt(3, ca.getCustomerId());
        state.executeUpdate();
    }

}
