package main;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application Main class.
 *
 * @author Rodney Agosto
 */
public class Main extends Application {

    String Login_Menu = "";

    /**
     * Start method used to start the FXML login screen.
     * @return Start method used to start the FXML login screen.
     */
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/loginScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);

        ResourceBundle rb1 = ResourceBundle.getBundle("language/Nat", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")){
            Login_Menu = rb1.getString("loginMenu");
        }

        stage.setTitle(Login_Menu);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Main method used to launch args.
     * @return Main method used to launch args.
     */
    public static void main(String[] args) {

        launch(args);
    }
}
