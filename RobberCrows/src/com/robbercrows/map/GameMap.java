package com.robbercrows.map;
import com.robbercrows.entity.Treasure;
import com.robbercrows.entity.Food;
import com.robbercrows.entity.PowerUp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class GameMap {

    //شناسه ها

    //عرض نقشه
    private int width;
    //ارتفاع نقشه
    private int height;
    // لیست همه اشیای نقشه برای مدیریت یکپارچه
    private List<GameObject> objects;

    private Random random = new Random();
    private final ReentrantLock mapLock;

    // تنظیمات پیش‌فرض ساخت نقشه
    private static final int ENERGY_POINT_COUNT = 6;
    private static final int ENERGY_POINT_VALUE = 20;
    private static final int OBSTACLE_COUNT = 6;
    private static final int FOOD_COUNT = 4;
    private static final int POWERUP_COUNT = 3;
    private static final int SILVER_TREASURE_COUNT = 12;
    private static final int GOLD_TREASURE_COUNT = 6;
    private static final int SILVER_VALUE = 15;
    private static final int GOLD_VALUE = 35;
    private static final float DEFAULT_TREASURE_WEIGHT = 1.0f;
    private static final float DEFAULT_TREASURE_VOLUME = 1.0f;

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
        this(16, 16); // Default 16x16 map instead of 20x20
    }
    // اضافه کردن یه شیء به نقشه
    public void addObject(GameObject obj)
    {
        if (obj == null) return;
        mapLock.lock();
        try {
            objects.add(obj);
        } finally {
            mapLock.unlock();
        }
    }
    // حذف یه شیء از نقشه
    public void removeObject(GameObject obj)
    {
        mapLock.lock();
        try {
            objects.remove(obj);
        } finally {
            mapLock.unlock();
        }
    }
    //ساخت نقشه با ابعاد و آیتم‌های تصادفی
    public void generateMap()
    {
        mapLock.lock();
        try {
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

            // غذاها (پنیر)
            for (int i = 0; i < FOOD_COUNT; i++) {
                Food food = new Food(randomPosition());
                addObject(food);
            }

            // پاورآپ‌ها (آهن‌ربا)
            for (int i = 0; i < POWERUP_COUNT; i++) {
                PowerUp powerUp = new PowerUp(randomPosition());
                addObject(powerUp);
            }

            // نقاط استراحت
            for (int i = 0; i < 4; i++) {
                RestPoint restPoint = new RestPoint(randomPosition());
                addObject(restPoint);
            }

        } finally {
            mapLock.unlock();
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
        mapLock.lock();
        try {
            return new ArrayList<>(objects);
        } finally {
            mapLock.unlock();
        }
    }
    // برگردوندن اشیای نزدیک یه نقطه (مثل موقعیت کلاغ) و بررسی برخورد
    public List<GameObject> getItemsAround(Position pos)
    {
        mapLock.lock();
        try {
            List<GameObject> nearby = new ArrayList<>();
            for (GameObject obj : objects) {
                if (obj.isActive() && obj.getPosition().distanceTo(pos) < 2) {
                    nearby.add(obj);
                }
            }
            return nearby;
        } finally {
            mapLock.unlock();
        }
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
