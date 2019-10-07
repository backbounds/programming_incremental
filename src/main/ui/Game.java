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
    List<Item> allItems;
    Item intern;
    Upgrade coffee;
    Upgrade exposure;
    Upgrade adderall;
    Upgrade money;
    List<Upgrade> internUpgrades;

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

                calculateMoney(player);
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
        System.out.println("Loaded player with " + player.getMoney() + " dollars.");
    }

    //EFFECTS: returns true if a save file exists, false otherwise
    public Boolean saveExists() {
        return Files.exists(Paths.get("savefile.sav"));
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
        coffee = new Upgrade("Coffee", 10, 1.2);
        exposure = new Upgrade("Exposure", 50, 1.5);
        adderall = new Upgrade("Adderall", 500, 2);
        money = new Upgrade("Money", 2000, 5);
        internUpgrades = new ArrayList<>();
        internUpgrades.add(coffee);
        internUpgrades.add(exposure);
        internUpgrades.add(adderall);
        internUpgrades.add(money);
        intern = new Item("Intern", 10, 0.1, internUpgrades);
        allItems.add(intern);
    }

    private double calculateMoney(Player player) {
        double money = player.getMoney();
        List<ItemCollection> itemsToCalculate = player.getItems();
        double totalIncome = 0;
        for (ItemCollection ic: itemsToCalculate) {
            totalIncome += ic.getItem().getIncome() * ic.getNumber();
        }
        money += totalIncome;
        player.setMoney(money);
        player.roundMoney();
        return player.getMoney();
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
                System.out.println(player.getMoney());
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
        System.out.println("Press [q] to exit");
        for (Item item: allItems) {
            if (player.getMoney() > item.getCost()) {
                System.out.println(item.name + ": Press [" + i + "] to buy or [" + (i + 1) + "] to see upgrades" + "\n"
                        + "Cost: " + item.getCost() + "\n"
                        + "Income: " + item.getIncome());
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
            item.listUpgrades();
            purchaseUpgradeDialogue(answer - 1, item);
        } else {
            purchaseItemDialogue(answer - 1);
        }
    }

    public void purchaseItemDialogue(int i) {
        Item item = allItems.get(i);
        System.out.println("How many of " + item.name + " would you like to purchase?");
        Scanner input = new Scanner(System.in);
        player.purchaseItem(item, input.nextInt());
    }

    public void purchaseUpgradeDialogue(int i, Item item) {
        Scanner input = new Scanner(System.in);
        int answer = input.nextInt();
        List<Upgrade> upgrades = item.getApplicableUpgrades();
        if (answer > 0) {
            try {
                player.purchaseUpgrade(item, upgrades.get(answer - 1));
            } catch (UpgradeAlreadyExists upgradeAlreadyExists) {
                System.out.println("You already have this item!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Upgrade doesn't exist!");
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Game game  = new Game();
        game.handleSave();
        game.gameTimer.schedule(game.timerTask, 0, 1000);
        while (true) {
            try {
                game.input();
            } catch (InputMismatchException e) {
                System.out.println("Unexpected input!");
                game.input();
            }
        }
    }
}