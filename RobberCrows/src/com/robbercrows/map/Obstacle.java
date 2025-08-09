package com.robbercrows.map;

import com.robbercrows.entity.Crow;

public class Obstacle extends GameObject
{
    //شناسه ها

    // نوع مانع
    private String type;
    // نشون می‌ده کلاغ می‌تونه از مانع رد بشه یا نه
    private boolean isPassable;
    //شدت آسیب به جون کلاغ
    private int damage;

    // سازنده
    public Obstacle(Position position, String type, boolean isPassable, int damage)
    {
        super(position, true); // موانع معمولاً فعال هستند
        if (type == null)
            throw new IllegalArgumentException("Obstacle type cannot be null");
        if (damage < 0)
            throw new IllegalArgumentException("Damage must be non-negative");
        this.type = type;
        this.isPassable = isPassable;
        this.damage = damage;
    }

    // گرفتن نوع مانع
    public String getType()
    {
        return type;
    }

    //ست کردن نوع مانع
    public void setType(String type)
    {
        if (type == null)
            throw new IllegalArgumentException("Obstacle type cannot be null");
        this.type = type;
    }

    // گرفتن وضعیت عبورپذیری
    public boolean isPassable()
    {
        return isPassable;
    }

    // تنظیم وضعیت عبورپذیری
    public void setPassable(boolean passable)
    {
        this.isPassable = passable;
    }

    // گرفتن شدت آسیب
    public int getDamage()
    {
        return damage;
    }

    // تنظیم شدت آسیب
    public void setDamage(int damage)
    {
        if (damage < 0)
            throw new IllegalArgumentException("Damage must be non-negative");
        this.damage = damage;
    }

    // چک می‌کنه مانع جلوی حرکت کلاغ رو می‌گیره یا نه
    public boolean blockMovement()
    {
        return !isPassable;
    }
    // به‌روزرسانی وضعیت مانع (خالی چون مانع ثابته)
    @Override
    public void update()
    {
        // نیازی به به‌روزرسانی نیست
    }
    // نمایش گرافیکی مانع
    @Override
    public void render()
    {
        // TODO: کد نمایش گرافیکی اینجا نوشته میشود
        System.out.println("Obstacle [type=" + type + ", position=(" + getPosition().getX() + "," + getPosition().getY() + "), passable=" + isPassable + ", damage=" + damage + "]");
    }
    //تعامل مانع با کلاغ
    @Override
    public void interact(Crow crow)
    {
        if (damage == 0 || crow == null || isPassable)
            return;

        crow.reduceHealth(damage);
        System.out.println("Crow hit obstacle and lost " + damage + " health!");
    }
}

