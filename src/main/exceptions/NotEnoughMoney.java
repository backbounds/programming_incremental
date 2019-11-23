package exceptions;

public class NotEnoughMoney extends PurchaseFailed {
    public double neededCost;

    public NotEnoughMoney(double neededCost) {
        this.neededCost = neededCost;
    }

    @Override
    public String toString() {
        return "$" + neededCost;
    }
}
