package ui;


import java.util.ArrayList;
import java.util.List;

public class Player {
    String name;
    int money;
    int prestigeToBeGained;
    int prestige;
    public List<Upgrade> upgrades = new ArrayList<>();
    public List<Item> items = new ArrayList<>();

    public Player() {
        money = 60;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void prestigePlayer() {
        money = 1;
        prestige = +prestigeToBeGained;
    }

    public void purchaseUpgrade(Upgrade u) {
        if (money >= u.cost) {
            upgrades.add(u);
            money -= u.cost;
            System.out.println("You have purchased the upgrade " + u.name + "! You have " + money + " dollars left.");
        } else {
            System.out.println("You need to have " + u.cost + " dollars , but you have " + money + ".");
        }
    }

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

    public void purchaseItem(Item i) {
        if (money >= i.cost) {
            items.add(i);
            money -= i.cost;
            System.out.println("You have purchased the item" + i.name + "! You have " + money + " dollars left.");
        }
    }
}
