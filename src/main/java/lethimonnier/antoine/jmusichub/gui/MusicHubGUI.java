package lethimonnier.antoine.jmusichub.gui;

import javax.swing.*;

import lethimonnier.antoine.jmusichub.gui.frontend.ComponentsInitializer;

import java.awt.*;
import java.util.logging.Logger;

/**
 * The type Music hub gui.
 */
public class MusicHubGUI extends JFrame {

    private static final long serialVersionUID = 5930451128361756517L;
    private transient ComponentsInitializer ci = new ComponentsInitializer();

    /**
     * GUI Constructor
     *
     * @param title the window title
     */
    private MusicHubGUI(String title) {
        super(title);

        initLayout();
        setJMenuBar(ci.initMenuBar());
        getContentPane().add(ci.initComponents());
    }

    /**
     * Sets the icon if the window (replacing Java's default one)
     *
     * @param path the path of the icon to use
     */
    private void setWindowIcon(String path) {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(path)));
        } catch (Exception e) {
            Logger.getGlobal().warning("[WINDOW_ICON] L'image spécifiée est introuvable.");
        }
    }

    /**
     * Makes the GUI use the system's Look And Feel (LAF)
     */
    private void changeLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            ci.error("Look And Feel", e.getMessage());
        }
    }

    /**
     * Layout Initialization
     */
    private void initLayout() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(1, 1));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension((int) screenSize.getWidth() * 2 / 3, (int) screenSize.getHeight() * 2 / 3));
        setWindowIcon("icons/icon.png");
        changeLook();

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
        SwingUtilities.invokeLater(() -> new MusicHubGUI("JMusicHub"));
    }
}
