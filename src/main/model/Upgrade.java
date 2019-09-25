package model;

public class Upgrade {
    public String name;
    public int cost;
    double effect;
    public String description;
    public Item item;

    public Upgrade(String name, int cost, double effect, Item item) {
        this.name = name;
        this.cost = cost;
        this.effect = effect;
        this.item = item;
        this.description = "Increases the power of " + item.name + " by " + effect * 100 + "%.";
    }


}
