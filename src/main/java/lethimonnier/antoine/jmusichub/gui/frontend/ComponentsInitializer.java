package lethimonnier.antoine.jmusichub.gui.frontend;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.gui.backend.InterfacesLinker;

/**
 * The type Components initializer.
 */
public class ComponentsInitializer {

    private DynamicGraphics dg = new DynamicGraphics();
    private InterfacesLinker il = InterfacesLinker.getInstance();

    private static final Dimension DEF_COMBO_BOX_DIM = new Dimension(150, 20);
    private static final String ADD_LABEL = "Add";
    private static final String REMOVE_LABEL = "Remove";
    private static final String VIEW_LABEL = "View";
    private static final String SAVE_LABEL = "Save";

    // PANES
    private JTabbedPane songActionsPane;
    private JTabbedPane audioActionsPane;
    private JTabbedPane albumActionsPane;
    private JTabbedPane playActionsPane;

	/**
	 * JMenuBar initialization
	 *
	 * @return the menubar
	 */
	public JMenuBar initMenuBar() {
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
        JMenuItem refreshItem = new JMenuItem("Refresh tables");
        refreshItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(quitItem);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);
        menubar.add(helpMenu);

        importItem.addActionListener(e -> {
            il.importToLibrary();
            dg.refreshTables(new JTabbedPane[] { songActionsPane, audioActionsPane, albumActionsPane, playActionsPane },
                    il.getLibrary());
        });

        exportItem.addActionListener(e -> {
            il.exportFromLibrary();
            dg.refreshTables(new JTabbedPane[] { songActionsPane, audioActionsPane, albumActionsPane, playActionsPane },
                    il.getLibrary());
        });

        refreshItem.addActionListener(e -> dg.refreshTables(
                new JTabbedPane[] { songActionsPane, audioActionsPane, albumActionsPane, playActionsPane },
                il.getLibrary()));

