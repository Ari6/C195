/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import c195.Session;
import c195.User;
import c195.UserDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A Suzuki
 */
public class MainController implements Initializable {
    @FXML
    private TextField userName;
    @FXML
    private Button loginButton;
    @FXML
    private PasswordField password;
    @FXML
    private Label messageLabel;
    @FXML private Label loginLabel;
    @FXML private Label userNameLabel;
    @FXML private Label passwordLabel;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if(!Locale.getDefault().getLanguage().equals("en")){
            loginLabel.setText("ログイン");
            userNameLabel.setText("ユーザ");
            passwordLabel.setText("パスワード");
            loginButton.setText("ログイン");
        }
    }    

    @FXML
    private void loginButtonOnAction(ActionEvent event) throws SQLException, IOException {
        if(!userName.getText().equals("") && !password.getText().equals("")){
            UserDAO userDao = new UserDAO();
            User user = userDao.getUserWithUserName(userName.getText());
            if(user != null){
                if(user.getPassword().equals(password.getText())){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Calendar.fxml"));
                    Parent root = loader.load();
                    Session.setSession(user.getUserId(), user.getUserName());
                    CalendarController cal = loader.getController();
                    cal.setUserId(user.getUserId());
                    cal.checkSchedule();
                    Timestamp limit = Timestamp.valueOf(
                            Session.getStartDate().plusWeeks(1));
                    cal.setTable(Timestamp.valueOf(
                            Session.getStartDate()), limit);
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setTitle("Calendar");
                    stage.setScene(scene);
                    stage.show();

                    ((Stage)((Node)(event.getSource())).getScene().getWindow()).close();
                    Session.writeLog("Login");
                } else {
                    if(Locale.getDefault().getLanguage().equals("en")){
                        messageLabel.setText("The username or password is incorrect.");
                    } else {
                        messageLabel.setText("ユーザ・パスワードに誤りがあります。");
                    }
                    Session.writeLog("Login Error");
                }
            } else {
                if(Locale.getDefault().getLanguage().equals("en")){                
                    messageLabel.setText("The username or password is incorrect.");
                } else {
                    messageLabel.setText("ユーザ・パスワードに誤りがあります。");
                }
                Session.writeLog("Login Error");
            }
        } else {
            if(Locale.getDefault().getLanguage().equals("en")){
                messageLabel.setText("The username and password are required.");
            } else {
                messageLabel.setText("ユーザ・パスワードは必須項目です。");
            }
        }
    }
    
}
