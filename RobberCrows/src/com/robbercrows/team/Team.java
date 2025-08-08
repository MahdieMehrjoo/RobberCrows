package com.robbercrows.team;

import com.robbercrows.entity.Crow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team
{
    //شناسه ها

    // شناسه یکتا برای تیم
    private final int teamId;
    // لیست کلاغ‌های عضو تیم
    private List<Crow> members;
    //رنگ تیم برای نمایش در UI
    private String teamColor;
    // حداکثر تعداد کلاغ‌های مجاز در تیم (مثل ۵)
    private final int maxMembers;
    //امتیاز تیم
    private int totalScore;

    // سازنده
    public Team(int teamId, String teamColor, int maxMembers)
    {
        if (teamId < 0)
            throw new IllegalArgumentException("Team ID must be non-negative");
        if (teamColor == null || teamColor.trim().isEmpty())
            throw new IllegalArgumentException("Team color cannot be null or empty");
        if (maxMembers <= 0)
            throw new IllegalArgumentException("Max members must be positive");

        this.teamId = teamId;
        this.teamColor = teamColor;
        this.maxMembers = maxMembers;
        this.members = new ArrayList<>();
        this.totalScore=0;
    }

    //متد ها
    //اضافه کردن کلاغ به تیم
    public void addMember(Crow crow)
    {
        if (crow == null)
            throw new IllegalArgumentException("Crow cannot be null");
        if (!canAddMember())
            throw new IllegalStateException("Team is full");
        if (members.contains(crow))
            throw new IllegalArgumentException("Crow is already a member of this team");

        members.add(crow);
        crow.setTeam(this);
    }
    // چک کردن وجود کلاغ در تیم
    public boolean containsMember(Crow crow)
    {
        return members.contains(crow);
    }

    //حذف کلاغ از تیم
    public void removeMember(Crow crow)
    {
        if (crow == null)
            throw new IllegalArgumentException("Crow cannot be null");
        if (members.remove(crow))
        {
            crow.setTeam(null);
        }
    }

    // گرفتن حداکثر تعداد اعضای مجاز تیم
    public int getMaxMembers()
    {
        return maxMembers;
    }

    // گرفتن شناسه تیم برای شناسایی
    public int getTeamId()
    {
        return teamId;
    }

    // گرفتن رنگ تیم برای نمایش در UI
    public String getTeamColor()
    {
        return teamColor;
    }

    // گرفتن لیست کلاغ‌های تیم برای نقشه یا UI
    public List<Crow> getMembers()
    {
        return new ArrayList<>(members); // Return a copy to prevent external modification
    }

    // گرفتن تعداد کلاغ‌های تیم
    public int getMemberCount()
    {
        return members.size();
    }

    // چک کردن امکان اضافه کردن کلاغ جدید
    public boolean canAddMember()
    {
        return members.size() < maxMembers;
    }

    // اضافه کردن امتیاز به امتیاز کل تیم
    public void addTotalScore(int points)
    {
        if (points < 0)
        {
            throw new IllegalArgumentException("Points must be non-negative");
        }
        this.totalScore += points;
        System.out.println("Team " + teamId + " gained " + points + " points, totalScore=" + totalScore);
    }

    // گرفتن امتیاز کل تیم
    public int getTotalScore()
    {
        return totalScore;
    }
    //امتیاز تیم رو برای شروع مجدد تیم صفر میکنه
    public void resetTotalScore()
    {
        this.totalScore = 0;
        System.out.println("Team " + teamId + " score reset to 0");
    }

    // چک کردن خالی بودن تیم (عدم وجود هیچ عضوی)
    public boolean isEmpty()
    {
        return members.isEmpty();
    }

    //برای ریست تیم
    public void clearMembers()
    {
        for (Crow crow : members)
        {
            crow.setTeam(null);
        }
        members.clear();
    }
    // نمایش اطلاعات تیم برای دیباگ
    @Override
    public String toString()
    {
        return "Team{" +
                "teamId=" + teamId +
                ", teamColor='" + teamColor + '\'' +
                ", memberCount=" + getMemberCount() +
                ", maxMembers=" + maxMembers +
                '}';
    }

    // مقایسه تیم‌ها بر اساس teamId
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Team team = (Team) obj;
        return teamId == team.teamId;
    }

    // تولید کد هش برای تیم
    @Override
    public int hashCode()
    {
        return Objects.hash(teamId);
    }
}
