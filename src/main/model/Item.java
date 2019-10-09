package model;

import java.util.ArrayList;
import java.util.List;

public class Item extends Purchasable {
    public List<Upgrade> applicableUpgrades;
    public List<Upgrade> purchasedUpgrades;


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
}
