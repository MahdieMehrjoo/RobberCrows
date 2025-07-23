package entity;

import java.util.ArrayList;
import java.util.List;

// کلاس کوله‌پشتی برای ذخیره گنج‌های جمع‌آوری‌شده توسط کلاغ
public class Backpack {
    // لیست گنج‌های داخل کوله‌پشتی
    private List<Treasure> treasures = new ArrayList<>();
    // وزن فعلی کوله‌پشتی
    private float currentWeight = 0;
    // حداکثر وزن مجاز کوله‌پشتی
    private float maxWeight = 10;
    // حجم فعلی کوله‌پشتی
    private float currentVolume = 0;
    // حداکثر حجم مجاز کوله‌پشتی
    private float maxVolume = 10;

    // سازنده پیش‌فرض کوله‌پشتی
    public Backpack() { }

    // اضافه کردن گنج به کوله‌پشتی (در صورت کافی بودن وزن و حجم)
    public void addTreasure(Treasure treasure) {
        if (currentWeight + treasure.getWeight() <= maxWeight && currentVolume + treasure.getVolume() <= maxVolume) {
            treasures.add(treasure);
            currentWeight += treasure.getWeight();
            currentVolume += treasure.getVolume();
        }
    }

    // محاسبه مجموع ارزش گنج‌های داخل کوله‌پشتی
    public int getTotalValue() {
        int sum = 0;
        for (Treasure t : treasures) {
            sum += t.getValue();
        }
        return sum;
    }

    // گرفتن لیست گنج‌های داخل کوله‌پشتی
    public List<Treasure> getTreasures() {
        return treasures;
    }
}