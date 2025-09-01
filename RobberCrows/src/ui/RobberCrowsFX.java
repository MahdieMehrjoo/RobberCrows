package com.robbercrows.ui;

import com.robbercrows.core.Game;
import com.robbercrows.entity.Crow;
// import com.robbercrows.map.EnergyPoint;
// import com.robbercrows.map.Obstacle;
import com.robbercrows.map.Position;
// import com.robbercrows.map.RestPoint;
import com.robbercrows.team.Team;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
// import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
// import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
// import javafx.stage.Modality;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Alert.AlertType;
// import javafx.scene.control.ButtonType;
import com.robbercrows.map.GameMap;

public class RobberCrowsFX extends Application {

    private static final int TILE_SIZE = 32;
    private Game game;
    private Canvas canvas;
    private VBox redTeamInfo, blueTeamInfo;
    private Label gameStatus;

    @Override
    public void start(Stage stage) {
        // Init game and demo data
        game = new Game();
        initDemoContent();

        // Canvas sized to map - make it smaller to fit laptop screens
        int width = Math.min(game.getGameMap().getWidth() * TILE_SIZE, 800);
        int height = Math.min(game.getGameMap().getHeight() * TILE_SIZE, 600);
        canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        game.setGraphicsContext(gc);

        // Create beautiful team info display at top
        VBox topInfo = createTeamInfoDisplay();

        // Create beautiful control buttons
        HBox controls = createBeautifulControls();

        // Game status label
        gameStatus = new Label("🎮 بازی شروع شد! کلیدهای جهت‌نما برای حرکت | R برای شروع/توقف");
        gameStatus.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gameStatus.setStyle("-fx-text-fill: #2E8B57; -fx-background-color: #F0F8FF; -fx-padding: 10; -fx-border-radius: 5;");

        BorderPane root = new BorderPane();
        root.setTop(topInfo);
        root.setCenter(new StackPane(canvas));
        root.setBottom(new VBox(10, gameStatus, controls));

        Scene scene = new Scene(root);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> game.handleInput(e.getCode()));

        stage.setTitle("Robber Crows - بازی کلاغ‌های دزد");
        stage.setScene(scene);
        stage.setResizable(true); // Allow resizing
        stage.setMinWidth(900);   // Minimum width
        stage.setMinHeight(700);  // Minimum height
        stage.setMaxWidth(1200);  // Maximum width
        stage.setMaxHeight(900);  // Maximum height

        // Center the window on screen
        stage.centerOnScreen();
        stage.show();

        // Start game update/render loop
        game.startGame();

