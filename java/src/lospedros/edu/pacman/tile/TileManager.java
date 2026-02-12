package lospedros.edu.pacman.tile;

import lospedros.edu.pacman.ui.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];

        getTileImage();
        loadMap();
    }

    public void getTileImage() {
        // 0 = floor, 1 = wall
        tile[0] = new Tile();
        tile[0].collision = false;

        tile[1] = new Tile();
        tile[1].collision = true;
    }

    public void loadMap() {
        // 21 columns x 23 rows
        int[][] defaultMap = {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,0,1},
            {1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,0,1},
            {1,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,1},
            {1,1,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,0,0,0,0,0,0,0,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,0,1,1,0,1,1,0,1,0,1,1,1,1,1},
            {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,0}, // Tunnel area
            {1,1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,0,0,0,0,0,0,0,1,0,1,1,1,1,1},
            {1,1,1,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,1,0,1},
            {1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,1},
            {1,1,1,0,1,0,1,0,1,1,1,1,1,0,1,0,1,0,1,1,1},
            {1,0,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,0,1,0,1,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
        
        for(int row = 0; row < gp.maxScreenRow; row++) {
            for(int col = 0; col < gp.maxScreenCol; col++) {
                if (row < defaultMap.length && col < defaultMap[0].length) {
                    mapTileNum[col][row] = defaultMap[row][col];
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
            int tileNum = mapTileNum[col][row];

            if (tileNum == 1) {
                g2.setColor(Color.BLUE);
            } else {
                g2.setColor(Color.BLACK);
            }
            g2.fillRect(x, y, gp.tileSize, gp.tileSize);

            col++;
            x += gp.tileSize;

            if (col == gp.maxScreenCol) {
                col = 0;
                x = 0;
                row++;
                y += gp.tileSize;
            }
        }
    }
}