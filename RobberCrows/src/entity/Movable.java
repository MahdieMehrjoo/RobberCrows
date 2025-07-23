package entity;

// اینترفیس برای اشیایی که قابلیت حرکت دارند
public interface Movable {
    // متد حرکت در جهت مشخص
    void move(Direction direction);
}