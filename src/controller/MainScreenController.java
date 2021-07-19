package controller;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Callback;
import lambdaexpression.LambdaOneString;
import lambdaexpression.LambdaTwoStrings;
import utils.DBConnection;

/**
 * FXML Controller class used for the main screen of the application.
 *
 * @author Rodney Agosto
 */
public class MainScreenController implements Initializable {

    Stage stage;
    String tempAppID;
    String tempAppMinutes;
    String tempAppStart;
    String tempAppEnd;
    String upcomingAppointmentAlertTitle;
    String upcomingAppointmentMessage1;
    String upcomingAppointmentMessage2;
    String upcomingAppointmentMessage3;
    String upcomingAppointmentID;
    String upcomingAppointmentStart;
    String upcomingAppointmentEnd;
    String errorDeletingCustomer;
    String noRecordSelected;
    String deleteSelectedRecord;
    String reportGeneratedLocation;
    String openReportQuestion;

    private ObservableList<ObservableList> customerData = FXCollections.observableArrayList();
    private ObservableList<ObservableList> appointmentData = FXCollections.observableArrayList();
    private ObservableList<ObservableList> appointmentMonthData = FXCollections.observableArrayList();
    private ObservableList<ObservableList> appointmentWeekData = FXCollections.observableArrayList();
    private ObservableList<ObservableList> appointmentUpAppData = FXCollections.observableArrayList();

    @FXML
    private TableView customerTableview;
    @FXML
    private TableView appointmentTableview;
    @FXML
    private TableView appointmentMonthTableview;
    @FXML
    private TableView appointmentWeekTableview;
    @FXML
    private TableView appointmentUpAppTableview;
    @FXML
    private Label exitButtonLabel;
    @FXML
    private Label addCustomerButtonLabel;
    @FXML
    private Label updateCustomerButtonLabel;
    @FXML
    private Label deleteCustomerButtonLabel;
    @FXML
    private Label addAppointmentButtonLabel;
    @FXML
    private Label updateAppointmentButtonLabel;
    @FXML
    private Label deleteAppointmentButtonLabel;
    @FXML
    private Label userLoginReportLabel;
    @FXML
    private Label customersByTypeMonthReportLabel;
    @FXML
    private Label customReportLabel;
    @FXML
    private Label contactAppointmentsReportLabel;
    @FXML
    private Label generateReportLabel1;
    @FXML
    private Label generateReportLabel2;
    @FXML
    private Label generateReportLabel3;
    @FXML
    private Label generateReportLabel4;
    @FXML
    private Label reportLocationLabel;
    @FXML
    private TabPane mainScreenTabPane;
    @FXML
    private Tab CustomersTabPane;
    @FXML
    private Tab AppointmentsTabPane;
    @FXML
    private Tab monthlyViewTabPane;
    @FXML
    private Tab weeklyViewTabPane;
    @FXML
    private Tab upcomingAppTabPane;
    @FXML
    private Tab reportsTabPane;

