package testModel;

import exceptions.NotEnoughMoney;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ItemTest {
    Player testPlayer;
    Item testItem;

    @BeforeEach
    public void setup() {
        testItem = new Item("Test", 1, 1, new ArrayList<>());
    }

    @Test
    public void itemCostTest() {
        assertEquals(2.08, testItem.costToPurchase(2));
        assertEquals(5.87, testItem.costToPurchase(5));
    }

    @Test
    public void itemCostSetTest() {
        testPlayer = new Player(10);
        try {
            testPlayer.purchase(testItem, 1);
        } catch (NotEnoughMoney e) {
            fail();
        }
        assertEquals(1.08, testItem.costToPurchase(1));
        assertEquals(4.87, testItem.costToPurchase(4));
    }
}
