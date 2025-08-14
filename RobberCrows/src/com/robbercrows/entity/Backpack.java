package com.robbercrows.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Collections;

// کلاس کوله‌پشتی برای ذخیره گنج‌های جمع‌آوری‌شده توسط کلاغ
public class Backpack {
    // لیست گنج‌های داخل کوله‌پشتی
    private List<Treasure> treasures = new ArrayList<>();
    // وزن فعلی کوله‌پشتی
    private float currentWeight = 0;
    // حداکثر وزن مجاز کوله‌پشتی
    private float maxWeight = 20;
    // حجم فعلی کوله‌پشتی
    private float currentVolume = 0;
    // حداکثر حجم مجاز کوله‌پشتی
    private float maxVolume = 20;

    // Thread safety lock
    private final ReentrantLock backpackLock;

    // سازنده پیش‌فرض کوله‌پشتی
    public Backpack() {
        this.backpackLock = new ReentrantLock();
    }

    // اضافه کردن گنج به کوله‌پشتی (در صورت کافی بودن وزن و حجم)
    public void addTreasure(Treasure treasure) {
        backpackLock.lock();
        try {
            if (currentWeight + treasure.getWeight() <= maxWeight && currentVolume + treasure.getVolume() <= maxVolume) {
                treasures.add(treasure);
                currentWeight += treasure.getWeight();
                currentVolume += treasure.getVolume();
            }
        } finally {
            backpackLock.unlock();
        }
    }

    // محاسبه مجموع ارزش گنج‌های داخل کوله‌پشتی
    public int getTotalValue() {
        backpackLock.lock();
        try {
            int sum = 0;
            for (Treasure t : treasures) {
                sum += t.getValue();
            }
            return sum;
        } finally {
            backpackLock.unlock();
        }
    }

    // گرفتن لیست گنج‌های داخل کوله‌پشتی
    public List<Treasure> getTreasures() {
        backpackLock.lock();
        try {
            return new ArrayList<>(treasures); // Return copy for thread safety
        } finally {
            backpackLock.unlock();
        }
    }
}