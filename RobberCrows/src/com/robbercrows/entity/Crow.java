package com.robbercrows.entity;

import com.robbercrows.map.Position;
import com.robbercrows.team.Team;

// کلاس کلاغ که رفتار و ویژگی‌های یک بازیکن را مدل می‌کند
public class Crow implements Movable {
    // نام کلاغ
    private String name;
    // تعداد جان‌های کلاغ
    private int health;
    // امتیاز فعلی کلاغ
    private int score;
    // مدت زمان استراحت (در صورت نیاز)
    private int restTime;
    // سرعت حرکت کلاغ
    private int speed;
    // انرژی فعلی کلاغ
    private int energy;
    // آیا کلاغ در حال استراحت است؟
    private boolean isResting;
    // کوله‌پشتی کلاغ برای ذخیره گنج‌ها
    private Backpack backpack;
    // موقعیت فعلی کلاغ روی نقشه
    private Position position;
    // تیمی که کلاغ عضو آن است
    private Team team;

    // وضعیت‌های موقت برای اثرها
    private volatile boolean magnetActive;
    private int magnetRadius;
    private Thread magnetTimerThread;
    private volatile boolean speedBoostActive;
    private Thread speedBoostTimerThread;

    // سازنده کلاس کلاغ
    // name: نام کلاغ
    // startPosition: موقعیت شروع
    // team: تیم کلاغ
    public Crow (String name , Position startPosition, Team team ) {
        this.name = name;
        this.health = 3; // مقدار پیش‌فرض جان
        this.score = 0;
        this.restTime = 0;
        this.speed = 1; // مقدار پیش‌فرض سرعت
        this.energy = 100; // مقدار پیش‌فرض انرژی
        this.isResting = false;
        this.backpack = new Backpack();
        this.position = startPosition;
        this.team = team;
        this.magnetActive = false;
        this.magnetRadius = 0;
        this.speedBoostActive = false;
    }

    // متد حرکت کلاغ در جهت داده‌شده
    @Override
    public void move (Direction direction) {
        if (isResting) return; // اگر کلاغ در حال استراحت است، حرکت نکند

        int x = position.getX();
        int y = position.getY();

        switch (direction) {
            case UP:
                position.setY(y - speed); // حرکت به بالا
                break;
            case DOWN:
                position.setY(y + speed); // حرکت به پایین
                break;
            case LEFT:
                position.setX(x - speed); // حرکت به چپ
                break;
            case RIGHT:
                position.setX(x + speed); // حرکت به راست
                break;
        }
    }

    // جمع‌آوری گنج توسط کلاغ
    public void collectTreasure (Treasure treasure) {
        backpack.addTreasure(treasure); // اضافه کردن گنج به کوله‌پشتی
        score += treasure.getValue();   // افزایش امتیاز
    }

    // جمع‌آوری غذا توسط کلاغ
    public void collectFood (Food food) {
        if (food == null) return;
        boostSpeedTemporarily(2.0, food.getDurationMs());
    }

    // متد ضربه خوردن کلاغ (کم شدن جان)
    public void hit() {
        health--;
        if (health < 0) health = 0; // جلوگیری از منفی شدن جان
    }

    // استفاده از قدرت ویژه توسط کلاغ
    public void usePowerUp(PowerUp powerUp) {
        powerUp.apply(this); // اعمال قدرت روی کلاغ
    }

    // متد استراحت کلاغ برای مدت زمان مشخص
    public void rest( int time ){
        isResting = true;
        restTime = time;
    }

    // Getterها برای دسترسی به ویژگی‌ها
    public String getName() {return name;}
    public int getHealth() {return health;}
    public int getScore() {return score;}
    public int getRestTime() {return restTime;}
    public int getSpeed() {return speed;}
    public int getEnergy() {return energy;}
    public boolean isResting() {return isResting;}
    public Backpack getBackpack() {return backpack;}
    public Position getPosition() {return position;}

