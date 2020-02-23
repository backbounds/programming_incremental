package model;


import exceptions.NotEnoughMoney;

import java.io.Serializable;
import java.util.*;

public class Player extends Observable implements Serializable {
    private String name;
    private String companyName;
    private double money;
    private List<Upgrade> upgrades = new ArrayList<>();
    private HashMap<Item, Integer> items = new HashMap<>();


    //EFFECTS: creates a player
    public Player(int money) {
        this.money = money;
    }

    //getters

    //EFFECTS: returns name
    public String getName() {
        return name;
    }

    //EFFECTS: returns money
    public double getMoney() {
        roundMoney();
        return money;
    }


    //EFFECTS: return company name
    public String getCompanyName() {
        return companyName;
    }


    //EFFECTS: returns the map of player items
    public Map<Item, Integer> getItemMap() {
        return items;
    }


    //EFFECTS: returns the list of upgrades the player has
    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    //setters

    //EFFECTS: sets name
    public void setName(String name) {
        this.name = name;
    }


    //EFFECTS: sets the company name
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    //helpers

    //EFFECTS: returns true if a player has an item, false otherwise
    public Boolean itemsContain(Item item) {
        return items.containsKey(item);
    }

    //EFFECTS: returns the number of an item a player has
    public int getItemNumber(Item item) {
        if (itemsContain(item)) {
            return items.get(item);
        }
        return 0;
    }

    //EFFECTS: rounds the player's money
    public void roundMoney() {
        money = Math.round(money * 100d) / 100d;
    }

    //EFFECTS: returns true if player has the upgrade, false otherwise
    public boolean upgradesContain(Upgrade upgrade) {
        return upgrades.contains(upgrade);
    }


    //MODIFIES : this, item
    //EFFECTS : adds the upgrade to the player if the player has enough money
    public String purchase(Item i, Upgrade u) throws NotEnoughMoney {
        if (money >= u.getCost()) {
            i.getPurchasedUpgrades().add(u);
            i.addUpgrade(u);
            upgrades.add(u);
            money -= u.getCost();
            roundMoney();
            setChanged();
            notifyObservers();
        } else {
            throw new NotEnoughMoney(u.getCost());
        }
        return String.format("You have purchased the upgrade %s for %s!", u.getName(), i.getName());
    }

    //MODIFIES: this, item
    //EFFECTS: purchases an item, throws a NotEnoughMoney exception if the player doesn't have enough money
    public String purchase(Item item) throws NotEnoughMoney {
        double cost = item.getCost();
        String result = "";
        if (money >= cost) {
            result = makePurchase(item);
            money -= cost;
            setChanged();
            notifyObservers();
        } else {
            throw new NotEnoughMoney(item.getCost());
        }
        return result;
    }

    private String makePurchase(Item item) {
        if (items.containsKey(item)) {
            items.replace(item, items.get(item) + 1);
            item.setNewCostAfterPurchase(items.get(item));
            return String.format("You now have %s of %s!", items.get(item), item.getName());
        } else {
            items.put(item, 1);
            item.setNewCostAfterPurchase(1);
            return String.format("You bought your first %s!", item.getName());
        }
    }

    //EFFECTS: returns the current income of the player (in money/s)
    public double calculateIncome() {
        double totalIncome = 0;
        for (Item item: items.keySet()) {
            totalIncome += item.getIncome() * items.get(item);
        }
        totalIncome = Math.round(totalIncome * 100d) / 100d;
        return totalIncome;
    }


    //MODIFIES: this
    //EFFECTS: adds the player's income to their money
    public double calculateMoney() {
        money += calculateIncome();
        roundMoney();
        return money;
    }
}
