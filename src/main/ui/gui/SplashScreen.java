package ui.gui;

import javax.swing.*;

// Represents a Splash Screen
public class SplashScreen extends JWindow {

    // EFFECTS: creates a splash screen
    public SplashScreen() {
        init();
    }

    // EFFECTS: initialize the splash screen
    private void init() {
        JLabel splashLabel = new JLabel(new ImageIcon("./data/UBC-Vancouver-campus.jpg"));
        add(splashLabel);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

