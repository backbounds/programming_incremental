package model;


import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int money;
    int prestigeToBeGained;
    int prestige;
    private List<Upgrade> upgrades = new ArrayList<>();
    private List<ItemCollection> items = new ArrayList<>();

    public Player(int money) {
        this.money = money;
    }

    //getters

    //EFFECTS: returns name
    public String getName() {
        return name;
    }

    //EFFECTS: returns money
    public int getMoney() {
        return money;
    }


    //setters

    //EFFECTS: sets name
    public void setName(String name) {
        this.name = name;
    }


    //EFFECTS: sets money
    public void setMoney(int money) {
        this.money = money;
    }

    //helpers


    //EFFECTS: returns true if player has the upgrade, false otherwise
    public boolean upgradesContain(Upgrade u) {
        return upgrades.contains(u);
    }


    //EFFECTS: returns true if player has the item, false otherwise
    public boolean itemsContain(Item i) {
        for (ItemCollection ic: items) {
            if (ic.item.equals(i)) {
                return true;
            }
        }
        return false;
    }


    //EFFECTS: returns the number of the particular item the player has
    public int getItemNumber(Item i) {
        for (ItemCollection ic: items) {
            if (ic.item.equals(i)) {
                return ic.number;
            }
        }
        return 0;
    }


    //EFFECTS: gets a specific item from the player's items
    public ItemCollection getItem(Item i) {
        for (ItemCollection ic: items) {
            if (ic.item.equals(i)) {
                return ic;
            }
        }
        return null;
    }

    //MODIFIES : this
    //EFFECTS: reset money, items, and upgrades, but add accumulated prestige points
    public void prestigePlayer() {
        money = 1;
        upgrades.clear();
        items.clear();
        prestige = +prestigeToBeGained;
    }


    //MODIFIES : this
    //EFFECTS : adds the upgrade to the player if the player has enough money
    public void purchaseUpgrade(Upgrade u) {
        if (money >= u.cost) {
            upgrades.add(u);
            money -= u.cost;
            u.item.applyUpgrade(u);
            System.out.println("You have purchased the upgrade " + u.name + "! You have " + money + " dollars left.");
        } else {
            System.out.println("You need to have " + u.cost + " dollars, but you have " + money + ".");
        }
    }


    //EFFECTS : lists all upgrades the player has
    public void showUpgrades() {
        if (upgrades.isEmpty()) {
            System.out.println("You don't have any upgrades!");
        } else {
            System.out.println("Here are your upgrades:");
            for (Upgrade u : upgrades) {
                System.out.println(u.name);
            }
        }
    }

    //MODIFIES : this
    //EFFECTS : adds the item to the player if the player has enough money
    public void purchaseItem(Item i, int purchaseAmount) {
        if (money >= i.cost * purchaseAmount) {
            addItem(i, purchaseAmount);
        } else {
            System.out.println("You need to have " + i.cost * purchaseAmount
                    + " dollars, but you have " + money  + ".");
        }
    }


    //REQUIRES: amount is positive
    //MODIFIES: this
    //EFFECTS: adds an amount of item to the player
    public void addItem(Item i, int amount) {
        if (getItem(i) != null) {
            getItem(i).addNumber(amount);
        } else {
            ItemCollection purchasedItem = new ItemCollection(i, amount);
            items.add(purchasedItem);
            money -= i.cost * amount;
            System.out.println("You have purchased " + amount + " of the item " + i.name
                    + "! You have " + money + " dollars left.");
        }
    }
}
