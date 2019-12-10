/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package c195;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author A Suzuki
 */
public class UserDAO {
    public User getUser(int userId) throws SQLException {
        String sql = "SELECT * from user where userId = ?";
        PreparedStatement statement = Database.conn.prepareStatement(sql);
        statement.setString(1, String.valueOf(userId));
        ResultSet rs = statement.executeQuery();
        User user = null;
        if(rs.next()){
            user = new User();
            user.setUserId(rs.getInt("userId"));
            user.setUserName(rs.getString("userName"));
            user.setPassword(rs.getString("password"));
            user.setActive(rs.getShort("active"));
            user.setCreateDate(rs.getTimestamp("createDate"));
            user.setCreatedBy(rs.getString("createdBy"));
            user.setLastUpdate(rs.getTimestamp("lastUpdate"));
            user.setLastUpdateBy(rs.getString("lastUpdateBy"));
        }
        return user;
    }
    public User getUserWithUserName(String userName) throws SQLException{
        String sql = "Select userId from user where userName = ?";
        PreparedStatement statement = Database.conn.prepareStatement(sql);
        statement.setString(1, userName);
        ResultSet rs = statement.executeQuery();
        int userId = 0;
        if(rs.next()){
            userId = rs.getInt("userId");
        }
        return getUser(userId);
    }
    public List<User> getAllUser()throws SQLException {
        String sql = "Select * from user order by userId;";
        PreparedStatement state = Database.conn.prepareStatement(sql);
        ResultSet rs = state.executeQuery();
        List<User> userList = new ArrayList<>();
        while(rs.next()){
            userList.add(getUser(rs.getInt("userId")));
        }
        return userList;
    }
}
