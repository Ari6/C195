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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author A Suzuki
 */
public class AppointmentDAO {
    public static ObservableList<Appointment> getAppointment(
            int userId, Timestamp start, Timestamp limit) throws SQLException {
        ObservableList<Appointment> ret = FXCollections.observableArrayList();
        String sql = "select * from appointment "
                + "where userId = ? and (start >= ? and start < ?) order by start;";
        PreparedStatement state = Database.conn.prepareStatement(sql);
        state.setInt(1, userId);
        state.setTimestamp(2, start);
        state.setTimestamp(3, limit);
        ResultSet rs = state.executeQuery();
        while(rs.next()){
            Appointment ap = new Appointment();
            ap.setAppointmentId(rs.getInt("appointmentId"));
            ap.setCustomerId(rs.getInt("customerId"));
            ap.setUserId(rs.getInt("userId"));
            ap.setTitle(rs.getString("title"));
            ap.setDescription(rs.getString("description"));
            ap.setLocation(rs.getString("location"));
            ap.setContact(rs.getString("contact"));
            ap.setType(rs.getString("type"));
            ap.setUrl(rs.getString("url"));
            ap.setStart(ap.changeUTCToLocal(rs.getTimestamp("start")));
            ap.setEnd(ap.changeUTCToLocal(rs.getTimestamp("end")));
            ap.setCreateDate(rs.getTimestamp("createDate"));
            ap.setCreatedBy(rs.getString("createdBy"));
            ap.setLastUpdate(rs.getTimestamp("lastUpdate"));
            ap.setLastUpdateBy(rs.getString("lastUpdateBy"));
            ret.add(ap);
        }
        return ret;
    }
    public static ObservableList<Appointment> checkOverlap(
            int userId, Timestamp start, Timestamp end) throws SQLException {
        ObservableList<Appointment> ret = FXCollections.observableArrayList();
        String sql = "select * from appointment "
                + "where userId = ? and ("
                + "(start >= ? and start <= ?) or "
                + "(? >= start and ? <= end) or "
                + "(?>= start  and ?<= end)) order by start;";
        PreparedStatement state = Database.conn.prepareStatement(sql);
        state.setInt(1, userId);
        state.setTimestamp(2, start);
        state.setTimestamp(3, end);
        state.setTimestamp(4, start);
        state.setTimestamp(5, start);
        state.setTimestamp(6, end);
        state.setTimestamp(7, end);
        ResultSet rs = state.executeQuery();
        while(rs.next()){
            Appointment ap = new Appointment();
            ap.setAppointmentId(rs.getInt("appointmentId"));
            ap.setCustomerId(rs.getInt("customerId"));
            ap.setUserId(rs.getInt("userId"));
            ap.setTitle(rs.getString("title"));
            ap.setDescription(rs.getString("description"));
            ap.setLocation(rs.getString("location"));
            ap.setContact(rs.getString("contact"));
            ap.setType(rs.getString("type"));
            ap.setUrl(rs.getString("url"));
            ap.setStart(ap.changeUTCToLocal(rs.getTimestamp("start")));
            ap.setEnd(ap.changeUTCToLocal(rs.getTimestamp("end")));
            ap.setCreateDate(rs.getTimestamp("createDate"));
            ap.setCreatedBy(rs.getString("createdBy"));
            ap.setLastUpdate(rs.getTimestamp("lastUpdate"));
            ap.setLastUpdateBy(rs.getString("lastUpdateBy"));
            ret.add(ap);
        }
        return ret;
    }
    
