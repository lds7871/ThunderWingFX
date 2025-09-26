package com.lds7871.flyaway;

import com.lds7871.flyaway.A_Contorller.Main;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        String[] args = new String[0];
        Main.launch(args);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

