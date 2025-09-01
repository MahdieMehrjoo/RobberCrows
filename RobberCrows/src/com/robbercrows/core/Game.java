package com.robbercrows.core;

import com.robbercrows.entity.Crow;
import com.robbercrows.entity.Food;
import com.robbercrows.entity.PowerUp;
import com.robbercrows.entity.Treasure;
import com.robbercrows.map.GameMap;
import com.robbercrows.team.ScoreManager;
import com.robbercrows.team.Team;
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
    private static final int WIN_SCORE = 500;

    // Thread management
    private ExecutorService crowExecutor;
    private ReentrantLock gameLock;
    private final int MAX_CROW_THREADS = 10;

    // Constructor
    public Game() {
        this.gameMap = new GameMap();
        this.teams = new ArrayList<>();
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

            // Generate new map
            gameMap.generateMap();

            // Start crow threads
            startCrowThreads();

            // Start game loop
            startGameLoop();
        }
    }

    private void startCrowThreads() {
        for (Team team : teams) {
            for (Crow crow : team.getCrows()) {
                startCrowThread(crow);
            }
        }
    }

    private void crowAI(Crow crow) {
        // Improved AI: seek nearest treasure or energy; otherwise random walk.
        if (crow.isDead()) return;

        // Stop moving if energy is completely depleted
        if (crow.getEnergy() <= 0) {
            System.out.println(crow.getName() + " has no energy - cannot move!");
            return;
        }

        // Do not control the first team's first crow (player)
        if (!teams.isEmpty() && !teams.get(0).getCrows().isEmpty() && teams.get(0).getCrows().get(0) == crow) {
            return;
        }

        // Find nearest target (EnergyPoint, Treasure, Food, or PowerUp) based on need
        Position crowPos = crow.getPosition();
        GameObject nearestTarget = null;
        double nearestDist = Double.MAX_VALUE;

        // If energy is very low, prioritize energy points
        if (crow.getEnergy() < 20) {
            for (GameObject obj : gameMap.getObjects()) {
                if (obj instanceof EnergyPoint && obj.isActive() && obj.getPosition() != null) {
                    double dist = crowPos.distanceTo(obj.getPosition());
                    if (dist < nearestDist) {
                        nearestDist = dist;
                        nearestTarget = obj;
                    }
                }
            }
            if (nearestTarget != null) {
                // Move towards energy point
                moveTowardsTarget(crow, nearestTarget.getPosition());
                return;
            }
        }

        // If backpack is full, prioritize rest points
        if (crow.isBackpackFull()) {
            for (GameObject obj : gameMap.getObjects()) {
                if (obj instanceof RestPoint && obj.isActive() && obj.getPosition() != null) {
                    double dist = crowPos.distanceTo(obj.getPosition());
                    if (dist < nearestDist) {
                        nearestDist = dist;
                        nearestTarget = obj;
                    }
                }
            }
            if (nearestTarget != null) {
                // Move towards rest point
                moveTowardsTarget(crow, nearestTarget.getPosition());
                return;
            }
        }

        // Otherwise, seek treasures and other items
        for (GameObject obj : gameMap.getObjects()) {
            if (obj == null || !obj.isActive() || obj.getPosition() == null) continue;
            if (obj instanceof Obstacle) continue;

            // Prefer energy if low
            if (crow.getEnergy() < 30 && !(obj instanceof EnergyPoint)) continue;
            // Prefer food if speed boost is not active
            if (obj instanceof Food && !crow.isSpeedBoostActive()) {
                double dist = crowPos.distanceTo(obj.getPosition());
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearestTarget = obj;
                }
                continue;
            }
            // Prefer powerup if magnet is not active
            if (obj instanceof PowerUp && !crow.isMagnetActive()) {
                double dist = crowPos.distanceTo(obj.getPosition());
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearestTarget = obj;
                }
                continue;
            }
            // Prefer treasure if backpack has space
            if (obj instanceof Treasure && !crow.isBackpackFull()) {
                double dist = crowPos.distanceTo(obj.getPosition());
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearestTarget = obj;
                }
                continue;
            }
            // Prefer energy point if energy is low
            if (obj instanceof EnergyPoint && crow.getEnergy() < 50) {
                double dist = crowPos.distanceTo(obj.getPosition());
                if (dist < nearestDist) {
                    nearestDist = dist;
                    nearestTarget = obj;
                }
                continue;
            }
            double dist = crowPos.distanceTo(obj.getPosition());
            if (dist < nearestDist) {
                nearestDist = dist;
                nearestTarget = obj;
            }
        }

        if (nearestTarget != null) {
            moveTowardsTarget(crow, nearestTarget.getPosition());
        } else {
            // Random walk fallback
            randomWalk(crow);
        }
    }

    private void moveTowardsTarget(Crow crow, Position target) {
        Position crowPos = crow.getPosition();
        int dx = Integer.compare(target.getX(), crowPos.getX());
        int dy = Integer.compare(target.getY(), crowPos.getY());

        // Try axis with greater delta first
        com.robbercrows.entity.Direction dir = null;
        if (Math.abs(dx) >= Math.abs(dy)) {
            dir = dx < 0 ? com.robbercrows.entity.Direction.LEFT : (dx > 0 ? com.robbercrows.entity.Direction.RIGHT : null);
            if (dir != null && isMoveBlocked(crow, dir)) dir = null;
            if (dir == null) {
                dir = dy < 0 ? com.robbercrows.entity.Direction.UP : (dy > 0 ? com.robbercrows.entity.Direction.DOWN : null);
            }
        } else {
            dir = dy < 0 ? com.robbercrows.entity.Direction.UP : (dy > 0 ? com.robbercrows.entity.Direction.DOWN : null);
            if (dir != null && isMoveBlocked(crow, dir)) dir = null;
            if (dir == null) {
                dir = dx < 0 ? com.robbercrows.entity.Direction.LEFT : (dx > 0 ? com.robbercrows.entity.Direction.RIGHT : null);
            }
        }

        if (dir != null) {
            tryMoveCrow(crow, dir);
        } else {
            randomWalk(crow);
        }
    }

    private void randomWalk(Crow crow) {
        com.robbercrows.entity.Direction[] dirs = com.robbercrows.entity.Direction.values();
        com.robbercrows.entity.Direction dir = dirs[(int) (Math.random() * dirs.length)];
        // try up to 4 times to find a non-blocked direction
        for (int i = 0; i < 4 && isMoveBlocked(crow, dir); i++) {
            dir = dirs[(int) (Math.random() * dirs.length)];
        }

        if (dir != null) {
            tryMoveCrow(crow, dir);
        }
    }

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

            // Update map objects (timers, etc.)
            for (GameObject obj : gameMap.getObjects()) {
                if (obj != null) obj.update();
            }

            // Handle interactions between crows and nearby objects
            for (Team team : teams) {
                for (Crow crow : team.getCrows()) {
                    if (crow == null || crow.getPosition() == null) continue;
                    Position cpos = crow.getPosition();
                    for (GameObject obj : gameMap.getObjects()) {
                        if (obj == null || !obj.isActive() || obj.getPosition() == null) continue;
                        // interact when on same tile or very close
                        if (isSameTile(cpos, obj.getPosition()) || cpos.distanceTo(obj.getPosition()) < 0.75) {
                            obj.interact(crow);
                        }
                    }

                    // Apply magnet effect if active
                    if (crow.isMagnetActive()) {
                        List<GameObject> nearbyTreasures = gameMap.getItemsAround(crow.getPosition());
                        List<Treasure> treasures = new ArrayList<>();
                        for (GameObject obj : nearbyTreasures) {
                            if (obj instanceof Treasure && obj.isActive()) {
                                treasures.add((Treasure) obj);
                            }
                        }
                        crow.applyMagnetToTreasures(treasures);
                    }
                }
            }

            checkWinner();
        } finally {
            gameLock.unlock();
        }
    }

    private void updateTeams() {
        for (Team team : teams) {
            team.update();
        }
    }

    public void render() {
        // JavaFX rendering
        if (graphicsContext == null) {
            return;
        }

        // Clear
        double canvasWidth = gameMap.getWidth() * tileSize;
        double canvasHeight = gameMap.getHeight() * tileSize;
        graphicsContext.setFill(Color.web("#1a1a1a")); // تیره‌تر برای کنتراست بهتر
        graphicsContext.fillRect(0, 0, canvasWidth, canvasHeight);

        // Grid
        graphicsContext.setStroke(Color.web("#333333"));
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
                // موانع: مربع‌های قرمز بزرگ و واضح
                graphicsContext.setFill(Color.web("#FF0000"));
                graphicsContext.fillRect(px + 1, py + 1, tileSize - 2, tileSize - 2);
                // حاشیه تیره
                graphicsContext.setStroke(Color.web("#8B0000"));
                graphicsContext.setLineWidth(2);
                graphicsContext.strokeRect(px + 1, py + 1, tileSize - 2, tileSize - 2);
            } else if (obj instanceof EnergyPoint) {
                // نقاط انرژی: دایره‌های آبی درخشان
                graphicsContext.setFill(Color.web("#00BFFF"));
                graphicsContext.fillOval(px + 4, py + 4, tileSize - 8, tileSize - 8);
                // حاشیه آبی تیره
                graphicsContext.setStroke(Color.web("#0066CC"));
                graphicsContext.setLineWidth(2);
                graphicsContext.strokeOval(px + 4, py + 4, tileSize - 8, tileSize - 8);
            } else if (obj instanceof RestPoint) {
                // نقاط استراحت: مربع‌های بنفش با گوشه‌های گرد
                graphicsContext.setFill(Color.web("#9370DB"));
                graphicsContext.fillRoundRect(px + 2, py + 2, tileSize - 4, tileSize - 4, 8, 8);
                // حاشیه بنفش تیره
                graphicsContext.setStroke(Color.web("#4B0082"));
                graphicsContext.setLineWidth(2);
                graphicsContext.strokeRoundRect(px + 2, py + 2, tileSize - 4, tileSize - 4, 8, 8);
            } else if (obj instanceof Food) {
                // غذا: دایره‌های زرد درخشان
                graphicsContext.setFill(Color.web("#FFD700"));
                graphicsContext.fillOval(px + 6, py + 6, tileSize - 12, tileSize - 12);
                // حاشیه نارنجی
                graphicsContext.setStroke(Color.web("#FF8C00"));
                graphicsContext.setLineWidth(2);
                graphicsContext.strokeOval(px + 6, py + 6, tileSize - 12, tileSize - 12);
            } else if (obj instanceof PowerUp) {
                // آهن‌ربا: مربع‌های صورتی با علامت +
                graphicsContext.setFill(Color.web("#FF69B4"));
                graphicsContext.fillRect(px + 4, py + 4, tileSize - 8, tileSize - 8);
                // علامت + سفید
                graphicsContext.setFill(Color.WHITE);
                graphicsContext.setLineWidth(3);
                double centerX = px + tileSize / 2;
                double centerY = py + tileSize / 2;
                graphicsContext.strokeLine(centerX - 6, centerY, centerX + 6, centerY);
                graphicsContext.strokeLine(centerX, centerY - 6, centerX, centerY + 6);
            } else if (obj instanceof Treasure) {
                Treasure treasure = (Treasure) obj;
                if (treasure.getType() != null && treasure.getType().equals("GOLD")) {
                    // گنج طلا: مربع‌های طلایی درخشان
                    graphicsContext.setFill(Color.web("#FFD700"));
                    graphicsContext.fillRect(px + 2, py + 2, tileSize - 4, tileSize - 4);
                    // حاشیه نارنجی
                    graphicsContext.setStroke(Color.web("#FF8C00"));
                    graphicsContext.setLineWidth(2);
                    graphicsContext.strokeRect(px + 2, py + 2, tileSize - 4, tileSize - 4);
                } else {
                    // گنج نقره: مربع‌های نقره‌ای
                    graphicsContext.setFill(Color.web("#C0C0C0"));
                    graphicsContext.fillRect(px + 2, py + 2, tileSize - 4, tileSize - 4);
                    // حاشیه خاکستری تیره
                    graphicsContext.setStroke(Color.web("#696969"));
                    graphicsContext.setLineWidth(2);
                    graphicsContext.strokeRect(px + 2, py + 2, tileSize - 4, tileSize - 4);
                }
            } else {
                // سایر اشیاء: مربع‌های خاکستری
                graphicsContext.setFill(Color.GRAY);
                graphicsContext.fillRect(px + 8, py + 8, tileSize - 16, tileSize - 16);
            }
        }

        // Crows - بزرگ‌تر و واضح‌تر
        List<Team> teamsSnapshot = getTeams();
        for (Team team : teamsSnapshot) {
            Color teamColor = parseTeamColor(team.getTeamColor());
            for (Crow crow : team.getCrows()) {
                if (crow == null || crow.getPosition() == null) continue;
                Position p = crow.getPosition();
                double cx = p.getX() * tileSize + tileSize / 2.0;
                double cy = p.getY() * tileSize + tileSize / 2.0;

                // کلاغ: دایره‌های بزرگ‌تر
                graphicsContext.setFill(teamColor);
                graphicsContext.fillOval(cx - 12, cy - 12, 24, 24);

                // حاشیه تیره
                graphicsContext.setStroke(Color.BLACK);
                graphicsContext.setLineWidth(2);
                graphicsContext.strokeOval(cx - 12, cy - 12, 24, 24);

                // نشانگر جهت (برای کلاغ بازیکن)
                if (team.getTeamId() == 1 && crow == team.getCrows().get(0)) {
                    graphicsContext.setFill(Color.WHITE);
                    graphicsContext.fillOval(cx - 3, cy - 3, 6, 6);
                }

                // نمایش نوار انرژی بالای کلاغ
                double energyBarWidth = 20;
                double energyBarHeight = 4;
                double energyBarX = cx - energyBarWidth / 2;
                double energyBarY = cy - 18;

                // پس‌زمینه نوار انرژی
                graphicsContext.setFill(Color.DARKGRAY);
                graphicsContext.fillRect(energyBarX, energyBarY, energyBarWidth, energyBarHeight);

                // نوار انرژی بر اساس درصد انرژی
                double energyPercent = Math.max(0, Math.min(1, crow.getEnergy() / 100.0));
                if (energyPercent > 0.5) {
                    graphicsContext.setFill(Color.GREEN);
                } else if (energyPercent > 0.25) {
                    graphicsContext.setFill(Color.YELLOW);
                } else {
                    graphicsContext.setFill(Color.RED);
                }
                graphicsContext.fillRect(energyBarX, energyBarY, energyBarWidth * energyPercent, energyBarHeight);

                // نمایش تعداد گنج‌های کوله‌پشتی
                if (crow.getBackpack() != null && crow.getBackpackTreasureCount() > 0) {
                    graphicsContext.setFill(Color.ORANGE);
                    graphicsContext.setFont(new javafx.scene.text.Font("Arial", 10));
                    graphicsContext.fillText(crow.getBackpackTreasureCount() + "", cx + 8, cy + 8);
                }
            }
        }

        // Reset line width
        graphicsContext.setLineWidth(1);
    }

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

    private void stopAllCrowThreads() {
        for (Thread thread : crowThreads) {
            thread.interrupt();
        }
        crowThreads.clear();
    }

    public void handleInput(KeyCode key) {
        // Player control: move the first crow of the first team with collision/bounds
        if (teams.isEmpty() || teams.get(0).getCrows().isEmpty()) return;
        Crow player = teams.get(0).getCrows().get(0);
        switch (key) {
            case UP:
                tryMoveCrow(player, com.robbercrows.entity.Direction.UP);
                break;
            case DOWN:
                tryMoveCrow(player, com.robbercrows.entity.Direction.DOWN);
                break;
            case LEFT:
                tryMoveCrow(player, com.robbercrows.entity.Direction.LEFT);
                break;
            case RIGHT:
                tryMoveCrow(player, com.robbercrows.entity.Direction.RIGHT);
                break;
            case M:
                // فعال‌سازی آهن‌ربا (اختیاری)
                if (!player.isMagnetActive()) {
                    PowerUp magnet = new PowerUp();
                    magnet.apply(player);
                }
                break;
            case R:
                // شروع/توقف بازی
                if (isGameRunning()) {
                    endGame();
                } else {
                    startGame();
                }
                break;
            default:
                break;
        }
    }

    public void runSimulation() {
        if (!isSimulation) {
            isSimulation = true;
            System.out.println("Simulation started!");
        }
    }

    public void checkWinner() {
        Team winner = null;
        int highestScore = -1;

        // Check for teams that have lost all members
        List<Team> teamsToRemove = new ArrayList<>();

        for (Team team : teams) {
            // Remove dead crows
            List<Crow> deadCrows = new ArrayList<>();
            for (Crow crow : team.getCrows()) {
                if (crow.isDead() || crow.getEnergy() <= 0) {
                    deadCrows.add(crow);
                    if (crow.getEnergy() <= 0) {
                        System.out.println(crow.getName() + " eliminated due to energy depletion!");
                    }
                }
            }
            for (Crow deadCrow : deadCrows) {
                team.removeMember(deadCrow);
            }

            // Check if team has no members left
            if (team.getMemberCount() == 0) {
                teamsToRemove.add(team);
                System.out.println("Team " + team.getTeamId() + " has no members left - eliminated!");
                continue;
            }

            int teamScore = team.getTotalScore();
            if (teamScore > highestScore) {
                highestScore = teamScore;
                winner = team;
            }

            // Add new member when score reaches 50
            if (teamScore >= 50 && team.getMemberCount() < team.getMaxMembers()) {
                addNewTeamMember(team);
            }
        }

        // Remove eliminated teams
        for (Team eliminatedTeam : teamsToRemove) {
            teams.remove(eliminatedTeam);
        }

        // Check if only one team remains
        if (teams.size() == 1) {
            winner = teams.get(0);
            System.out.println("🏆 Winner by elimination: Team #" + winner.getTeamId() + " (" + winner.getTeamColor() + ") - Last team standing! 🏆");
            endGame();
            return;
        }

        // Check if no teams remain
        if (teams.isEmpty()) {
            System.out.println("💀 All teams eliminated - Game Over! 💀");
            endGame();
            return;
        }

        // Check for score-based winner
        if (winner != null && highestScore >= WIN_SCORE) {
            System.out.println("🏆 Winner by score: Team #" + winner.getTeamId() + " (" + winner.getTeamColor() + ") with score: " + highestScore + "! 🏆");
            endGame();
        }
    }

    public Team getWinningTeam() {
        Team winner = null;
        int highestScore = -1;

        for (Team team : teams) {
            int teamScore = team.getTotalScore();
            if (teamScore > highestScore) {
                highestScore = teamScore;
                winner = team;
            }
        }

        if (winner != null && highestScore >= WIN_SCORE) {
            return winner;
        }
        return null;
    }

    private void addNewTeamMember(Team team) {
        if (team.getMemberCount() >= team.getMaxMembers()) return;

        // پیدا کردن موقعیت خالی برای کلاغ جدید
        Position newPos = findEmptyPosition();
        if (newPos != null) {
            Crow newCrow = new Crow("AI-" + team.getTeamColor() + "-" + (team.getMemberCount() + 1), newPos, team);
            team.addMember(newCrow);
            System.out.println("New member added to Team " + team.getTeamId() + " at position " + newPos.getX() + "," + newPos.getY());

            // شروع thread برای کلاغ جدید
            startCrowThread(newCrow);
        }
    }

    private void startCrowThread(Crow crow) {
        Thread crowThread = new Thread(() -> {
            while (isGameRunning.get() && !Thread.currentThread().isInterrupted()) {
                try {
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

    private Position findEmptyPosition() {
        // پیدا کردن موقعیت خالی در نقشه
        for (int x = 0; x < gameMap.getWidth(); x++) {
            for (int y = 0; y < gameMap.getHeight(); y++) {
                Position pos = new Position(x, y);
                boolean isEmpty = true;

                // چک کردن اینکه آیا کلاغی در این موقعیت هست
                for (Team team : teams) {
                    for (Crow crow : team.getCrows()) {
                        if (crow.getPosition() != null && isSameTile(crow.getPosition(), pos)) {
                            isEmpty = false;
                            break;
                        }
                    }
                    if (!isEmpty) break;
                }

                // چک کردن اینکه آیا شیءی در این موقعیت هست
                if (isEmpty) {
                    for (GameObject obj : gameMap.getObjects()) {
                        if (obj != null && obj.isActive() && obj.getPosition() != null && isSameTile(obj.getPosition(), pos)) {
                            isEmpty = false;
                            break;
                        }
                    }
                }

                if (isEmpty) {
                    return pos;
                }
            }
        }
        return new Position(0, 0); // موقعیت پیش‌فرض
    }

    public void switchToSinglePlayer(Crow computerCrow) {
        // Switch to single player mode
        System.out.println("Switching to single player mode");
    }

    public void stopSimulation() {
        if (isSimulation) {
            isSimulation = false;
            System.out.println("Simulation stopped!");
        }
    }

    public List<Team> getTeams() {
        return new ArrayList<>(teams); // Return copy for thread safety
    }

    public void addTeam(Team team) {
        gameLock.lock();
        try {
            teams.add(team);
        } finally {
            gameLock.unlock();
        }
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public boolean isGameRunning() {
        return isGameRunning.get();
    }

    public void setGameRunning(boolean gameRunning) {
        if (gameRunning) {
            startGame();
        } else {
            endGame();
        }
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public void setGameMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    // Renderer wiring
    public void setGraphicsContext(GraphicsContext gc) {
        this.graphicsContext = gc;
    }

    // Movement helpers
    private boolean isSameTile(Position a, Position b) {
        return a != null && b != null && a.getX() == b.getX() && a.getY() == b.getY();
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < gameMap.getWidth() && y < gameMap.getHeight();
    }

    private boolean isMoveBlocked(Crow crow, com.robbercrows.entity.Direction direction) {
        Position pos = crow.getPosition();
        int nx = pos.getX();
        int ny = pos.getY();
        switch (direction) {
            case UP: ny -= 1; break;
            case DOWN: ny += 1; break;
            case LEFT: nx -= 1; break;
            case RIGHT: nx += 1; break;
        }
        if (!isWithinBounds(nx, ny)) return true;
        for (GameObject obj : gameMap.getObjects()) {
            if (obj instanceof Obstacle && obj.isActive()) {
                Position op = obj.getPosition();
                if (op != null && op.getX() == nx && op.getY() == ny) {
                    // Reduce health and energy when hitting obstacle
                    crow.reduceHealth(1);
                    crow.setEnergy(Math.max(0, crow.getEnergy() - 2));
                    System.out.println(crow.getName() + " hit obstacle! Health: " + crow.getHealth() + ", Energy: " + crow.getEnergy());
                    return true;
                }
            }
        }
        return false;
    }

    private void tryMoveCrow(Crow crow, com.robbercrows.entity.Direction direction) {
        if (isMoveBlocked(crow, direction)) return;

        // Consume energy when moving
        crow.setEnergy(Math.max(0, crow.getEnergy() - 1));

        crow.move(direction);
        // Clamp to bounds in case speed > 1
        Position p = crow.getPosition();
        int cx = Math.max(0, Math.min(gameMap.getWidth() - 1, p.getX()));
        int cy = Math.max(0, Math.min(gameMap.getHeight() - 1, p.getY()));
        p.setX(cx);
        p.setY(cy);
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
    public void cleanup() {
        endGame();
        if (crowExecutor != null && !crowExecutor.isShutdown()) {
            crowExecutor.shutdownNow();
        }
    }
}