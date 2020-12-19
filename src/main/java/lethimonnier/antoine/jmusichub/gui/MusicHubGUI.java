package lethimonnier.antoine.jmusichub.gui;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * The type Music hub gui.
 */
public class MusicHubGUI extends JFrame {

    private MusicHubGUI(String title) {
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(new Dimension(640, 480)); // À changer ?
        setWindowIcon();
        changeLook();
        init();

        // pack(); <- peut être utile en cas de non-fonctionnement du programme
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MusicHubGUI(""));
    }

    private void setWindowIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(""))); // Chemin du .png/.jpg/
            // .ico[/...] à définir en icône
        } catch (Exception e) {
            Logger.getGlobal().warning("[WINDOW_ICON] L'image spécifiée est introuvable."); // Remplaçable par un throw
        }
    }

    private void changeLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // TODO : Créér UI
    }
}
