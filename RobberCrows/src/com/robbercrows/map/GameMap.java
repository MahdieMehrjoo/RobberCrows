package com.robbercrows.map;
import java.util.List;

public class GameMap {

    //شناسه ها

    //عرض نقشه
    private int width;
    //ارتفاع نقشه
    private int height;
    // لیست همه اشیای نقشه برای مدیریت یکپارچه
    private List<GameObject> objects;


    //متد ها
    // اضافه کردن یه شیء به نقشه
    public void addObject(GameObject obj)
    {

    }
    // حذف یه شیء از نقشه
    public void removeObject(GameObject obj)
    {

    }
    //ساخت نقشه با ابعاد و آیتم‌های تصادفی
    public void generateMap()
    {

    }
    // برگردوندن همه اشیای نقشه برای گرافیک یا شبیه‌سازی
    public List<GameObject> getObjects()
    {

    }
    // برگردوندن اشیای نزدیک یه نقطه (مثل موقعیت کلاغ) و بررسی برخورد
    public List<GameObject> getItemsAround(Position pos)
    {

    }
}
