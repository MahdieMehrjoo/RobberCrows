package entity;

public abstract class PowerUp {
    protected String type;
    protected int duration;

    public PowerUp (String type, int duration)  {
        this.type = type;
        this.duration = duration;
    }

    public abstract void apply(Crow crow);

    public abstract void remove(Crow crow);
}
