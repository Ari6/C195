/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import c195.CustomerAddress;
import c195.CustomerAddressDAO;
import c195.Database;
import c195.Session;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A Suzuki
 */
public class CustomerController implements Initializable {
    @FXML
    private TableView<CustomerAddress> customerTable;
    @FXML
    private TableColumn<CustomerAddress, IntegerProperty> id;
    @FXML
    private TableColumn<CustomerAddress, String> name;
    @FXML
    private TableColumn<CustomerAddress, String> address;
    @FXML
    private TableColumn<CustomerAddress, String> address2;
    @FXML
    private TableColumn<CustomerAddress, String> city;
    @FXML
    private TableColumn<CustomerAddress, String> phone;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;
    @FXML
    private Label message;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        id.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        address2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        city.setCellValueFactory(new PropertyValueFactory<>("city"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        setTable(); 
    }    

    @FXML
    private void addButtonOnAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/View/CustomerDetail.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Customer Edit");
        stage.setScene(scene);
        stage.show();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void editButtonOnAction(ActionEvent event) 
            throws IOException, SQLException {
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            int selectedId = customerTable.getSelectionModel()
                    .getSelectedItem().getCustomerId();
            String sql = "select customerId from customer where customerId = ?";
            PreparedStatement statement = Database.conn.prepareStatement(sql);
            statement.setInt(1, selectedId);
            ResultSet rs = statement.executeQuery();
            int count = rs.last() ? rs.getRow() : 0;
            if(count == 0){
                if(Locale.getDefault().getLanguage().equals("en")){
                    setMessage("The customer you selected does not exist.");
                } else {
                    setMessage("選択された顧客は存在しません。");
                }
                customerTable.setItems(
                        CustomerAddressDAO.getAllCustomerAddress());
            } else {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/View/CustomerDetail.fxml"));
                Parent root = loader.load();
                CustomerDetailController CDController = loader.getController();
                CDController.setFields(selectedId);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Customer Edit");
                stage.setScene(scene);
                stage.show();
                ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
            }
        } else {
            if(Locale.getDefault().getLanguage().equals("en")){
                setMessage("You need to select a line.");
            } else {
                setMessage("列を選択してください。");
            }
        }
    }

    @FXML
    private void deleteButtonOnAction(ActionEvent event) 
            throws SQLException, IOException{
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            int selectedId = customerTable.getSelectionModel()
                    .getSelectedItem().getCustomerId();
            String sql = "delete from customer where customerId = ?";
            PreparedStatement statement = Database.conn.prepareStatement(sql);
            statement.setInt(1, selectedId);
            statement.execute();
            customerTable.setItems(
                        CustomerAddressDAO.getAllCustomerAddress());
            if(Locale.getDefault().getLanguage().equals("en")){
                setMessage("The customer you selected has been deleted.");
            } else {
                setMessage("顧客が削除されました。");
            }
            Session.writeLog("Deleted a customer recoed customerId = " + selectedId);
        } else {
            if(Locale.getDefault().getLanguage().equals("en")){
                setMessage("You need to select a line.");
            } else {
                setMessage("列を選択してください。");
            }
        }
    }

    @FXML
    private void backButtonOnAction(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/View/Calendar.fxml"));
        Parent root = loader.load();
        CalendarController cc = loader.getController();
        Timestamp limit;
        if(Session.getView() == 0){
            limit = Timestamp.valueOf(Session.getStartDate().plusWeeks(1));
        } else {
            limit = Timestamp.valueOf(Session.getStartDate().plusMonths(1));
        }
        cc.setTable(Timestamp.valueOf(Session.getStartDate()),limit);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();        
    } 
    
    void setMessage(String message){
        this.message.setText(message);
    }
    
    void setTable(){
        try {
            customerTable.setItems(CustomerAddressDAO.getAllCustomerAddress());
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
}
