package com.robbercrows.entity;

import com.robbercrows.map.GameObject;
import com.robbercrows.map.Position;

// کلاس گنج که اشیای قابل جمع‌آوری را مدل می‌کند
public class Treasure extends GameObject implements Collectible {
    // نوع گنج (مثلاً طلا، نقره و ...)
    private String type;
    // ارزش گنج
    private int value;
    // وزن گنج
    private float weight;
    // حجم گنج
    private float volume;
    // امتیاز گنج (در صورت نیاز)
    private int score;

    // سازنده گنج
    public Treasure (String type, int value , Position position, float weight, float volume, int score) {
        super(position, true);
        this.type = type;
        this.value = value;
        this.weight = weight;
        this.volume = volume;
        this.score = score;
    }

    // متد جمع‌آوری گنج توسط کلاغ
    // پیاده‌سازی در انتهای کلاس با مدیریت null و پیام دیباگ قرار دارد

    // گرفتن ارزش گنج
    public int getValue() {
        return value;
    }
    // گرفتن نوع گنج
    public String getType() {
        return type;
    }
    // گرفتن وزن گنج
    public float getWeight() {
        return weight;
    }
    // گرفتن حجم گنج
    public float getVolume() { return  volume; }
    // گرفتن امتیاز گنج
    public int getScore() {
        return score;
    }
   
    // پیاده‌سازی متد جمع‌آوری از Collectible interface
    @Override
    public void onCollect(Crow crow)
    {
        if (crow != null)
        {
            crow.collectTreasure(this);
            System.out.println("Treasure " + type + " collected by " + crow.getName());
        }
    }

    // به‌روزرسانی وضعیت گنج (معمولاً خالی است)
    @Override
    public void update() {
        // No-op for static treasure
    }

    // نمایش اطلاعات گنج (برای تست/دیباگ)
    @Override
    public void render() {
        System.out.println("Treasure [type=" + type + ", value=" + value + ", position=(" + getPosition().getX() + ", " + getPosition().getY() + ")] ");
    }

    // تعامل با کلاغ: وقتی نزدیک شود، جمع‌آوری و غیرفعال شود
    @Override
    public void interact(Crow crow) {
        if (isActive() && crow != null) {
            onCollect(crow);
            setActive(false);
        }
    }
}