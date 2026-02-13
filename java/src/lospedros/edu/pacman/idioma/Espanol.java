package lospedros.edu.pacman.idioma;

public class Espanol extends Lenguaje {
    @Override public String getTitulo() { return "PAC-MAN: EDICIÓN DANIEL"; }
    @Override public String getNombreBlinky() { return "Blinky (Rojo)"; }
    @Override public String getNombrePinky() { return "Pinky (Rosa)"; }
    @Override public String getNombreInky() { return "Inky (Azul)"; }
    @Override public String getNombreClyde() { return "Clyde (Naranja)"; }
    @Override public String getNombrePacman() { return "Los Pedros Pacman"; }
    @Override public String getMensajePerdiste() { return "¡HAS PERDIDO! GAME OVER"; }
    @Override public String getMensajeGanaste() { return "¡FELICIDADES! HAS GANADO"; }
}