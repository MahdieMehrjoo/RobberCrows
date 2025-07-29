package com.robbercrows.map;

import com.robbercrows.entity.Crow;

public abstract class GameObject
{
    //شناسه ها

    // نشون می‌ده شیء فعاله (مثل غذای جمع‌نشده) یا غیرفعال (مثل غذای جمع‌شده)
    private boolean isActive;
    // موقعیت شیء تو نقشه
    private Position position;

    //متد ها

    //سازنده
    public GameObject(Position position,boolean isActive)
    {
        this.position=position;
        this.isActive=isActive;
    }
    //گرفتن وضعیت فعال بودن شیء
    public boolean isActive()
    {
        return isActive;
    }

    //تنظیم وضعیت فعال بودن شیء
    public void setActive(boolean active)
    {
        this.isActive = active;
    }

    //گرفتن موقعیت شیء
    public Position getPosition()
    {
        return position;
    }

    //تنظیم موقعیت شیء
    public void setPosition(Position position)
    {
        if (position == null)
        {
            throw new IllegalArgumentException("Position cannot be null");
        }
        this.position = position;
    }

    // به‌روزرسانی وضعیت شیء
    public void update()
    {

    }
    //نمایش گرافیکی شیء
    public void render()
    {

    }
    // تعامل شیء با کلاغ (مثل دادن امتیاز یا افزایش سرعت)
    public void interact(Crow crow)
    {

    }
}
