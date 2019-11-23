package ui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import exceptions.NotEnoughMoney;
import model.*;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.Timer;

import static javax.swing.JOptionPane.YES_NO_OPTION;

public class Game implements Savable, Loadable, Observer, ActionListener {
    public Player player;
    private Timer gameTimer;
    private TimerTask timerTask;
    List<Item> allItems;
    private Item intern;
    private Item juniorDev;
    private JPanel gamePanel;
    private JTextArea outputField;
    private JButton internBtn;
    private JButton juniorDevBtn;
    private JButton seniorDevBtn;
    private JButton teamLeaderBtn;
    private JButton outsourceBtn;
    private JLabel moneyLabel;
    private JLabel incomeLabel;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem save;
    private JMenuItem load;
    private JMenuItem quit;

    //EFFECTS: creates a new game and instantiates a player
    private Game() {
        player = new Player(60);
        allItems = new ArrayList<>();
        gameTimer = new Timer();
        player.addObserver(this);
        timerTask = new TimerTask() {
            @Override
            public void run() {

                player.calculateMoney();
                moneyLabel.setText("Money: $" + player.getMoney());
                incomeLabel.setText("Income: $" + player.calculateIncome() + "/s");
                setItemCostsOnButton();
            }
        };
        initialize();
        setUpButtons();
    }

    @Override
    public void update(Observable o, Object arg) {
        setItemCostsOnButton();
        moneyLabel.setText("Money: $" + player.getMoney());
        incomeLabel.setText("Income: $" + player.calculateIncome() + "/s");
    }

    private void setUpButtons() {
        internBtn.setActionCommand(intern.getName());
        internBtn.addActionListener(this);
        juniorDevBtn.setActionCommand(juniorDev.getName());
        juniorDevBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Item toPurchase = intern;
        String item = ae.getActionCommand();
        for (Item i: allItems) {
            if (i.getName().equals(item)) {
                toPurchase = i;
            }
        }
        try {
            String result = player.purchase(toPurchase, 1);
            outputField.setText(outputField.getText() + result);
        } catch (NotEnoughMoney e) {
            outputField.setText(outputField.getText()
                    + String.format("You need %s, but you have $%s!\n", e, player.getMoney()));
        }
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

    private  void setItemCostsOnButton() {
        internBtn.setText("Purchase: $" + intern.getCost());
        juniorDevBtn.setText("Purchase: $" + juniorDev.getCost());

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
        outputField.setEditable(false);
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
        juniorDev = new Item("Junior Dev", 15, 1.0, juniorDevUpgrades);
        allItems.add(juniorDev);
        juniorDevBtn.setText("Cost: $" + juniorDev.getCost());

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
        intern = new Item("Intern", 15, 1.0, internUpgrades);
        allItems.add(intern);
        internBtn.setText("Cost: $" + intern.getCost());
    }

    private Boolean saveExists() {
        return Files.exists(Paths.get("saveFile.sav"));
    }


    private void handleSave() {
        if (saveExists()) {
            int loadGame = JOptionPane.showConfirmDialog(gamePanel, "You have a saved game, load it?",
                    "Save game found", YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (loadGame == 0) {
                try {
                    load();
                } catch (IOException | ClassNotFoundException e) {
                    JOptionPane.showMessageDialog(gamePanel, "Could not load save!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Game game = Game.getInstance();
        game.handleSave();
        game.gameTimer.schedule(game.timerTask, 0, 1000);
        JFrame frame = new JFrame("Game");
        frame.setContentPane(game.gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 300));
        frame.setVisible(true);
    }
}