        quitItem.addActionListener(e -> System.exit(0));

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(null, new JLabel(
                "<html><center><br />JMusicHub<br/><br />Made by Jérémy RODGRIGUES and<br />Antoine LETHIMONNIER <br/><br /> © Copyright 2020<br /></center></html>",
                SwingConstants.CENTER), "About", JOptionPane.PLAIN_MESSAGE));

        return menubar;
    }

	/**
	 * Window Initialization
	 *
	 * @return the j tabbed pane
	 */
	public JTabbedPane initComponents() {

        // Buttons
        JButton addItemSong = new JButton(SAVE_LABEL);
        JButton addItemAudio = new JButton(SAVE_LABEL);
        JButton addItemAlbum = new JButton(SAVE_LABEL);
        JButton addItemPlaylist = new JButton(SAVE_LABEL);

        // SONG PANE
        songActionsPane = new JTabbedPane();

        songActionsPane.setFocusable(false);
        songActionsPane.setTabPlacement(SwingConstants.LEFT);
        dg.getSongAddPanel().setLayout(new BoxLayout(dg.getSongAddPanel(), BoxLayout.Y_AXIS));
        songActionsPane.addTab(ADD_LABEL, new JScrollPane(dg.getSongAddPanel(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        songActionsPane.addTab(REMOVE_LABEL, dg.removePanelForType(Song.class, il.getLibrary()));
        songActionsPane.addTab(VIEW_LABEL, dg.viewPanelForType(Song.class));

        // AUDIOBOOK PANE
        audioActionsPane = new JTabbedPane();

        audioActionsPane.setFocusable(false);
        audioActionsPane.setTabPlacement(SwingConstants.LEFT);
        dg.getAudioAddPanel().setLayout(new BoxLayout(dg.getAudioAddPanel(), BoxLayout.Y_AXIS));
        audioActionsPane.addTab(ADD_LABEL, new JScrollPane(dg.getAudioAddPanel(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)); //
        audioActionsPane.addTab(REMOVE_LABEL, dg.removePanelForType(AudioBook.class, il.getLibrary()));
        audioActionsPane.addTab(VIEW_LABEL, dg.viewPanelForType(AudioBook.class));

        // ALBUM PANE
        albumActionsPane = new JTabbedPane();

        albumActionsPane.setFocusable(false);
        albumActionsPane.setTabPlacement(SwingConstants.LEFT);
        dg.getAlbumAddPanel().setLayout(new BoxLayout(dg.getAlbumAddPanel(), BoxLayout.Y_AXIS));
        albumActionsPane.addTab(ADD_LABEL, new JScrollPane(dg.getAlbumAddPanel(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        albumActionsPane.addTab(REMOVE_LABEL, dg.removePanelForType(Album.class, il.getLibrary()));
        albumActionsPane.addTab(VIEW_LABEL, dg.viewPanelForType(Album.class));

        // PLAYLIST PANE
        playActionsPane = new JTabbedPane();

        playActionsPane.setFocusable(false);
        playActionsPane.setTabPlacement(SwingConstants.LEFT);
        dg.getPlaylistAddPanel().setLayout(new BoxLayout(dg.getPlaylistAddPanel(), BoxLayout.Y_AXIS));
        playActionsPane.addTab(ADD_LABEL, new JScrollPane(dg.getPlaylistAddPanel(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
        playActionsPane.addTab(REMOVE_LABEL, dg.removePanelForType(Playlist.class, il.getLibrary()));
        playActionsPane.addTab(VIEW_LABEL, dg.viewPanelForType(Playlist.class));

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
            ArrayList<Component> comps = new ArrayList<>();
            for (Component comp : dg.getSongAddPanel().getComponents()) {
                if (comp instanceof JPanel) {
                    Collections.addAll(comps, ((JPanel) comp).getComponents());
                } else {
                    comps.add(comp);
                }
            }
            dg.addToLibraryFromComponents(comps, il.getLibrary());
        });

        addItemAudio.addActionListener(e -> {
            ArrayList<Component> comps = new ArrayList<>();
            for (Component comp : dg.getAudioAddPanel().getComponents()) {
                if (comp instanceof JPanel) {
                    Collections.addAll(comps, ((JPanel) comp).getComponents());
                } else {
                    comps.add(comp);
                }
            }
            dg.addToLibraryFromComponents(comps, il.getLibrary());
        });

        addItemAlbum.addActionListener(e -> {
            ArrayList<Component> comps = new ArrayList<>();
            for (Component comp : dg.getAlbumAddPanel().getComponents()) {
                if (comp instanceof JPanel) {
                    Collections.addAll(comps, ((JPanel) comp).getComponents());
                } else {
                    comps.add(comp);
                }
            }
            dg.addToLibraryFromComponents(comps, il.getLibrary());
        });

        addItemPlaylist.addActionListener(e -> {
            ArrayList<Component> comps = new ArrayList<>();
            for (Component comp : dg.getPlaylistAddPanel().getComponents()) {
                if (comp instanceof JPanel) {
                    Collections.addAll(comps, ((JPanel) comp).getComponents());
                } else {
                    comps.add(comp);
                }
            }
            dg.addToLibraryFromComponents(comps, il.getLibrary());
        });

        // Add section
        dg.getSongAddPanel().add(dg.addPanelForType(Song.class));
        dg.getAudioAddPanel().add(dg.addPanelForType(AudioBook.class));
        dg.getAlbumAddPanel().add(dg.addPanelForType(Album.class));
        dg.getPlaylistAddPanel().add(dg.addPanelForType(Playlist.class));
        dg.getAddButtonCenterPlaylist().setLayout(new BorderLayout());
        addItemPlaylist.setPreferredSize(DEF_COMBO_BOX_DIM);
        dg.getAddButtonCenterPlaylist().add(addItemPlaylist, BorderLayout.SOUTH);
        dg.getAddButtonCenterAlbum().setLayout(new BorderLayout());
        dg.getAddButtonCenterAlbum().add(addItemAlbum, BorderLayout.SOUTH);
        dg.getAlbumAddPanel().add(dg.getAddButtonCenterAlbum());
        dg.getAddButtonCenterAudio().setLayout(new BorderLayout());
        dg.getAddButtonCenterAudio().add(addItemAudio, BorderLayout.SOUTH);
        dg.getAudioAddPanel().add(dg.getAddButtonCenterAudio());
        dg.getAddButtonCenterSong().setLayout(new BorderLayout());
        dg.getAddButtonCenterSong().add(addItemSong, BorderLayout.SOUTH);
        dg.getSongAddPanel().add(dg.getAddButtonCenterSong());
        return tabbedPane;
    }

	/**
	 * Info.
	 *
	 * @param title   the title
	 * @param message the message
	 */
	public void info(String title, String message) {
        il.info(title, message);
    }

	/**
	 * Error.
	 *
	 * @param title   the title
	 * @param message the message
	 */
	public void error(String title, String message) {
        il.error(title, message);
    }
}
