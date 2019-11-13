package ui;


import java.io.*;

import model.*;
import network.*;

import java.util.*;

public class Game implements Savable, Loadable {
    public Player player;
    private Timer gameTimer;
    private TimerTask timerTask;
    List<Item> allItems;
    private InputHandler inputHandler;

    //EFFECTS: creates a new game and instantiates a player
    private Game() {
        player = new Player(60);
        allItems = new ArrayList<>();
        gameTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                player.calculateMoney();
            }
        };
        initialize();
        inputHandler = new InputHandler(this);
    }

    private static class GameHolder {
        private static final Game GAME = new Game();
    }

    public static Game getInstance() {
        return GameHolder.GAME;
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

    private void updateItemsAfterLoading() {
        Map<Item, Integer> itemsToUpdate = player.getItemMap();
        for (Item item: itemsToUpdate.keySet()) {
            for (Item gameItem: allItems) {
                if (item.getName().equals(gameItem.getName())) {
                    gameItem.setNewCostAfterPurchase(itemsToUpdate.get(item));
                }
            }
        }
    }


    //EFFECTS: creates all the items and upgrades necessary for the game
    private void initialize() {
        initializeIntern();
        initializeJuniorDev();
    }

    private void initializeJuniorDev() {
        Upgrade coffee = new Upgrade("Coffee", 10, 1.2);
        Upgrade exposure = new Upgrade("Exposure", 50, 1.5);
        Upgrade adderall = new Upgrade("Adderall", 500, 2);
        Upgrade money = new Upgrade("Money", 2000, 5);
        List<Upgrade> juniorDevUpgrades = new ArrayList<>();
        juniorDevUpgrades.add(coffee);
        juniorDevUpgrades.add(exposure);
        juniorDevUpgrades.add(adderall);
        juniorDevUpgrades.add(money);
        Item juniorDev = new Item("Junior Dev", 15, 1.0, juniorDevUpgrades);
        allItems.add(juniorDev);
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


    public static void main(String[] args) throws IOException {
        try {
            System.out.println(WebData.getWebString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Game game = Game.getInstance();
        game.inputHandler.handleSave();
        game.gameTimer.schedule(game.timerTask, 0, 1000);
        while (true) {
            try {
                game.inputHandler.input();
            } catch (InputMismatchException e) {
                System.out.println("Unexpected input!");
            }
        }

    }
}