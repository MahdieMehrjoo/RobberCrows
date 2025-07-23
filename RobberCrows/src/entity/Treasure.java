package entity;

public class Treasure {
    private String type;
    private int value;
    private Position position;
    private float weight;
    private float volume;
    private int score;

    public Treasure (String type, int value , Position position, float weight, float volume, int score) {
        this.type = type;
        this.value = value;
        this.position = position;
        this.weight = weight;
        this.volume = volume;
        this.score = score;
    }
    // getters

    public int getValue() {
        return value;
    }

    public float getWeight() {
        return weight;
    }
    public float getVolume() { return  volume; }

    public int getScore() {
        return score;
    }
}
