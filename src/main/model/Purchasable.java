package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Observable;

public abstract class Purchasable implements Serializable {
    private String name;
    private double cost;
    private double income;

    Purchasable(String name, int cost, double income) {
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

    void setCost(double cost) {
        this.cost = cost;
    }

    public double getIncome() {
        return income;
    }

    void setIncome(double income) {
        this.income = income;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Purchasable that = (Purchasable) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void printInformation() {
        System.out.printf("\tName: %s\n \tCost: %s\n", name, cost);
    }
}
