package ui;

import model.*;

import java.util.Scanner;

public class Game {
    public Player player;

    public Game() {
        player = new Player(60);
    }

    public void initialize() {
        Item basicClicker = new Item("Basic Clicker", 10, 0.1);
        Upgrade firstClick = new Upgrade("First Clicker", 10, 1.2, basicClicker);
        Upgrade upgradedClicker = new Upgrade("Upgraded Clicker", 50, 1.5, basicClicker);
    }

    public void startGame(Player player) {
        int money = player.getMoney();
        System.out.println("Please enter your name:");
        Scanner inputName = new Scanner(System.in);
        player.setName(inputName.next());
        String name = player.getName();
        System.out.println("Hello, " + name + ", your account has " + money + " to start with.");

    }

    public static void main(String[] args) {
        Game game  = new Game();
        game.initialize();
        game.startGame(game.player);
    }
}