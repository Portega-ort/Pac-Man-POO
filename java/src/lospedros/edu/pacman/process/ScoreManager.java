package lospedros.edu.pacman.process;

import lospedros.edu.pacman.data.Item;
import java.util.ArrayList;

public class ScoreManager {
    private int currentScore = 0;
    private ArrayList<Item> items;

    public ScoreManager() {
        items = new ArrayList<>();
    }

    public void addPoints(int points) {
        this.currentScore += points;
    }

    public int getScore() {
        return currentScore;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(int x, int y, int points, boolean isBonus) {
        items.add(new Item(x, y, points, isBonus));
    }

    public void reset() {
        currentScore = 0;
        items.clear();
    }
}
