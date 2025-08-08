package com.robbercrows.team;

import java.util.HashMap;
import java.util.Map;

public class ScoreManager {
    //شناسه ها

    // بالاترین امتیاز کل بازی
    private int highScore;
    // آستانه امتیاز برای اضافه کردن کلاغ جدید
    private final int scoreThreshold;

    //متد ها

    // سازنده
    public ScoreManager(int scoreThreshold)
    {
        if (scoreThreshold <= 0)
        {
            throw new IllegalArgumentException("Score threshold must be positive");
        }
        this.highScore = 0;
        this.scoreThreshold = scoreThreshold;
    }

    // اضافه کردن امتیاز به تیم
    public void addScore(Team team, int score)
    {
        if (team == null)
        {
            throw new IllegalArgumentException("Team cannot be null");
        }
        if (score < 0)
        {
            throw new IllegalArgumentException("Score must be non-negative");
        }

        team.addTotalScore(score);
        updateHighScore(team);
    }

    // محاسبه و گرفتن امتیاز کل تیم
    public int calculateTeamScore(Team team)
    {
        if (team == null)
        {
            throw new IllegalArgumentException("Team cannot be null.");
        }
        return team.calculateScore();
    }

    // به‌روزرسانی بالاترین امتیاز اگه امتیاز تیم بیشتر بشه
    public void updateHighScore(Team team)
    {
        int teamScore = team.calculateScore();
        if (teamScore > highScore)
        {
            highScore = teamScore;
        }
    }

    // چک کردن اینکه آیا امتیاز تیم به آستانه رسیده یا نه
    public boolean isThresholdReached(Team team)
    {
        return calculateTeamScore(team) >= scoreThreshold;
    }

    // گرفتن بالاترین امتیاز کل بازی
    public int getHighScore()
    {
        return highScore;
    }

    // ریست کردن همه امتیازات (مثل شروع دوباره بازی)
    public void resetScores()
    {
        highScore = 0;
    }

}

