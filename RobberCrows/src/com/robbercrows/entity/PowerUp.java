package com.robbercrows.entity;

import com.robbercrows.map.GameObject;
import com.robbercrows.map.Position;

// کلاس پاورآپ مخصوص "آهن‌ربا"
public class PowerUp extends GameObject implements Collectible {

    // مدت زمان اثر به ثانیه (برای آهن‌ربا: 10 ثانیه)
    private final int durationSeconds;
    // شعاع جذب به واحد خانه/تایل (پیشنهادی: 3 خانه)
    private final int magnetRadius;

    // سازنده پاورآپ آهن‌ربا
    public PowerUp() {
        super(new Position(0, 0), true);
        this.durationSeconds = 10;
        this.magnetRadius = 3;
    }

    // سازنده با مقداردهی دستی (در صورت نیاز)
    public PowerUp(Position position) {
        super(position, true);
        this.durationSeconds = 10;
        this.magnetRadius = 3;
    }

    public PowerUp(int durationSeconds, int magnetRadius) {
        super(new Position(0, 0), true);
        this.durationSeconds = durationSeconds;
        this.magnetRadius = magnetRadius;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public int getMagnetRadius() {
        return magnetRadius;
    }

    // اعمال اثر آهن‌ربا روی کلاغ (فعال‌سازی موقت)
    public void apply(Crow crow) {
        if (crow == null) return;
        // فعال‌سازی آهن‌ربا برای مدت مشخص
        crow.activateMagnet(magnetRadius, durationSeconds * 1000L);
    }

    // حذف اثر آهن‌ربا (در صورت نیاز به خاموش کردن زودتر از موعد)
    public void remove(Crow crow) {
        if (crow == null) return;
        crow.deactivateMagnet();
    }

    @Override
    public void onCollect(Crow crow) {
        if (crow == null) return;
        apply(crow);
        setActive(false); // پاورآپ بعد از استفاده غیرفعال می‌شود
    }

    @Override
    public void interact(Crow crow) {
        if (isActive() && crow != null) {
            onCollect(crow);
        }
    }
}