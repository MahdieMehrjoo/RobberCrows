package com.robbercrows.entity;

// کلاس غذا مخصوص "پنیر" که سرعت کلاغ را 10 ثانیه دوبرابر می‌کند
public class Food implements Collectible {
    // مدت زمان اثر پنیر به میلی‌ثانیه (10 ثانیه)
    private final long durationMs;

    public Food() {
        this.durationMs = 10_000L;
    }

    public Food(long durationMs) {
        this.durationMs = durationMs;
    }

    public long getDurationMs() {
        return durationMs;
    }

    // متد جمع‌آوری: اعمال اثر افزایش سرعت موقت روی کلاغ
    @Override
    public void onCollect(Crow crow) {
        if (crow == null) return;
        crow.collectFood(this);
    }
}