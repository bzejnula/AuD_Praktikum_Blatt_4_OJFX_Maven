package de.hsos.aud.routing.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * UI zur Bestimmung kuerzester Wege im Graphen.
 * 
 * @author Simon Balzer
 * @Heinz-Josef Eikerling
 */
public class UIApplication extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UINavigator.fxml"));
        stage.setTitle("Routenplaner - " + UIConfig.DEFAULT_FILE);
        stage.setScene(new Scene(root));
        stage.setResizable(true);
        stage.show();
    }

    /**
     * @param args Kommandozeilen Parameter
     */
    public static void main(String[] args) {
        launch();
    }
}