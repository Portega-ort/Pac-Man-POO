package lospedros.edu.pacman.enemigos;

import java.awt.Color;

public class Blinky extends Atacante {
    public Blinky(int x, int y, int velocidad) {
        super(x, y, velocidad, Color.RED);
    }

    @Override
    public void calcularSiguienteMovimiento(int pacmanX, int pacmanY) {
        if (getX() < pacmanX) {
            setX(getX() + getVelocidad());
        } else if (getX() > pacmanX) {
            setX(getX() - getVelocidad());
        }

        if (getY() < pacmanY) {
            setY(getY() + getVelocidad());
        } else if (getY() > pacmanY) {
            setY(getY() - getVelocidad());
        }
    }
}