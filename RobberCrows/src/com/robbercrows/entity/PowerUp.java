package com.robbercrows.entity;

// کلاس پاورآپ مخصوص "آهن‌ربا"
public class PowerUp {

    // مدت زمان اثر به ثانیه (برای آهن‌ربا: 10 ثانیه)
    private final int durationSeconds;
    // شعاع جذب به واحد خانه/تایل (پیشنهادی: 3 خانه)
    private final int magnetRadius;

    // سازنده پاورآپ آهن‌ربا
    public PowerUp() {
        this.durationSeconds = 10;
        this.magnetRadius = 3;
    }

    // سازنده با مقداردهی دستی (در صورت نیاز)
    public PowerUp(int durationSeconds, int magnetRadius) {
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
}