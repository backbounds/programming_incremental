package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Item extends Purchasable {
    private List<Upgrade> applicableUpgrades;
    private List<Upgrade> purchasedUpgrades;
    private static final double MULTIPLIER = 1.08;
    private double baseCost;

    //EFFECTS: creates an item
    public Item(String name, int cost, double income, List<Upgrade> applicableUpgrades) {
        super(name, cost, income);
        this.applicableUpgrades = applicableUpgrades;
        purchasedUpgrades = new ArrayList<>();
        baseCost = cost;
    }


    public List<Upgrade> getApplicableUpgrades() {
        return applicableUpgrades;
    }

    public List<Upgrade> getPurchasedUpgrades() {
        return purchasedUpgrades;
    }

    //REQUIRES: upgrade is in applicableUpgrades
    //MODIFIES: this
    //EFFECTS: adds the upgrade to the item
    void addUpgrade(Upgrade u) {
        purchasedUpgrades.add(u);
        setIncome(u.applyUpgrade(getIncome()));
    }

    //REQUIRES: numberOfItems is positive
    //MODIFIES: this
    //EFFECTS: increases the cost of the item depending on how many the player has
    public void setNewCostAfterPurchase(int numberOfItems) {
        double newCost = getCost() * Math.pow(MULTIPLIER, numberOfItems);
        newCost = Math.round(newCost * 100d) / 100d;
        setCost(newCost);
    }


    //EFFECTS: compares applicableUpgrades and purchasedUpgrades to see if they have the same items, returns true
    //         if they do, false otherwise.
    public boolean hasPurchasedAllUpgrades() {
        return new HashSet<>(applicableUpgrades).equals(new HashSet<>(purchasedUpgrades));
    }

}
