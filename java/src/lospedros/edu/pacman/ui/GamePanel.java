package lospedros.edu.pacman.ui;

import lospedros.edu.pacman.process.Pacman;
import lospedros.edu.pacman.utils.GameLocale;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;

public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    Thread gameThread;
    KeyHandler keyH = new KeyHandler();
    Pacman player = new Pacman(this, keyH);
    
    // Game State
    public int score = 0;
    public int lives = 3;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startGameThread();
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
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Draw Player
        player.draw(g2);
        
        // Draw UI
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString(GameLocale.getString("game.score") + score, 10, 20);
        g2.drawString(GameLocale.getString("game.lives") + lives, screenWidth - 100, 20);

        g2.dispose();
    }
}