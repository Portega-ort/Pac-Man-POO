package lospedros.edu.pacman.data;

public class Item {
    public int x, y;
    public int points;
    public boolean isBonus; //
    public boolean collected = false;

    public Item(int x, int y, int points, boolean isBonus) {
        this.x = x;
        this.y = y;
        this.points = points;
        this.isBonus = isBonus;
    }
}