package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class used for the CrudAppointments screen of the application.
 *
 * @author Rodney Agosto
 */
public class CrudAppointmentsController implements Initializable {

    Stage stage;
    String lookupCustomer_ID;
    String lookupUser_ID;
    String lookupContact_ID;
    String tempAddAppointment;
    String tempUpdateAppointment;
    String emptyFieldsError;
    String appMainMenu;
    String dateTimeError1;
    String dateTimeError2;
    String dateTimeError3;
    String dateTimeError4;
    ObservableList<String> overlappingAppointmentsData = FXCollections.observableArrayList();

    @FXML
    private Label Appointment_ID;
    @FXML
    private Label Title;
    @FXML
    private Label Description;
    @FXML
    private Label Location;
    @FXML
    private Label Contact_Name;
    @FXML
    private Label Type;
    @FXML
    private Label Start_Time;
    @FXML
    private Label End_Time;
    @FXML
    private Label Customer_Name;
    @FXML
    private Label User_Name;
    @FXML
    private Label cancelAppointmentButtonLabel;
    @FXML
    private Label addUpdateAppointmentButtonLabel;
    @FXML
    private TextField Appointment_ID_AutoGen1;
    @FXML
    private TextField Title1;
    @FXML
    private TextField Description1;
    @FXML
    private TextField Location1;
    @FXML
    private TextField Type1;
    @FXML
    private DatePicker Start_Date_DatePicker;
    @FXML
    private DatePicker End_Date_DatePicker;
    @FXML
    private ComboBox<String> Customer_Name_ComboBox;
    @FXML
    private ComboBox<String> User_Name_ComboBox;
    @FXML
    private ComboBox<String> Contact_Name_ComboBox;
    @FXML
    private ComboBox<String> Start_Hour_ComboBox;
    @FXML
    private ComboBox<String> Start_Minute_ComboBox;
    @FXML
    private ComboBox<String> End_Hour_ComboBox;
    @FXML
    private ComboBox<String> End_Minute_ComboBox;

