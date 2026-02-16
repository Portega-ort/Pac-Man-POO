package lospedros.edu.pacman.enemigos;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Clase abstracta Atacante simplificada.
 * Ahora utiliza círculos de colores en lugar de imágenes.
 */
public abstract class Atacante {
    private int x;
    private int y;
    private int velocidad;
    private final Color color;

    // El constructor ya no recibe un String de imagen, sino un objeto Color
    public Atacante(int x, int y, int velocidad, Color color) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.color = color;
    }

    /**
     * Dibuja un círculo relleno (punto) del color asignado.
     */
    public void dibujar(Graphics g) {
        if (color != null) {
            g.setColor(color);
            // Dibuja un círculo de 25x25 píxeles en la posición actual
            g.fillOval(x, y, 25, 25);
        }
    }

    protected double calcularDistancia(int objetivoX, int objetivoY) {
        return Math.sqrt(Math.pow(objetivoX - x, 2) + Math.pow(objetivoY - y, 2));
    }

    public abstract void calcularSiguienteMovimiento(int pacmanX, int pacmanY);

    // Getters y Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public Color getColor() {
        return color;
    }
}