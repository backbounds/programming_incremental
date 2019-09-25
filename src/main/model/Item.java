package model;

public class Item {
    String name;
    int cost;
    double income;

    public Item(String name, int cost, double income) {
        this.name = name;
        this.cost = cost;
        this.income = income;
    }


    //MODIFIES: this
    //EFFECTS: Changes item income to reflect upgrade
    public void applyUpgrade(Upgrade u) {
        this.income *= u.effect;
    }
}
