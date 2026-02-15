package lospedros.edu.pacman.enemigos;

import java.awt.*;

public class Pinky extends Atacante {
    public Pinky(int x, int y, int velocidad) {
        super(x, y, velocidad, Color.PINK);
    }

    @Override
    public void calcularSiguienteMovimiento(int pacmanX, int pacmanY) {
        // Intenta adelantarse 4 casillas (aprox 60px)
        int objetivoX = pacmanX + 60;
        if (this.x < objetivoX) this.x += velocidad;
        else if (this.x > objetivoX) this.x -= velocidad;

        if (this.y < pacmanY) this.y += velocidad;
        else if (this.y > pacmanY) this.y -= velocidad;
    }
}