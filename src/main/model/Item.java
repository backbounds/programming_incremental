package model;

import java.util.ArrayList;
import java.util.List;

public class Item extends Purchasable {
    public List<Upgrade> applicableUpgrades;
    public List<Upgrade> purchasedUpgrades;
    public static final double MULTIPLIER = 1.08;


    //EFFECTS: creates an item
    public Item(String name, int cost, double income, List<Upgrade> applicableUpgrades) {
        super(name, cost, income);
        this.applicableUpgrades = applicableUpgrades;
        purchasedUpgrades = new ArrayList<>();
    }

    //REQUIRES: upgrade is in applicableUpgrades
    //MODIFIES: this
    //EFFECTS: adds the upgrade to the item
    public void addUpgrade(Upgrade u) {
        purchasedUpgrades.add(u);
        setIncome(u.applyUpgrade(getIncome()));
    }

    public void setNewCostAfterPurchase(int numberOfItems) {
        double newCost = getCost() * Math.pow(MULTIPLIER, numberOfItems);
        newCost = Math.round(newCost * 100d) / 100d;
        setCost(newCost);
    }

    public double moneyRequired(int existingAmount, int numberOfItems) {
        double moneyRequired = 0;
        for (int i = existingAmount; i < numberOfItems; i++) {
            moneyRequired += getCost() * Math.pow(MULTIPLIER, i);
        }

        moneyRequired = Math.round(moneyRequired * 100d) / 100d;
        return moneyRequired;
    }

    public void addUpgradeToItem(Upgrade upgrade) {
        if (!applicableUpgrades.contains(upgrade)) {
            applicableUpgrades.add(upgrade);
            upgrade.addItemToUpgrade(this);
        }
    }

    public void removeUpgrade(Upgrade upgrade) {
        applicableUpgrades.remove(upgrade);
        upgrade.removeFromItem(this);
    }



}
