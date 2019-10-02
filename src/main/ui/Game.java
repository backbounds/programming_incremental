package ui;


import java.io.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;
import java.util.*;

public class Game extends Application implements Savable, Loadable {
    public Player player;
    private Timer gameTimer;
    private TimerTask timerTask;
    Item intern;
    Upgrade coffee;
    Upgrade exposure;
    Upgrade adderall;
    Upgrade money;
    List<Upgrade> internUpgrades;

    //EFFECTS: creates a new game and instantiates a player
    public Game() {
        player = new Player(60);
        gameTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                calculateMoney(player);
            }
        };
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
    }

    private double calculateMoney(Player player) {
        double money = player.getMoney();
        List<ItemCollection> itemsToCalculate = player.getItems();
        double totalIncome = 0;
        for (ItemCollection ic: itemsToCalculate) {
            totalIncome += ic.getItem().getIncome() * ic.getNumber();
        }
        money += totalIncome;
        money = Math.round(money * 100d) / 100d;
        player.setMoney(money);
        System.out.println(player.getMoney());
        return player.getMoney();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("window.fxml"));
        primaryStage.setTitle("Idle Programmer");
        Scene scene = new Scene(root, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Game game  = new Game();
        Timer timer = game.gameTimer;
        TimerTask timerTask = game.timerTask;
        game.initialize();
        game.player.purchaseItem(game.intern, 3);
        timer.schedule(timerTask, 0, 1000);
        launch(args);
    }
}