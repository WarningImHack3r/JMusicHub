package lethimonnier.antoine.jmusichub.gui;

import javax.swing.SwingUtilities;

public class MusicHubGUI {

    private MusicHubGUI() {}
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicHubGUI::new);
    }
}
