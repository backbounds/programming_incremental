package model;

import java.io.Serializable;

public abstract class Purchasable implements Serializable {
    private String name;
    private double cost;
    private double income;

    public Purchasable(String name, int cost, double income) {
        this.name = name;
        this.cost = cost;
        this.income = income;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
