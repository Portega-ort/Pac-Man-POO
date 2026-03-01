package lospedros.edu.pacman.process;

import lospedros.edu.pacman.data.Entity;
import lospedros.edu.pacman.ui.GamePanel;
import lospedros.edu.pacman.utils.Directions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

public class Pacman extends Entity {

    GamePanel gp;
    Controller controller;
    
    // Command Cache (Queue)
    private LinkedList<Integer> inputQueue = new LinkedList<>();
    private final int MAX_CACHE = 2; // Limit buffer to 2 commands
    
    private int nextDirection = Directions.NONE;

    public Pacman(GamePanel gp, Controller controller) {
        this.gp = gp;
        this.controller = controller;
        
        // Hitbox setup
        solidArea = new Rectangle(1, 1, 30, 30); 
        
        setDefaultValues();
    }

    public void setDefaultValues() {
        x = gp.tileSize * 1; 
        y = gp.tileSize * 1;
        speed = 2;
        direction = Directions.RIGHT;
        nextDirection = Directions.RIGHT;
        inputQueue.clear();
    }

    @Override
    public void update() {
        // 1. Read Input and Add to Queue
        int newDir = Directions.NONE;
        if (controller.isUp()) newDir = Directions.UP;
        else if (controller.isDown()) newDir = Directions.DOWN;
        else if (controller.isLeft()) newDir = Directions.LEFT;
        else if (controller.isRight()) newDir = Directions.RIGHT;

        // Only add if it's a valid direction and different from the last queued one (to avoid spam)
        if (newDir != Directions.NONE) {
            if (inputQueue.isEmpty() || inputQueue.getLast() != newDir) {
                if (inputQueue.size() >= MAX_CACHE) {
                    inputQueue.removeFirst(); // Remove oldest if full
                }
                inputQueue.add(newDir);
            }
        }

        // 2. Process Queue
        if (!inputQueue.isEmpty()) {
            nextDirection = inputQueue.getFirst();
        }

        // 3. Movement Logic
        boolean atCenter = (x % gp.tileSize == 0) && (y % gp.tileSize == 0);

        if (atCenter) {
            // Try to turn to nextDirection
            if (nextDirection != Directions.NONE && !gp.cChecker.isCollision(this, nextDirection)) {
                direction = nextDirection;
                // Consume the command only if we successfully turned
                if (!inputQueue.isEmpty() && inputQueue.getFirst() == nextDirection) {
                    inputQueue.removeFirst();
                }
            }
            
            // Check if we can continue in current direction
            if (gp.cChecker.isCollision(this, direction)) {
                // Stop moving
            } else {
                move();
            }
        } else {
            // Not at center
            if (isOpposite(direction, nextDirection)) {
                direction = nextDirection;
                if (!inputQueue.isEmpty() && inputQueue.getFirst() == nextDirection) {
                    inputQueue.removeFirst();
                }
            }
            
            if (!gp.cChecker.isCollision(this, direction)) {
                move();
            } else {
                // Snap to grid if stuck mid-tile
                int snapX = (x + gp.tileSize/2) / gp.tileSize * gp.tileSize;
                int snapY = (y + gp.tileSize/2) / gp.tileSize * gp.tileSize;
                x = snapX;
                y = snapY;
            }
        }
        
        // Handle tunnel
        if (x <= -gp.tileSize) x = gp.screenWidth;
        if (x >= gp.screenWidth) x = -gp.tileSize;
    }
    
    private void move() {
        switch (direction) {
            case Directions.UP: y -= speed; break;
            case Directions.DOWN: y += speed; break;
            case Directions.LEFT: x -= speed; break;
            case Directions.RIGHT: x += speed; break;
        }
    }
    
    private boolean isOpposite(int dir1, int dir2) {
        if (dir1 == Directions.UP && dir2 == Directions.DOWN) return true;
        if (dir1 == Directions.DOWN && dir2 == Directions.UP) return true;
        if (dir1 == Directions.LEFT && dir2 == Directions.RIGHT) return true;
        if (dir1 == Directions.RIGHT && dir2 == Directions.LEFT) return true;
        return false;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.yellow);
        g2.fillOval(x, y, gp.tileSize, gp.tileSize);
    }
}