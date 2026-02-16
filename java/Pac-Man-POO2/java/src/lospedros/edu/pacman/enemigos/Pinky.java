package lospedros.edu.pacman.enemigos;

import java.awt.Color;

public class Pinky extends Atacante {
    public Pinky(int x, int y, int velocidad) {
        super(x, y, velocidad, Color.PINK);
    }

    @Override
    public void calcularSiguienteMovimiento(int pacmanX, int pacmanY) {
        // Intenta adelantarse 4 casillas (aprox 60px)
        int objetivoX = pacmanX + 60;
        if (getX() < objetivoX) {
            setX(getX() + getVelocidad());
        } else if (getX() > objetivoX) {
            setX(getX() - getVelocidad());
        }

        if (getY() < pacmanY) {
            setY(getY() + getVelocidad());
        } else if (getY() > pacmanY) {
            setY(getY() - getVelocidad());
        }
    }
}