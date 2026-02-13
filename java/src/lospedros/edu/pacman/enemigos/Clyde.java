package lospedros.edu.pacman.enemigos;

import java.awt.*;

public class Clyde extends Atacante {
    public Clyde(int x, int y, int velocidad) {
        super(x, y, velocidad, Color.ORANGE);
    }

    @Override
    public void calcularSiguienteMovimiento(int pacmanX, int pacmanY) {
        double distancia = calcularDistancia(pacmanX, pacmanY);

        if (distancia < 120) { // Si está cerca, huye a la esquina inferior
            if (this.x > 0) this.x -= velocidad;
            if (this.y < 500) this.y += velocidad;
        } else { // Si está lejos, persigue
            if (this.x < pacmanX) this.x += velocidad;
            else if (this.x > pacmanX) this.x -= velocidad;
            if (this.y < pacmanY) this.y += velocidad;
            else if (this.y > pacmanY) this.y -= velocidad;
        }
    }
}