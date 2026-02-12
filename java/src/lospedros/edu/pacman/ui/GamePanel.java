package lospedros.edu.pacman.ui;

import lospedros.edu.pacman.process.CollisionChecker;
import lospedros.edu.pacman.process.Pacman;
import lospedros.edu.pacman.process.ScoreManager;
import lospedros.edu.pacman.data.Item;
import lospedros.edu.pacman.tile.TileManager;
import lospedros.edu.pacman.utils.GameLocale;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

public class GamePanel extends JPanel implements Runnable {

    // Configuración de pantalla
    final int originalTileSize = 16;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale; // 32x32
    public final int maxScreenCol = 21;
    public final int maxScreenRow = 23;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    public TileManager tileM = new TileManager(this);
    public CollisionChecker cChecker = new CollisionChecker(this);

    // --- TU PARTE: Lógica de Puntos y Bonos ---
    public ScoreManager scoreManager = new ScoreManager();
    public String bonusMessage = ""; // Mensaje flotante
    private int bonusTimer = 0;      // Duración del mensaje

    // Entidades
    Pacman player = new Pacman(this, keyH);
    public int lives = 3;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        setupGame();
        startGameThread();
    }

    public void setupGame() {
        // 1. Generar bolitas normales (10 pts) donde hay pasillo (ID 0)
        for (int row = 0; row < maxScreenRow; row++) {
            for (int col = 0; col < maxScreenCol; col++) {
                if (tileM.mapTileNum[col][row] == 0) {
                    int x = col * tileSize;
                    int y = row * tileSize;
                    scoreManager.addItem(x, y, 10, false);
                }
            }
        }

        // 2. Cereza de Bono (100 pts) en el pasillo horizontal central (Fila 9)
        int cherryCol = 10;
        int cherryRow = 9;
        scoreManager.addItem(cherryCol * tileSize, cherryRow * tileSize, 100, true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            update();
            repaint();
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();
        checkItemCollision();
    }

    public void checkItemCollision() {
        for (Item item : scoreManager.getItems()) {
            if (!item.collected) {
                if (Math.abs(player.x - item.x) < tileSize/2 && Math.abs(player.y - item.y) < tileSize/2) {
                    item.collected = true;
                    scoreManager.addPoints(item.points);

                    // Si es cereza, activar mensaje de bono
                    if(item.isBonus) {
                        bonusMessage = "¡BONO +100!";
                        bonusTimer = 90; // Visible por 1.5 segundos
                    }
                }
            }
        }

        // Manejo del tiempo del mensaje
        if (bonusTimer > 0) {
            bonusTimer--;
        } else {
            bonusMessage = "";
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Dibujar el Laberinto
        tileM.draw(g2);

        // Dibujar Bolitas y Cerezas
        for (Item item : scoreManager.getItems()) {
            if (!item.collected) {
                if (item.isBonus) {
                    // Dibujo de Cereza (Dos círculos rojos y tallo)
                    g2.setColor(Color.red);
                    g2.fillOval(item.x + 4, item.y + 10, 12, 12);
                    g2.fillOval(item.x + 16, item.y + 10, 12, 12);
                    g2.setColor(Color.green);
                    g2.fillRect(item.x + 14, item.y + 4, 4, 8);
                } else {
                    g2.setColor(Color.white);
                    g2.fillOval(item.x + 13, item.y + 13, 6, 6);
                }
            }
        }

        // Dibujar Pacman
        player.draw(g2);

        // UI: Score y Vidas
        g2.setColor(Color.yellow);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Score: " + scoreManager.getScore(), 15, 25);
        g2.drawString("Lives: " + lives, screenWidth - 110, 25);

        // UI: Mensaje de Bono flotante
        if (!bonusMessage.isEmpty()) {
            g2.setColor(Color.CYAN);
            g2.setFont(new Font("Arial", Font.ITALIC, 22));
            g2.drawString(bonusMessage, (screenWidth/2) - 60, (screenHeight/2) - 50);
        }

        g2.dispose();
    }
}