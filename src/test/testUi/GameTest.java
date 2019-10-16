package testUi;

import exceptions.PurchaseFailed;
import exceptions.UpgradeAlreadyExists;
import ui.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void saveTest() throws IOException, PurchaseFailed {
        Game game = new Game();
        game.player = new Player(100);
        game.player.purchase(cheapItem, 5);
        game.player.purchase(cheapItem, cheapUpgrade);
        game.player.purchase(expensiveItem, expensiveUpgrade);
        game.save();
        assertTrue(Files.exists(Paths.get("saveFile.sav")));
    }

    @Test
    public void loadTest() throws IOException, ClassNotFoundException {
        Game game = new Game();
        assertTrue(Files.exists(Paths.get("saveFile.sav")));
        game.load();
        assertEquals(5, game.player.getItemNumber(cheapItem));
        assertTrue(game.player.upgradesContain(cheapUpgrade));
        assertFalse(game.player.upgradesContain(expensiveUpgrade));
    }
}
