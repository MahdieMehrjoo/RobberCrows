package entity;

import entity.Position;

public class Crow implements Movable {
    private String name;
    private int health;
    private int score;
    private int restTime;
    private int speed;
    private int energy;
    private boolean isResting;
    private Backpack backpack;
    private Position position;
    private Team team;




    //سازنده
    public Crow (String name , Position startPosition, Team team ) {
        this.name = name;
        this.health = 3;
        this.score = 0;
        this.restTime = 0;
        this.speed = 1;
        this.energy = 100;
        this.isResting = false;
        this.backpack = new Backpack();
        this.position = startPosition;
        this.team = team;
    }

    @Override
    public void move (Direction direction) {
        if (isResting) return;

        int x = position.getX();
        int y = position.getY();

        switch (direction) {
            case UP:
                position.setY(y - speed);
                break;
            case DOWN:
                position.setY(y + speed);
                break;
            case LEFT:
                position.setX(x - speed);
                break;
            case RIGHT:
                position.setX(x + speed);
                break;
        }
        def
    }

    public void collectTreasure (Treasure treasure) {
        backpack.addTreasure(treasure);
        score += treasure.getValue();
    }

    public void collectFood (Food food) {
        energy += food.getNutrition();
    }

    public void hit() {  }

    public void usePowerUp(PowerUp powerUp) {  }

    public void rest( int time ){ }

    // getter --------------------------------------------------------
    public String getName() {return name;}
    public int getHealth() {return health;}
    public int getScore() {return score;}
    public int getRestTime() {return restTime;}
    public int getSpeed() {return speed;}
    public int getEnergy() {return energy;}
    public boolean isResting() {return isResting;}
    public Backpack getBackpack() {return backpack;}
    public Position getPosition() {return position;}


    //setter---------------------------------
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
