package lospedros.edu.pacman.enemigos;

import java.awt.*;

public class Blinky extends Atacante {
    public Blinky(int x, int y, int velocidad) {
        super(x, y, velocidad, Color.RED);
    }

    @Override
    public void calcularSiguienteMovimiento(int pacmanX, int pacmanY) {
        if (this.x < pacmanX) this.x += velocidad;
        else if (this.x > pacmanX) this.x -= velocidad;

        if (this.y < pacmanY) this.y += velocidad;
        else if (this.y > pacmanY) this.y -= velocidad;
    }
}