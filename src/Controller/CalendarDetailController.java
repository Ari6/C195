/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import c195.Appointment;
import c195.AppointmentDAO;
import c195.CustomerAddress;
import c195.CustomerAddressDAO;
import c195.Session;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.nashorn.internal.objects.NativeArray;

/**
 * FXML Controller class
 *
 * @author A Suzuki
 */
public class CalendarDetailController implements Initializable {
    @FXML
    private TextField title;
    @FXML
    private TextField location;
    @FXML
    private TextField type;
    @FXML
    private TextField url;
    @FXML
    private TextField contact;
    @FXML
    private TextArea description;
    @FXML
    private ChoiceBox<String> customer;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label message;
    @FXML
    private Button seeButton;
    @FXML private ChoiceBox<Integer> startHH;
    @FXML private ChoiceBox<Integer> startmm;    
    @FXML private ChoiceBox<Integer> endHH;
    @FXML private ChoiceBox<Integer> endmm;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    private int appointmentId;
    private final ObservableList<Integer> hour = 
            FXCollections.observableArrayList();
    private final ObservableList<Integer> minute = 
            FXCollections.observableArrayList();

    /**,
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for(int i = 0; i < 24; i++){
            hour.add(i);
        }
        for(int i = 0; i < 60; i++){
            minute.add(i);
        }
        startHH.setItems(hour);
        startmm.setItems(minute);
        endHH.setItems(hour);
        endmm.setItems(minute);
        
        try{
            setCustomer();
        } catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        }
    }      

    @FXML
    private void saveButtonOnAction(ActionEvent event) 
            throws SQLException, IOException {
        if(startDatePicker.getValue() != null &&
                endDatePicker.getValue() != null &&
                startHH.getValue() != null &&
                startmm.getValue() != null &&
                endHH.getValue() != null &&
                endmm.getValue() != null) {
            if(startHH.getValue() < 9 || endHH.getValue() < 9 ||
                    (startHH.getValue() == 18 && startmm.getValue() != 0) ||
                    (endHH.getValue() == 18 && endmm.getValue() != 0) ||
                    startHH.getValue() >= 19 || endHH.getValue() >= 19 ){
                if(Locale.getDefault().getLanguage().equals("en")){
                    setMessage("You need to schedule in our bussiness hour.(9-18)");
                } else {
                    setMessage("営業時間9時から18時までの間にスケジュールしてください。");
                }
            } else if(customer.getSelectionModel().isEmpty()){
                if(Locale.getDefault().getLanguage().equals("en")){
                    setMessage("You need to select a customer.");
                } else {
                    setMessage("顧客を選択してください。");
                }
            }else {
                Appointment ap = new Appointment();
                Timestamp tsStart = 
                        ap.changeLocalToUTC(Timestamp.valueOf(
                        (startDatePicker.getValue() + " "  +
                         startHH.getValue() + ":" + startmm.getValue() + 
                         ":00")));
                Timestamp tsEnd =                         
                        ap.changeLocalToUTC(Timestamp.valueOf(
                        (endDatePicker.getValue() + " "  +
                         endHH.getValue() + ":" + endmm.getValue() + 
                         ":00")));
                if(tsStart.before(tsEnd)){
                    ObservableList<Appointment> ol = 
                            AppointmentDAO.checkOverlap(
                                    Session.getLoginId(), tsStart, tsEnd);
                    if((ol.size() < 2 && appointmentId != 0) || (ol.size() == 0 && appointmentId == 0)){
                        String time = startHH.getValue() + ":" + startmm.getValue();
                        ap.setStart(ap.changeLocalToUTC(
                                Timestamp.valueOf(
                                        (startDatePicker.getValue() + " " + time + ":00"))));
                        time = endHH.getValue() + ":" + endmm.getValue();
                        ap.setEnd(ap.changeLocalToUTC(
                                Timestamp.valueOf(
                                        (endDatePicker.getValue() + " " + time + ":00"))));            
                        ap.setCustomerId(Integer.parseInt(customer.getValue().split(" - ")[0]));
                        ap.setTitle(title.getText());
                        ap.setDescription(description.getText());
                        ap.setLocation(location.getText());
                        ap.setContact(contact.getText());
                        ap.setType(type.getText());
                        ap.setUrl(url.getText());
                        if(appointmentId == 0){
                            AppointmentDAO.addAppointment(ap);
                            Session.writeLog("Added an appointment record");
                        } else {
                            ap.setAppointmentId(appointmentId);
                            AppointmentDAO.updateAppointment(ap);
                            Session.writeLog("Updated an appointment record appointId = "
                            + ap.getAppointmentId());
                        }
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/View/Calendar.fxml"));
                        Parent root = loader.load();
                        CalendarController cc = loader.getController();
                        if(Locale.getDefault().getLanguage().equals("en")){
                            cc.setMessage("The record has been saved.");
                        } else {
                            cc.setMessage("保存されました。");
                        }
                        Timestamp limit;
                        if(Session.getView() == 0){
                            limit = Timestamp.valueOf(
                                    Session.getStartDate().plusWeeks(1));
                        } else {
                            limit = Timestamp.valueOf(
                                    Session.getStartDate().plusMonths(1));                
                        }
                        cc.setTable(Timestamp.valueOf(
                                Session.getStartDate()), limit);
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setTitle("Calendar");
                        stage.setScene(scene);
                        stage.show();
                        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
                    } else {
                        if(Locale.getDefault().getLanguage().equals("en")){
                            setMessage("The schedule is overlapped.");
                        } else {
                            setMessage("スケジュールが重複しています。");
                        }
                    }
                } else {
                    if(Locale.getDefault().getLanguage().equals("en")){
                        setMessage("End has to be later than Start.");
                    } else {
                        setMessage("終了は開始より後の時点を指定してください。");
                    }
                }
            }
        } else if(customer.getSelectionModel().isEmpty()){
            if(Locale.getDefault().getLanguage().equals("en")){
                setMessage("You need to select a customer.");
            } else {
                setMessage("顧客を選択してください。");
            }
        } else {
            if(Locale.getDefault().getLanguage().equals("en")){
                setMessage("You need to select correct date and time.");
            } else {
                setMessage("正しい日付を選択してください。");
            }
        }
        
    }

    @FXML
    private void cancelButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/View/Calendar.fxml"));
        Parent root = loader.load();
        CalendarController cc = loader.getController();
        Timestamp limit;
        if(Session.getView() == 0){
            limit = Timestamp.valueOf(
                    Session.getStartDate().plusWeeks(1));
            cc.setTable(Timestamp.valueOf(
                    Session.getStartDate()), limit);
        } else {
            limit = Timestamp.valueOf(
                    Session.getStartDate().plusMonths(1));
            cc.setTable(Timestamp.valueOf(
                    Session.getStartDate()), limit);            
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.show();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void seeButtonOnAction(ActionEvent event) 
            throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/View/CustomerDetail.fxml"));
        Parent root = loader.load();
        CustomerDetailController cdc = loader.getController();
        int selectedId = Integer.parseInt(customer.getSelectionModel().getSelectedItem().split(" - ")[0]);
        cdc.setFields(selectedId);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initOwner(((Node)event.getSource()).getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Customer Detail");
        stage.setScene(scene);
        stage.showAndWait();
        setCustomer();
        AppointmentDAO adao = new AppointmentDAO();
        Appointment ap = adao.getAppointment(appointmentId);
        selectCustomer(ap.getCustomerId());
    }
    
    public void setAppointDetails(int appointmentId) throws SQLException {
        AppointmentDAO adao = new AppointmentDAO();
        Appointment ap = adao.getAppointment(appointmentId);
        this.appointmentId = ap.getAppointmentId();
        this.startDatePicker.setValue(
                ap.getStart().toLocalDateTime().toLocalDate());
        this.startHH.getSelectionModel().select(
                ap.getStart().toLocalDateTime().toLocalTime().getHour());
        this.startmm.setValue(
                ap.getStart().toLocalDateTime().toLocalTime().getMinute());
        this.endDatePicker.setValue(
                ap.getEnd().toLocalDateTime().toLocalDate());
        this.endHH.getSelectionModel().select(
                ap.getEnd().toLocalDateTime().toLocalTime().getHour());
        this.endmm.setValue(
                ap.getEnd().toLocalDateTime().toLocalTime().getMinute());
        this.title.setText(ap.getTitle());
        this.location.setText(ap.getLocation());
        this.type.setText(ap.getType());
        this.url.setText(ap.getUrl());
        this.contact.setText(ap.getContact());
        this.description.setText(ap.getDescription());
        setCustomer();
        selectCustomer(ap.getCustomerId());   
    }
    
    void setMessage(String message){
        this.message.setText(message);
    }
    
    void setCustomer() throws SQLException {
        ObservableList<CustomerAddress> ca = 
                CustomerAddressDAO.getAllCustomerAddress();
        ObservableList<String> cus = FXCollections.observableArrayList();
        //Rewrite code to this lambda expression 
        //because lambda expression makes a code simpler and  more readable.
        ca.stream().map(e -> e.getCustomerId() + " - " + e.getName()).forEach(cus::add);
        this.customer.setItems(cus);        
    }
    void selectCustomer(int customerId)throws SQLException{
        this.customer.getSelectionModel().select(
            CustomerAddressDAO.getCustomerAddress(customerId).getCustomerId()
                    + " - " +
            CustomerAddressDAO.getCustomerAddress(customerId).getName());        
    }
}
