package com.robbercrows.map;

public class Position
{
    //شناسه ها

    //مختصات افقی شیء
    private int x;
    // مختصات عمودی شیء
    private int y;

    //متد ها

    // گرفتن مختصات افقی شیء
    public int getX()
    {
        return x;
    }
    // گرفتن مختصات عمودی شیء
    public int getY()
    {
        return y;
    }
    // تنظیم مختصات افقی شیء
    public void setX(int x)
    {
        this.x = x;
    }
    // تنظیم مختصات عمودی شیء
    public void setY(int y)
    {
        this.y = y;
    }

    // محاسبه فاصله بین موقعیت فعلی و یه موقعیت دیگه
    public double distanceTo(Position other)
    {
        int deltaX = this.x - other.x;
        int deltaY = this.y - other.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);//فرمول فاصله
    }
}
