/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package technicalknowledgechat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.techchat.config.AppConfig;
import com.techchat.util.ThemeManager;
import com.techchat.util.LanguageManager;

/**
 *
 * @author User
 */
public class TechnicalKnowledgeChat extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize configurations
        AppConfig config = AppConfig.getInstance();
        LanguageManager.initialize();

        // Check if FXML file exists
        java.net.URL fxmlUrl = getClass().getResource("/com/techchat/view/WelcomeView.fxml");
        if (fxmlUrl == null) {
            System.err.println("FATAL: Cannot find WelcomeView.fxml in classpath");
            System.err.println("Expected location: /com/techchat/view/WelcomeView.fxml");
            throw new RuntimeException("WelcomeView.fxml not found - check your build configuration");
        }

        Parent root = FXMLLoader.load(fxmlUrl);

        Scene scene = new Scene(root);

        // Apply theme from config
        ThemeManager.applyCurrentTheme(scene);

        stage.setTitle(config.getAppName() + " - Welcome");
        stage.setScene(scene);

        // Set reasonable default window size
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
