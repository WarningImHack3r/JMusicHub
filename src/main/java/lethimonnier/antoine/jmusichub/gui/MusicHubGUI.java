package lethimonnier.antoine.jmusichub.gui;

import com.github.lgooddatepicker.components.DatePicker;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;

import lethimonnier.antoine.jmusichub.cli.CSVManager;
import lethimonnier.antoine.jmusichub.cli.Library;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;

/**
 * The type Music hub gui.
 */
public class MusicHubGUI extends JFrame {

    private static final long serialVersionUID = 5930451128361756517L;
    private static final Dimension DEF_TEXT_FIELD_DIM = new Dimension(200, 20);
    private static final Dimension DEF_COMBO_BOX_DIM = new Dimension(150, 20);
    private static final String ADD_LABEL = "Add";
    private static final String REMOVE_LABEL = "Remove";
    private static final String VIEW_LABEL = "View";
    private JPanel playlistAddPanel = new JPanel();
    private JPanel songAddPanel = new JPanel();
    private JPanel albumAddPanel = new JPanel();
    private JPanel audioAddPanel = new JPanel();
    private JPanel addButtonCenterPlaylist = new JPanel();
    private JPanel addButtonCenterAlbum = new JPanel();
    private JPanel addButtonCenterAudio = new JPanel();
    private JPanel addButtonCenterSong = new JPanel();
    private final transient Library library;
    private String filePath;
    private transient CSVManager csv = new CSVManager();

    /**
     * GUI Constructor
     * 
     * @param title the window title
     */
    private MusicHubGUI(String title) {
        super(title);
        library = new Library();

        initLayout();
        initComponents();
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
            error("Look", e.getMessage());
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
     * Window Initialization
     */
    private void initComponents() {
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
            try {
                File currentFile = csv.openFileFromChooser(filePath);
                if (currentFile == null) {
                    error("Warning", "No input file found.");
                } else {
                    filePath = currentFile.getAbsolutePath();
                    int imported = csv.importSavedContentFromFile(currentFile, library);
                    info("Success", "Successfully imported " + imported + " elements");
                }
            } catch (IOException | CsvValidationException ex) {
                error("Error", ex.getMessage());
            }
        });

        exportItem.addActionListener(e -> {
            try {
                csv.saveLibaryToFile(library, filePath);
                info("Success", "Saved library in the designed file");
            } catch (IOException ex) {
                error("Error", ex.getMessage());
            }
        });

