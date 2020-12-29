package lethimonnier.antoine.jmusichub.gui;

import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;

import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.enums.Language;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.logging.Logger;

/**
 * The type Music hub gui.
 */
public class MusicHubGUI extends JFrame {

    private static final long serialVersionUID = 5930451128361756517L;

    private static final String ADD_LABEL = "Add";
    private static final String REMOVE_LABEL = "Remove";
    private static final String VIEW_LABEL = "View";

    private MusicHubGUI(String title) {
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridLayout(1, 1));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension((int) screenSize.getWidth() * 2 / 3, (int) screenSize.getHeight() * 2 / 3));
        setWindowIcon();
        changeLook();
        init();

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void setWindowIcon() {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icons/icon.png")));
        } catch (Exception e) {
            Logger.getGlobal().warning("[WINDOW_ICON] L'image spécifiée est introuvable.");
        }
    }

    private void changeLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // Menubar
        JMenuBar menubar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem importItem = new JMenuItem("Import");
        importItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_I, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        JMenuItem exportItem = new JMenuItem("Export");
        exportItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menubar.add(fileMenu);
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(quitItem);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);
        menubar.add(helpMenu);

        importItem.addActionListener(e -> {
        });

        exportItem.addActionListener(e -> {
        });

        quitItem.addActionListener(e -> System.exit(0)); // TODO if not save warning

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(), new JLabel(
                "<html><center><br />JMusicHub<br/><br />Made by Jérémy RODGRIGUES and<br />Antoine LETHIMONNIER <br/><br /> © Copyright 2020<br /></center></html>",
                SwingConstants.CENTER), "About", JOptionPane.PLAIN_MESSAGE));

        // Panels
        JTabbedPane songActionsPane = new JTabbedPane();
        songActionsPane.setFocusable(false);
        songActionsPane.setTabPlacement(SwingConstants.LEFT);
        songActionsPane.addTab(ADD_LABEL, addPanelForType(Song.class));
        songActionsPane.addTab(REMOVE_LABEL, removePanelForType(Song.class));
        songActionsPane.addTab(VIEW_LABEL, viewPanelForType(Song.class));
        JTabbedPane audioActionsPane = new JTabbedPane();
        audioActionsPane.setFocusable(false);
        audioActionsPane.setTabPlacement(SwingConstants.LEFT);
        audioActionsPane.addTab(ADD_LABEL, addPanelForType(AudioBook.class));
        audioActionsPane.addTab(REMOVE_LABEL, removePanelForType(AudioBook.class));
        audioActionsPane.addTab(VIEW_LABEL, viewPanelForType(AudioBook.class));
        JTabbedPane albumActionsPane = new JTabbedPane();
        albumActionsPane.setFocusable(false);
        albumActionsPane.setTabPlacement(SwingConstants.LEFT);
        albumActionsPane.addTab(ADD_LABEL, addPanelForType(Album.class));
        albumActionsPane.addTab(REMOVE_LABEL, removePanelForType(Album.class));
        albumActionsPane.addTab(VIEW_LABEL, viewPanelForType(Album.class));
        JTabbedPane playActionsPane = new JTabbedPane();
        playActionsPane.setFocusable(false);
        playActionsPane.setTabPlacement(SwingConstants.LEFT);
        playActionsPane.addTab(ADD_LABEL, addPanelForType(Playlist.class));
        playActionsPane.addTab(REMOVE_LABEL, removePanelForType(Playlist.class));
        playActionsPane.addTab(VIEW_LABEL, viewPanelForType(Playlist.class));
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFocusable(false);
        tabbedPane.addTab("Song", songActionsPane);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_S);
        tabbedPane.addTab("AudioBook", audioActionsPane);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_U);
        tabbedPane.addTab("Album", albumActionsPane);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_A);
        tabbedPane.addTab("Playlist", playActionsPane);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_P);

        // Add
        setJMenuBar(menubar);
        add(tabbedPane);
    }

    private JPanel addPanelForType(Class<?> type) {
        JPanel panel = new JPanel();
        // layout: (grid avec 2 lignes ?)
        // text | text | text | text
        // -----------------------------------
        // field | field | field | field
        for (Field field : type.getFields()) {
            // Label
            // String cap = str.substring(0, 1).toUpperCase() + str.substring(1);
            // Input
            Class<?> fieldType = field.getType();
            if (Enum.class.isAssignableFrom(fieldType)) {
                // menu déroulant
                JComboBox<String> combo = null;
                if (fieldType.equals(Genre.class)) {
                    combo = new JComboBox<>(Genre.getStringValues());
                } else if (fieldType.equals(Category.class)) {
                    combo = new JComboBox<>(Category.getStringValues());
                } else if (fieldType.equals(Language.class)) {
                    combo = new JComboBox<>(Language.getStringValues());
                } else {
                    continue;
                }
                combo.setFocusable(false);
                panel.add(combo);
            } else if (fieldType.equals(Long.TYPE) || fieldType.equals(Integer.TYPE)) {
                // stepper
                panel.add(new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1)));
            } else if (fieldType.equals(Date.class)) {
                // calendar
                panel.add(new DatePicker());
            } else {
                // normal
                panel.add(new JTextField(field.getName()));
            }
        }
        return panel;
    }

    private JPanel removePanelForType(Class<?> type) {
        return new JPanel();
    }

    private JPanel viewPanelForType(Class<?> type) {
        return new JPanel();
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
