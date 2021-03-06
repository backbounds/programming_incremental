package testUi;

import exceptions.NotEnoughMoney;
import exceptions.PurchaseFailed;
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
        Game game = Game.getInstance();
        game.player = new Player(100);
        game.player.purchase(cheapItem, 1);
        game.player.purchase(cheapItem, cheapUpgrade);
        try {
            game.player.purchase(expensiveItem, expensiveUpgrade);
        } catch (NotEnoughMoney ignored) {

        }
        game.save();
        assertTrue(Files.exists(Paths.get("saveFile.sav")));
    }

    @Test
    public void loadTest() throws IOException, ClassNotFoundException {
        Game game = Game.getInstance();
        assertTrue(Files.exists(Paths.get("saveFile.sav")));
        game.load();
        assertEquals(1, game.player.getItemNumber(cheapItem));
        assertTrue(game.player.upgradesContain(cheapUpgrade));
        assertFalse(game.player.upgradesContain(expensiveUpgrade));
    }
}
