/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import c195.CityCountry;
import c195.CustomerAddress;
import c195.CustomerAddressDAO;
import c195.Session;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A Suzuki
 */
public class CustomerDetailController implements Initializable {
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField address2;
    @FXML
    private TextField phone;
    @FXML
    private ChoiceBox<String> city;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML private TextField postalCode;
    @FXML private Label message;
    private boolean addFlg = true; 
    private int customerId;
    private int addressId;
    private int cityId;
    private int countryId;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> cityCountry = FXCollections.observableArrayList();
        
        try {
            cityCountry = CityCountry.createCityCountry();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDetailController.class.getName()).log(Level.SEVERE, null, ex);
        }
        city.setItems(cityCountry);
        
    }    



    @FXML
    private void saveButtonOnAction(ActionEvent event) throws SQLException, IOException {
        if(!this.city.getSelectionModel().isEmpty()){
            CustomerAddress ca = new CustomerAddress(
                    this.customerId,
                    this.name.getText(), this.addressId, this.address.getText(), 
                    this.address2.getText(), 
                    Integer.parseInt(this.city.getSelectionModel().
                            getSelectedItem().split(":*")[0]), //FIXED 
                    "",0,"",
                    this.phone.getText(), this.postalCode.getText()
            );
            if(inputValidation()){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Customer.fxml"));
                Parent root = loader.load();
                CustomerController cc = loader.getController();
                CustomerAddressDAO caDao = new CustomerAddressDAO();
                if(addFlg) {
                    caDao.addCustomerAddress(ca);
                    if(Locale.getDefault().getLanguage().equals("en")){
                        setMessage("The customer has been added.");
                    } else {
                        setMessage("顧客が追加されました。");
                    }
                    cc.setTable();
                    Session.writeLog("Added a customer record");
                } else {
                    caDao.updateCustomerAddress(ca);
                    if(Locale.getDefault().getLanguage().equals("en")){
                        setMessage("The customer has been updated.");
                    } else {
                        setMessage("顧客が変更されました。");
                    }
                    cc.setTable();
                    Session.writeLog("Updated a customer record customerId = " + ca.getCustomerId());
                }
                if(((Stage)((Node)event.getSource()).
                        getScene().getWindow()).getOwner() == null){
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setTitle("Customer");
                    stage.setScene(scene);
                    stage.show();
                    ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
                } else {
                    ((Stage)((Node)event.getSource()).getScene().getWindow()).close();                
                }
            }
        } else {
                if(Locale.getDefault().getLanguage().equals("en")){
                    setMessage("You need to select city.");
                } else {
                    setMessage("市を選択してください。");
                }
        }
    }
    boolean inputValidation(){
        if(name.getText().isEmpty()){
            if(Locale.getDefault().getLanguage().equals("en")){
                setMessage("Name is require.");
            } else {
                setMessage("名前は必須項目です。");
            }
            return false;
        } else {
            if(address.getText().isEmpty()){
                if(Locale.getDefault().getLanguage().equals("en")){
                    setMessage("address is require.");
                } else {
                    setMessage("住所は必須項目です。");
                }
                return false;
            } else {
                if(postalCode.getText().isEmpty()){
                    if(Locale.getDefault().getLanguage().equals("en")){
                        setMessage("Postal code is require.");
                    } else {
                        setMessage("郵便番号は必須項目です。");
                    }
                    return false;
                } else {
                    if(phone.getText().isEmpty()){
                        if(Locale.getDefault().getLanguage().equals("en")){
                            setMessage("Phone is require.");
                        } else {
                            setMessage("電話番号は必須項目です。");
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }
    @FXML
    private void cancelButtonOnAction(ActionEvent event) throws IOException {
        if(((Stage)((Node)event.getSource()).
                getScene().getWindow()).getOwner() == null){
            Parent root = FXMLLoader.load(
                    getClass().getResource("/View/Customer.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Customer");
            stage.setScene(scene);
            stage.show();
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        } else {
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();        
        }
    }
    
    void setFields(int id) throws SQLException {
        CustomerAddress ca = CustomerAddressDAO.getCustomerAddress(id);
        if(ca!= null){
            this.customerId = ca.getCustomerId();
            this.name.setText(ca.getName());
            this.addressId = ca.getAddressId();
            this.address.setText(ca.getAddress());
            this.address2.setText(ca.getAddress2());
            this.cityId = ca.getCityId();
            this.city.getSelectionModel()
                    .select(ca.getCityId()+ ":" + 
                            ca.getCity() + " - " +
                            ca.getCountryId() + ":" + ca.getCountry());
            this.countryId = ca.getCountryId();
            this.phone.setText(ca.getPhone());
            this.postalCode.setText(ca.getPostalCode());
            this.addFlg = false;
            
        }
    }

    void setMessage(String message){
        this.message.setText(message);
   }
}
