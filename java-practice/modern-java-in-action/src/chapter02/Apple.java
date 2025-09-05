package chapter02;

public class Apple {
    enum Color { RED, GREEN }

    Color color;
    Integer weight;

    public Apple(Color color, Integer weight) {
        this.color = color;
        this.weight = weight;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public Integer getWeight() {
        return weight;
    }
}
