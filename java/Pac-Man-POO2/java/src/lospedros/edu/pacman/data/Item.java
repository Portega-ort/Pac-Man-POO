package lospedros.edu.pacman.data;

public class Item {
    private final int x;
    private final int y;
    private final int points;
    private final boolean bonus;
    private boolean collected;

    public Item(int x, int y, int points, boolean bonus) {
        this.x = x;
        this.y = y;
        this.points = points;
        this.bonus = bonus;
        this.collected = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPoints() {
        return points;
    }

    public boolean isBonus() {
        return bonus;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}