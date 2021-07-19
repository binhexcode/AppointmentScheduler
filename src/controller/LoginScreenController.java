package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;

/**
 * FXML Controller class used for the login screen of the application.
 *
 * @author Rodney Agosto
 */
public class LoginScreenController implements Initializable {

    Stage stage;
    String User_Name_Login = "";
    String Password_Login = "";
    String Login_Error = "";
    String Main_Menu = "";

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField location;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label zoneIDLabel;
    @FXML
    private Label loginButtonLabel;

    /**
     * Initializes the controller
     * @return Initializes the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ResourceBundle rb1 = ResourceBundle.getBundle("language/Nat", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
            usernameLabel.setText(rb1.getString("username"));
            passwordLabel.setText(rb1.getString("password"));
            zoneIDLabel.setText(rb1.getString("zoneID"));
            loginButtonLabel.setText(rb1.getString("login"));
            Login_Error = rb1.getString("loginError");
            Main_Menu = rb1.getString("mainMenu");
        }

        ZoneId zone = ZoneId.systemDefault();
        String zoneID = zone.getId();
        location.setText(zoneID);
    }

    /**
     * Login button handler used to login to the application.
     * @return Login button handler used to login to the application.
     */
    @FXML
    private void loginButtonHandler(ActionEvent event) throws SQLException, IOException {

        String reportName = System.getProperty("user.dir") + "\\" + "src" + "\\" + "reports" + "\\" + "login_activity.txt";

        if (User_Name_Login.equals("") && Password_Login.equals("")) {
            Connection conn = DBConnection.startConnection(); // Connect to database
            String selectStatement = "SELECT * FROM users WHERE User_Name = 'test'"; // SQL statement

            DBQuery.setPreparedStatement(conn, selectStatement); // Create PreparedStatement
            PreparedStatement ps = DBQuery.getPreparedStatement();

            ps.execute(); //Execute PreparedStatement
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                String User_Name = rs.getString("User_Name");
                String Password = rs.getString("Password");

                User_Name_Login = User_Name;
                Password_Login = Password;
            }

            DBConnection.closeConnection();
        }

        if (username.getText().equals(User_Name_Login) && password.getText().equals(Password_Login)) {

            try {

                FileWriter txtReport = new FileWriter(reportName,true);
                txtReport.write("Successful login attempt from user(" + User_Name_Login + ") at "
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getMonthValue()
                        + "/"
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getDayOfMonth()
                        + "/"
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getYear()
                        + " "
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getHour()
                        + ":"
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getMinute()
                        + ":"
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getSecond()
                        + " UTC.\n");
                txtReport.close();

            }
            catch (IOException ex) {

                System.err.println("IOException: " + ex.getMessage());

            }


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/mainScreen.fxml"));
            loader.load();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(Main_Menu);
            stage.centerOnScreen();
            stage.show();

            MainScreenController promptUpcomingAppointments = loader.getController();
            promptUpcomingAppointments.checkUpcomingAppointmentsAtLogin();

        } else {

            Alert error = new Alert(Alert.AlertType.ERROR, Login_Error);
            error.show();

            try {

                FileWriter txtReport = new FileWriter(reportName,true);
                txtReport.write("Unsuccessful login attempt from user(" + User_Name_Login + ") at "
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getMonthValue()
                        + "/"
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getDayOfMonth()
                        + "/"
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getYear()
                        + " "
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getHour()
                        + ":"
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getMinute()
                        + ":"
                        + LocalDateTime.now().atOffset(ZoneOffset.UTC).getSecond()
                        + " UTC.\n");
                txtReport.close();

            }
            catch (IOException ex) {

                System.err.println("IOException: " + ex.getMessage());

            }

        }

    }

}