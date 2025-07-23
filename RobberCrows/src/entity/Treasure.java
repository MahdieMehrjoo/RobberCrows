package entity;

// import صحیح کلاس Position از پکیج خودت
import entity.Position;

// کلاس گنج که اشیای قابل جمع‌آوری را مدل می‌کند
public class Treasure implements Collectible {
    // نوع گنج (مثلاً طلا، نقره و ...)
    private String type;
    // ارزش گنج
    private int value;
    // موقعیت گنج روی نقشه
    private Position position;
    // وزن گنج
    private float weight;
    // حجم گنج
    private float volume;
    // امتیاز گنج (در صورت نیاز)
    private int score;

    // سازنده گنج
    public Treasure (String type, int value , Position position, float weight, float volume, int score) {
        this.type = type;
        this.value = value;
        this.position = position;
        this.weight = weight;
        this.volume = volume;
        this.score = score;
    }

    // متد جمع‌آوری گنج توسط کلاغ
    @Override
    public void onCollect(Crow crow){
        crow.collectTreasure(this);
    }

    // گرفتن ارزش گنج
    public int getValue() {
        return value;
    }
    // گرفتن وزن گنج
    public float getWeight() {
        return weight;
    }
    // گرفتن حجم گنج
    public float getVolume() { return  volume; }
    // گرفتن امتیاز گنج
    public int getScore() {
        return score;
    }
    // گرفتن موقعیت گنج
    public Position getPosition() { return position; }
}