    /**
     * Initializes the controller
     * @return Initializes the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ResourceBundle rb1 = ResourceBundle.getBundle("language/Nat", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
            exitButtonLabel.setText(rb1.getString("exit"));
            addCustomerButtonLabel.setText(rb1.getString("addCustomerButtonLabel"));
            updateCustomerButtonLabel.setText(rb1.getString("updateCustomerButtonLabel"));
            deleteCustomerButtonLabel.setText(rb1.getString("deleteCustomerButtonLabel"));
            addAppointmentButtonLabel.setText(rb1.getString("addAppointmentButtonLabel"));
            updateAppointmentButtonLabel.setText(rb1.getString("updateAppointmentButtonLabel"));
            deleteAppointmentButtonLabel.setText(rb1.getString("deleteAppointmentButtonLabel"));
            userLoginReportLabel.setText(rb1.getString("userLoginReportLabel"));
            customersByTypeMonthReportLabel.setText(rb1.getString("customersByTypeMonthReportLabel"));
            customReportLabel.setText(rb1.getString("customReportLabel"));
            contactAppointmentsReportLabel.setText(rb1.getString("contactAppointmentsReportLabel"));
            generateReportLabel1.setText(rb1.getString("generateReportLabel1"));
            generateReportLabel2.setText(rb1.getString("generateReportLabel1"));
            generateReportLabel3.setText(rb1.getString("generateReportLabel1"));
            generateReportLabel4.setText(rb1.getString("generateReportLabel1"));
            reportLocationLabel.setText(rb1.getString("reportLocationLabel"));
            CustomersTabPane.setText(rb1.getString("CustomersTabPane"));
            AppointmentsTabPane.setText(rb1.getString("AppointmentsTabPane"));
            monthlyViewTabPane.setText(rb1.getString("monthlyViewTabPane"));
            weeklyViewTabPane.setText(rb1.getString("weeklyViewTabPane"));
            upcomingAppTabPane.setText(rb1.getString("upcomingAppTabPane"));
            reportsTabPane.setText(rb1.getString("reportsTabPane"));
            upcomingAppointmentAlertTitle = rb1.getString("upcomingAppointmentAlertTitle");
            upcomingAppointmentMessage1 = rb1.getString("upcomingAppointmentMessage1");
            upcomingAppointmentMessage2 = rb1.getString("upcomingAppointmentMessage2");
            upcomingAppointmentMessage3 = rb1.getString("upcomingAppointmentMessage3");
            upcomingAppointmentID = rb1.getString("upcomingAppointmentID");
            upcomingAppointmentStart = rb1.getString("upcomingAppointmentStart");
            upcomingAppointmentEnd = rb1.getString("upcomingAppointmentEnd");
            errorDeletingCustomer = rb1.getString("errorDeletingCustomer");
            noRecordSelected = rb1.getString("noRecordSelected");
            deleteSelectedRecord = rb1.getString("deleteSelectedRecord");
            reportGeneratedLocation = rb1.getString("reportGeneratedLocation");
            openReportQuestion = rb1.getString("openReportQuestion");

        }

        buildCustomerData();
        buildAppointmentData();
        buildAppointmentMonthData();
        buildAppointmentWeekData();
        checkUpcomingAppointments();

    }

    /**
     * Builds customer data dynamically.<br>
     * https://stackoverflow.com/questions/32808905/javafx-dynamic-tableview-is-not-showing-data-from-the-database<br>
     * https://www.codegrepper.com/code-examples/java/javafx+fill+tableview+with+data<br>
     * @return Builds customer data dynamically.
     */
    public void buildCustomerData() {

        Connection c;

        try {

            c = DBConnection.startConnection();

            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT "
                    + "customers.Customer_ID, "
                    + "customers.Customer_Name, "
                    + "customers.Address, "
                    + "customers.Postal_Code, "
                    + "customers.Phone, "
                    + "first_level_divisions.Division, "
                    + "countries.Country "
                    + "FROM customers "
                    + "JOIN first_level_divisions "
                    + "ON customers.Division_ID = first_level_divisions.Division_ID "
                    + "JOIN countries "
                    + "ON first_level_divisions.COUNTRY_ID = countries.Country_ID "
                    + "ORDER BY customers.Customer_ID "
                    ;

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                customerTableview.getColumns().addAll(col);
                //System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added " + row);
                customerData.add(row);

            }

            //Populate data on TableView
            customerTableview.setItems(customerData);

            DBConnection.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

    }

