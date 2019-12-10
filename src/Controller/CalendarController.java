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
import c195.User;
import c195.UserDAO;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A Suzuki
 */
public class CalendarController implements Initializable {
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button customerButton;
    @FXML private Button logoutButton;
    @FXML private Label nameLabel;
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private TableView<Appointment> appointmentTable;
    @FXML private TableColumn<Appointment, String> start;
    @FXML private TableColumn<Appointment, String> end;
    @FXML private TableColumn<Appointment, String> title;
    @FXML private TableColumn<Appointment, String> location;
    @FXML private TableColumn<Appointment, String> type;
    @FXML private Label message;
    @FXML private Button todayButton;
    @FXML private Label baseDate;
    @FXML private ChoiceBox<String> report;
    @FXML private ChoiceBox<String> consultant;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        start.setCellValueFactory(new PropertyValueFactory<>("startDisplay"));
        end.setCellValueFactory(new PropertyValueFactory<>("endDisplay"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        choiceBox.setItems(FXCollections.observableArrayList("Week", "Month"));
        choiceBox.getSelectionModel().select(Session.getView());
        //Used lambda expression here because setOnSction for the choiceBox is
        //used only here and there is no reason to make a class or a method.
        choiceBox.setOnAction(e -> {
                    Session.setView(
                            choiceBox.getSelectionModel().getSelectedIndex());
                    Timestamp limit;
                    if(choiceBox.getSelectionModel().isSelected(0)){ // 0 = Week
                        limit = Timestamp.valueOf(
                                Session.getStartDate().
                                        plusWeeks(1));
                    } else {
                        limit = Timestamp.valueOf(
                                Session.getStartDate().
                                        plusMonths(1));
                    }
                    setTable(Timestamp.valueOf(
                            Session.getStartDate()), limit);});
        report.setItems(FXCollections.observableArrayList(
                "Number of Appointment type of a month",
                "The schedule for each consultant",
                "All customer data"
                ));
        UserDAO udao = new UserDAO();
        List<User> userList = null;
        try {
            userList = udao.getAllUser();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ObservableList<String> ol = FXCollections.observableArrayList();
        userList.stream().map(
                e -> e.getUserId()+ " - " + e.getUserName()).forEach(ol::add);
        consultant.setItems(ol);
    }    
    public void setUserId(int userId){
        this.nameLabel.setText("Hello " + Session.getLoginName() + "!");
    }
    
    @FXML
    private void addButtonOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/View/CalendarDetail.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Calendar Detail");
        stage.setScene(scene);
        stage.show();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void editButtonOnAction(ActionEvent event) 
            throws IOException, SQLException {
        if(!appointmentTable.getSelectionModel().isEmpty()){
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/View/CalendarDetail.fxml"));
            Parent root = loader.load();
            CalendarDetailController cdc = loader.getController();

            int appointmentId = appointmentTable.getSelectionModel().
                    getSelectedItem().getAppointmentId();
            cdc.setAppointDetails(appointmentId);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Calendar Detail");
            stage.setScene(scene);
            stage.show();
            ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
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
            throws SQLException, IOException {
        if(appointmentTable.getSelectionModel().getSelectedItem() != null){
            int selectedId = appointmentTable.getSelectionModel().
                    getSelectedItem().getAppointmentId();
            int result = AppointmentDAO.deleteAppointment(selectedId);
            if(result == 1){
                if(Locale.getDefault().getLanguage().equals("en")){
                    setMessage("The record you selected has been deleted.");
                } else {
                    setMessage("レコードは削除されました。");
                }
                Session.writeLog("Deleted an appointment appointmentId = " + selectedId);
                Timestamp limit;
                if(Session.getView() == 0){
                    limit = Timestamp.valueOf(
                            Session.getStartDate().plusWeeks(1));
                } else {
                    limit = Timestamp.valueOf(
                            Session.getStartDate().plusMonths(1));
                }
                setTable(Timestamp.valueOf(
                        Session.getStartDate()), limit);
            } else {
                if(Locale.getDefault().getLanguage().equals("en")){
                    setMessage("The record you selected does not exist.");
                } else {
                    setMessage("選択されたレコードは存在しません。");
                }
            }
        }
        
    }

    @FXML
    private void customerButtonOnAction(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(
                getClass().getResource("/View/Customer.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Customer");
        stage.setScene(scene);
        stage.show();
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void logoutButtonOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource("/View/Main.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        Session.setSession(0, null);
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
        Session.writeLog("Logout");
    }

    @FXML
    private void previousButtonOnAction(ActionEvent event) {
        Timestamp limit;
        if(Session.getView() == 0) {
            Session.setStartDate(Session.getStartDate().minusWeeks(1));
            limit = Timestamp.valueOf(
                    Session.getStartDate().plusWeeks(1));
        } else {
            Session.setStartDate(Session.getStartDate().minusMonths(1));
            limit = Timestamp.valueOf(
                    Session.getStartDate().plusMonths(1));            
        }
        setTable(Timestamp.valueOf(Session.getStartDate()), limit);
        
    }

    @FXML
    private void nextButtonOnAction(ActionEvent event) {
        Timestamp limit;
        if(Session.getView() == 0) {
            Session.setStartDate(Session.getStartDate().plusWeeks(1));
            limit = Timestamp.valueOf(
                    Session.getStartDate().plusWeeks(1));
        } else {
            Session.setStartDate(Session.getStartDate().plusMonths(1));
            limit = Timestamp.valueOf(
                    Session.getStartDate().plusMonths(1));            
        }
        setTable(Timestamp.valueOf(Session.getStartDate()), limit);
    }
    
    void setMessage(String message) {
        this.message.setText(message);
    }
    
    void setTable(Timestamp start, Timestamp limit) {
        AppointmentDAO ap = new AppointmentDAO();
        ObservableList<Appointment> apList;
        try {
            apList = ap.getAppointment(Session.getLoginId(), start, limit);
            appointmentTable.setItems(apList);
        } catch (SQLException ex) {
            Logger.getLogger(
                    CalendarController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }        
    }

    @FXML
    private void todayButtonOnAction(ActionEvent event) {
        Session.setStartDate();
        Timestamp limit;
        if (Session.getView() == 0){
            limit = Timestamp.valueOf(
                    Session.getStartDate().plusWeeks(1));
        } else {
            limit = Timestamp.valueOf(
                    Session.getStartDate().plusMonths(1));

        }
        setTable(Timestamp.valueOf(Session.getStartDate()), limit);
    }
   public void checkSchedule() throws SQLException {
        ZonedDateTime zdt = ZonedDateTime.now();
        Timestamp ts = Timestamp.valueOf(
                zdt.withZoneSameInstant(
                        TimeZone.getTimeZone("UTC").toZoneId())
                        .toLocalDateTime());
        AppointmentDAO adao = new AppointmentDAO();
        ObservableList<Appointment> ol =
        adao.getAppointment(
                Session.getLoginId(), ts,
                Timestamp.valueOf(ts.toLocalDateTime().plusMinutes(15)));
        if(!ol.isEmpty()){
            if(Locale.getDefault().getLanguage().equals("en")){
                setMessage("You have an appointment within 15 minutes.");
            } else {
                setMessage("１５分以内にアポイントメントがあります。");
            }
        }
   }
   @FXML private void runOnAction(ActionEvent event) 
           throws SQLException, IOException {
        if(report.getSelectionModel().getSelectedIndex() == 0){
            //0 Number of appointment type of a month
            AppointmentDAO adao = new AppointmentDAO();
            ResultSet rs = adao.reportNumberOfAppointmentTypeOfMonth();
            try(FileWriter fw = new FileWriter("TypeOfMonth.txt")){
                while(rs.next()){
                    fw.write(rs.getString(1)+","+ rs.getString(2)+","+ rs.getInt(3) + "\n");
                }
            }
            if(Locale.getDefault().getLanguage().equals("en")){
                setMessage("The report is created as TypeOfMonth.txt.");
            } else {
                setMessage("TypeOfMonth.txt が作成されました。");
            }
        } else {
           if(report.getSelectionModel().getSelectedIndex() == 1){
                //1 The schedule for each consultant
                if(consultant.getSelectionModel().isEmpty()){
                    if(Locale.getDefault().getLanguage().equals("en")){
                        setMessage("You need to select consultant.");
                    } else {
                        setMessage("コンサルタントを選択してください。");
                    }
                } else {
                    int selectedId = Integer.parseInt(
                            consultant.getSelectionModel().getSelectedItem()
                            .split(" - ")[0]);
                    AppointmentDAO adao = new AppointmentDAO();
                    ObservableList<Appointment> ol = 
                            FXCollections.observableArrayList();
                    ol = adao.getAppointment(selectedId, 
                            Timestamp.valueOf("0000-01-01 00:00:00"), 
                            Timestamp.valueOf("9999-12-31 23:59:59"));
                    try(FileWriter fw = 
                            new FileWriter("ScheduleForEachConsul.txt")){
                        ol.stream().map(e->
                            e.getUserId() + "," + 
                            e.getAppointmentId() + "," +
                            e.changeUTCToLocal(e.getStart()) + "," +
                            e.changeUTCToLocal(e.getEnd()) + "," +
                            e.getTitle() + "," +
                            e.getLocation() + "," +
                            e.getType() + "," +
                            e.getContact() + "," +
                            e.getDescription()+ "," +
                            e.getCustomerId()+ "\n").forEach((e) -> {
                            try {
                                fw.write(e);
                            } catch (IOException ex) {
                                Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                    if(Locale.getDefault().getLanguage().equals("en")){
                        setMessage("The report was created as ScheduleForEachConsul.txt");
                    } else {
                        setMessage("ScheduleForEachConsul.txtが作成されました。");
                    }
                }
           } else {
               //2 All customer
               ObservableList<CustomerAddress> cusList = FXCollections.observableArrayList();
               cusList = CustomerAddressDAO.getAllCustomerAddress();
               try(FileWriter fw = new FileWriter("AllCustomer.txt")){
                   cusList.stream().map(e->
                           e.getCustomerId() + "," +
                           e.getName()+ "," +
                           e.getAddress()+ "," +
                           e.getAddress2()+ "," +
                           e.getCity() + "," +
                           e.getCountry()+ "\n"
                                   
                                   ).forEach(e->{
                       try {
                           fw.write(e);
                       } catch (IOException ex) {
                           Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
                       }
                   });
               }
               if(Locale.getDefault().getLanguage().equals("en")){
                   setMessage("The report AllCustomer.txt has been created.");
               } else {
                   setMessage("AllCustomer.txtが作成されました。");
               }
           }
       }
   }
}
