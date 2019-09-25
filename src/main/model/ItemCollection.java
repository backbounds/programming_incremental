package model;

public class ItemCollection {
    public Item item;
    public int number;

    public ItemCollection(Item item, int number) {
        this.item = item;
        this.number = number;
    }

    //REQUIRES: number is positive
    //MODIFIES: this
    //EFFECTS: adds to the existing number of items
    public void addNumber(int number) {
        this.number += number;
    }
}
