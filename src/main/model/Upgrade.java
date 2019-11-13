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
    double applyUpgrade(double d) {
        double newIncome = d * getIncome();
        newIncome = Math.round(newIncome * 100d) / 100d;
        return newIncome;
    }

    public void addItemToUpgrade(Item item) {
        if (applicableItem == null) {
            applicableItem = item;
            item.addApplicableUpgrade(this);
        } else if (!applicableItem.equals(item)) {
            applicableItem.removeApplicableUpgrade(this);
            applicableItem = item;
            item.addApplicableUpgrade(this);
        }
    }

    public void removeFromItem(Item item) {
        if (applicableItem != null && applicableItem.equals(item)) {
            applicableItem = null;
            item.removeApplicableUpgrade(this);
        }
    }

    public void getDataForList() {
        System.out.printf("\t%s\n\t\tCost: %s\n", getName(), getCost());
    }


}
