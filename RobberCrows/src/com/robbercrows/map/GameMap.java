package com.robbercrows.map;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Collections;

public class GameMap {

    //شناسه ها

    //عرض نقشه
    private int width;
    //ارتفاع نقشه
    private int height;
    // لیست همه اشیای نقشه برای مدیریت یکپارچه
    private List<GameObject> objects;

    // Thread safety lock
    private final ReentrantLock mapLock;


    //متد ها
    //سازنده
    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.objects = new ArrayList<>();
        this.mapLock = new ReentrantLock();
    }

    // Default constructor
    public GameMap() {
        this(20, 20); // Default 20x20 map
    }
    // اضافه کردن یه شیء به نقشه
    public void addObject(GameObject obj)
    {
        if (obj != null)
        {
            objects.add(obj);
        }}
    // حذف یه شیء از نقشه
    public void removeObject(GameObject obj)
    {
        objects.remove(obj);
    }
    //ساخت نقشه با ابعاد و آیتم‌های تصادفی
    public void generateMap()
    {
        // اینجا می‌تونی گنج‌ها، غذاها، موانع و ... رو به صورت تصادفی اضافه کنی
    }

    // برگردوندن همه اشیای نقشه برای گرافیک یا شبیه‌سازی
    public List<GameObject> getObjects()
    {
        return new ArrayList<>(objects);
    }
    // برگردوندن اشیای نزدیک یه نقطه (مثل موقعیت کلاغ) و بررسی برخورد
    public List<GameObject> getItemsAround(Position pos)
    {
        List<GameObject> nearby = new ArrayList<>();
        for (GameObject obj : objects) {
            if (obj.isActive() && obj.getPosition().distanceTo(pos) < 2) {
                nearby.add(obj);
            }
        }
        return nearby;
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
