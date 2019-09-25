package testUi;

import ui.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    Player p;
    Item cheapItem;
    Item expensiveItem;
    Upgrade cheapUpgrade;
    Upgrade expensiveUpgrade;

    @BeforeEach
    void PlayerTest(){
        p = new Player(100);
        cheapItem = new Item("Cheap Item", 10, 1);
        expensiveItem = new Item("Expensive Item", 100, 1);
        cheapUpgrade = new Upgrade("Cheap Upgrade", 10, 1.1, cheapItem);
        expensiveUpgrade = new Upgrade("Expensive Upgrade", 100, 1.1, expensiveItem);

    }

    @Test
    public void purchaseTest(){
        p.purchaseItem(cheapItem, 1);
        p.purchaseUpgrade(cheapUpgrade);
        p.purchaseItem(expensiveItem, 1);
        p.purchaseUpgrade(expensiveUpgrade);

        assertTrue(p.itemsContain(cheapItem));
        assertTrue(p.upgradesContain(cheapUpgrade));
        assertFalse(p.itemsContain(expensiveItem));
        assertFalse(p.upgradesContain(expensiveUpgrade));
    }

    @Test
    public void purchaseBulkTest(){
        p.purchaseItem(expensiveItem, 5);
        p.purchaseItem(cheapItem, 5);
        p.purchaseItem(cheapItem, 10);

        assertFalse(p.itemsContain(expensiveItem));
        assertTrue(p.itemsContain(cheapItem));
        assertEquals(5, p.getItemNumber(cheapItem));
    }

}
