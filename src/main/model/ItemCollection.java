package model;

public class ItemCollection {
    private Item item;
    private int number;

    public ItemCollection(Item item, int number) {
        this.item = item;
        this.number = number;
    }

    //getters

    //EFFECTS: returns item
    public Item getItem() {
        return item;
    }

    //EFFECTS: returns number
    public int getNumber() {
        return number;
    }

    //REQUIRES: number is positive
    //MODIFIES: this
    //EFFECTS: adds to the existing number of items
    public void addNumber(int number) {
        this.number += number;
    }
}
