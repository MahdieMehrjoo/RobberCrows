package com.robbercrows.map;

import com.robbercrows.entity.Crow;

public class RestPoint extends GameObject
{
    //شناسه ها
    //مدت زمان استراحت کلاغ برای خالی کردن کوله‌پشتی
    private int restDuration;
    // نشون می‌ده که آیا نقطه استراحت توسط کلاغ اشغال شده یا نه
    private boolean isOccupied;

    //متد ها
    // به‌روزرسانی وضعیت نقطه استراحت (مثل چک کردن تایمر استراحت)
    @Override
    public void update()
    {

    }
    // نمایش گرافیکی نقطه استراحت
    @Override
    public void render()
    {

    }
    // تعامل نقطه استراحت با کلاغ (مثل شروع استراحت برای خالی کردن کوله‌پشتی)
    @Override
    public void interact(Crow crow)
    {

    }
    // شروع استراحت کلاغ
    public void startRest(Crow crow)
    {

    }
    // پایان استراحت کلاغ
    public void endRest()
    {

    }
}
