package model;

import java.io.Serializable;

public abstract class Purchasable implements Serializable {
    private String name;
    private int cost;
    private double income;

    public Purchasable(String name, int cost, double income) {
        this.name = name;
        this.cost = cost;
        this.income = income;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
