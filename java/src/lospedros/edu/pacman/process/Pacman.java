package lospedros.edu.pacman.process;

import lospedros.edu.pacman.data.Entity;
import lospedros.edu.pacman.ui.GamePanel;
import lospedros.edu.pacman.ui.KeyHandler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Pacman extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    
    public String nextDirection;

    public Pacman(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
        
        // Hitbox setup (20x20 centered in 32x32 tile)
        solidArea = new Rectangle(6, 6, 20, 20); 
        
        setDefaultValues();
    }

    public void setDefaultValues() {
        x = gp.tileSize * 1; 
        y = gp.tileSize * 1;
        speed = 2;
        direction = "right";
        nextDirection = "right";
    }

    public void update() {
        // Read input
        if (keyH.upPressed) nextDirection = "up";
        if (keyH.downPressed) nextDirection = "down";
        if (keyH.leftPressed) nextDirection = "left";
        if (keyH.rightPressed) nextDirection = "right";

        // Check if we can turn
        boolean canTurn = false;
        
        // Allow turning if we are close to the center of the tile
        int xRem = x % gp.tileSize;
        int yRem = y % gp.tileSize;
        
        // Tolerance logic: if remainder is small (less than speed), consider it aligned
        if (Math.abs(xRem) < speed && Math.abs(yRem) < speed) {
            canTurn = true;
        }

        if (canTurn) {
            String prevDir = direction;
            direction = nextDirection;
            
            // Check collision in the NEW direction
            collisionOn = false;
            gp.cChecker.checkTile(this);
            
            if (collisionOn) {
                // If we can't move in nextDirection, revert to previous
                direction = prevDir;
            } else {
                // If turn is successful, snap to exact grid to prevent drift
                // This ensures we are perfectly aligned for the new axis
                if (direction.equals("up") || direction.equals("down")) {
                    x = (x + gp.tileSize/2) / gp.tileSize * gp.tileSize;
                } else {
                    y = (y + gp.tileSize/2) / gp.tileSize * gp.tileSize;
                }
            }
        } else if (isOpposite(direction, nextDirection)) {
            // Allow immediate U-turn anywhere
            direction = nextDirection;
        }

        // Move in current direction
        collisionOn = false;
        gp.cChecker.checkTile(this);

        if (!collisionOn) {
            switch (direction) {
                case "up": y -= speed; break;
                case "down": y += speed; break;
                case "left": x -= speed; break;
                case "right": x += speed; break;
            }
        } else {
            // COLLISION DETECTED
            // Snap to the nearest grid position to ensure we can turn next time
            int snapX = (x + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
            int snapY = (y + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
            x = snapX;
            y = snapY;
        }
        
        // Handle tunnel
        if (x < -gp.tileSize) x = gp.screenWidth;
        if (x > gp.screenWidth) x = -gp.tileSize;
    }
    
    private boolean isOpposite(String dir1, String dir2) {
        if (dir1.equals("up") && dir2.equals("down")) return true;
        if (dir1.equals("down") && dir2.equals("up")) return true;
        if (dir1.equals("left") && dir2.equals("right")) return true;
        if (dir1.equals("right") && dir2.equals("left")) return true;
        return false;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.yellow);
        g2.fillOval(x, y, gp.tileSize, gp.tileSize);
    }
}