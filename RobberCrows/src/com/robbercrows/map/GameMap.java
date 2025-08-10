package com.robbercrows.map;
import com.robbercrows.entity.Treasure;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameMap {

    //شناسه ها

    //عرض نقشه
    private int width;
    //ارتفاع نقشه
    private int height;
    // لیست همه اشیای نقشه برای مدیریت یکپارچه
    private List<GameObject> objects;

    private Random random = new Random();

    // تنظیمات پیش‌فرض ساخت نقشه
    private static final int ENERGY_POINT_COUNT = 5;
    private static final int ENERGY_POINT_VALUE = 20;
    private static final int OBSTACLE_COUNT = 8;
    private static final int SILVER_TREASURE_COUNT = 6;
    private static final int GOLD_TREASURE_COUNT = 3;
    private static final int SILVER_VALUE = 10;
    private static final int GOLD_VALUE = 25;
    private static final float DEFAULT_TREASURE_WEIGHT = 1.0f;
    private static final float DEFAULT_TREASURE_VOLUME = 1.0f;

    //متد ها
    //سازنده
    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.objects = new ArrayList<>();
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
        objects.clear(); // اول همه اشیا رو پاک کن

        // انرژی پوینت‌ها
        for (int i = 0; i < ENERGY_POINT_COUNT; i++) {
            EnergyPoint ep = new EnergyPoint(randomPosition(), ENERGY_POINT_VALUE);
            addObject(ep);
        }

        // موانع
        for (int i = 0; i < OBSTACLE_COUNT; i++) {
            Obstacle obstacle = new Obstacle(randomPosition(), "ROCK", false, 15);
            addObject(obstacle);
        }

        // گنج‌های نقره‌ای
        for (int i = 0; i < SILVER_TREASURE_COUNT; i++) {
            Treasure silver = new Treasure(
                    "SILVER",
                    SILVER_VALUE,
                    randomPosition(),
                    DEFAULT_TREASURE_WEIGHT,
                    DEFAULT_TREASURE_VOLUME,
                    SILVER_VALUE
            );
            addObject(silver);
        }

        // گنج‌های طلایی
        for (int i = 0; i < GOLD_TREASURE_COUNT; i++) {
            Treasure gold = new Treasure(
                    "GOLD",
                    GOLD_VALUE,
                    randomPosition(),
                    DEFAULT_TREASURE_WEIGHT,
                    DEFAULT_TREASURE_VOLUME,
                    GOLD_VALUE
            );
            addObject(gold);
        }
    }

    private Position randomPosition() {
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        return new Position(x, y);
    }

    // متد addObject قبلاً تعریف شده است؛ نسخهٔ تکراری حذف شد

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
