package testModel;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PurchasableTest {
    Item item;
    Item item2;
    Upgrade upgrade;

    @BeforeEach
    public void setup() {
        upgrade = new Upgrade("TestUpgrade", 20, 10);
        item = new Item("Test", 10, 1, new ArrayList<Upgrade>());
        item2 = new Item("Test2", 10, 1, new ArrayList<Upgrade>());
    }

    @Test
    public void testAddRemoveUpgrade() {
        item.addUpgradeToItem(upgrade);
        assertTrue(item.applicableUpgrades.contains(upgrade));
        assertEquals(item, upgrade.getApplicableItem());
        item.removeUpgrade(upgrade);
        assertFalse(item.applicableUpgrades.contains(upgrade));
        assertNull(upgrade.getApplicableItem());
    }

    @Test
    public void testAddRemoveItem() {
        upgrade.addItemToUpgrade(item);
        assertTrue(item.applicableUpgrades.contains(upgrade));
        assertEquals(item, upgrade.getApplicableItem());
        upgrade.removeFromItem(item);
        assertFalse(item.applicableUpgrades.contains(upgrade));
        assertNull(upgrade.getApplicableItem());
    }

    @Test
    public void testAddMultipleItems() {
        upgrade.addItemToUpgrade(item);
        assertTrue(item.applicableUpgrades.contains(upgrade));
        assertEquals(item, upgrade.getApplicableItem());
        upgrade.addItemToUpgrade(item2);
        assertFalse(item.applicableUpgrades.contains(upgrade));
        assertEquals(item2, upgrade.getApplicableItem());
        assertTrue(item2.applicableUpgrades.contains(upgrade));
        assertEquals(item2, upgrade.getApplicableItem());
    }

    @Test
    public void testSwitchUpgrade() {
        item.addUpgradeToItem(upgrade);
        assertTrue(item.applicableUpgrades.contains(upgrade));
        assertEquals(item, upgrade.getApplicableItem());
        item2.addUpgradeToItem(upgrade);
        assertFalse(item.applicableUpgrades.contains(upgrade));
        assertEquals(item2, upgrade.getApplicableItem());
        assertTrue(item2.applicableUpgrades.contains(upgrade));
        assertEquals(item2, upgrade.getApplicableItem());
    }
}
