package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import utils.DBConnection;

/**
 * FXML Controller class used for the CrudCustomers screen of the application.
 *
 * @author Rodney Agosto
 */
public class CrudCustomersController implements Initializable {

    Stage stage;
    String tempCustomerCountry;
    String tempCustomerDivision;
    String lookupDivision_ID;
    String tempAddCustomer;
    String tempUpdateCustomer;
    String appMainMenu;
    String emptyFieldsError;

    @FXML
    private Label cancelCustomerButtonLabel;
    @FXML
    private Label Customer_ID;
    @FXML
    private Label Customer_Name;
    @FXML
    private Label Address;
    @FXML
    private Label Postal_Code;
    @FXML
    private Label Phone;
    @FXML
    private Label Country;
    @FXML
    private Label Division;
    @FXML
    private Label addUpdateCustomerButtonLabel;
    @FXML
    private TextField Customer_ID_AutoGen1;
    @FXML
    private TextField Customer_Name1;
    @FXML
    private TextField Address1;
    @FXML
    private TextField Postal_Code1;
    @FXML
    private TextField Phone1;
    @FXML
    private ComboBox<String> Country_ComboBox;
    @FXML
    private ComboBox<String> Division_ComboBox;

    /**
     * Initializes the controller
     * @return Initializes the controller
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ResourceBundle rb1 = ResourceBundle.getBundle("language/Nat", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {

            cancelCustomerButtonLabel.setText(rb1.getString("cancelAppointmentButtonLabel"));
            Customer_ID.setText(rb1.getString("Customer_ID"));
            Customer_Name.setText(rb1.getString("Customer_Name"));
            Address.setText(rb1.getString("Address"));
            Postal_Code.setText(rb1.getString("Postal_Code"));
            Phone.setText(rb1.getString("Phone"));
            Country.setText(rb1.getString("Country"));
            Division.setText(rb1.getString("Division"));
            tempAddCustomer = rb1.getString("tempAddAppointment");
            tempUpdateCustomer = rb1.getString("tempUpdateAppointment");
            appMainMenu = rb1.getString("mainMenu");
            emptyFieldsError = rb1.getString("emptyFieldsError");

        }

        try {
            initializeCountry();
        } catch (SQLException ex) {
            Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Add/Update customer button handler used to add/update customer data.
     * @return Add/Update customer button handler used to add/update customer data.
     */
    public void addUpdateCustomerButtonHandler(ActionEvent event) throws IOException, SQLException {

        //Check null fields
        if ((Customer_Name1.getText() == null || Customer_Name1.getText().trim().isEmpty())
                || (Address1.getText() == null || Address1.getText().trim().isEmpty())
                || (Postal_Code1.getText() == null || Postal_Code1.getText().trim().isEmpty())
                || (Phone1.getText() == null || Phone1.getText().trim().isEmpty())
                || Country_ComboBox.getValue() == null
                || Division_ComboBox.getValue() == null
        ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(emptyFieldsError);
            alert.showAndWait();

        } else {

            //Look up Division_ID
            Connection conn = DBConnection.startConnection();

            ResultSet rs = conn.createStatement().executeQuery("SELECT "
                    + "Division_ID FROM first_level_divisions "
                    + "WHERE Division = \"" + Division_ComboBox.getSelectionModel().getSelectedItem() + "\" "

            );

            try {

                while (rs.next()) {

                    lookupDivision_ID = rs.getString("Division_ID");

                }
            } catch (SQLException ex) {
                Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);
            }

            DBConnection.closeConnection();

            //Create customer record
            if (Customer_ID_AutoGen1.getText().equals("Customer_ID_AutoGen")) {

                Connection conn1 = DBConnection.startConnection();

                Statement stmt1 = conn1.createStatement();

                stmt1.executeUpdate("INSERT INTO "
                        + "customers "
                        + "(Customer_Name,Address,Postal_Code,Phone,Created_By,Last_Updated_By,Division_ID) "
                        + "VALUES "
                        + "(\"" + Customer_Name1.getText() + "\", \"" + Address1.getText() + "\", \"" + Postal_Code1.getText() + "\", \"" + Phone1.getText() + "\", 'script', 'script', \"" + lookupDivision_ID + "\") "
                );

                DBConnection.closeConnection();

            }

            //Update customer record
            if (!Customer_ID_AutoGen1.getText().equals("Customer_ID_AutoGen")) {

                Connection conn1 = DBConnection.startConnection();

                Statement stmt1 = conn1.createStatement();

                stmt1.executeUpdate("UPDATE customers "
                        + "SET "
                        + "Customer_Name = \"" + Customer_Name1.getText() + "\", "
                        + "Address = \"" + Address1.getText() + "\", "
                        + "Postal_Code = \"" + Postal_Code1.getText() + "\", "
                        + "Phone = \"" + Phone1.getText() + "\", "
                        + "Last_Updated_By = 'script', "
                        + "Division_ID = \"" + lookupDivision_ID + "\" "
                        + "WHERE "
                        + "Customer_ID = \"" + Customer_ID_AutoGen1.getText() + "\" "
                );

                DBConnection.closeConnection();

            }

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/mainScreen.fxml"));
            loader.load();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.setTitle(appMainMenu);
            stage.centerOnScreen();
            stage.show();

        }

    }

    /**
     * Cancel customer button handler used to cancel customer data.
     * @return Cancel customer button handler used to cancel customer data.
     */
    public void cancelCustomerButtonHandler(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/mainScreen.fxml"));
        loader.load();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.setTitle(appMainMenu);
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * Add customer used to change label in the form.
     * @return Add customer used to change label in the form.
     */
    public void addCustomer() {

        addUpdateCustomerButtonLabel.setText(tempAddCustomer);

    }

    /**
     * Send customer used to receive customer data.
     * @return Send customer used to receive customer data.
     */
    public void sendCustomer(String customerID,
                             String customerName,
                             String customerAddress,
                             String customerPostalCode,
                             String customerPhone,
                             String customerDivision,
                             String customerCountry
                            ) throws SQLException {

        addUpdateCustomerButtonLabel.setText(tempUpdateCustomer);

        Customer_ID_AutoGen1.setText(String.valueOf(customerID));
        Customer_Name1.setText(String.valueOf(customerName));
        Address1.setText(String.valueOf(customerAddress));
        Postal_Code1.setText(String.valueOf(customerPostalCode));
        Phone1.setText(String.valueOf(customerPhone));

        tempCustomerDivision = customerDivision;
        tempCustomerCountry = customerCountry;

        selectCountryAndDivision();

    }

    /**
     * Select country and division used to select form data.
     * @return Select country and division used to select form data.
     */
    private void selectCountryAndDivision() throws SQLException {

        Country_ComboBox.getSelectionModel().select(0);
        Country_ComboBox.getSelectionModel().select(tempCustomerCountry);

        initializeDivision();

        Division_ComboBox.getSelectionModel().select(0);
        Division_ComboBox.getSelectionModel().select(tempCustomerDivision);

    }

    /**
     * Initialize country used to initialize country data from the database.
     * @return Initialize country used to initialize country data from the database.
     */
    @FXML
    private void initializeCountry() throws SQLException {

        Connection conn = DBConnection.startConnection();

        ResultSet rs = conn.createStatement().executeQuery("SELECT "
                        + "Country FROM countries "
                        + "ORDER BY Country "
        );

        try {

            while (rs.next()) {

                Country_ComboBox.getItems().add(rs.getString("Country"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        DBConnection.closeConnection();

        Country_ComboBox.getSelectionModel().select(0);

        initializeDivision();

    }

    /**
     * Initialize division used to initialize division data from the database.
     * @return Initialize division used to initialize division data from the database.
     */
    @FXML
    private void initializeDivision() throws SQLException {

        Division_ComboBox.getItems().clear();

        String country = Country_ComboBox.getValue();

        Connection conn = DBConnection.startConnection();

        ResultSet rs = conn.createStatement().executeQuery("SELECT "
                + "first_level_divisions.Division "
                + "FROM countries "
                + "JOIN first_level_divisions "
                + "ON countries.Country_ID = first_level_divisions.COUNTRY_ID "
                + "AND countries.Country = \"" + country + "\" "
                + "ORDER BY first_level_divisions.Division "
        );

        try {
            while (rs.next()) {

                Division_ComboBox.getItems().add(rs.getString("Division"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(CrudCustomersController.class.getName()).log(Level.SEVERE, null, ex);
        }

        DBConnection.closeConnection();

        Division_ComboBox.getSelectionModel().select(0);

    }

}
