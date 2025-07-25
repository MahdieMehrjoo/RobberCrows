package com.robbercrows.team;

import com.robbercrows.entity.Crow;

import java.util.List;

public class Team
{
    //شناسه ها

    // شناسه یکتا برای تیم (مثل ۱ برای تیم ۱)
    private final int teamId;
    // لیست کلاغ‌های عضو تیم
    private List<Crow> members;
    // رنگ تیم برای نمایش در UI (مثل "red" یا "blue")
    private String teamColor;
    // مختصات x پایگاه تیم در نقشه
    private int baseX;
    // مختصات y پایگاه تیم در نقشه
    private int baseY;
    // وضعیت فعال بودن تیم (مثل true برای فعال)
    private boolean isActive;
    // حداکثر تعداد کلاغ‌های مجاز در تیم (مثل ۵)
    private int maxMembers;

    //متد ها
    //اضافه کردن کلاغ به تیم
    public void addMember(Crow crow)
    {

    }
    //حذف کلاغ از تیم
    public void removeMember(Crow crow)
    {

    }
    // گرفتن شناسه تیم برای شناسایی
    public int getTeamId()
    {

    }
    // گرفتن رنگ تیم برای نمایش در UI
    public String getTeamColor()
    {

    }
    // گرفتن لیست کلاغ‌های تیم برای نقشه یا UI
    public List<Crow> getMembers()
    {

    }
    // گرفتن مختصات x پایگاه تیم
    public int getBaseX()
    {

    }
    // گرفتن مختصات y پایگاه تیم
    public int getBaseY()
    {

    }
    // چک کردن فعال بودن تیم
    public boolean isActive()
    {

    }
    // غیرفعال کردن تیم (مثل وقتی کلاغ‌ها تموم می‌شن)
    public void deactivateTeam()
    {

    }
    // گرفتن تعداد کلاغ‌های تیم
    public int getMemberCount()
    {

    }
    // چک کردن امکان اضافه کردن کلاغ جدید
    public boolean canAddMember()
    {

    }
    // نمایش اطلاعات تیم برای دیباگ
    @Override
    public String toString()
    {

    }
    // مقایسه تیم‌ها بر اساس teamId
    @Override
    public boolean equals(Object obj)
    {

    }
    // تولید کد هش برای تیم
    @Override
    public int hashCode()
    {

    }
}
