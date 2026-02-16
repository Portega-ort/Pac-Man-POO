package lospedros.edu.pacman.ui;

import lospedros.edu.pacman.audio.SoundManager;
import lospedros.edu.pacman.enemigos.Atacante;
import lospedros.edu.pacman.enemigos.Blinky;
import lospedros.edu.pacman.enemigos.Clyde;
import lospedros.edu.pacman.enemigos.Inky;
import lospedros.edu.pacman.enemigos.Pinky;
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
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    // Configuración de pantalla
    final int originalTileSize = 16;
    final int scale = 2;
    public final int tileSize = originalTileSize * scale; // 32x32
    public final int maxScreenCol = 21;
    public final int maxScreenRow = 23;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    private static final int ENEMY_SIZE = 25;
    private static final int MIN_SPAWN_DISTANCE_FROM_CENTER = 4;
    private static final int[][] ENEMY_SPAWN_OFFSETS = new int[][]{
            {0, 0},
            {-1, 0},
            {1, 0},
            {0, 1}
    };
    private static final String LANG_ES = "es";
    private static final String LANG_EN = "en";
    private final int[][] enemySpawns;

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
    private final Atacante[] enemigos;
    public int lives = 3;

    private final SoundManager soundManager = new SoundManager();
    private boolean juegoTerminado = false;
    private boolean enInicio = true;
    private final Random random = new Random();
    private int[][] objetivosInicio;
    private String idiomaSeleccionado = LANG_ES;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        enemySpawns = initEnemySpawns();

        enemigos = new Atacante[]{
                new Blinky(tileSize * enemySpawns[0][0], tileSize * enemySpawns[0][1], 1),
                new Pinky(tileSize * enemySpawns[1][0], tileSize * enemySpawns[1][1], 1),
                new Inky(tileSize * enemySpawns[2][0], tileSize * enemySpawns[2][1], 1),
                new Clyde(tileSize * enemySpawns[3][0], tileSize * enemySpawns[3][1], 1)
        };

        GameLocale.setLocale(idiomaSeleccionado);
        initObjetivosInicio();
        setupGame();
        soundManager.playIntro();
        soundManager.startSirenLoop();
        startGameThread();
    }

    public void setupGame() {
        // 1. Generar bolitas normales (10 pts) donde hay pasillo (ID 0)
        int[][] mapTileNum = tileM.getMapTileNum();
        for (int row = 0; row < maxScreenRow; row++) {
            for (int col = 0; col < maxScreenCol; col++) {
                if (mapTileNum[col][row] == 0) {
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
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (juegoTerminado) {
            return;
        }

        if (enInicio) {
            if (keyH.izquierdaPresionada) {
                setIdioma(LANG_ES);
                keyH.izquierdaPresionada = false;
            }
            if (keyH.derechaPresionada) {
                setIdioma(LANG_EN);
                keyH.derechaPresionada = false;
            }

            if (keyH.enterPresionado) {
                enInicio = false;
                player.setDefaultValues();
                resetEnemyPositions();
                initObjetivosInicio();
            }

            for (int i = 0; i < enemigos.length; i++) {
                Atacante enemigo = enemigos[i];
                int prevX = enemigo.getX();
                int prevY = enemigo.getY();
                moverEnemigoInicio(i, enemigo);
                if (isEnemyColliding(enemigo)) {
                    enemigo.setX(prevX);
                    enemigo.setY(prevY);
                    asignarNuevoObjetivoInicio(i);
                }
            }
            return;
        }

        player.update();
        for (Atacante enemigo : enemigos) {
            int prevX = enemigo.getX();
            int prevY = enemigo.getY();
            enemigo.calcularSiguienteMovimiento(player.getX(), player.getY());
            if (isEnemyColliding(enemigo)) {
                enemigo.setX(prevX);
                enemigo.setY(prevY);
            }
            if (isEnemyHittingPlayer(enemigo)) {
                handlePlayerHit();
                break;
            }
        }
        checkItemCollision();
    }

    public void checkItemCollision() {
        for (Item item : scoreManager.getItems()) {
            if (!item.isCollected()) {
                if (Math.abs(player.getX() - item.getX()) < tileSize / 2 && Math.abs(player.getY() - item.getY()) < tileSize / 2) {
                    item.setCollected(true);
                    scoreManager.addPoints(item.getPoints());
                    soundManager.playChomp();

                    // Si es cereza, activar mensaje de bono
                    if (item.isBonus()) {
                        bonusMessage = "¡BONO +100!";
                        bonusTimer = 90; // Visible por 1.5 segundos
                        soundManager.playBonus();
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
            if (!item.isCollected()) {
                if (item.isBonus()) {
                    // Dibujo de Cereza (Dos círculos rojos y tallo)
                    g2.setColor(Color.red);
                    g2.fillOval(item.getX() + 4, item.getY() + 10, 12, 12);
                    g2.fillOval(item.getX() + 16, item.getY() + 10, 12, 12);
                    g2.setColor(Color.green);
                    g2.fillRect(item.getX() + 14, item.getY() + 4, 4, 8);
                } else {
                    g2.setColor(Color.white);
                    g2.fillOval(item.getX() + 13, item.getY() + 13, 6, 6);
                }
            }
        }

        // Dibujar Pacman
        player.draw(g2);

        // Dibujar Enemigos
        for (Atacante enemigo : enemigos) {
            enemigo.dibujar(g2);
        }

        // UI: Score y Vidas
        g2.setColor(Color.yellow);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Score: " + scoreManager.getScore(), 15, 25);
        g2.drawString("Lives: " + lives, screenWidth - 110, 25);

        // UI: Mensaje de Bono flotante
        if (!bonusMessage.isEmpty()) {
            g2.setColor(Color.CYAN);
            g2.setFont(new Font("Arial", Font.ITALIC, 22));
            g2.drawString(bonusMessage, (screenWidth / 2) - 60, (screenHeight / 2) - 50);
        }

        if (enInicio) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            String inicioTexto = idiomaSeleccionado.equals(LANG_ES) ? "PRESIONA ENTER PARA INICIAR" : "PRESS ENTER TO START";
            g2.drawString(inicioTexto, (screenWidth / 2) - 190, (screenHeight / 2));

            g2.setFont(new Font("Arial", Font.BOLD, 20));
            String idiomaTexto = idiomaSeleccionado.equals(LANG_ES)
                    ? "IDIOMA: [ESPAÑOL] / ENGLISH"
                    : "LANGUAGE: ENGLISH / [ESPAÑOL]";
            g2.drawString(idiomaTexto, (screenWidth / 2) - 190, (screenHeight / 2) + 35);

            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            String ayudaTexto = idiomaSeleccionado.equals(LANG_ES) ? "A/D para cambiar" : "A/D to change";
            g2.drawString(ayudaTexto, (screenWidth / 2) - 70, (screenHeight / 2) + 60);
        }

        if (juegoTerminado) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            g2.drawString("FIN DEL JUEGO", (screenWidth / 2) - 130, (screenHeight / 2));
        }

        g2.dispose();
    }

    private boolean isEnemyColliding(Atacante enemigo) {
        int leftCol = enemigo.getX() / tileSize;
        int rightCol = (enemigo.getX() + ENEMY_SIZE - 1) / tileSize;
        int topRow = enemigo.getY() / tileSize;
        int bottomRow = (enemigo.getY() + ENEMY_SIZE - 1) / tileSize;

        return isWallTile(leftCol, topRow)
                || isWallTile(rightCol, topRow)
                || isWallTile(leftCol, bottomRow)
                || isWallTile(rightCol, bottomRow);
    }

    private boolean isWallTile(int col, int row) {
        if (col < 0 || row < 0 || col >= maxScreenCol || row >= maxScreenRow) {
            return true;
        }
        int tileNum = tileM.getMapTileNum()[col][row];
        return tileM.getTile(tileNum) != null && tileM.getTile(tileNum).isCollision();
    }

    private boolean isEnemyHittingPlayer(Atacante enemigo) {
        return Math.abs(enemigo.getX() - player.getX()) < tileSize / 2
                && Math.abs(enemigo.getY() - player.getY()) < tileSize / 2;
    }

    private void handlePlayerHit() {
        lives = Math.max(0, lives - 1);
        if (lives == 0) {
            juegoTerminado = true;
            soundManager.stopSirenLoop();
            soundManager.playDeath();
            return;
        }
        player.setDefaultValues();
        resetEnemyPositions();
    }

    private void resetEnemyPositions() {
        for (int i = 0; i < enemigos.length; i++) {
            enemigos[i].setX(tileSize * enemySpawns[i][0]);
            enemigos[i].setY(tileSize * enemySpawns[i][1]);
        }
    }

    private int[][] initEnemySpawns() {
        int[][] spawns = new int[ENEMY_SPAWN_OFFSETS.length][2];
        boolean[][] used = new boolean[maxScreenCol][maxScreenRow];
        int centerCol = maxScreenCol / 2;
        int centerRow = maxScreenRow / 2;

        for (int i = 0; i < ENEMY_SPAWN_OFFSETS.length; i++) {
            int startCol = centerCol + ENEMY_SPAWN_OFFSETS[i][0];
            int startRow = centerRow + ENEMY_SPAWN_OFFSETS[i][1];
            int[] spawn = findOpenSpawn(startCol, startRow, centerCol, centerRow, MIN_SPAWN_DISTANCE_FROM_CENTER, used);
            spawns[i][0] = spawn[0];
            spawns[i][1] = spawn[1];
            used[spawn[0]][spawn[1]] = true;
        }

        return spawns;
    }

    private int[] findOpenSpawn(int startCol, int startRow, int centerCol, int centerRow, int minDistance, boolean[][] used) {
        int[][] mapTileNum = tileM.getMapTileNum();
        int maxRadius = Math.max(maxScreenCol, maxScreenRow);

        for (int radius = minDistance; radius <= maxRadius; radius++) {
            for (int row = startRow - radius; row <= startRow + radius; row++) {
                for (int col = startCol - radius; col <= startCol + radius; col++) {
                    if (col < 0 || row < 0 || col >= maxScreenCol || row >= maxScreenRow) {
                        continue;
                    }
                    if (used[col][row]) {
                        continue;
                    }
                    int distance = Math.abs(col - centerCol) + Math.abs(row - centerRow);
                    if (distance < minDistance) {
                        continue;
                    }
                    if (mapTileNum[col][row] == 0) {
                        return new int[]{col, row};
                    }
                }
            }
        }

        return new int[]{1, 1};
    }

    private void initObjetivosInicio() {
        objetivosInicio = new int[enemigos.length][2];
        for (int i = 0; i < enemigos.length; i++) {
            asignarNuevoObjetivoInicio(i);
        }
    }

    private void asignarNuevoObjetivoInicio(int index) {
        int[] objetivo = buscarTileLibreAleatorio();
        objetivosInicio[index][0] = objetivo[0] * tileSize;
        objetivosInicio[index][1] = objetivo[1] * tileSize;
    }

    private int[] buscarTileLibreAleatorio() {
        int[][] mapa = tileM.getMapTileNum();
        while (true) {
            int col = random.nextInt(maxScreenCol);
            int row = random.nextInt(maxScreenRow);
            if (mapa[col][row] == 0) {
                return new int[]{col, row};
            }
        }
    }

    private void moverEnemigoInicio(int index, Atacante enemigo) {
        int objetivoX = objetivosInicio[index][0];
        int objetivoY = objetivosInicio[index][1];

        int dx = Integer.compare(objetivoX, enemigo.getX());
        int dy = Integer.compare(objetivoY, enemigo.getY());

        enemigo.setX(enemigo.getX() + dx * enemigo.getVelocidad());
        enemigo.setY(enemigo.getY() + dy * enemigo.getVelocidad());

        if (Math.abs(enemigo.getX() - objetivoX) <= enemigo.getVelocidad()
                && Math.abs(enemigo.getY() - objetivoY) <= enemigo.getVelocidad()) {
            asignarNuevoObjetivoInicio(index);
        }
    }

    private void setIdioma(String idioma) {
        if (!idiomaSeleccionado.equals(idioma)) {
            idiomaSeleccionado = idioma;
            GameLocale.setLocale(idioma);
        }
    }
}