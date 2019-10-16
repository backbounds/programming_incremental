package ui;


import java.io.*;

import exceptions.*;
import model.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Game implements Savable, Loadable {
    public Player player;
    private Timer gameTimer;
    private TimerTask timerTask;
    private List<Item> allItems;

    //EFFECTS: creates a new game and instantiates a player
    public Game() {
        player = new Player(60);
        allItems = new ArrayList<>();
        gameTimer = new Timer();
        timerTask = new TimerTask() {
            private Boolean isRunning = true;

            public Boolean getRunning() {
                return isRunning;
            }

            @Override
            public void run() {

                player.calculateMoney();
            }
        };
        initialize();
    }

    //EFFECTS: saves the game
    @Override
    public void save() throws IOException {
        FileOutputStream saveFile = new FileOutputStream("saveFile.sav");
        ObjectOutputStream saved = new ObjectOutputStream(saveFile);
        saved.writeObject(player);
        saved.close();
        System.out.println("Saved player with " + player.getMoney() + " dollars.");
    }


    //REQUIRES: saveFile.sav exists
    //MODIFIES: this
    //EFFECTS: loads the game
    @Override
    public void load() throws IOException, ClassNotFoundException {
        FileInputStream loadFile = new FileInputStream("saveFile.sav");
        ObjectInputStream loaded = new ObjectInputStream(loadFile);
        player = (Player) loaded.readObject();
        loaded.close();
        updateItemsAfterLoading();
        System.out.println("Loaded player with " + player.getMoney() + " dollars.");
    }

    public void updateItemsAfterLoading() {
        Map<Item, Integer> itemsToUpdate = player.getItemMap();
        for (Item item: itemsToUpdate.keySet()) {
            for (Item gameItem: allItems) {
                if (item.getName().equals(gameItem.getName())) {
                    gameItem.setNewCostAfterPurchase(itemsToUpdate.get(item));
                }
            }
        }
    }

    //EFFECTS: returns true if a save file exists, false otherwise
    public Boolean saveExists() {
        return Files.exists(Paths.get("saveFile.sav"));
    }

    public void handleSave() {
        if (saveExists()) {
            System.out.println("You already have an existing save file. Press [1] to load, [2] to start a new game.");
            Scanner input = new Scanner(System.in);
            if (input.nextInt() == 1) {
                try {
                    load();
                } catch (IOException | ClassNotFoundException ice) {
                    System.out.println("Unable to load save file, creating new game.");
                }
            }
        }
    }

    //EFFECTS: terminates the process
    public void quit() {
        System.exit(0);
    }


    //EFFECTS: creates all the items and upgrades necessary for the game
    public void initialize() {
        initializeIntern();
    }

    private void initializeJuniorDev() {

    }

    //EFFECTS: creates all upgrades necessary for the intern item
    private void initializeIntern() {
        Upgrade coffee = new Upgrade("Coffee", 10, 1.2);
        Upgrade exposure = new Upgrade("Exposure", 50, 1.5);
        Upgrade adderall = new Upgrade("Adderall", 500, 2);
        Upgrade money = new Upgrade("Money", 2000, 5);
        List<Upgrade> internUpgrades = new ArrayList<>();
        internUpgrades.add(coffee);
        internUpgrades.add(exposure);
        internUpgrades.add(adderall);
        internUpgrades.add(money);
        Item intern = new Item("Intern", 15, 1.0, internUpgrades);
        allItems.add(intern);
    }


    public void input() throws IOException {
        System.out.println("List of inputs: \n"
                + "[1] to check your money \n"
                + "[2] to buy items or upgrades \n"
                + "[3] to save your game \n"
                + "[4] to quit");
        Scanner input = new Scanner(System.in);
        handleInput(input.nextInt());
    }

    public void handleInput(int input) throws IOException {
        switch (input) {
            case 1:
                String s = String.format("\tMoney: %s\n\tIncome: %s/s", player.getMoney(), player.calculateIncome());
                System.out.println(s);
                break;
            case 2:
                listItems();
                break;
            case 3:
                save();
                break;
            case 4:
                quit();
                break;
            default:
                System.out.println("Unexpected input! Please try again.");
                break;
        }
    }

    public void listItems() {
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

    public void handlePurchaseInput() {
        Scanner input = new Scanner(System.in);
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

    public void listUpgrades(Item item) {
        int i = 1;
        for (Upgrade upgrade : item.applicableUpgrades) {
            if (item.purchasedUpgrades.contains(upgrade)) {
                i++;
            } else {
                System.out.println(String.format("Name: %s \n Cost: %s \n Effect %s \n [%s] to buy or [0] to go back\n",
                        upgrade.getName(), upgrade.getCost(), upgrade.getIncome(), i));
                i++;
            }
        }
    }

    public void purchaseItemDialogue(int i) {
        Item item = allItems.get(i);
        System.out.println(String.format("How many of %s would you like to purchase?", item.getName()));
        Scanner input = new Scanner(System.in);
        try {
            player.purchase(item, input.nextInt());
            System.out.println(String.format("Your new income is %s.", player.calculateIncome()));
        } catch (NotEnoughMoney notEnoughMoney) {
            System.out.println(String.format("You need to have %s dollars.", notEnoughMoney.neededCost));
        } finally {
            System.out.println(String.format("You have %s dollars.", player.getMoney()));
        }
    }

    public void purchaseUpgradeDialogue(Item item) {
        Scanner input = new Scanner(System.in);
        int answer = input.nextInt();
        List<Upgrade> upgrades = item.applicableUpgrades;
        if (answer > 0) {
            try {
                player.purchase(item, upgrades.get(answer - 1));
            } catch (UpgradeAlreadyExists upgradeAlreadyExists) {
                System.out.println("You already have this item!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Upgrade doesn't exist!");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.handleSave();
        game.gameTimer.schedule(game.timerTask, 0, 1000);
        while (true) {
            try {
                game.input();
            } catch (InputMismatchException e) {
                System.out.println("Unexpected input!");
            }
        }
    }
}