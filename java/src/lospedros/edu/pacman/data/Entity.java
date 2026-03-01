package lospedros.edu.pacman.data;

import lospedros.edu.pacman.utils.Directions;
import java.awt.Rectangle;

public abstract class Entity {
    public int x, y;
    public int speed;
    public int direction = Directions.NONE;
    public Rectangle solidArea;
    
    public abstract void update();
}