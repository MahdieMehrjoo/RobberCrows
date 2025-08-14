package com.robbercrows.entity;

import com.robbercrows.map.GameObject;
import com.robbercrows.map.Position;

// کلاس غذا مخصوص "پنیر" که سرعت کلاغ را 10 ثانیه دوبرابر می‌کند
public class Food extends GameObject implements Collectible {
    // مدت زمان اثر پنیر به میلی‌ثانیه (10 ثانیه)
    private final long durationMs;

    public Food() {
        super(new Position(0, 0), true);
        this.durationMs = 10_000L;
    }

    public Food(Position position) {
        super(position, true);
        this.durationMs = 10_000L;
    }

    public Food(long durationMs) {
        super(new Position(0, 0), true);
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
        setActive(false); // غذا بعد از جمع‌آوری غیرفعال می‌شود
    }

    @Override
    public void interact(Crow crow) {
        if (isActive() && crow != null) {
            onCollect(crow);
        }
    }
}