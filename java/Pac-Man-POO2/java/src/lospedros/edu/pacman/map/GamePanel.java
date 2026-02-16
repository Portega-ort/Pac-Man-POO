package lospedros.edu.pacman.map;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private final GameMap map;
    private final int tileSize = 30;

    public GamePanel(GameMap map) {
        this.map = map;
        setPreferredSize(new Dimension(
                map.getCols() * tileSize,
                map.getRows() * tileSize
        ));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[][] grid = map.getGrid();

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                if (grid[r][c] == GameMap.WALL) {
                    g.setColor(Color.BLUE);
                    g.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
                } else if (grid[r][c] == GameMap.GHOST_EXIT) {
                    g.setColor(Color.RED);
                    g.fillOval(c * tileSize + 8, r * tileSize + 8, 14, 14);
                }
            }
        }
    }
}
