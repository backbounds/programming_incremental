package ui;

import exceptions.NotEnoughMoney;
import exceptions.UpgradeAlreadyExists;
import model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

class InputHandler {

    private Game game;
    private Player player;
    private List<Item> allItems;
    private Scanner input;

    InputHandler(Game game) {
        this.game = game;
        player = game.player;
        allItems = game.allItems;
        input = new Scanner(System.in);
    }

    //EFFECTS: returns true if a save file exists, false otherwise
    private Boolean saveExists() {
        return Files.exists(Paths.get("saveFile.sav"));
    }

    void handleSave() {
        if (saveExists()) {
            System.out.println("You already have an existing save file. Press [1] to load, [2] to start a new game.");
            if (input.nextInt() == 1) {
                try {
                    game.load();
                } catch (IOException | ClassNotFoundException ice) {
                    System.out.println("Unable to load save file, creating new game.");
                }
            }
        }
    }


    //EFFECTS: terminates the process
    private void quit() {
        System.exit(0);
    }

    void input() throws IOException {
        System.out.println("List of inputs: \n"
                + "[1] to check your money \n"
                + "[2] to buy items or upgrades \n"
                + "[3] to save your game \n"
                + "[4] to view items and applicable upgrades\n"
                + "[5] to quit");
        handleInput(input.nextInt());
    }

    private void handleInput(int input) throws IOException {
        if (input == 1) {
            String s = String.format("\tMoney: %s\n\tIncome: %s/s", player.getMoney(), player.calculateIncome());
            System.out.println(s);
        } else if (input == 2) {
            listItems();
        } else if (input == 3) {
            game.save();
        } else if (input == 4) {
            listPlayerItems();
        } else if (input == 5) {
            quit();
        }
    }

    private void listPlayerItems() {
        for (Item i: allItems) {
            i.listApplicableUpgrades();
        }
    }

    private void listItems() {
        int i = 1;
        for (Item item : allItems) {
            if (player.getMoney() > item.getCost()) {
                System.out.println(String.format("%s:\n\tCost: %s\n\tIncome: %s\nPress %s to buy or %s to see upgrades",
                        item.getName(), item.getCost(), item.getIncome(), i, i + 1));
            }
        }
        try {
            handlePurchaseInput();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Item doesn't exist!");
        }
    }

    private void handlePurchaseInput() {
        int answer = input.nextInt();
        if (answer % 2 == 0) {
            if ((answer / 2 - 1) > allItems.size()) {
                throw new IndexOutOfBoundsException();
            }
            Item item = allItems.get(answer / 2 - 1);
            listUpgrades(item);
            purchaseUpgradeDialogue(item);
        } else {
            purchaseItemDialogue(answer - 1);
        }
    }

    private void listUpgrades(Item item) {
        int i = 1;
        for (Upgrade upgrade : item.getApplicableUpgrades()) {
            if (item.getPurchasedUpgrades().contains(upgrade)) {
                i++;
            } else {
                System.out.println(String.format("Name: %s \n Cost: %s \n Effect %s \n [%s] to buy or [0] to go back\n",
                        upgrade.getName(), upgrade.getCost(), upgrade.getIncome(), i));
                i++;
            }
        }
    }

    private void purchaseItemDialogue(int i) {
        Item item = allItems.get(i);
        System.out.println(String.format("How many of %s would you like to purchase?", item.getName()));
        try {
            player.purchase(item, input.nextInt());
            System.out.println(String.format("Your new income is %s.", player.calculateIncome()));
        } catch (NotEnoughMoney notEnoughMoney) {
            System.out.println(String.format("You need to have %s dollars.", notEnoughMoney.neededCost));
        } finally {
            System.out.println(String.format("You have %s dollars.", player.getMoney()));
        }
    }

    private void purchaseUpgradeDialogue(Item item) {
        int answer = input.nextInt();
        List<Upgrade> upgrades = item.getApplicableUpgrades();
        if (answer > 0) {
            try {
                player.purchase(item, upgrades.get(answer - 1));
            } catch (UpgradeAlreadyExists upgradeAlreadyExists) {
                System.out.println("You already have this upgrade!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Upgrade doesn't exist!");
            }
        }
    }
}
