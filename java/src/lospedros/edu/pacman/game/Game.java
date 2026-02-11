package lospedros.edu.pacman.game;

import lospedros.edu.pacman.map.GameMap;
import lospedros.edu.pacman.map.GamePanel;

import javax.swing.*;

public class Game {

    public void start() {
        SwingUtilities.invokeLater(() -> {
            GameMap map = new GameMap();
            JFrame frame = new JFrame("Pacman");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GamePanel(map));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
