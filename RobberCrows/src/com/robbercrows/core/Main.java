package com.robbercrows.core;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Robber Crows");
        stage.show();

        Game game = new Game();
        game.startGame(); // حالا AnimationTimer روی JavaFX Thread اجرا می‌شود
    }

    public static void main(String[] args) {
        launch(args);
import com.robbercrows.ui.RobberCrowsFX;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Application.launch(RobberCrowsFX.class, args);
    }
}
