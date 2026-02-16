package lospedros.edu.pacman.ui;

import lospedros.edu.pacman.utils.GameLocale;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    private GamePanel gamePanel;

    public MainWindow() {
        // Inicializar idioma por defecto
        GameLocale.setLocale("en");

        initUI();
    }

    private void initUI() {
        setTitle(GameLocale.getString("window.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Crear barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu languageMenu = new JMenu(GameLocale.getString("menu.language"));

        JMenuItem enItem = new JMenuItem(GameLocale.getString("menu.language.en"));
        enItem.addActionListener(e -> changeLanguage("en"));

        JMenuItem esItem = new JMenuItem(GameLocale.getString("menu.language.es"));
        esItem.addActionListener(e -> changeLanguage("es"));

        languageMenu.add(enItem);
        languageMenu.add(esItem);
        menuBar.add(languageMenu);
        setJMenuBar(menuBar);

        if (gamePanel == null) {
            gamePanel = new GamePanel();
            add(gamePanel);
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void changeLanguage(String lang) {
        GameLocale.setLocale(lang);
        // Actualizar título y menú
        setTitle(GameLocale.getString("window.title"));
        JMenuBar menuBar = getJMenuBar();
        JMenu languageMenu = menuBar.getMenu(0);
        languageMenu.setText(GameLocale.getString("menu.language"));
        languageMenu.getItem(0).setText(GameLocale.getString("menu.language.en"));
        languageMenu.getItem(1).setText(GameLocale.getString("menu.language.es"));
        
        // Aquí podrías notificar al GamePanel si necesita actualizar textos internos
        // gamePanel.updateTexts(); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}