        // Fallback render loop if needed (ensures first frame)
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.render();
                updateTeamInfo();
            }
        }.start();
    }

    private VBox createTeamInfoDisplay() {
        VBox topInfo = new VBox(10);
        topInfo.setStyle("-fx-background-color: linear-gradient(to right, #FF6B6B, #4ECDC4); -fx-padding: 15; -fx-border-radius: 10;");
        topInfo.setAlignment(Pos.CENTER);

        // Title
        Label title = new Label("🏆 کلاغ‌های دزد 🏆");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: white; -fx-effect: dropshadow(gaussian, black, 3, 0, 0, 2);");

        // Team info containers
        HBox teamContainer = new HBox(30);
        teamContainer.setAlignment(Pos.CENTER);

        // Red Team Info
        redTeamInfo = createTeamInfoBox("🔴 تیم قرمز", "#FF6B6B");

        // Blue Team Info
        blueTeamInfo = createTeamInfoBox("🔵 تیم آبی", "#4ECDC4");

        teamContainer.getChildren().addAll(redTeamInfo, blueTeamInfo);
        topInfo.getChildren().addAll(title, teamContainer);

        return topInfo;
    }

    private VBox createTeamInfoBox(String teamName, String color) {
        VBox teamBox = new VBox(5);
        teamBox.setStyle("-fx-background-color: " + color + "; -fx-padding: 15; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);");
        teamBox.setAlignment(Pos.CENTER);
        teamBox.setMinWidth(200);

        Label nameLabel = new Label(teamName);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setStyle("-fx-text-fill: white;");

        Label scoreLabel = new Label("امتیاز: 0");
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        scoreLabel.setStyle("-fx-text-fill: white;");

        Label healthLabel = new Label("جان: 3");
        healthLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        healthLabel.setStyle("-fx-text-fill: white;");

        Label energyLabel = new Label("انرژی: 100%");
        energyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        energyLabel.setStyle("-fx-text-fill: white;");

        Label backpackLabel = new Label("کوله: 0/8");
        backpackLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backpackLabel.setStyle("-fx-text-fill: white;");

        teamBox.getChildren().addAll(nameLabel, scoreLabel, healthLabel, energyLabel, backpackLabel);
        return teamBox;
    }

    private HBox createBeautifulControls() {
        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER);
        controls.setStyle("-fx-background-color: #F8F9FA; -fx-padding: 20; -fx-border-radius: 10;");

        // Regenerate Map Button
        Button regenerate = createBeautifulButton("🗺️ ساخت نقشه جدید", "#28A745");
        regenerate.setOnAction(e -> {
            game.getGameMap().generateMap();
            game.render();
        });

        // Start/Stop Button
        Button startStop = createBeautifulButton("▶️ شروع/توقف بازی", "#007BFF");
        startStop.setOnAction(e -> {
            if (game.isGameRunning()) {
                game.endGame();
                startStop.setText("⏸️ شروع بازی");
                startStop.setStyle(regenerate.getStyle().replace("#007BFF", "#FFC107"));
            } else {
                game.startGame();
                startStop.setText("⏸️ توقف بازی");
                startStop.setStyle(regenerate.getStyle().replace("#FFC107", "#007BFF"));
            }
        });

        // Restart Button
        Button restart = createBeautifulButton("🔄 شروع مجدد", "#DC3545");
        restart.setOnAction(e -> restartGame());

        // Map Size Button
        Button mapSize = createBeautifulButton("📏 اندازه نقشه", "#17A2B8");
        mapSize.setOnAction(e -> toggleMapSize());

        controls.getChildren().addAll(regenerate, startStop, restart, mapSize);
        return controls;
    }

    private Button createBeautifulButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 12 20; -fx-border-radius: 8; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1);");
        button.setMinWidth(150);
        button.setMinHeight(45);

        // Hover effects
        button.setOnMouseEntered(e -> button.setStyle(button.getStyle().replace("; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1)", "; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2)")));
        button.setOnMouseExited(e -> button.setStyle(button.getStyle().replace("; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 5, 0, 0, 2)", "; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0, 0, 1)")));

        return button;
    }

    private void restartGame() {
        // Stop current game
        if (game.isGameRunning()) {
            game.endGame();
        }

        // Reset teams
        game.getTeams().clear();

        // Initialize new game
        initDemoContent();

        // Start new game
        game.startGame();

        // Reset UI
        gameStatus.setText("🔄 بازی جدید شروع شد! کلیدهای جهت‌نما برای حرکت | R برای شروع/توقف");
        gameStatus.setStyle("-fx-text-fill: #2E8B57; -fx-background-color: #F0F8FF; -fx-padding: 10; -fx-border-radius: 5;");

        // Reset team info displays
        if (redTeamInfo != null && redTeamInfo.getChildren().size() >= 5) {
            ((Label) redTeamInfo.getChildren().get(1)).setText("امتیاز: 0");
            ((Label) redTeamInfo.getChildren().get(2)).setText("جان: 3");
            ((Label) redTeamInfo.getChildren().get(3)).setText("انرژی: 100%");
            ((Label) redTeamInfo.getChildren().get(4)).setText("کوله: 0/8");
        }

        if (blueTeamInfo != null && blueTeamInfo.getChildren().size() >= 5) {
            ((Label) blueTeamInfo.getChildren().get(1)).setText("امتیاز: 0");
            ((Label) blueTeamInfo.getChildren().get(2)).setText("جان: 3");
            ((Label) blueTeamInfo.getChildren().get(3)).setText("انرژی: 100%");
            ((Label) blueTeamInfo.getChildren().get(4)).setText("کوله: 0/8");
        }
    }

    private void toggleMapSize() {
        // Stop current game
        if (game.isGameRunning()) {
            game.endGame();
        }

        // Toggle between 16x16 and 20x20
        GameMap currentMap = game.getGameMap();
        if (currentMap.getWidth() == 16) {
            // Switch to larger map
            game.setGameMap(new GameMap(20, 20));
            gameStatus.setText("📏 نقشه بزرگ (20x20) انتخاب شد - برای صفحه‌های بزرگ");
        } else {
            // Switch to smaller map
            game.setGameMap(new GameMap(16, 16));
            gameStatus.setText("📏 نقشه کوچک (16x16) انتخاب شد - برای صفحه‌های کوچک");
        }

        // Regenerate map and restart
        game.getGameMap().generateMap();
        restartGame();
    }

    private void updateTeamInfo() {
        if (game.getTeams().size() < 2) return;

        Team redTeam = game.getTeams().get(0);
        Team blueTeam = game.getTeams().get(1);

        // Check for winner
        Team winner = game.getWinningTeam();
        if (winner != null) {
            String winnerColor = winner.getTeamColor().equals("RED") ? "قرمز" : "آبی";
            gameStatus.setText("🏆 تیم " + winnerColor + " برنده شد! امتیاز: " + winner.getTotalScore() + " | دکمه شروع مجدد را بزنید");
            gameStatus.setStyle("-fx-text-fill: #FFD700; -fx-background-color: #FF6B6B; -fx-padding: 15; -fx-border-radius: 10; -fx-font-weight: bold;");

            return;
        }

        if (redTeam != null && redTeam.getCrows().size() > 0) {
            Crow redCrow = redTeam.getCrows().get(0);
            updateTeamInfoBox(redTeamInfo, redTeam, redCrow);
        }

        if (blueTeam != null && blueTeam.getCrows().size() > 0) {
            Crow blueCrow = blueTeam.getCrows().get(0);
            updateTeamInfoBox(blueTeamInfo, blueTeam, blueCrow);
        }

        // Update game status with current scores
        if (redTeam != null && blueTeam != null) {
            gameStatus.setText("🔴 تیم قرمز: " + redTeam.getTotalScore() + " | 🔵 تیم آبی: " + blueTeam.getTotalScore() + " | هدف: 500 امتیاز");
        }
    }

    private void updateTeamInfoBox(VBox teamBox, Team team, Crow crow) {
        if (teamBox.getChildren().size() < 5) return;

        // Update score
        Label scoreLabel = (Label) teamBox.getChildren().get(1);
        scoreLabel.setText("امتیاز: " + team.getTotalScore());

        // Update health
        Label healthLabel = (Label) teamBox.getChildren().get(2);
        healthLabel.setText("جان: " + crow.getHealth());

        // Update energy
        Label energyLabel = (Label) teamBox.getChildren().get(3);
        energyLabel.setText("انرژی: " + crow.getEnergy() + "%");

        // Update backpack
        Label backpackLabel = (Label) teamBox.getChildren().get(4);
        backpackLabel.setText("کوله: " + crow.getBackpackTreasureCount() + "/8");

        // Check if backpack is full and show warning
        if (crow.isBackpackFull()) {
            backpackLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-background-color: #FF4500; -fx-padding: 5; -fx-border-radius: 5;");
            backpackLabel.setText("⚠️ کوله پر! به نقطه استراحت برو!");
        } else if (crow.getBackpackTreasureCount() >= 6) {
            backpackLabel.setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
        } else {
            backpackLabel.setStyle("-fx-text-fill: white;");
        }

        // Show energy warning
        if (crow.getEnergy() < 25) {
            energyLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold; -fx-background-color: #FF4500; -fx-padding: 5; -fx-border-radius: 5;");
            energyLabel.setText("⚠️ انرژی کم! به نقطه انرژی برو!");
        } else if (crow.getEnergy() < 50) {
            energyLabel.setStyle("-fx-text-fill: #FFA500; -fx-font-weight: bold;");
        } else {
            energyLabel.setStyle("-fx-text-fill: white;");
        }
    }

    private void initDemoContent() {
        // Teams and crows
        Team red = new Team(1, "RED", 3);
        Crow player = new Crow("Player", new Position(2, 2), red);
        red.addMember(player);

        Team blue = new Team(2, "BLUE", 3);
        Crow ai = new Crow("AI", new Position(15, 15), blue);
        blue.addMember(ai);

        game.addTeam(red);
        game.addTeam(blue);

        // Generate initial map
        game.getGameMap().generateMap();
    }
}


