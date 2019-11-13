package model;

import java.util.ArrayList;
import java.util.List;

public class Item extends Purchasable {
    private List<Upgrade> applicableUpgrades;
    private List<Upgrade> purchasedUpgrades;
    private static final double MULTIPLIER = 1.08;


    //EFFECTS: creates an item
    public Item(String name, int cost, double income, List<Upgrade> applicableUpgrades) {
        super(name, cost, income);
        this.applicableUpgrades = applicableUpgrades;
        purchasedUpgrades = new ArrayList<>();
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

    public void addApplicableUpgrade(Upgrade upgrade) {
        if (!applicableUpgrades.contains(upgrade)) {
            applicableUpgrades.add(upgrade);
            upgrade.addItemToUpgrade(this);
        }
    }

    public void removeApplicableUpgrade(Upgrade upgrade) {
        applicableUpgrades.remove(upgrade);
        upgrade.removeFromItem(this);
    }

    @Override
    public void printInformation() {
        System.out.printf("Name: %s\nCost: %s\nApplicable:\n", getName(), getCost());
        for (Purchasable p: applicableUpgrades) {
            p.printInformation();
        }
        System.out.println("\n\n");
    }

}