    /**
     * Initializes the controller
     * @return Initializes the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ResourceBundle rb1 = ResourceBundle.getBundle("language/Nat", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {

            Appointment_ID.setText(rb1.getString("Appointment_ID"));
            Title.setText(rb1.getString("Title"));
            Description.setText(rb1.getString("Description"));
            Location.setText(rb1.getString("Location"));
            Contact_Name.setText(rb1.getString("Contact_Name"));
            Type.setText(rb1.getString("Type"));
            Start_Time.setText(rb1.getString("Start_Time"));
            End_Time.setText(rb1.getString("End_Time"));
            Customer_Name.setText(rb1.getString("Customer_Name"));
            User_Name.setText(rb1.getString("User_Name"));
            cancelAppointmentButtonLabel.setText(rb1.getString("cancelAppointmentButtonLabel"));
            tempAddAppointment = rb1.getString("tempAddAppointment");
            tempUpdateAppointment = rb1.getString("tempUpdateAppointment");
            emptyFieldsError = rb1.getString("emptyFieldsError");
            appMainMenu = rb1.getString("mainMenu");
            dateTimeError1 = rb1.getString("dateTimeError1");
            dateTimeError2 = rb1.getString("dateTimeError2");
            dateTimeError3 = rb1.getString("dateTimeError3");
            dateTimeError4 = rb1.getString("dateTimeError4");

        }

        Start_Hour_ComboBox.getItems().addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");

        Start_Minute_ComboBox.getItems().addAll("00", "15", "30", "45");

        End_Hour_ComboBox.getItems().addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");

        End_Minute_ComboBox.getItems().addAll("00", "15", "30", "45");

        Start_Hour_ComboBox.getSelectionModel().select(8);
        Start_Minute_ComboBox.getSelectionModel().select(0);
        End_Hour_ComboBox.getSelectionModel().select(8);
        End_Minute_ComboBox.getSelectionModel().select(1);
        Start_Date_DatePicker.setValue(LocalDate.now());
        End_Date_DatePicker.setValue(LocalDate.now());

        try {

            initializeContactCustomerUser();

        } catch (SQLException ex) {

            Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    /**
     * Add/Update appointment button handler used to add/update appointment data.
     * @return Add/Update appointment button handler used to add/update appointment data.
     */
    public void addUpdateAppointmentButtonHandler(ActionEvent event) throws SQLException, IOException {

        //Check null fields
        if ((Title1.getText() == null || Title1.getText().trim().isEmpty())
                || (Description1.getText() == null || Description1.getText().trim().isEmpty())
                || (Location1.getText() == null || Location1.getText().trim().isEmpty())
                || (Type1.getText() == null || Type1.getText().trim().isEmpty())
                || Start_Date_DatePicker.getValue() == null
                || End_Date_DatePicker.getValue() == null
                || Customer_Name_ComboBox.getValue() == null
                || User_Name_ComboBox.getValue() == null
                || Contact_Name_ComboBox.getValue() == null
                || Start_Hour_ComboBox.getValue() == null
                || Start_Minute_ComboBox.getValue() == null
                || End_Hour_ComboBox.getValue() == null
                || End_Minute_ComboBox.getValue() == null
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(emptyFieldsError);
            alert.showAndWait();

        } else {

            //Set DatePickers and Time ComboBoxes to LocalDate and String
            LocalDate dateStart = Start_Date_DatePicker.getValue();
            String hourStart = Start_Hour_ComboBox.getValue();
            String minuteStart = Start_Minute_ComboBox.getValue();
            LocalDate dateEnd = End_Date_DatePicker.getValue();
            String hourEnd = End_Hour_ComboBox.getValue();
            String minuteEnd = End_Minute_ComboBox.getValue();

            //DateTimeFormatter
            DateTimeFormatter customFormatterFullDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter customFormatterDay = DateTimeFormatter.ofPattern("dd");
            DateTimeFormatter customFormatterHour = DateTimeFormatter.ofPattern("HH");
            DateTimeFormatter customFormatterMinute = DateTimeFormatter.ofPattern("mm");

            //Start DateTime
            // obtain the LocalDateTime
            LocalDateTime ldtStart = LocalDateTime.of(dateStart.getYear(), dateStart.getMonthValue(), dateStart.getDayOfMonth(), Integer.parseInt(hourStart), Integer.parseInt(minuteStart));
            // obtain the ZonedDateTime version of LocalDateTime
            ZonedDateTime locZdtStart = ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
            // obtain the UTC ZonedDateTime of the ZonedDateTime version of LocalDateTime
            ZonedDateTime utcZdtStart = locZdtStart.withZoneSameInstant(ZoneOffset.UTC);
            // obtain the UTC ZonedDateTime of the ZonedDateTime version of LocalDateTime
            ZonedDateTime estZdtStart = locZdtStart.withZoneSameInstant(ZoneOffset.of("-05:00"));

            //End DateTime
            // obtain the LocalDateTime
            LocalDateTime ldtEnd = LocalDateTime.of(dateEnd.getYear(), dateEnd.getMonthValue(), dateEnd.getDayOfMonth(), Integer.parseInt(hourEnd), Integer.parseInt(minuteEnd));
            // obtain the ZonedDateTime version of LocalDateTime
            ZonedDateTime locZdtEnd = ZonedDateTime.of(ldtEnd, ZoneId.systemDefault());
            // obtain the UTC ZonedDateTime of the ZonedDateTime version of LocalDateTime
            ZonedDateTime utcZdtEnd = locZdtEnd.withZoneSameInstant(ZoneOffset.UTC);
            // obtain the UTC ZonedDateTime of the ZonedDateTime version of LocalDateTime
            ZonedDateTime estZdtEnd = locZdtEnd.withZoneSameInstant(ZoneOffset.of("-05:00"));

            //Day Number
            String estStartDayString = customFormatterDay.format(estZdtStart);
            int estStartDayInt = Integer.parseInt(estStartDayString);
            String estEndDayString = customFormatterDay.format(estZdtEnd);
            int estEndDayInt = Integer.parseInt(estEndDayString);

            //Hour
            String estStartHourString = customFormatterHour.format(estZdtStart);
            int estStartHourInt = Integer.parseInt(estStartHourString);
            String estEndHourString = customFormatterHour.format(estZdtEnd);
            int estEndHourInt = Integer.parseInt(estEndHourString);

            //Minute
            String estStartMinuteString = customFormatterMinute.format(estZdtStart);
            int estStartMinuteInt = Integer.parseInt(estStartMinuteString);
            String estEndMinuteString = customFormatterMinute.format(estZdtEnd);
            int estEndMinuteInt = Integer.parseInt(estEndMinuteString);


            //Lookup Overlapping Appointments
            //Create appointment record
            if (Appointment_ID_AutoGen1.getText().equals("Appointment_ID_AutoGen")) {
                Connection conn = DBConnection.startConnection();


                ResultSet rs = conn.createStatement().executeQuery("SELECT Appointment_ID, Start, End FROM appointments WHERE "

                        + "\"" + customFormatterFullDateTime.format(utcZdtStart) + "\" < End "
                        + "AND "
                        + "\"" + customFormatterFullDateTime.format(utcZdtEnd) + "\" > Start "

                );

                try {

                    overlappingAppointmentsData.clear();

                    while (rs.next()) {

                        //Iterate Row
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                            //Iterate Column
                            overlappingAppointmentsData.add(rs.getString(i));
                        }

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);
                }

                DBConnection.closeConnection();

            }

            //Lookup Overlapping Appointments
            //Update appointment record
            if (!Appointment_ID_AutoGen1.getText().equals("Appointment_ID_AutoGen")) {
                Connection conn = DBConnection.startConnection();


                ResultSet rs = conn.createStatement().executeQuery("SELECT Appointment_ID, Start, End FROM appointments WHERE "

                        + "\"" + customFormatterFullDateTime.format(utcZdtStart) + "\" < End "
                        + "AND "
                        + "\"" + customFormatterFullDateTime.format(utcZdtEnd) + "\" > Start "
                        + "AND "
                        + "Appointment_ID != \"" + Appointment_ID_AutoGen1.getText() + "\""

                );

                try {

                    overlappingAppointmentsData.clear();

                    while (rs.next()) {

                        //Iterate Row
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {

                            //Iterate Column
                            overlappingAppointmentsData.add(rs.getString(i));
                        }

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);
                }

                DBConnection.closeConnection();

            }


            //Check input constraints, if pass, create or add record
            if (
                    //Checks business hours
                    estStartHourInt >= 8
                    //Checks business hours
                    && estEndHourInt <= 22
                    //Checks business hours
                    && ((estEndHourInt == 22 && estEndMinuteInt == 0) || estEndHourInt != 22)
                    //Checks StartDate is before EndDate
                    && estZdtStart.isBefore(estZdtEnd)
                    //Checks Day number in EST is equal to avoid multi-day appointments
                    && estStartDayInt == estEndDayInt
                    //Checks overlapping appointments
                    && overlappingAppointmentsData.isEmpty()
            ) {
                //Lookup Customer, User, and Contact IDs
                Connection conn0 = DBConnection.startConnection();

                ResultSet rs0 = conn0.createStatement().executeQuery("SELECT customers.Customer_ID, users.User_ID, contacts.Contact_ID "
                        + "FROM customers, users, contacts "
                        + "WHERE customers.Customer_Name = \"" + Customer_Name_ComboBox.getSelectionModel().getSelectedItem() + "\" "
                        + "AND users.User_Name = \"" + User_Name_ComboBox.getSelectionModel().getSelectedItem() + "\" "
                        + "AND contacts.Contact_Name = \"" + Contact_Name_ComboBox.getSelectionModel().getSelectedItem() + "\" "
                );

                try {

                    while (rs0.next()) {

                        lookupCustomer_ID = rs0.getString("Customer_ID");
                        lookupUser_ID = rs0.getString("User_ID");
                        lookupContact_ID = rs0.getString("Contact_ID");

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);
                }

                DBConnection.closeConnection();


                //Create appointment record
                if (Appointment_ID_AutoGen1.getText().equals("Appointment_ID_AutoGen")) {

                    Connection conn1 = DBConnection.startConnection();

                    Statement stmt1 = conn1.createStatement();

                    stmt1.executeUpdate("INSERT INTO "
                            + "appointments "
                            + "(Title,Description,Location,Type,Start,End,Created_By,Last_Updated_By,Customer_ID,User_ID,Contact_ID) "
                            + "VALUES "
                            + "(\"" + Title1.getText() + "\","
                            + "\"" + Description1.getText() + "\","
                            + "\"" + Location1.getText() + "\","
                            + "\"" + Type1.getText() + "\","
                            + "\"" + customFormatterFullDateTime.format(utcZdtStart) + "\","
                            + "\"" + customFormatterFullDateTime.format(utcZdtEnd) + "\","
                            + "'script',"
                            + "'script',"
                            + "\"" + lookupCustomer_ID + "\","
                            + "\"" + lookupUser_ID + "\","
                            + "\"" + lookupContact_ID + "\") "
                    );

                    DBConnection.closeConnection();

                }

                //Update appointment record
                if (!Appointment_ID_AutoGen1.getText().equals("Appointment_ID_AutoGen")) {

                    Connection conn1 = DBConnection.startConnection();

                    Statement stmt1 = conn1.createStatement();

                    stmt1.executeUpdate("UPDATE appointments "
                            + "SET "
                            + "Title = \"" + Title1.getText() + "\", "
                            + "Description = \"" + Description1.getText() + "\", "
                            + "Location = \"" + Location1.getText() + "\", "
                            + "Type = \"" + Type1.getText() + "\", "
                            + "Start = \"" + customFormatterFullDateTime.format(utcZdtStart) + "\", "
                            + "End = \"" + customFormatterFullDateTime.format(utcZdtEnd) + "\", "
                            + "Created_By = 'script', "
                            + "Last_Updated_By = 'script', "
                            + "Customer_ID = \"" + lookupCustomer_ID + "\", "
                            + "User_ID = \"" + lookupUser_ID + "\", "
                            + "Contact_ID = \"" + lookupContact_ID + "\" "
                            + "WHERE "
                            + "Appointment_ID = \"" + Appointment_ID_AutoGen1.getText() + "\" "
                    );

                    DBConnection.closeConnection();

                }

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/mainScreen.fxml"));
                loader.load();
                MainScreenController selectAppointmentTabPane = loader.getController();
                selectAppointmentTabPane.selectAppointmentsTabPane();
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.setTitle(appMainMenu);
                stage.centerOnScreen();
                stage.show();

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(
                        dateTimeError1
                        + "\n\n"
                        + dateTimeError2
                        + "\n\n"
                        + dateTimeError3
                        + "\n\n"
                        + dateTimeError4
                );
                alert.showAndWait();

            }

        }

    }

    /**
     * Cancel appointment button handler used to cancel appointment data.
     * @return Cancel appointment button handler used to cancel appointment data.
     */
    public void cancelAppointmentButtonHandler(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/mainScreen.fxml"));
        loader.load();
        MainScreenController selectAppointmentTabPane = loader.getController();
        selectAppointmentTabPane.selectAppointmentsTabPane();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle(appMainMenu);
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * Add appointment used to change label in the form.
     * @return Add appointment used to change label in the form.
     */
    public void addAppointment() {

        addUpdateAppointmentButtonLabel.setText(tempAddAppointment);

    }

    /**
     * Send appointment used to receive customer data.
     * @return Send appointment used to receive customer data.
     */
    public void sendAppointment(String appointmentID,
                             String appointmentTitle,
                             String appointmentDescription,
                             String appointmentLocation,
                             String appointmentContactName,
                             String appointmentType,
                             String appointmentStart,
                             String appointmentEnd,
                             String appointmentCustomerName,
                             String appointmentUserName
    ) {

        addUpdateAppointmentButtonLabel.setText(tempUpdateAppointment);

        Appointment_ID_AutoGen1.setText(String.valueOf(appointmentID));
        Title1.setText(String.valueOf(appointmentTitle));
        Description1.setText(String.valueOf(appointmentDescription));
        Location1.setText(String.valueOf(appointmentLocation));
        Type1.setText(String.valueOf(appointmentType));
        Contact_Name_ComboBox.getSelectionModel().select(appointmentContactName);
        Customer_Name_ComboBox.getSelectionModel().select(appointmentCustomerName);
        User_Name_ComboBox.getSelectionModel().select(appointmentUserName);

        String startDateString = appointmentStart.substring(0, 10);
        DateTimeFormatter startFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDateString, startFormatter);
        Start_Date_DatePicker.setValue(startLocalDate);

        String endDateString = appointmentEnd.substring(0, 10);
        DateTimeFormatter endFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate endLocalDate = LocalDate.parse(endDateString, endFormatter);
        End_Date_DatePicker.setValue(endLocalDate);

        Start_Hour_ComboBox.getSelectionModel().select(appointmentStart.substring(11, 13));
        Start_Minute_ComboBox.getSelectionModel().select(appointmentStart.substring(14, 16));

        End_Hour_ComboBox.getSelectionModel().select(appointmentEnd.substring(11, 13));
        End_Minute_ComboBox.getSelectionModel().select(appointmentEnd.substring(14, 16));

    }

    /**
     * Initialize Contact/Customer/User data from the database.
     * @return Initialize Contact/Customer/User data from the database.
     */
    @FXML
    private void initializeContactCustomerUser() throws SQLException {

        //////////////////////////////////////////////////
        Connection conn = DBConnection.startConnection();

        ResultSet rs = conn.createStatement().executeQuery("SELECT Contact_Name FROM contacts ORDER BY Contact_Name"
        );

        try {

            while (rs.next()) {

                Contact_Name_ComboBox.getItems().add(rs.getString("Contact_Name"));

            }
        } catch (SQLException ex) {

            Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);

        }

        DBConnection.closeConnection();

        Contact_Name_ComboBox.getSelectionModel().select(0);

        //////////////////////////////////////////////////
        Connection conn1 = DBConnection.startConnection();

        ResultSet rs1 = conn1.createStatement().executeQuery("SELECT Customer_Name FROM customers ORDER BY Customer_Name"
        );

        try {

            while (rs1.next()) {

                Customer_Name_ComboBox.getItems().add(rs1.getString("Customer_Name"));

            }
        } catch (SQLException ex) {

            Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);

        }

        DBConnection.closeConnection();

        Customer_Name_ComboBox.getSelectionModel().select(0);

        //////////////////////////////////////////////////
        Connection conn2 = DBConnection.startConnection();

        ResultSet rs2 = conn2.createStatement().executeQuery("SELECT User_Name FROM users ORDER BY User_Name"
        );

        try {

            while (rs2.next()) {

                User_Name_ComboBox.getItems().add(rs2.getString("User_Name"));

            }
        } catch (SQLException ex) {

            Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);

        }

        DBConnection.closeConnection();

        User_Name_ComboBox.getSelectionModel().select(0);

    }

}
