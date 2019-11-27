package ui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
    private List<Item> allItems;
    private Item intern;
    private Item juniorDev;
    private Item seniorDev;
    private Item teamLeader;
    private Item outsource;
    private JPanel gamePanel;
    private JTextArea outputField;
    private JButton internBtn;
    private JButton juniorDevBtn;
    private JButton seniorDevBtn;
    private JButton teamLeaderBtn;
    private JButton outsourceBtn;
    private JLabel moneyLabel;
    private JLabel incomeLabel;
    private JButton upgradeIntern;
    private JButton upgradeJuniorDev;
    private JButton upgradeSeniorDev;
    private JButton upgradeTeamLeader;
    private JButton upgradeOutsourcing;
    private JLabel internLabel;
    private JLabel juniorDevLabel;
    private JLabel seniorDevLabel;
    private JLabel teamLeaderLabel;
    private JLabel outsourceLabel;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem save;
    private JMenuItem load;
    private JMenuItem quit;
    private Map<Item, JButton> upgradeButtons = new HashMap<>();
    private Map<Item, JButton> itemButtons = new HashMap<>();

    //EFFECTS: creates a new game and instantiates a player
    private Game() {
        player = new Player(40);
        allItems = new ArrayList<>();
        gameTimer = new Timer();
        player.addObserver(this);
        initialize();
        setUpButtons();
        setUpMenuBar();
        timerTask = new TimerTask() {
            @Override
            public void run() {

                player.calculateMoney();
                setLabels();
                setItemCostsOnButton();
                setUpgradeButtons();
            }
        };
    }

    private void setLabels() {
        moneyLabel.setText("Money: $" + player.getMoney());
        incomeLabel.setText("Income: $" + player.calculateIncome() + "/s");
        setButtonLabels();
    }

    private void setButtonLabels() {
        List<JLabel> itemLabels = new ArrayList<>();
        itemLabels.add(internLabel);
        itemLabels.add(juniorDevLabel);
        itemLabels.add(seniorDevLabel);
        itemLabels.add(teamLeaderLabel);
        itemLabels.add(outsourceLabel);
        Map<Item, Integer> items = player.getItemMap();
        for (Item item: items.keySet()) {
            for (JLabel label: itemLabels) {
                if (label.getText().contains(item.getName())) {
                    label.setText(item.getName() + " (x" + items.get(item) + ")"
                            + "[$" + item.getIncome() + "/s]");
                }
            }
        }
    }

    private void setUpgradeButtons() {
        for (Item item: upgradeButtons.keySet()) {
            JButton button = upgradeButtons.get(item);
            if (!item.hasPurchasedAllUpgrades()) {
                for (Upgrade upgrade : item.getApplicableUpgrades()) {
                    if (!item.getPurchasedUpgrades().contains(upgrade) && player.getItemMap().containsKey(item)
                            && !player.getUpgrades().contains(upgrade)) {
                        setUpUpgradeButton(button, upgrade, item);
                        break;
                    }
                }
            } else {
                button.setEnabled(false);
                button.setText("No other upgrades for " + item.getName());
                button.setToolTipText("");
            }
        }
    }

    private void setUpUpgradeButton(JButton button, Upgrade upgrade, Item item) {
        button.setText(upgrade.getName() + ": $" + upgrade.getCost());
        button.setActionCommand(upgrade.getName());
        button.setToolTipText("Increase effectiveness of " + item.getName() + " by "
                + (upgrade.getIncome() - 1) * 100 + "%.");
        button.setEnabled(true);
    }

    private void setUpMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        setUpMenuItems();
    }

    private void setUpMenuItems() {
        save = new JMenuItem("Save");
        save.setMnemonic(KeyEvent.VK_S);
        save.setActionCommand("save");
        save.addActionListener(this);
        load = new JMenuItem("Load");
        load.setMnemonic(KeyEvent.VK_L);
        load.setActionCommand("load");
        load.addActionListener(this);
        quit = new JMenuItem("Quit");
        quit.setMnemonic(KeyEvent.VK_Q);
        quit.setActionCommand("quit");
        quit.addActionListener(this);
        fileMenu.add(save);
        fileMenu.add(load);
        fileMenu.add(quit);
    }

    //MODIFIES: this
    //EFFECTS: updates UI labels after the player makes a purchase
    @Override
    public void update(Observable o, Object arg) {
        setItemCostsOnButton();
        setUpgradeButtons();
        setLabels();
    }

    private void setUpButtons() {
        for (Item item: itemButtons.keySet()) {
            JButton button = itemButtons.get(item);
            button.setActionCommand(item.getName());
            button.addActionListener(this);
            button = upgradeButtons.get(item);
            button.addActionListener(this);
        }
    }

    //MODIFIES: this
    //EFFECTS: responds with the appropriate action based on a UI button press
    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();
        if (command.equals("save") || command.equals("load") || command.equals("quit")) {
            fileCommand(command);
        } else {
            for (Item item: allItems) {
                if (item.getName().equals(command)) {
                    purchaseItem(item);
                } else {
                    for (Upgrade upgrade: item.getApplicableUpgrades()) {
                        if (upgrade.getName().equals(command)) {
                            purchaseUpgrade(item, upgrade);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void purchaseItem(Item item) {
        try {
            String result = player.purchase(item);
            setOutputField(result);
        } catch (NotEnoughMoney e) {
            setOutputField(String.format("You need %s, but you have $%s.", e, player.getMoney()));
        }
    }

    private void purchaseUpgrade(Item item, Upgrade upgrade) {
        try {
            String result = player.purchase(item, upgrade);
            setOutputField(result);
        } catch (NotEnoughMoney e) {
            setOutputField(String.format("You need %s, but you have $%s!", e, player.getMoney()));
        }
    }

    private void fileCommand(String command) {
        if (command.equals("save")) {
            try {
                save();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(gamePanel, "Could not save game!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (command.equals("load")) {
            try {
                load();
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(gamePanel, "Could not load save!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.exit(0);
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
        setOutputField("Saved player with " + player.getMoney() + " dollars.");
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
        setLabels();
        setUpgradeButtons();
        setOutputField("Loaded player with " + player.getMoney() + " dollars.");
    }

    private void setItemCostsOnButton() {
        internBtn.setText("Purchase: $" + intern.getCost());
        juniorDevBtn.setText("Purchase: $" + juniorDev.getCost());
        seniorDevBtn.setText("Purchase: $" + seniorDev.getCost());
        teamLeaderBtn.setText("Purchase: $" + teamLeader.getCost());
        outsourceBtn.setText("Purchase: $" + outsource.getCost());
    }

    private void setOutputField(String output) {
        outputField.setText(outputField.getText() + output + "\n");
    }

    private void updateItemsAfterLoading() {
        Map<Item, Integer> itemsToUpdate = player.getItemMap();
        for (Item item: itemsToUpdate.keySet()) {
            for (Item gameItem: allItems) {
                if (item.getName().equals(gameItem.getName())) {
                    gameItem = item;
                }
            }
        }
    }


    //EFFECTS: creates all the items and upgrades necessary for the game
    private void initialize() {
        outputField.setEditable(false);
        initializeIntern();
        initializeJuniorDev();
        initializeSeniorDev();
        initializeTeamLeader();
        initializeOutsource();
    }

    //EFFECTS: creates all upgrades necessary for the intern item
    private void initializeIntern() {
        Upgrade coffee = new Upgrade("Coffee", 10, 1.5);
        Upgrade exposure = new Upgrade("Exposure", 50, 2);
        Upgrade adderall = new Upgrade("Adderall", 500, 2);
        Upgrade money = new Upgrade("Money", 2000, 5);
        List<Upgrade> internUpgrades = new ArrayList<>();
        internUpgrades.add(coffee);
        internUpgrades.add(exposure);
        internUpgrades.add(adderall);
        internUpgrades.add(money);
        intern = new Item("Intern", 15, 1.0, internUpgrades);
        allItems.add(intern);
        itemButtons.put(intern, internBtn);
        upgradeButtons.put(intern, upgradeIntern);
    }

    private void initializeJuniorDev() {
        Upgrade workstation = new Upgrade("Workstation", 250, 2);
        Upgrade slack = new Upgrade("Slack", 400, 3);
        Upgrade snacks = new Upgrade("Snacks", 1200, 2);
        Upgrade breaks = new Upgrade("Breaks", 3000, 3);
        List<Upgrade> juniorDevUpgrades = new ArrayList<>();
        juniorDevUpgrades.add(workstation);
        juniorDevUpgrades.add(slack);
        juniorDevUpgrades.add(snacks);
        juniorDevUpgrades.add(breaks);
        juniorDev = new Item("Junior Dev", 100, 5.0, juniorDevUpgrades);
        allItems.add(juniorDev);
        itemButtons.put(juniorDev, juniorDevBtn);
        upgradeButtons.put(juniorDev, upgradeJuniorDev);
    }

    private void initializeSeniorDev() {
        Upgrade office = new Upgrade("Office", 2000, 1.5);
        Upgrade training = new Upgrade("Leader Training", 5000, 2);
        Upgrade vacation = new Upgrade("Vacation", 15000, 3);
        Upgrade teams = new Upgrade("Teams", 20000, 5);
        List<Upgrade> seniorDevUpgrades = new ArrayList<>();
        seniorDevUpgrades.add(office);
        seniorDevUpgrades.add(training);
        seniorDevUpgrades.add(vacation);
        seniorDevUpgrades.add(teams);
        seniorDev = new Item("Senior Dev", 500, 40, seniorDevUpgrades);
        allItems.add(seniorDev);
        itemButtons.put(seniorDev, seniorDevBtn);
        upgradeButtons.put(seniorDev, upgradeSeniorDev);
    }

    private void initializeTeamLeader() {
        Upgrade communication = new Upgrade("Communication", 12000, 2);
        Upgrade shirts = new Upgrade("Dress Code", 20000, 3);
        Upgrade size = new Upgrade("Team Size", 40000, 2);
        Upgrade share = new Upgrade("Sell Shares", 100000, 3);
        List<Upgrade> teamLeaderUpgrades = new ArrayList<>();
        teamLeaderUpgrades.add(communication);
        teamLeaderUpgrades.add(shirts);
        teamLeaderUpgrades.add(size);
        teamLeaderUpgrades.add(share);
        teamLeader = new Item("Team Leader", 3000, 100, teamLeaderUpgrades);
        allItems.add(teamLeader);
        itemButtons.put(teamLeader, teamLeaderBtn);
        upgradeButtons.put(teamLeader, upgradeTeamLeader);
    }

    private void initializeOutsource() {
        Upgrade documentation = new Upgrade("Documentation", 50000, 2);
        Upgrade diversify = new Upgrade("More Countries", 200000, 3);
        Upgrade oversight = new Upgrade("Provide Oversight", 400000, 2);
        Upgrade contract = new Upgrade("Contract Employees", 1000000, 2);
        List<Upgrade> outsourceUpgrades = new ArrayList<>();
        outsourceUpgrades.add(documentation);
        outsourceUpgrades.add(diversify);
        outsourceUpgrades.add(oversight);
        outsourceUpgrades.add(contract);
        outsource = new Item("Outsource", 10000, 400, outsourceUpgrades);
        allItems.add(outsource);
        itemButtons.put(outsource, outsourceBtn);
        upgradeButtons.put(outsource, upgradeOutsourcing);
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
        JFrame frame = new JFrame("Game");
        frame.setJMenuBar(game.menuBar);
        frame.setContentPane(game.gamePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 300));
        game.handleSave();
        frame.setVisible(true);
        game.gameTimer.schedule(game.timerTask, 0, 1000);
    }
}