    /**
     * Add customer button handler used to forward users to the crudCustomersScreen.
     * @return Add customer button handler used to forward users to the crudCustomersScreen.
     */
    public void addCustomerButtonHandler(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/crudCustomersScreen.fxml"));
        loader.load();

        CrudCustomersController addCustomerAction = loader.getController();
        addCustomerAction.addCustomer();

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle(addCustomerButtonLabel.getText());
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * Update customer button handler used to forward users to the crudCustomersScreen.
     * @return Update customer button handler used to forward users to the crudCustomersScreen.
     */
    public void updateCustomerButtonHandler(ActionEvent event) throws IOException, SQLException {

        if (customerTableview.getSelectionModel().getSelectedItem() == null) {

            Alert error = new Alert(Alert.AlertType.ERROR, noRecordSelected);
            error.showAndWait();

        } else {

            TablePosition getSelectedRow = (TablePosition) customerTableview.getSelectionModel().getSelectedCells().get(0);
            int selectedRowInt = getSelectedRow.getRow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/crudCustomersScreen.fxml"));
            loader.load();

            CrudCustomersController updateCustomerAction = loader.getController();
            updateCustomerAction.sendCustomer(
                    getSelectedRow.getTableView().getVisibleLeafColumn(0).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(1).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(2).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(3).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(4).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(5).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(6).getCellData(selectedRowInt).toString()
            );

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(updateCustomerButtonLabel.getText());
            stage.centerOnScreen();
            stage.show();

        }

    }

    /**
     * Delete customer button handler used to delete customers from the database.
     * @return Delete customer button handler used to delete customers from the database.
     */
    public void deleteCustomerButtonHandler(ActionEvent actionEvent) throws SQLException {

        if (customerTableview.getSelectionModel().getSelectedItem() == null) {

            Alert error = new Alert(Alert.AlertType.ERROR, noRecordSelected);
            error.showAndWait();

        } else {

            TablePosition getSelectedRow = (TablePosition) customerTableview.getSelectionModel().getSelectedCells().get(0);
            int selectedRowInt = getSelectedRow.getRow();
            String selectedRowColumn0 = getSelectedRow.getTableView().getVisibleLeafColumn(0).getCellData(selectedRowInt).toString();

            //String noResultFound;
            ObservableList<ObservableList> noResultFound = FXCollections.observableArrayList();

            Connection conn0 = DBConnection.startConnection();

            String SQL = "SELECT "
                    + "IFNULL((SELECT "
                    + "appointments.Customer_ID "
                    + "FROM appointments "
                    + "WHERE appointments.Customer_ID = \"" + selectedRowColumn0 + "\" "
                    + "),'No Result Found') "
                    + "AS 'Customer_ID' "
                    ;

            //ResultSet
            ResultSet rs0 = conn0.createStatement().executeQuery(SQL);


            while (rs0.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs0.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs0.getString(i));
                }
                //System.out.println("Row [1] added " + row);
                noResultFound.add(row);

            }



            if (noResultFound.get(0).toString().equals("[No Result Found]")) {

                Connection conn1 = DBConnection.startConnection();
                Statement stmt1 = conn1.createStatement();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,deleteSelectedRecord);
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {

                    try {

                        stmt1.executeUpdate("DELETE FROM "
                                + "customers "
                                + "WHERE "
                                + "Customer_ID = \"" + selectedRowColumn0 + "\" "
                        );

                    } catch (SQLException ex) {

                        Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }

                DBConnection.closeConnection();

                customerData.clear();
                customerTableview.getItems().clear();
                customerTableview.getColumns().clear();
                buildCustomerData();

            } else {

                Alert error = new Alert(Alert.AlertType.ERROR,errorDeletingCustomer);
                error.show();

            }

        }

    }

    /**
     * Builds appointment data dynamically.<br>
     * https://stackoverflow.com/questions/32808905/javafx-dynamic-tableview-is-not-showing-data-from-the-database<br>
     * https://www.codegrepper.com/code-examples/java/javafx+fill+tableview+with+data<br>
     * @return Builds appointment data dynamically.
     */
    public void buildAppointmentData() {

        Connection c;

        try {

            c = DBConnection.startConnection();

            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT "
                    + "appointments.Appointment_ID, "
                    + "appointments.Title, "
                    + "appointments.Description, "
                    + "appointments.Location, "
                    + "appointments.Contact_ID, "
                    + "contacts.Contact_Name, "
                    + "appointments.Type, "
                    + "CONVERT_TZ (appointments.Start,'+00:00',\"" + ZonedDateTime.now().getOffset() + "\") AS 'Start', "
                    + "CONVERT_TZ (appointments.End,'+00:00',\"" + ZonedDateTime.now().getOffset() + "\") AS 'End', "
                    + "appointments.Customer_ID, "
                    + "customers.Customer_Name, "
                    + "appointments.User_ID, "
                    + "users.User_Name "
                    + "FROM appointments "
                    + "JOIN contacts "
                    + "ON appointments.Contact_ID = contacts.Contact_ID "
                    + "JOIN customers "
                    + "ON appointments.Customer_ID = customers.Customer_ID "
                    + "JOIN users "
                    + "ON appointments.User_ID = users.User_ID "
                    + "ORDER BY appointments.Appointment_ID "
                    ;

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                appointmentTableview.getColumns().addAll(col);
                //System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added " + row);
                appointmentData.add(row);

            }

            //Populate data on TableView
            appointmentTableview.setItems(appointmentData);

            DBConnection.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

    }


    /**
     * Builds appointment month data dynamically.<br>
     * https://stackoverflow.com/questions/32808905/javafx-dynamic-tableview-is-not-showing-data-from-the-database<br>
     * https://www.codegrepper.com/code-examples/java/javafx+fill+tableview+with+data<br>
     * @return Builds appointment month data dynamically.
     */
    public void buildAppointmentMonthData() {

        Connection c;

        try {

            c = DBConnection.startConnection();

            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT "
                    + "YEAR(Start) AS 'Year', "
                    + "DATE_FORMAT(Start, \"%M\") AS 'Month', "
                    + "appointments.Appointment_ID, "
                    + "appointments.Title, "
                    + "appointments.Description, "
                    + "appointments.Location, "
                    + "appointments.Contact_ID AS 'Co_ID', "
                    + "contacts.Contact_Name AS 'Co_Name', "
                    + "appointments.Type, "
                    + "CONVERT_TZ (appointments.Start,'+00:00',\"" + ZonedDateTime.now().getOffset() + "\") AS 'Start', "
                    + "CONVERT_TZ (appointments.End,'+00:00',\"" + ZonedDateTime.now().getOffset() + "\") AS 'End', "
                    + "appointments.Customer_ID AS 'Cu_ID', "
                    + "customers.Customer_Name AS 'Cu_Name' "
                    + "FROM appointments "
                    + "JOIN contacts "
                    + "ON appointments.Contact_ID = contacts.Contact_ID "
                    + "JOIN customers "
                    + "ON appointments.Customer_ID = customers.Customer_ID "
                    + "ORDER BY appointments.Appointment_ID "
                    ;

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                appointmentMonthTableview.getColumns().addAll(col);
                //System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added " + row);
                appointmentMonthData.add(row);

            }

            //Populate data on TableView
            appointmentMonthTableview.setItems(appointmentMonthData);

            DBConnection.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

    }


    /**
     * Builds appointment week data dynamically.<br>
     * https://stackoverflow.com/questions/32808905/javafx-dynamic-tableview-is-not-showing-data-from-the-database<br>
     * https://www.codegrepper.com/code-examples/java/javafx+fill+tableview+with+data<br>
     * @return Builds appointment week data dynamically.
     */
    public void buildAppointmentWeekData() {

        Connection c;

        try {

            c = DBConnection.startConnection();

            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT "
                    + "YEAR(Start) AS 'Year', "
                    + "WEEK(Start) AS 'Week#', "
                    + "appointments.Appointment_ID, "
                    + "appointments.Title, "
                    + "appointments.Description, "
                    + "appointments.Location, "
                    + "appointments.Contact_ID AS 'Co_ID', "
                    + "contacts.Contact_Name AS 'Co_Name', "
                    + "appointments.Type, "
                    + "CONVERT_TZ (appointments.Start,'+00:00',\"" + ZonedDateTime.now().getOffset() + "\") AS 'Start', "
                    + "CONVERT_TZ (appointments.End,'+00:00',\"" + ZonedDateTime.now().getOffset() + "\") AS 'End', "
                    + "appointments.Customer_ID AS 'Cu_ID', "
                    + "customers.Customer_Name AS 'Cu_Name' "
                    + "FROM appointments "
                    + "JOIN contacts "
                    + "ON appointments.Contact_ID = contacts.Contact_ID "
                    + "JOIN customers "
                    + "ON appointments.Customer_ID = customers.Customer_ID "
                    + "ORDER BY appointments.Appointment_ID "
                    ;

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                appointmentWeekTableview.getColumns().addAll(col);
                //System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added " + row);
                appointmentWeekData.add(row);

            }

            //Populate data on TableView
            appointmentWeekTableview.setItems(appointmentWeekData);

            DBConnection.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

    }

    /**
     * Check upcoming appointment from the database and is used for login alerts.
     * @return Check upcoming appointment from the database and is used for login alerts.
     */
    public void checkUpcomingAppointments() {

        DateTimeFormatter customFormatterFullDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //Start DateTime
        // obtain the LocalDateTime
        LocalDateTime ldtStart = LocalDateTime.now();
        // obtain the ZonedDateTime version of LocalDateTime
        ZonedDateTime locZdtStart = ZonedDateTime.of(ldtStart, ZoneId.systemDefault());
        // obtain the UTC ZonedDateTime of the ZonedDateTime version of LocalDateTime
        ZonedDateTime utcZdtStart = locZdtStart.withZoneSameInstant(ZoneOffset.UTC);


        Connection c;

        try {

            c = DBConnection.startConnection();

            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT "
                    + "IFNULL((SELECT "
                    + "Appointment_ID "
                    + "FROM appointments "
                    + "WHERE (TIME_TO_SEC(TIMEDIFF(Start,NOW()))/60) < 15 "
                    + "AND (TIME_TO_SEC(TIMEDIFF(Start,NOW()))/60) >= 0),'No Result Found') "
                    + "AS 'Appointment_ID', "

                    + "IFNULL((SELECT "
                    + "ROUND(TIME_TO_SEC(TIMEDIFF(Start,NOW()))/60) AS 'Minutes'  "
                    + "FROM appointments "
                    + "WHERE (TIME_TO_SEC(TIMEDIFF(Start,NOW()))/60) < 15 "
                    + "AND (TIME_TO_SEC(TIMEDIFF(Start,NOW()))/60) >= 0),'No Result Found') "
                    + "AS 'Minutes', "

                    + "IFNULL((SELECT "
                    + "Start "
                    + "FROM appointments "
                    + "WHERE (TIME_TO_SEC(TIMEDIFF(Start,NOW()))/60) < 15 "
                    + "AND (TIME_TO_SEC(TIMEDIFF(Start,NOW()))/60) >= 0),'No Result Found') "
                    + "AS 'Start', "

                    + "IFNULL((SELECT "
                    + "End "
                    + "FROM appointments "
                    + "WHERE (TIME_TO_SEC(TIMEDIFF(Start,NOW()))/60) < 15 "
                    + "AND (TIME_TO_SEC(TIMEDIFF(Start,NOW()))/60) >= 0),'No Result Found') "
                    + "AS 'End' "

                    ;

            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);

            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                appointmentUpAppTableview.getColumns().addAll(col);
                //System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                //System.out.println("Row [1] added " + row);
                appointmentUpAppData.add(row);

            }

            //Populate data on TableView
            appointmentUpAppTableview.setItems(appointmentUpAppData);

            DBConnection.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }

        tempAppID = appointmentUpAppTableview.getSelectionModel().getTableView().getVisibleLeafColumn(0).getCellData(0).toString();
        tempAppMinutes = appointmentUpAppTableview.getSelectionModel().getTableView().getVisibleLeafColumn(1).getCellData(0).toString();
        tempAppStart = appointmentUpAppTableview.getSelectionModel().getTableView().getVisibleLeafColumn(2).getCellData(0).toString();
        tempAppEnd = appointmentUpAppTableview.getSelectionModel().getTableView().getVisibleLeafColumn(3).getCellData(0).toString();

    }

    /**
     * Check upcoming appointment from the database and is used for login alerts.
     * @return Check upcoming appointment from the database and is used for login alerts.
     */
    public void checkUpcomingAppointmentsAtLogin() {

        if (tempAppID.equals("No Result Found")) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(upcomingAppointmentAlertTitle);
            alert.setHeaderText(null);
            alert.setContentText(

                    "No upcoming appointments."

            );

            alert.showAndWait();

        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(upcomingAppointmentAlertTitle);
            alert.setHeaderText(null);
            alert.setContentText(

                    upcomingAppointmentMessage1 + tempAppMinutes + upcomingAppointmentMessage2
                            + "\n\n"
                            + upcomingAppointmentID + tempAppID
                            + "\n\n"
                            + upcomingAppointmentStart + tempAppStart
                            + "\n\n"
                            + upcomingAppointmentEnd + tempAppEnd

            );

            alert.showAndWait();

        }

    }

    /**
     * Add appointment button handler used to forward users to the crudAppointmentsScreen.
     * @return Add appointment button handler used to forward users to the crudAppointmentsScreen.
     */
    public void addAppointmentButtonHandler(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/crudAppointmentsScreen.fxml"));
        loader.load();

        CrudAppointmentsController addAppointmentAction = loader.getController();
        addAppointmentAction.addAppointment();

        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle(addAppointmentButtonLabel.getText());
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * Update appointment button handler used to forward users to the crudAppointmentsScreen.
     * @return Update appointment button handler used to forward users to the crudAppointmentsScreen.
     */
    public void updateAppointmentButtonHandler(ActionEvent event) throws IOException, SQLException {

        if (appointmentTableview.getSelectionModel().getSelectedItem() == null) {

            Alert error = new Alert(Alert.AlertType.ERROR, noRecordSelected);
            error.showAndWait();

        } else {

            TablePosition getSelectedRow = (TablePosition) appointmentTableview.getSelectionModel().getSelectedCells().get(0);
            int selectedRowInt = getSelectedRow.getRow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/crudAppointmentsScreen.fxml"));
            loader.load();

            CrudAppointmentsController updateAppointmentAction = loader.getController();
            updateAppointmentAction.sendAppointment(
                    getSelectedRow.getTableView().getVisibleLeafColumn(0).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(1).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(2).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(3).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(5).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(6).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(7).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(8).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(10).getCellData(selectedRowInt).toString(),
                    getSelectedRow.getTableView().getVisibleLeafColumn(12).getCellData(selectedRowInt).toString()
            );

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(updateAppointmentButtonLabel.getText());
            stage.centerOnScreen();
            stage.show();

        }

    }

    /**
     * Delete appointment button handler used to delete appointments from the database.
     * @return Delete appointment button handler used to delete appointments from the database.
     */
    public void deleteAppointmentButtonHandler(ActionEvent actionEvent) throws SQLException {

        if (appointmentTableview.getSelectionModel().getSelectedItem() == null) {

            Alert error = new Alert(Alert.AlertType.ERROR, noRecordSelected);
            error.showAndWait();

        } else {

            TablePosition getSelectedRow = (TablePosition) appointmentTableview.getSelectionModel().getSelectedCells().get(0);
            int selectedRowInt = getSelectedRow.getRow();
            String selectedRowColumn0 = getSelectedRow.getTableView().getVisibleLeafColumn(0).getCellData(selectedRowInt).toString();
            Connection conn1 = DBConnection.startConnection();
            Statement stmt1 = conn1.createStatement();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,deleteSelectedRecord);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                try {

                    stmt1.executeUpdate("DELETE FROM "
                            + "appointments "
                            + "WHERE "
                            + "Appointment_ID = \"" + selectedRowColumn0 + "\" "
                    );

                } catch (SQLException ex) {
                    Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    Alert error = new Alert(Alert.AlertType.ERROR,errorDeletingCustomer);
                    error.show();
                }

            }

            DBConnection.closeConnection();

            appointmentData.clear();
            appointmentTableview.getItems().clear();
            appointmentTableview.getColumns().clear();
            buildAppointmentData();


            appointmentMonthData.clear();
            appointmentMonthTableview.getItems().clear();
            appointmentMonthTableview.getColumns().clear();
            buildAppointmentMonthData();

            appointmentWeekData.clear();
            appointmentWeekTableview.getItems().clear();
            appointmentWeekTableview.getColumns().clear();
            buildAppointmentWeekData();

        }

    }

    /**
     * Customers by type and month report button handler used to generate reports.
     * @return Customers by type and month report button handler used to generate reports.
     */
    public void customersByTypeMonthReportButtonHandler(ActionEvent actionEvent) throws SQLException {

        String reportName = "NumberOfCus_Type_Month.csv";

        //Create connection for report
        Connection conn = DBConnection.startConnection();

        ResultSet rs = conn.createStatement().executeQuery(

                "SELECT "
                        + "Type AS 'Type', "
                        + "COUNT(Type) AS 'Customer_Count_By_Type' "
                        + "FROM appointments "
                        + "GROUP BY Type "

        );

        Connection conn1 = DBConnection.startConnection();

        ResultSet rs1 = conn1.createStatement().executeQuery(

                "SELECT "
                        + "MONTHNAME(Start) AS 'Month', "
                        + "COUNT(Start) AS 'Customer_Count_By_Month' "
                        + "FROM appointments "
                        + "GROUP BY MONTHNAME(Start) "

        );


        //Write data to csv file
        try {

            FileWriter csvReport = new FileWriter(System.getProperty("user.dir") + "\\" + "src" + "\\" + "reports" + "\\" + reportName);


            int cols = rs.getMetaData().getColumnCount();

            for(int i = 1; i <= cols; i ++) {

                csvReport.append(rs.getMetaData().getColumnLabel(i));
                if (i < cols) csvReport.append(',');
                else csvReport.append('\n');

            }

            while (rs.next()) {

                for (int i = 1; i <= cols; i++) {
                    csvReport.append(rs.getString(i));
                    if (i < cols) csvReport.append(',');
                }
                csvReport.append('\n');

            }


            csvReport.append('\n');
            csvReport.append('\n');


            int cols1 = rs1.getMetaData().getColumnCount();

            for(int i = 1; i <= cols1; i ++) {

                csvReport.append(rs1.getMetaData().getColumnLabel(i));
                if (i < cols1) csvReport.append(',');
                else csvReport.append('\n');

            }

            while (rs1.next()) {

                for (int i = 1; i <= cols1; i++) {
                    csvReport.append(rs1.getString(i));
                    if (i < cols1) csvReport.append(',');
                }
                csvReport.append('\n');

            }


            csvReport.flush();
            csvReport.close();

            conn.close();
            conn1.close();

        } catch (Exception ex) {

            Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);

        }

        DBConnection.closeConnection();

        //Create alert box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(reportsTabPane.getText());
        alert.setHeaderText(null);
        alert.setContentText(

                reportGeneratedLocation
                        + "\n\n"
                        + "\\" + "root" + "\\" + "src" + "\\" + "reports" + "\\" + reportName
                        + "\n\n"
                        + openReportQuestion

        );

        alert.getButtonTypes().add(ButtonType.CLOSE);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            //OK button pressed
            System.out.println("ButtonType.OK");

            try {

                //Name File and path
                File file = new File(System.getProperty("user.dir") + "\\" + "src" + "\\" + "reports" + "\\" + reportName);

                //Check if OS is supported
                if (!Desktop.isDesktopSupported()) {

                    System.out.println("OS not supported.");
                    return;

                }

                Desktop desktop = Desktop.getDesktop();

                //Check if file exists
                if (file.exists()) {

                    //If exists, open file
                    desktop.open(file);

                }

            }
            catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    /**
     * Contact appointments report button handler used to generate reports.
     * @return Contact appointments report button handler used to generate reports.
     */
    public void contactAppointmentsReportButtonHandler(ActionEvent actionEvent) throws SQLException {

        String reportName = "AppointmentByContact.csv";

        //Create connection for report
        Connection conn = DBConnection.startConnection();

        ResultSet rs = conn.createStatement().executeQuery(

                "SELECT "
                        + "contacts.Contact_Name, "
                        + "appointments.Appointment_ID, "
                        + "appointments.Title, "
                        + "appointments.Description, "
                        + "appointments.Start, "
                        + "appointments.End, "
                        + "appointments.Customer_ID, "
                        + "customers.Customer_Name "
                        + "FROM contacts "
                        + "JOIN appointments "
                        + "ON contacts.Contact_ID = appointments.Contact_ID "
                        + "JOIN customers "
                        + "ON appointments.Customer_ID = customers.Customer_ID "
                        + "ORDER BY contacts.Contact_Name "

        );


        //Write data to csv file
        try {

            FileWriter csvReport = new FileWriter(System.getProperty("user.dir") + "\\" + "src" + "\\" + "reports" + "\\" + reportName);


            int cols = rs.getMetaData().getColumnCount();

            for(int i = 1; i <= cols; i ++) {

                csvReport.append(rs.getMetaData().getColumnLabel(i));
                if (i < cols) csvReport.append(',');
                else csvReport.append('\n');

            }

            while (rs.next()) {

                for (int i = 1; i <= cols; i++) {
                    csvReport.append(rs.getString(i));
                    if (i < cols) csvReport.append(',');
                }
                csvReport.append('\n');

            }


            csvReport.flush();
            csvReport.close();

            conn.close();

        } catch (Exception ex) {

            Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);

        }

        DBConnection.closeConnection();

        //Create alert box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(reportsTabPane.getText());
        alert.setHeaderText(null);
        alert.setContentText(

                reportGeneratedLocation
                        + "\n\n"
                        + "\\" + "root" + "\\" + "src" + "\\" + "reports" + "\\" + reportName
                        + "\n\n"
                        + openReportQuestion

        );

        alert.getButtonTypes().add(ButtonType.CLOSE);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            //OK button pressed
            System.out.println("ButtonType.OK");

            try {

                //Name File and path
                File file = new File(System.getProperty("user.dir") + "\\" + "src" + "\\" + "reports" + "\\" + reportName);

                //Check if OS is supported
                if (!Desktop.isDesktopSupported()) {

                    System.out.println("OS not supported.");
                    return;

                }

                Desktop desktop = Desktop.getDesktop();

                //Check if file exists
                if (file.exists()) {

                    //If exists, open file
                    desktop.open(file);

                }

            }
            catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    /**
     * Custom report button handler used to generate reports. This report gathers Appointment_ID, Title, <br>
     *     Type, Customer_Name, Division and Country.<br>
     *         This report also includes two lambda expressions.<br>
     *             The justification for this lambda expression to remove redundancies within the code.<br>
     * @return Custom report button handler used to generate reports.
     */
    public void customReportButtonHandler(ActionEvent actionEvent) throws SQLException {

        String reportName = "CustomReport.csv";
        String sqlQueryCustomReport = "SELECT "
                + "appointments.Appointment_ID, "
                + "appointments.Title, "
                + "appointments.Type, "
                + "customers.Customer_Name, "
                + "first_level_divisions.Division, "
                + "countries.Country "
                + "FROM appointments "
                + "JOIN customers "
                + "ON appointments.Customer_ID = customers.Customer_ID "
                + "JOIN first_level_divisions "
                + "ON customers.Division_ID = first_level_divisions.Division_ID "
                + "JOIN countries "
                + "ON first_level_divisions.COUNTRY_ID = countries.Country_ID "
                ;


        /**
         * LAMBDA EXPRESSIONS 1 of 2
         */
        LambdaOneString customReport = text -> {
            //Create alert box
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(reportsTabPane.getText());
            alert.setHeaderText(null);
            alert.setContentText(

                    reportGeneratedLocation
                            + "\n\n"
                            + "\\" + "root" + "\\" + "src" + "\\" + "reports" + "\\" + text
                            + "\n\n"
                            + openReportQuestion

            );

            alert.getButtonTypes().add(ButtonType.CLOSE);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {

                //OK button pressed
                System.out.println("ButtonType.OK");

                try {

                    //Name File and path
                    File file = new File(System.getProperty("user.dir") + "\\" + "src" + "\\" + "reports" + "\\" + text);

                    //Check if OS is supported
                    if (!Desktop.isDesktopSupported()) {

                        System.out.println("OS not supported.");
                        return;

                    }

                    Desktop desktop = Desktop.getDesktop();

                    //Check if file exists
                    if (file.exists()) {

                        //If exists, open file
                        desktop.open(file);

                    }

                }
                catch (Exception e) {

                    e.printStackTrace();

                }

            }

        };


        /**
         * LAMBDA EXPRESSIONS 2 of 2
         */
        LambdaTwoStrings customReport2 = (text1,text2) -> {

            //Create connection for report
            Connection conn = DBConnection.startConnection();

            ResultSet rs = conn.createStatement().executeQuery(text1);


            //Write data to csv file
            try {

                FileWriter csvReport = new FileWriter(System.getProperty("user.dir") + "\\" + "src" + "\\" + "reports" + "\\" + text2);


                int cols = rs.getMetaData().getColumnCount();

                for(int i = 1; i <= cols; i ++) {

                    csvReport.append(rs.getMetaData().getColumnLabel(i));
                    if (i < cols) csvReport.append(',');
                    else csvReport.append('\n');

                }

                while (rs.next()) {

                    for (int i = 1; i <= cols; i++) {
                        csvReport.append(rs.getString(i));
                        if (i < cols) csvReport.append(',');
                    }
                    csvReport.append('\n');

                }


                csvReport.flush();
                csvReport.close();

                conn.close();

            } catch (Exception ex) {

                Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);

            }

            DBConnection.closeConnection();

        };


        /**
         * Lambda expressions 1 and 2 implementation
         */
        customReport.apply(reportName);
        customReport2.apply(sqlQueryCustomReport,reportName);

    }

    /**
     * User Login report button handler used to generate reports.
     * @return User Login report button handler used to generate reports.
     */
    public void userLoginReportButtonHandler(ActionEvent actionEvent) {

        String reportName = "login_activity.txt";

        //Create alert box
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(reportsTabPane.getText());
        alert.setHeaderText(null);
        alert.setContentText(

                reportGeneratedLocation
                        + "\n\n"
                        + "\\" + "root" + "\\" + "src" + "\\" + "reports" + "\\" + reportName
                        + "\n\n"
                        + openReportQuestion

        );

        alert.getButtonTypes().add(ButtonType.CLOSE);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {

            //OK button pressed
            System.out.println("ButtonType.OK");

            try {

                //Name File and path
                File file = new File(System.getProperty("user.dir") + "\\" + "src" + "\\" + "reports" + "\\" + reportName);

                //Check if OS is supported
                if (!Desktop.isDesktopSupported()) {

                    System.out.println("OS not supported.");
                    return;

                }

                Desktop desktop = Desktop.getDesktop();

                //Check if file exists
                if (file.exists()) {

                    //If exists, open file
                    desktop.open(file);

                }

            }
            catch (Exception e) {

                e.printStackTrace();

            }

        }

    }

    /**
     * Selects appointments TabPane.
     * @return Selects appointments TabPane.
     */
    public void selectAppointmentsTabPane() {

        SingleSelectionModel<Tab> selectionModel = mainScreenTabPane.getSelectionModel();
        selectionModel.select(1);

    }

    /**
     * Exit button handler used to exit the application.
     * @return Exit button handler used to exit the application.
     */
    @FXML
    private void exitButtonHandler(ActionEvent event) throws IOException {

        System.exit(0);

    }

}
