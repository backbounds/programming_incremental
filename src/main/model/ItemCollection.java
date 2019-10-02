package model;

import java.io.Serializable;

public class ItemCollection implements Serializable {
    private Item item;
    private int number;

    //EFFECTS: creates a new ItemCollection with a given item and an amount
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
