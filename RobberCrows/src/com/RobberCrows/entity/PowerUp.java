package com.RobberCrows.entity;

// کلاس انتزاعی قدرت ویژه (PowerUp) برای کلاغ
public abstract class PowerUp {
    // نوع قدرت ویژه (مثلاً پنیر، آهن‌ربا و ...)
    protected String type;
    // مدت زمان اثر قدرت ویژه
    protected int duration;

    // سازنده قدرت ویژه
    public PowerUp (String type, int duration)  {
        this.type = type;
        this.duration = duration;
    }

    // متد اعمال قدرت ویژه روی کلاغ (باید در کلاس فرزند پیاده‌سازی شود)
    public abstract void apply(Crow crow);

    // متد حذف اثر قدرت ویژه از کلاغ (باید در کلاس فرزند پیاده‌سازی شود)
    public abstract void remove(Crow crow);
}