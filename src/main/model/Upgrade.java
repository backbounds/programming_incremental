package model;

import java.io.Serializable;

public class Upgrade implements Serializable {
    public String name;
    public int cost;
    double effect;
    public String description;

    //EFFECTS: creates an upgrade object
    public Upgrade(String name, int cost, double effect) {
        this.name = name;
        this.cost = cost;
        this.effect = effect;
    }

    //EFFECTS: applies the upgrade to double
    public double applyUpgrade(double d) {
        return (d * effect);
    }


}
