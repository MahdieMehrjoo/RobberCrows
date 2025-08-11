package com.robbercrows.core;

import com.robbercrows.entity.Crow;
import com.robbercrows.map.GameMap;
import com.robbercrows.map.Position;
import com.robbercrows.map.GameObject;
import com.robbercrows.team.ScoreManager;
import com.robbercrows.team.Team;
import com.robbercrows.entity.PowerUp;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Random;


 // نگهداری نقشه، تیم‌ها و مدیریت امتیاز
 // مدیریت حلقه‌ی اصلی بازی (update/render) و Threadهای کلاغ‌ها
 //مدیریت چرخه‌ی ظاهر/محو «آهن‌ربا» روی نقشه و جمع‌آوری آن توسط کلاغ
 // بررسی برنده شدن بازی و کنترل شروع/پایان بازی
import com.robbercrows.map.GameObject;
import com.robbercrows.map.Position;
import com.robbercrows.map.Obstacle;
import com.robbercrows.map.EnergyPoint;
import com.robbercrows.map.RestPoint;

public class Game {
    // شناسه ها
    private GameMap gameMap;
    private List<Team> teams;
    private ScoreManager scoreManager;
    private AtomicBoolean isGameRunning;
    private AnimationTimer timer;
    private boolean isSimulation;
    private List<Thread> crowThreads;
    private GraphicsContext graphicsContext;
    private final int tileSize = 32;
    private static final int WIN_SCORE = 1000;

    // Thread management
    private ExecutorService crowExecutor;
    private ReentrantLock gameLock;
    private final int MAX_CROW_THREADS = 10;

    // Constructor
    public Game() {
        this.gameMap = new GameMap();
        this.teams = new ArrayList<>();
        this.scoreManager = new ScoreManager(50); // threshold example: 50 points
        this.scoreManager = new ScoreManager(WIN_SCORE);
        this.isGameRunning = new AtomicBoolean(false);
        this.crowThreads = new ArrayList<>();
        this.crowExecutor = Executors.newFixedThreadPool(MAX_CROW_THREADS);
        this.gameLock = new ReentrantLock();
        this.isSimulation = false;
    }

    // متد ها
    public void startGame() {
        if (isGameRunning.compareAndSet(false, true)) {
            System.out.println("Game started!");
            
            // Start crow threads
            startCrowThreads();
            
            // Start game loop
            startGameLoop();
        }
    }
    