    public Appointment getAppointment(int appointmentId) throws SQLException {
        Appointment ap = new Appointment();
        String sql = "select * from appointment where appointmentId = ?;";
        PreparedStatement state = Database.conn.prepareStatement(sql);
        state.setInt(1, appointmentId);
        ResultSet rs = state.executeQuery();
        if(rs.first()){
            ap.setAppointmentId(rs.getInt("appointmentId"));
            ap.setCustomerId(rs.getInt("customerId"));
            ap.setUserId(rs.getInt("userId"));
            ap.setTitle(rs.getString("title"));
            ap.setDescription(rs.getString("description"));
            ap.setLocation(rs.getString("location"));
            ap.setContact(rs.getString("contact"));
            ap.setType(rs.getString("type"));
            ap.setUrl(rs.getString("url"));
            ap.setStart(ap.changeUTCToLocal(rs.getTimestamp("start")));
            ap.setEnd(ap.changeUTCToLocal(rs.getTimestamp("end")));
            ap.setCreateDate(rs.getTimestamp("createDate"));
            ap.setCreatedBy(rs.getString("createdBy"));
            ap.setLastUpdate(rs.getTimestamp("lastUpdate"));
            ap.setLastUpdateBy(rs.getString("lastUpdateBy"));
        }
        return ap;        
    }
    public static int deleteAppointment(int appointmentId) throws SQLException{
            String sql = "delete from appointment where appointmentId = ?;";
            PreparedStatement state = Database.conn.prepareStatement(sql);
            state.setInt(1, appointmentId);
            return state.executeUpdate();   
    }
    
    public static void updateAppointment(Appointment ap)throws SQLException {
        String sql = "update appointment set " +
                "customerId = ?, title = ?, description = ?, " +
                "location = ?, contact = ?, type = ?, url = ?, " +
                "start = ?, end = ?, lastUpdate = utc_timestamp(), " +
                "lastUpdateBy = ? where appointmentId = ?;";
        PreparedStatement state = Database.conn.prepareStatement(sql);
        state.setInt(1, ap.getCustomerId());
        state.setString(2, ap.getTitle());
        state.setString(3, ap.getDescription());
        state.setString(4, ap.getLocation());
        state.setString(5, ap.getContact());
        state.setString(6, ap.getType());
        state.setString(7, ap.getUrl());
        state.setTimestamp(8, ap.getStart());
        state.setTimestamp(9, ap.getEnd());
        state.setString(10, Session.getLoginName());
        state.setInt(11, ap.getAppointmentId());
        state.executeUpdate();
        
    }
    public static void addAppointment(Appointment ap)throws SQLException {
        String sql = "insert into appointment ( " +
                "customerId, title, description, " +
                "location, contact, type, url, " +
                "start, end, createDate, createdBy, lastUpdate, " +
                "lastUpdateBy, userId) values ("
                + "?,?,?,?,?,?,?,?,?,utc_timestamp(),?, utc_timestamp(), ?,?);";
        PreparedStatement state = Database.conn.prepareStatement(sql);
        state.setInt(1, ap.getCustomerId());
        state.setString(2, ap.getTitle());
        state.setString(3, ap.getDescription());
        state.setString(4, ap.getLocation());
        state.setString(5, ap.getContact());
        state.setString(6, ap.getType());
        state.setString(7, ap.getUrl());
        state.setTimestamp(8, ap.getStart());
        state.setTimestamp(9, ap.getEnd());
        state.setString(10, Session.getLoginName());
        state.setString(11, Session.getLoginName());
        state.setInt(12, Session.getLoginId());
        state.executeUpdate();
        
    }
    public ResultSet reportNumberOfAppointmentTypeOfMonth()
            throws SQLException {
        String sql = "select substring(start,1,7), type, count(type) from ("
                + "select * from appointment) "
                + "as origin group by substring(start,1,7), type;";
        PreparedStatement state = Database.conn.prepareStatement(sql);
//        int year = timestamp.toLocalDateTime().getYear();
//        int month = timestamp.toLocalDateTime().getMonthValue();
//        Timestamp start = Timestamp.valueOf(
//                LocalDateTime.of(year, month, 1, 0, 0));
//        Timestamp end = Timestamp.valueOf(
//                LocalDateTime.of(year, month, 31, 23, 59));
//        state.setTimestamp(1, start);
//        state.setTimestamp(2, end);
        return state.executeQuery();
    }    
}
