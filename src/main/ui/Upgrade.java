package ui;

public class Upgrade {
    String name;
    int cost;
    double effect;
    String description;
    Item item;

    public Upgrade(String name, int cost, double effect, Item item) {
        this.name = name;
        this.cost = cost;
        this.effect = effect;
        this.item = item;
        this.description = "Increases the power of " + item.name + " by " + effect * 100 + "%.";
    }


}
