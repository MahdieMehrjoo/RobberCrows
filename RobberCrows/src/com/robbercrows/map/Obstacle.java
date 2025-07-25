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

    //متد ها
    // چک می‌کنه مانع جلوی حرکت کلاغ رو می‌گیره یا نه
    public boolean blockMovement()
    {

    }
    // به‌روزرسانی وضعیت مانع (ممکنه خالی باشه چون مانع ثابته)
    @Override
    public void update()
    {

    }
    // نمایش گرافیکی مانع
    @Override
    public void render()
    {

    }
    // تعامل مانع با کلاغ (مثل کم کردن جون یا توقف حرکت)
    @Override
    public void interact(Crow crow)
    {

    }

}
