package com.robbercrows.core;

import com.robbercrows.entity.Crow;
import com.robbercrows.map.GameMap;
import com.robbercrows.team.ScoreManager;
import com.robbercrows.team.Team;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

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
}