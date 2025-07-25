package com.robbercrows.entity;

// اینترفیس برای اشیایی که قابلیت جمع‌آوری توسط کلاغ دارند
public interface Collectible {
    // متد جمع‌آوری شیء توسط کلاغ
    void onCollect(Crow crow);
}