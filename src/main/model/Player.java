package model;


import exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String name;
    private String companyName;
    private double money;
    private int prestigeToBeGained;
    private int prestige;
    private List<Upgrade> upgrades = new ArrayList<>();
    private List<ItemCollection> items = new ArrayList<>();


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
        return money;
    }


    //EFFECTS: return company name
    public String getCompanyName() {
        return companyName;
    }


    //EFFECTS: returns prestige level
    public int getPrestige() {
        return prestige;
    }


    //EFFECTS: returns potential prestige
    public int getPrestigeToBeGained() {
        return prestigeToBeGained;
    }


    //EFFECTS: returns the list of items the player has
    public List<ItemCollection> getItems() {
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


    //EFFECTS: sets money
    public void setMoney(double money) {
        this.money = money;
    }

    //EFFECTS: add prestige points
    public void setPrestigeToBeGained(int prestigeToBeGained) {
        this.prestigeToBeGained = prestigeToBeGained;
    }


    //EFFECTS: sets the company name
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    //helpers


    //EFFECTS: rounds the player's money
    public void roundMoney() {
        money = Math.round(money * 100d) / 100d;
    }

    //EFFECTS: returns true if player has the upgrade, false otherwise
    public boolean upgradesContain(Upgrade u) {
        String name = u.name;
        for (Upgrade upgrade: upgrades) {
            if (upgrade.name.equals(name)) {
                return true;
            }
        }
        return false;
    }


    //EFFECTS: returns true if player has the item, false otherwise
    public boolean itemsContain(Item i) {
        String name = i.name;
        for (ItemCollection ic: items) {
            if (ic.getItem().name.equals(name)) {
                return true;
            }
        }
        return false;
    }


    //EFFECTS: returns the number of the particular item the player has
    public int getItemNumber(Item i) {
        String name = i.name;
        for (ItemCollection ic: items) {
            if (ic.getItem().name.equals(name)) {
                return ic.getNumber();
            }
        }
        return 0;
    }


    //EFFECTS: gets a specific item from the player's items
    public ItemCollection getItem(Item i) {
        String name = i.name;
        for (ItemCollection ic: items) {
            if (ic.getItem().name.equals(name)) {
                return ic;
            }
        }
        return null;
    }

    //MODIFIES : this
    //EFFECTS: reset money, items, and upgrades, but add accumulated prestige points
    public void prestigePlayer() {
        money = 1;
        prestige = +prestigeToBeGained;
        upgrades.clear();
        items.clear();
        prestigeToBeGained = 0;
    }


    //MODIFIES : this
    //EFFECTS : adds the upgrade to the player if the player has enough money
    public void purchaseUpgrade(Item i, Upgrade u) throws UpgradeAlreadyExists {
        if (money >= u.cost) {
            if (i.purchasedUpgrades.contains(u)) {
                throw new UpgradeAlreadyExists();
            }
            i.purchasedUpgrades.add(u);
            i.addUpgrade(u);
            upgrades.add(u);
            money -= u.cost;
            roundMoney();
            System.out.println("You have purchased the upgrade " + u.name + "! You have " + money + " dollars left.");
        } else {
            System.out.println("You need to have " + u.cost + " dollars, but you have " + money + ".");
        }
    }

    //MODIFIES : this
    //EFFECTS : adds the item to the player if the player has enough money
    public void purchaseItem(Item i, int purchaseAmount) {
        if (money >= i.getCost() * purchaseAmount) {
            addItem(i, purchaseAmount);
        } else {
            System.out.println("You need to have " + i.getCost() * purchaseAmount
                    + " dollars, but you have " + money  + ".");
        }
    }


    //REQUIRES: amount is positive
    //MODIFIES: this
    //EFFECTS: adds an amount of item to the player
    public void addItem(Item i, int amount) {
        if (getItem(i) != null) {
            getItem(i).addNumber(amount);
            money -= i.getCost() * amount;
            System.out.println("You bought " + amount + " more of the item " + i.name + "! "
                    + "You have " + money + " dollars left.");
        } else {
            ItemCollection purchasedItem = new ItemCollection(i, amount);
            items.add(purchasedItem);
            money -= i.getCost() * amount;
            System.out.println(String.format("You have purchased %s more of the item %s! You have %s dollars left.",
                    amount, i.name, money));
        }
        roundMoney();
    }
}
