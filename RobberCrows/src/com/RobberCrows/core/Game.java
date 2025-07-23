package com.RobberCrows.core;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import com.RobberCrows.map.GameMap;
import com.RobberCrows.team.ScoreManager;
import com.RobberCrows.team.Team;

import java.util.List;

public class Game {
    //شناسه ها


    private GameMap gameMap;
    private List<Team> teams;
    private ScoreManager scoreManager;
    private boolean isGameRunning;
    private AnimationTimer timer;
    private boolean isSimulation;
    private List<Thread> crowThreads;


    //متد ها
    public void startGame()
    {

    }
    public void update()
    {

    }
    public void render()
    {

    }
    public void endGame()
    {

    }
    public void handleInput(KeyCode key)
    {

    }
    public void runSimulation()
    {

    }
    public void checkWinner()
    {

    }
    public void switchToSinglePlayer(Crow computerCrow)
    {

    }
    public void stopSimulation()
    {

    }
    public List<Team> getTeams()
    {

    }
    public ScoreManager getScoreManager()
    {

    }

    public boolean isGameRunning()
    {

    }
    public void setGameRunning(boolean gameRunning)
    {

    }
    public static void main(String[] args)
    {
    }
}