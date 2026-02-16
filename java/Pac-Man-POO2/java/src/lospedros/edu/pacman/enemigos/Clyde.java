package lospedros.edu.pacman.enemigos;

import java.awt.Color;

public class Clyde extends Atacante {
    public Clyde(int x, int y, int velocidad) {
        super(x, y, velocidad, Color.ORANGE);
    }

    @Override
    public void calcularSiguienteMovimiento(int pacmanX, int pacmanY) {
        double distancia = calcularDistancia(pacmanX, pacmanY);

        if (distancia < 120) { // Si está cerca, huye a la esquina inferior
            if (getX() > 0) {
                setX(getX() - getVelocidad());
            }
            if (getY() < 500) {
                setY(getY() + getVelocidad());
            }
        } else { // Si está lejos, persigue
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
}