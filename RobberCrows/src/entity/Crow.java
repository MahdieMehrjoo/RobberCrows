package entity;

import entity.Position;

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
        energy += food.getNutrition(); // افزایش انرژی
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

}