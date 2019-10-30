package model;


public class Upgrade extends Purchasable {
    private Item applicableItem;

    //EFFECTS: creates an upgrade object
    public Upgrade(String name, int cost, double effect) {
        super(name, cost, effect);
    }

    public Item getApplicableItem() {
        return applicableItem;
    }

    //EFFECTS: applies the upgrade to double
    public double applyUpgrade(double d) {
        double newIncome = d * getIncome();
        newIncome = Math.round(newIncome * 100d) / 100d;
        return newIncome;
    }

    public void addItemToUpgrade(Item item) {
        if (applicableItem == null) {
            applicableItem = item;
            item.addUpgradeToItem(this);
        } else if (!applicableItem.equals(item)) {
            applicableItem.removeUpgrade(this);
            applicableItem = item;
            item.addUpgradeToItem(this);
        }
    }

    public void removeFromItem(Item item) {
        if (applicableItem != null && applicableItem.equals(item)) {
            applicableItem = null;
            item.removeUpgrade(this);
        }
    }


}