    private void startCrowThreads() {
        for (Team team : teams) {
            for (Crow crow : team.getCrows()) {
                Thread crowThread = new Thread(() -> {
                    while (isGameRunning.get() && !Thread.currentThread().isInterrupted()) {
                        try {
                            // Crow AI logic here
                            crowAI(crow);
                            Thread.sleep(100); // Update every 100ms
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }, "Crow-" + crow.getName());
                
                crowThreads.add(crowThread);
                crowThread.start();
            }
        }
    }
    

     // منطق ساده AI برای کلاغ‌ها (قابل گسترش در آینده).

    private void crowAI(Crow crow) {
        // Simple AI: move randomly and collect items
        if (crow.isDead()) return;
        // Placeholder for AI logic
    }

    //آغاز حلقه‌ی اصلی بازی با AnimationTimer (فراخوانی پیوسته‌ی update و render).
    private void startGameLoop() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isGameRunning.get()) {
                    update();
                    render();
                } else {
                    stop();
                }
            }
        };
        timer.start();
    }

    public void update() {
        gameLock.lock();
        try {
            // Update game state
            updateTeams();
            checkWinner();
        } finally {
            gameLock.unlock();
        }
    }

    // به‌روزرسانی وضعیت تیم‌ها (قابل گسترش برای منطق تیمی).
    private void updateTeams() {
        for (Team team : teams) {
            team.update();
        }
    }
    
    // رندر وضعیت بازی (فعلاً لاگ متنی؛ در آینده با گرافیک جایگزین می‌شود).
    // همچنین شمارش معکوس زمان باقی‌مانده‌ی نمایش آهن‌ربا چاپ می‌شود.
    public void render() {
        // JavaFX rendering
        if (graphicsContext == null) {
            return;
        }

        // Clear
        double canvasWidth = gameMap.getWidth() * tileSize;
        double canvasHeight = gameMap.getHeight() * tileSize;
        graphicsContext.setFill(Color.web("#111111"));
        graphicsContext.fillRect(0, 0, canvasWidth, canvasHeight);

        // Grid
        graphicsContext.setStroke(Color.web("#2a2a2a"));
        for (int x = 0; x <= gameMap.getWidth(); x++) {
            graphicsContext.strokeLine(x * tileSize, 0, x * tileSize, canvasHeight);
        }
        for (int y = 0; y <= gameMap.getHeight(); y++) {
            graphicsContext.strokeLine(0, y * tileSize, canvasWidth, y * tileSize);
        }

        // Map objects
        List<GameObject> objects = gameMap.getObjects();
        for (GameObject obj : objects) {
            if (obj == null || !obj.isActive() || obj.getPosition() == null) continue;
            int ox = obj.getPosition().getX();
            int oy = obj.getPosition().getY();
            double px = ox * tileSize;
            double py = oy * tileSize;
            if (obj instanceof Obstacle) {
                graphicsContext.setFill(Color.web("#8B0000"));
                graphicsContext.fillRect(px + 2, py + 2, tileSize - 4, tileSize - 4);
            } else if (obj instanceof EnergyPoint) {
                graphicsContext.setFill(Color.web("#00CED1"));
                graphicsContext.fillOval(px + 6, py + 6, tileSize - 12, tileSize - 12);
            } else if (obj instanceof RestPoint) {
                graphicsContext.setFill(Color.web("#7B68EE"));
                graphicsContext.fillRoundRect(px + 4, py + 4, tileSize - 8, tileSize - 8, 8, 8);
            } else {
                graphicsContext.setFill(Color.GRAY);
                graphicsContext.fillRect(px + 8, py + 8, tileSize - 16, tileSize - 16);
            }
        }

        // Crows
        List<Team> teamsSnapshot = getTeams();
        for (Team team : teamsSnapshot) {
            Color teamColor = parseTeamColor(team.getTeamColor());
            for (Crow crow : team.getCrows()) {
                if (crow == null || crow.getPosition() == null) continue;
                Position p = crow.getPosition();
                double cx = p.getX() * tileSize + tileSize / 2.0;
                double cy = p.getY() * tileSize + tileSize / 2.0;
                graphicsContext.setFill(teamColor);
                graphicsContext.fillOval(cx - 10, cy - 10, 20, 20);
            }
        }
    }

    // پایان دادن به بازی: توقف حلقه، متوقف‌سازی Threadها و بستن منابع اجرایی.
    public void endGame() {
        if (isGameRunning.compareAndSet(true, false)) {
            System.out.println("Game ended!");
            
            // Stop all crow threads
            stopAllCrowThreads();
            
            // Stop game loop
            if (timer != null) {
                timer.stop();
            }
            
            // Shutdown executor
            if (crowExecutor != null) {
                crowExecutor.shutdown();
            }
        }
    }
    
    // قطع و پاک‌سازی همه‌ی Threadهای کلاغ‌ها.
    private void stopAllCrowThreads() {
        for (Thread thread : crowThreads) {
            thread.interrupt();
        }
        crowThreads.clear();
    }
    
    // مدیریت ورودی کاربر (برای نسخه‌ی گرافیکی آینده).
    public void handleInput(KeyCode key) {
        // Simple player control: move the first crow of the first team
        if (teams.isEmpty() || teams.get(0).getCrows().isEmpty()) return;
        Crow player = teams.get(0).getCrows().get(0);
        switch (key) {
            case UP:
                player.move(com.robbercrows.entity.Direction.UP);
                break;
            case DOWN:
                player.move(com.robbercrows.entity.Direction.DOWN);
                break;
            case LEFT:
                player.move(com.robbercrows.entity.Direction.LEFT);
                break;
            case RIGHT:
                player.move(com.robbercrows.entity.Direction.RIGHT);
                break;
            default:
                break;
        }
    }
    
    // فعال‌سازی حالت شبیه‌سازی (بدون ورودی کاربر).
    public void runSimulation() {
        if (!isSimulation) {
            isSimulation = true;
            System.out.println("Simulation started!");
        }
    }
    
    // بررسی برنده: تیمی که به امتیاز هدف برسد، بازی را می‌بَرد.
    public void checkWinner() {
        Team winner = null;
        int highestScore = -1;
        
        for (Team team : teams) {
            int teamScore = team.calculateScore();
            if (teamScore > highestScore) {
                highestScore = teamScore;
                winner = team;
            }
        }
        
        if (winner != null && highestScore >= 1000) { // Win condition
            System.out.println("Winner: Team " + winner.getTeamId() + " with score: " + highestScore);
        if (winner != null && highestScore >= WIN_SCORE) { // Win condition
            System.out.println("Winner: Team #" + winner.getTeamId() + " (" + winner.getTeamColor() + ") with score: " + highestScore);
            endGame();
        }
    }
    
    // سوییچ به حالت تک‌نفره (قابل پیاده‌سازی در آینده).
    public void switchToSinglePlayer(Crow computerCrow) {
        // Switch to single player mode
        System.out.println("Switching to single player mode");
    }
    
    // خروج از حالت شبیه‌سازی.
    public void stopSimulation() {
        if (isSimulation) {
            isSimulation = false;
            System.out.println("Simulation stopped!");
        }
    }
    
    // گرفتن لیست تیم‌ها (کپی برای ایمنی Thread).
    public List<Team> getTeams() {
        return new ArrayList<>(teams); // Return copy for thread safety
    }
    
    // افزودن تیم جدید به بازی به‌صورت thread-safe.
    public void addTeam(Team team) {
        gameLock.lock();
        try {
            teams.add(team);
        } finally {
            gameLock.unlock();
        }
    }
    
    // دسترسی به مدیر امتیاز بازی.
    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    // وضعیت اجرای بازی.
    public boolean isGameRunning() {
        return isGameRunning.get();
    }
    
    // تغییر وضعیت شروع/پایان بازی.
    public void setGameRunning(boolean gameRunning) {
        if (gameRunning) {
            startGame();
        } else {
            endGame();
        }
    }
    
    //دسترسی به نقشه‌ی بازی.
    public GameMap getGameMap() {
        return gameMap;
    }

    // Renderer wiring
    public void setGraphicsContext(GraphicsContext gc) {
        this.graphicsContext = gc;
    }

    private Color parseTeamColor(String colorName) {
        if (colorName == null) return Color.ORANGE;
        switch (colorName.trim().toUpperCase()) {
            case "RED": return Color.CRIMSON;
            case "BLUE": return Color.DODGERBLUE;
            case "GREEN": return Color.LIMEGREEN;
            case "YELLOW": return Color.GOLD;
            case "PURPLE": return Color.MEDIUMPURPLE;
            default:
                try { return Color.web(colorName); } catch (Exception ignored) { return Color.ORANGE; }
        }
    }
    
    // Cleanup method

     // پاک‌سازی منابع اجرایی بازی (جهت خروج امن).
    public void cleanup() {
        endGame();
        if (crowExecutor != null && !crowExecutor.isShutdown()) {
            crowExecutor.shutdownNow();
        }
    }
}