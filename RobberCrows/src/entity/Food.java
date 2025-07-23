package entity;

public class Food implements Collectible{
    private int nutrition;

    public Food (int nutrition){
        this.nutrition = nutrition;
    }
    public int getNutrition() {
        return nutrition;
    }
    @Override
    public void onCollect(Crow crow) {
        crow.collectFood(this);
    }
}
