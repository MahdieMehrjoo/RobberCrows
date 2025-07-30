package com.robbercrows.map;

import com.robbercrows.entity.Crow;

public class EnergyPoint extends GameObject
{
    //شناسه ها
    // مقدار انرژی که به کلاغ می‌ده
    private int energyValue;

    // سازنده
    public EnergyPoint(Position position, int energyValue)
    {
        super(position, true); // انرژی‌پوینت‌ها وقتی ساخته میشه فعال هست
        if (energyValue < 0)
            throw new IllegalArgumentException("Energy value must be non-negative");
        this.energyValue = energyValue;
    }

    // گرفتن مقدار انرژی
    public int getEnergyValue()
    {
        return energyValue;
    }

    // تنظیم مقدار انرژی
    public void setEnergyValue(int energyValue)
    {
        if (energyValue < 0)
            throw new IllegalArgumentException("Energy value must be non-negative");
        this.energyValue = energyValue;
    }

    //متد ها
    // به‌روزرسانی وضعیت نقطه انرژی (مثل غیرفعال شدن و پاک شدن بعد از جمع شدن)
    @Override
    public void update()
    {
        if (!isActive())
        {
            // اگر غیرفعال شده، می‌توان آن را از نقشه حذف کرد
        }
    }
    // نمایش گرافیکی نقطه انرژی
    @Override
    public void render()
    {
        if (isActive())
        {
            System.out.println("EnergyPoint [position=(" + getPosition().getX() + ", " + getPosition().getY() + "), energy=" + energyValue + "]");
        }
    }
    // تعامل نقطه انرژی با کلاغ (مثل پر کردن انرژی و غیرفعال شدن)
    @Override
    public void interact(Crow crow)
    {
        if (isActive() && crow != null)
        {
            crow.increaseEnergy(energyValue);
            System.out.println("Crow gained " + energyValue + " energy from energy point!");
            
            // انرژی‌پوینت غیرفعال می‌شود
            setActive(false);
        }
    }
}
