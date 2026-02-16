package lospedros.edu.pacman.enemigos;

import java.awt.Color;

public class Inky extends Atacante {
    public Inky(int x, int y, int velocidad) {
        super(x, y, velocidad, Color.CYAN);
    }

    @Override
    public void calcularSiguienteMovimiento(int pacmanX, int pacmanY) {
        // Movimiento alternado
        if (Math.random() > 0.5) {
            if (getX() < pacmanX) {
                setX(getX() + getVelocidad());
            } else {
                setX(getX() - getVelocidad());
            }
        } else {
            if (getY() < pacmanY) {
                setY(getY() + getVelocidad());
            } else {
                setY(getY() - getVelocidad());
            }
        }
    }
}