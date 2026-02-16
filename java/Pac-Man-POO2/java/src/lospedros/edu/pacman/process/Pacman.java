package lospedros.edu.pacman.process;

import lospedros.edu.pacman.data.Entity;
import lospedros.edu.pacman.ui.GamePanel;
import lospedros.edu.pacman.ui.KeyHandler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Pacman extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    public String siguienteDireccion;

    public Pacman(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        // Configurar la hitbox (20x20 centrada en el tile 32x32)
        setSolidArea(new Rectangle(6, 6, 20, 20));

        setDefaultValues();
    }

    public void setDefaultValues() {
        setX(gp.tileSize * 1);
        setY(gp.tileSize * 1);
        setSpeed(2);
        setDirection("right");
        siguienteDireccion = "right";
    }

    public void update() {
        // Leer entrada
        if (keyH.arribaPresionado) siguienteDireccion = "up";
        if (keyH.abajoPresionado) siguienteDireccion = "down";
        if (keyH.izquierdaPresionada) siguienteDireccion = "left";
        if (keyH.derechaPresionada) siguienteDireccion = "right";

        // Verificar si se puede girar
        boolean puedeGirar = false;

        // Permitir giro si estamos cerca del centro del tile
        int restoX = getX() % gp.tileSize;
        int restoY = getY() % gp.tileSize;
        int velocidad = getSpeed();

        // Lógica de tolerancia: si el resto es pequeño (menor que la velocidad), considerarlo alineado
        if (Math.abs(restoX) < velocidad && Math.abs(restoY) < velocidad) {
            puedeGirar = true;
        }

        if (puedeGirar) {
            String direccionAnterior = getDirection();
            setDirection(siguienteDireccion);

            // Verificar colisión en la NUEVA dirección
            setCollisionOn(false);
            gp.cChecker.checkTile(this);

            if (isCollisionOn()) {
                // Si no se puede mover en la siguiente dirección, volver a la anterior
                setDirection(direccionAnterior);
            } else {
                // Si el giro es exitoso, alinear a la grilla para evitar desplazamientos
                // Esto asegura que estemos perfectamente alineados para el nuevo eje
                if (getDirection().equals("up") || getDirection().equals("down")) {
                    setX((getX() + gp.tileSize / 2) / gp.tileSize * gp.tileSize);
                } else {
                    setY((getY() + gp.tileSize / 2) / gp.tileSize * gp.tileSize);
                }
            }
        } else if (esOpuesta(getDirection(), siguienteDireccion)) {
            // Permitir giro en U inmediato en cualquier lugar
            setDirection(siguienteDireccion);
        }

        // Mover en la dirección actual
        setCollisionOn(false);
        gp.cChecker.checkTile(this);

        if (!isCollisionOn()) {
            switch (getDirection()) {
                case "up":
                    setY(getY() - velocidad);
                    break;
                case "down":
                    setY(getY() + velocidad);
                    break;
                case "left":
                    setX(getX() - velocidad);
                    break;
                case "right":
                    setX(getX() + velocidad);
                    break;
            }
        } else {
            // COLISIÓN DETECTADA
            // Ajustar a la grilla más cercana para asegurar giro en el siguiente intento
            int snapX = (getX() + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
            int snapY = (getY() + gp.tileSize / 2) / gp.tileSize * gp.tileSize;
            setX(snapX);
            setY(snapY);
        }

        // Manejar túnel
        if (getX() < -gp.tileSize) setX(gp.screenWidth);
        if (getX() > gp.screenWidth) setX(-gp.tileSize);
    }

    private boolean esOpuesta(String dir1, String dir2) {
        if (dir1.equals("up") && dir2.equals("down")) return true;
        if (dir1.equals("down") && dir2.equals("up")) return true;
        if (dir1.equals("left") && dir2.equals("right")) return true;
        if (dir1.equals("right") && dir2.equals("left")) return true;
        return false;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.yellow);
        g2.fillOval(getX(), getY(), gp.tileSize, gp.tileSize);
    }
}