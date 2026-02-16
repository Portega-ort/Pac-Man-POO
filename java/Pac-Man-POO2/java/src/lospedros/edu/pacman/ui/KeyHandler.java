package lospedros.edu.pacman.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean arribaPresionado;
    public boolean abajoPresionado;
    public boolean izquierdaPresionada;
    public boolean derechaPresionada;
    public boolean enterPresionado;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            arribaPresionado = true;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            abajoPresionado = true;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            izquierdaPresionada = true;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            derechaPresionada = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPresionado = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
            arribaPresionado = false;
        }
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
            abajoPresionado = false;
        }
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
            izquierdaPresionada = false;
        }
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
            derechaPresionada = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            enterPresionado = false;
        }
    }
}