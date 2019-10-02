package testUi;

import ui.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {
    Game game;
    Item cheapItem;
    Item expensiveItem;
    Upgrade cheapUpgrade;
    Upgrade expensiveUpgrade;
    List<Upgrade> upgrades = new ArrayList<>();

    @BeforeEach
    void setup(){
        cheapItem = new Item("Cheap Item", 10, 1, upgrades);
        expensiveItem = new Item("Expensive Item", 100, 1, upgrades);
        cheapUpgrade = new Upgrade("Cheap Upgrade", 10, 1.1);
        expensiveUpgrade = new Upgrade("Expensive Upgrade", 100, 1.1);
    }

    @Test
    public void saveTest() throws IOException, ClassNotFoundException {
        Game game = new Game();
        game.player = new Player(100);
        game.player.setMoney(150);
        game.player.addItem(cheapItem, 5);
        game.player.purchaseUpgrade(cheapUpgrade);
        game.player.purchaseUpgrade(expensiveUpgrade);
        game.save();
        game.player = new Player(50);
        game.load();
        assertEquals(40, game.player.getMoney());
        assertEquals(5, game.player.getItemNumber(cheapItem));
        assertTrue(game.player.upgradesContain(cheapUpgrade));
        assertTrue(game.player.upgradesContain(expensiveUpgrade));
    }
}