    // Setterها برای تغییر ویژگی‌ها
    public void setName(String name) {this.name = name;}
    public void setHealth(int health) {this.health = health;}
    public void setScore(int score) {this.score = score;}
    public void setRestTime(int restTime) {this.restTime = restTime;}
    public void setSpeed(int speed) {this.speed = speed;}
    public void setEnergy(int energy) {this.energy = energy;}
    public void setResting(boolean isResting) {this.isResting = isResting;}
    public void setBackpack(Backpack backpack) {this.backpack = backpack;}
    public void setPosition(Position position) {this.position = position;}

    // متدهای اضافی مورد نیاز برای تعامل با بقیه کلاس‌ها
    public boolean isDead() {
        return this.health <= 0 || this.energy <= 0;
    }

    // گرفتن تیم کلاغ
    public Team getTeam() {
        return team;
    }

    // تنظیم تیم کلاغ
    public void setTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }this.team = team;
    }

    // کم کردن جان کلاغ (برای Obstacle)
    public void reduceHealth(int amount) {
        if (amount > 0) {
            this.health -= amount;
            if (this.health < 0) this.health = 0;
        }
    }

    // اضافه کردن انرژی (برای EnergyPoint)
    public void addEnergy(int amount) {
        if (amount > 0) {
            this.energy += amount;
        }
    }

    // افزایش انرژی (نام دیگر برای addEnergy)
    public void increaseEnergy(int amount) {
        addEnergy(amount);
    }

    // خالی کردن کوله‌پشتی
    public void emptyBackpack() {
        if (backpack != null) {
            int totalValue = backpack.getTotalValue();
            if (totalValue > 0) {
                this.score += totalValue; // امتیاز گنج‌ها به کلاغ اضافه می‌شود
            }
            backpack = new Backpack(); // کوله‌پشتی جدید
        }
    }

    // فعال‌سازی آهن‌ربا
    public synchronized void activateMagnet(int radius, long durationMs) {
        if (radius <= 0 || durationMs <= 0) return;
        this.magnetActive = true;
        this.magnetRadius = radius;
        if (magnetTimerThread != null && magnetTimerThread.isAlive()) {
            magnetTimerThread.interrupt();
        }
        magnetTimerThread = new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException ignored) {
            } finally {
                deactivateMagnet();
            }
        }, "MagnetTimer-" + name);
        magnetTimerThread.setDaemon(true);
        magnetTimerThread.start();
    }

    // غیرفعال‌سازی آهن‌ربا
    public synchronized void deactivateMagnet() {
        magnetActive = false;
        magnetRadius = 0;
        if (magnetTimerThread != null) {
            magnetTimerThread.interrupt();
            magnetTimerThread = null;
        }
    }

    public boolean isMagnetActive() { return magnetActive; }
    public int getMagnetRadius() { return magnetRadius; }

    // اعمال جذب روی لیست گنج‌ها (طلا/نقره)
    public void applyMagnetToTreasures(java.util.List<Treasure> treasures) {
        if (!magnetActive || treasures == null) return;
        for (Treasure t : treasures) {
            if (t == null || t.getPosition() == null) continue;
            if (this.position == null) continue;
            if (this.position.distanceTo(t.getPosition()) <= magnetRadius) {
                collectTreasure(t);
            }
        }
    }

    // تقویت موقت سرعت
    public synchronized void boostSpeedTemporarily(double factor, long durationMs) {
        if (factor <= 0 || durationMs <= 0) return;
        int originalSpeed = this.speed;
        int boosted = (int) Math.max(1, Math.round(originalSpeed * factor));
        this.speed = boosted;
        this.speedBoostActive = true;
        if (speedBoostTimerThread != null && speedBoostTimerThread.isAlive()) {
            speedBoostTimerThread.interrupt();
        }
        final String threadName = "SpeedBoostTimer-" + name;
        speedBoostTimerThread = new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException ignored) {
            } finally {
                synchronized (Crow.this) {
                    speed = originalSpeed;
                    speedBoostActive = false;
                    speedBoostTimerThread = null;
                }
            }
        }, threadName);
        speedBoostTimerThread.setDaemon(true);
        speedBoostTimerThread.start();
    }

}