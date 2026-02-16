package lospedros.edu.pacman.game;

import lospedros.edu.pacman.ui.GamePanel;

import javax.swing.*;

public class Game {

    public void start() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pacman");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new GamePanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}