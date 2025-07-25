package com.robbercrows.entity;

// کلاس غذا که اشیای قابل جمع‌آوری برای افزایش انرژی کلاغ است
public class Food implements Collectible {
    // مقدار انرژی که این غذا به کلاغ می‌دهد
    private int nutrition;

    // سازنده غذا
    public Food (int nutrition){
        this.nutrition = nutrition;
    }

    // گرفتن مقدار انرژی غذا
    public int getNutrition() {
        return nutrition;
    }

    // متد جمع‌آوری غذا توسط کلاغ
    @Override
    public void onCollect(Crow crow) {
        crow.collectFood(this);
    }
}