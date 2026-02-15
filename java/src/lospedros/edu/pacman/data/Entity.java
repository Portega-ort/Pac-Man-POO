package lospedros.edu.pacman.data;

import java.awt.Rectangle;

public class Entity {
    public int x, y;
    public int speed;
    
    public String direction;
    public Rectangle solidArea;
    public boolean collisionOn = false;
}