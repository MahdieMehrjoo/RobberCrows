package entity;

import java.util.ArrayList;
import java.util.List;

public class Backpack {
    private List<Treasure> treasures;
    private float currentWeight = 0;
    private float maxWeight = 10;
    private float currentVolume =0;
    private float maxVolume = 10;

    public Backpack(){     }

    public void addTreasure(Treasure treasure) {
        if (currentWeight + treasure.getWeight() <= maxWeight && currentVolume + treasure.getVolume() <= maxVolume) {
            treasures.add(treasure);
            currentWeight += treasure.getWeight();
            currentVolume += treasure.getVolume();
        }
    }
    public int getTotalValue() {
        int sum =0;
        for (Treasure t : tresures){
            sum+= t.getValue();
        }
        return sum;
    }

    public List<Treasure> getTreasures() {
    return treasures;
    }
}

