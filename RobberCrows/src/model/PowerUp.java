package model;

public abstract class PowerUp {
    protected String type;
    protected int duration;

    public PowerUp (String name , int duration)  {     }

    public abstract void apply(Crow crow);

    public abstract void remove(Crow crow);
}
