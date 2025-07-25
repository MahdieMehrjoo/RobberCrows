package com.robbercrows.map;

import com.robbercrows.entity.Crow;

public class GameObject
{
    //شناسه ها

    // نشون می‌ده شیء فعاله (مثل غذای جمع‌نشده) یا غیرفعال (مثل غذای جمع‌شده)
    private boolean isActive;
    // موقعیت شیء تو نقشه
    private Position position;

    //متد ها
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
