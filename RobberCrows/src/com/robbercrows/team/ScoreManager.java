package com.robbercrows.team;

import java.util.Map;

public class ScoreManager
{
    //شناسه ها

    // نقشه‌ای برای ذخیره امتیاز هر تیم
    private Map<Team, Integer> teamScores;
    //بالاترین امتیاز کل بازی
    private int highScore;
    // آستانه امتیاز برای اضافه کردن کلاغ جدید
    private int scoreThreshold;

    //متد ها
    //اضافه کردن امتیاز به تیم
    public void addScore(Team team, int score)
    {

    }
    // محاسبه و گرفتن امتیاز کل تیم
    public int calculateTeamScore(Team team)
    {

    }
    // به‌روزرسانی بالاترین امتیاز اگه امتیاز تیم بیشتر بشه
    public void updateHighScore()
    {

    }
    // چک کردن اینکه آیا امتیاز تیم به آستانه رسیده یا نه (برای کلاغ جدید)
    public boolean isThresholdReached(Team team)
    {

    }
    // گرفتن بالاترین امتیاز کل بازی (برای نمایش یا مقایسه)
    public int getHighScore()
    {


    }
    // ریست کردن همه امتیازات (مثل شروع دوباره بازی)
    public void resetScores()
    {

    }
}
}
