package lospedros.edu.pacman.enemigos;

import java.awt.*;

public class Inky extends Atacante {
    public Inky(int x, int y, int velocidad) {
        super(x, y, velocidad, Color.CYAN);
    }

    @Override
    public void calcularSiguienteMovimiento(int pacmanX, int pacmanY) {
        // Movimiento alternado
        if (Math.random() > 0.5) {
            if (this.x < pacmanX) this.x += velocidad;
            else this.x -= velocidad;
        } else {
            if (this.y < pacmanY) this.y += velocidad;
            else this.y -= velocidad;
        }
    }
}