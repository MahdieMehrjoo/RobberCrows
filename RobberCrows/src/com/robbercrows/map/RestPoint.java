package com.robbercrows.map;

import com.robbercrows.entity.Crow;
import com.robbercrows.team.Team;

public class RestPoint extends GameObject
{
    //شناسه ها
    //مدت زمان استراحت کلاغ برای خالی کردن کوله‌پشتی
    private int restDuration;
    // نشون می‌ده که آیا نقطه استراحت توسط کلاغ اشغال شده یا نه
    private boolean isOccupied;
    // شمارنده ساده برای شبیه‌سازی تایمر استراحت
    private int restCounter;
    // کلاغی که در حال استراحت است
    private Crow restingCrow;

    // سازنده
    public RestPoint(Position position, int restDuration)
    {
        super(position, true);
        if (restDuration < 0)
            throw new IllegalArgumentException("Rest duration must be non-negative");
        this.restDuration = restDuration;
        this.isOccupied = false;
        this.restCounter = 0;
        this.restingCrow = null;
    }

    // سازنده پیش‌فرض با 20 ثانیه
    public RestPoint(Position position)
    {
        this(position, 20); // 20 ثانیه به جای 120
    }

    // گرفتن مدت زمان استراحت
    public int getRestDuration()
    {
        return restDuration;
    }

    // تنظیم مدت زمان استراحت
    public void setRestDuration(int restDuration)
    {
        if (restDuration < 0)
            throw new IllegalArgumentException("Rest duration must be non-negative");
        this.restDuration = restDuration;
    }

    // گرفتن وضعیت اشغال بودن
    public boolean isOccupied()
    {
        return isOccupied;
    }

    // تنظیم وضعیت اشغال بودن
    public void setOccupied(boolean occupied)
    {
        this.isOccupied = occupied;
    }

    // شروع استراحت کلاغ
    public void startRest(Crow crow)
    {
        if (crow == null) {
            throw new IllegalArgumentException("Crow cannot be null");
        }
        isOccupied = true;
        restCounter = restDuration;
        restingCrow = crow;
        System.out.println("Crow started resting at RestPoint for " + restDuration + " ticks.");
    }

    // پایان استراحت کلاغ
    public void endRest()
    {
        isOccupied = false;
        if (restingCrow != null)
        {
            // Add backpack value to the team score, then empty backpack
            int totalValue = restingCrow.getBackpack() != null ? restingCrow.getBackpack().getTotalValue() : 0;
            Team team = restingCrow.getTeam();
            if (team != null && totalValue > 0) {
                team.addTotalScore(totalValue);
            }
            restingCrow.emptyBackpack();
            System.out.println("Crow finished resting and emptied backpack at RestPoint.");
            restingCrow = null;
        }
    }

    // به‌روزرسانی وضعیت نقطه استراحت (مثل چک کردن تایمر استراحت)
    @Override
    public void update()
    {
        if (isOccupied && restCounter > 0)
        {
            restCounter--;
            if (restCounter == 0)
            {
                endRest();
            }
        }
    }

    // نمایش گرافیکی نقطه استراحت
    @Override
    public void render()
    {
        // TODO: کد نمایش گرافیکی اینجا نوشته می‌شه
        System.out.println("RestPoint [position=(" + getPosition().getX() + ", " + getPosition().getY() + "), occupied=" + isOccupied + ", restDuration=" + restDuration + "]");
    }

    // تعامل نقطه استراحت با کلاغ (مثل شروع استراحت برای خالی کردن کوله‌پشتی)
    @Override
    public void interact(Crow crow)
    {
        if (!isOccupied && crow != null)
        {
            startRest(crow);
        }
    }
    //for debug
    @Override
    public String toString() {
        return "RestPoint[position=(" + getPosition().getX() + ", " + getPosition().getY() + "), occupied=" + isOccupied + ", restDuration=" + restDuration + "]";
    }
}