        quitItem.addActionListener(e -> System.exit(0));

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, new JLabel(
                "<html><center><br />JMusicHub<br/><br />Made by Jérémy RODGRIGUES and<br />Antoine LETHIMONNIER <br/><br /> © Copyright 2020<br /></center></html>",
                SwingConstants.CENTER), "About", JOptionPane.PLAIN_MESSAGE));

        // Buttons
        JButton addItemPlaylist = new JButton("Save");
        JButton addItemAlbum = new JButton("Save");
        JButton addItemAudio = new JButton("Save");
        JButton addItemSong = new JButton("Save");

        // SONG PANE
        JTabbedPane songActionsPane = new JTabbedPane();

        songActionsPane.setFocusable(false);
        songActionsPane.setTabPlacement(SwingConstants.LEFT);
        songAddPanel.setLayout(new BoxLayout(songAddPanel, BoxLayout.Y_AXIS));
        songActionsPane.addTab(ADD_LABEL, new JScrollPane(songAddPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        songActionsPane.addTab(REMOVE_LABEL, removePanelForType(Song.class));
        songActionsPane.addTab(VIEW_LABEL, viewPanelForType(Song.class));

        // AUDIOBOOK PANE
        JTabbedPane audioActionsPane = new JTabbedPane();

        audioActionsPane.setFocusable(false);
        audioActionsPane.setTabPlacement(SwingConstants.LEFT);
        audioAddPanel.setLayout(new BoxLayout(audioAddPanel, BoxLayout.Y_AXIS));
        audioActionsPane.addTab(ADD_LABEL, new JScrollPane(audioAddPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        audioActionsPane.addTab(REMOVE_LABEL, removePanelForType(AudioBook.class));
        audioActionsPane.addTab(VIEW_LABEL, viewPanelForType(AudioBook.class));

        // ALBUM PANE
        JTabbedPane albumActionsPane = new JTabbedPane();

        albumActionsPane.setFocusable(false);
        albumActionsPane.setTabPlacement(SwingConstants.LEFT);
        albumAddPanel.setLayout(new BoxLayout(albumAddPanel, BoxLayout.Y_AXIS));
        albumActionsPane.addTab(ADD_LABEL, new JScrollPane(albumAddPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        albumActionsPane.addTab(REMOVE_LABEL, removePanelForType(Album.class));
        albumActionsPane.addTab(VIEW_LABEL, viewPanelForType(Album.class));

        // PLAYLIST PANE
        JTabbedPane playActionsPane = new JTabbedPane();

        playActionsPane.setFocusable(false);
        playActionsPane.setTabPlacement(SwingConstants.LEFT);
        playlistAddPanel.setLayout(new BoxLayout(playlistAddPanel, BoxLayout.Y_AXIS));
        playActionsPane.addTab(ADD_LABEL, new JScrollPane(playlistAddPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        playActionsPane.addTab(REMOVE_LABEL, removePanelForType(Playlist.class));
        playActionsPane.addTab(VIEW_LABEL, viewPanelForType(Playlist.class));

        // Main tabbed pane
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

        // ADDING BUTTON LISTENERS
        addItemSong.addActionListener(e -> {
        });
        addItemAudio.addActionListener(e -> {
        });
        addItemAlbum.addActionListener(e -> {
        });
        addItemPlaylist.addActionListener(e -> {
            for (Component comp : playlistAddPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component intern : ((JPanel) comp).getComponents()) {
                        System.out.println(intern.getClass());
                    }
                } else {
                    System.out.println(comp.getClass());
                }
            }
        });

        // Add section
        songAddPanel.add(addPanelForType(Song.class));
        audioAddPanel.add(addPanelForType(AudioBook.class));
        albumAddPanel.add(addPanelForType(Album.class));
        playlistAddPanel.add(addPanelForType(Playlist.class));
        addItemPlaylist.setPreferredSize(DEF_COMBO_BOX_DIM);
        addButtonCenterPlaylist.setLayout(new BorderLayout());
        addButtonCenterPlaylist.add(addItemPlaylist, BorderLayout.SOUTH);
        addButtonCenterAlbum.setLayout(new BorderLayout());
        addButtonCenterAlbum.add(addItemAlbum, BorderLayout.SOUTH);
        albumAddPanel.add(addButtonCenterAlbum);
        addButtonCenterAudio.setLayout(new BorderLayout());
        addButtonCenterAudio.add(addItemAudio, BorderLayout.SOUTH);
        audioAddPanel.add(addButtonCenterAudio);
        addButtonCenterSong.setLayout(new BorderLayout());
        addButtonCenterSong.add(addItemSong, BorderLayout.SOUTH);
        songAddPanel.add(addButtonCenterSong);
        setJMenuBar(menubar);
        getContentPane().add(tabbedPane);
    }

    private void info(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    private void error(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Fetchs the <code>type</code>'s fields to create adapted user inputs.
     * <strong>Requires fields to be public</strong>.
     * 
     * @param type the class to look into
     * @return the generated panel
     */
    private JPanel addPanelForType(Class<?> type) {
        JPanel panel = new JPanel();
        ArrayList<JComboBox<String>> combos = new ArrayList<>();

        // layout: (grid avec 2 lignes ?)
        // text | text | text | text
        // -----------------------------------
        // field | field | field | field
        for (Field field : type.getFields()) {
            Class<?> fieldType = field.getType();
            if (Enum.class.isAssignableFrom(fieldType)) { // Les enums
                // dropdown menu
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
                combo.setPreferredSize(DEF_COMBO_BOX_DIM);
                combos.add(combo);
            } else if (fieldType.equals(Long.TYPE) || fieldType.equals(Integer.TYPE)) { // Les int/long
                // stepper
                panel.add(new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1)));
            } else if (fieldType.equals(Date.class)) { // Les Dates
                // calendar
                panel.add(new DatePicker());
            } else if (fieldType.isArray() && !fieldType.getComponentType().equals(String.class)) { // les Object[]
                                                                                                    // (sauf les String)
                // create a button for the specified type
                if (fieldType.getComponentType().equals(Song.class)) {
                    JButton addSong = new JButton("Add song");
                    addSong.addActionListener(e -> addElementToPanel(Song.class, albumAddPanel, addButtonCenterAlbum));
                    panel.add(addSong);
                }
            } else if (Collection.class.isAssignableFrom(fieldType)) { // Les List
                // more or less the same
                Class<?> collClass = (Class<?>) ((ParameterizedType) field.getGenericType())
                        .getActualTypeArguments()[0];
                if (collClass.equals(AudioContent.class)) {
                    JButton btn = new JButton("Add audio content");
                    btn.addActionListener(addContent);
                    panel.add(btn);
                }
            } else { // le reste
                // normal
                String cap = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                JTextField jTextGetNameField = new JTextField(cap);
                jTextGetNameField.setForeground(Color.GRAY);
                jTextGetNameField.setPreferredSize(DEF_TEXT_FIELD_DIM);
                jTextGetNameField.addFocusListener(new FocusListener() {

                    @Override
                    public void focusGained(FocusEvent e) {
                        if (jTextGetNameField.getText().equals(cap)) {
                            jTextGetNameField.setText("");
                            jTextGetNameField.setForeground(Color.BLACK);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (jTextGetNameField.getText().isEmpty()) {
                            jTextGetNameField.setForeground(Color.GRAY);
                            jTextGetNameField.setText(cap);
                        }
                    }
                });
                panel.add(jTextGetNameField);
            }
        }
        for (JComboBox<?> combo : combos) {
            panel.add(combo);
        }
        return panel;
    }

    transient ActionListener addContent = e -> {
        int selected = JOptionPane.showOptionDialog(this, "Which content do you want to add?", "Add audio content",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[] { "Song", "AudioBook", "Cancel" },
                0);
        switch (selected) {
            case 0:
                // song
                addElementToPanel(Song.class, playlistAddPanel, addButtonCenterPlaylist);
                break;

            case 1:
                // audiobook
                addElementToPanel(AudioBook.class, playlistAddPanel, addButtonCenterPlaylist);
                break;

            default:
                break;
        }
    };

    private void addElementToPanel(Class<?> type, Container panel, Component btnCenter) {
        panel.remove(btnCenter);
        panel.add(addPanelForType(type));
        panel.add(btnCenter);
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Creates a panel to remove instances of class <code>type</code>.
     * 
     * @param type the class to fetch
     * @return the generated panel
     */
    private JPanel removePanelForType(Class<?> type) {
        return new JPanel();
    }

    /**
     * Creates a panel to view existing instances of class <code>type</code>
     * 
     * @param type the class to fetch
     * @return the generated panel
     */
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
