package lospedros.edu.pacman.process;

import lospedros.edu.pacman.data.Entity;
import lospedros.edu.pacman.ui.GamePanel;
import lospedros.edu.pacman.ui.KeyHandler;

import java.awt.Color;
import java.awt.Graphics2D;

public class Pacman extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public Pacman(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
    }

    public void update() {
        if (keyH.upPressed) {
            y -= speed;
        } else if (keyH.downPressed) {
            y += speed;
        } else if (keyH.leftPressed) {
            x -= speed;
        } else if (keyH.rightPressed) {
            x += speed;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.yellow);
        g2.fillOval(x, y, gp.tileSize, gp.tileSize);
    }
}