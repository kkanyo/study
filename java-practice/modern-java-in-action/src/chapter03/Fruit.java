package chapter03;

public class Fruit {
    enum Color { RED, GREEN }

    Color color;
    Integer weight;
    int count;

    public Fruit(Color color, Integer weight, int count) {
        this.color = color;
        this.weight = weight;
        this.count = count;
    }

    public Color getColor() {
        return color;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void printFruit() {
        System.out.println(this.count + " " + this.color + " fruit of " + this.weight);
    }
}
