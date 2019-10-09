package model;


public class Upgrade extends Purchasable {

    //EFFECTS: creates an upgrade object
    public Upgrade(String name, int cost, double effect) {
        super(name, cost, effect);
    }

    //EFFECTS: applies the upgrade to double
    public double applyUpgrade(double d) {
        double newIncome = d * getIncome();
        newIncome = Math.round(newIncome * 100d) / 100d;
        return newIncome;
    }


}
