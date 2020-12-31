package lethimonnier.antoine.jmusichub.gui.backend;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import com.opencsv.exceptions.CsvValidationException;

import lethimonnier.antoine.jmusichub.cli.CSVManager;
import lethimonnier.antoine.jmusichub.cli.Library;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;

public class InterfacesLinker {

    // external libraries
    private CSVManager csv = new CSVManager();
    private Library library = new Library();

    // Other variables
    private String filePath;
    private ByteArrayOutputStream baos;

    // Singleton
    private static final InterfacesLinker singleton = new InterfacesLinker();

    private InterfacesLinker() {
    }

    public static InterfacesLinker getInstance() {
        return singleton;
    }

    // Stdout/stderr management
    /**
     * Starts capturing console output into a custom
     * <code>ByteArrayOutputStream</code>.
     */
    private void startLogCapture() {
        // Redirecting stdout & stderr to custom stream
        baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        System.setErr(new PrintStream(baos));
    }

    /**
     * Stops the capture of the console output started by
     * {@link lethimonnier.antoine.jmusichub.gui.backend startLogCapture()}.
     * 
     * @return the captured output as a <code>String</code>
     */
    private String stopLogCapture() {
        // Resetting stdout & stderr to their default behavior
        System.out.flush();
        System.err.flush();
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));

        // Formatting output to remove Logger lines and format
        String[] lines = baos.toString().split(System.getProperty("line.separator"));
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("lethimonnier.antoine.jmusichub")) {
                lines[i] = "";
            } else if (lines[i].startsWith("INFO: ")) {
                lines[i] = lines[i].replace("INFO: ", "");
            } else if (lines[i].startsWith("WARNING: ")) {
                lines[i] = lines[i].replace("WARNING: ", "");
            }
        }
        StringBuilder finalStringBuilder = new StringBuilder("");
        for (String s : lines) {
            if (!s.equals("")) {
                finalStringBuilder.append(s).append(System.getProperty("line.separator"));
            }
        }
        return finalStringBuilder.toString();
    }

    // Links
    public void importToLibrary() {
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
            error(ex.getMessage());
        }
    }

    public void exportFromLibrary() {
        try {
            csv.saveLibaryToFile(library, filePath);
            info("Success", "Saved library in the designed file");
        } catch (IOException ex) {
            error(ex.getMessage());
        }
    }

    public Object[][] getLibraryContentForType(Class<?> type) {
        if (type.equals(Song.class)) {
            return getSongs();
        } else if (type.equals(AudioBook.class)) {
            return getAudioBooks();
        } else if (type.equals(Album.class)) {
            return getAlbums();
        } else if (type.equals(Playlist.class)) {
            return getPlaylists();
        }
        return new Object[][] {};
    }

    private Object[][] getSongs() {
        Field[] songsFields = Song.class.getFields(); // all fields of the class
        List<Song> allSongs = library.getStoredSongs(); // all existing instances of the class
        // nested table with cols = fields and lines = objects
        Object[][] songs = new Object[allSongs.size()][songsFields.length];
        // Parsing
        try {
            for (int i = 0; i < allSongs.size(); i++) {
                for (int j = 0; j < songsFields.length; j++) {
                    Class<?> type = songsFields[j].getType();
                    if (type.isArray() && type.getComponentType().equals(String.class)) {
                        songs[i][j] = String.join("/",
                                        (String[]) songsFields[j].get(allSongs.get(i)));
                    } else {
                        songs[i][j] = songsFields[j].get(allSongs.get(i));
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            error(e.getMessage());
        }
        return songs;
    }

    private Object[][] getAudioBooks() {
        Field[] abFields = AudioBook.class.getFields();
        List<AudioBook> allAb = library.getStoredAudioBooks();
        Object[][] songs = new Object[allAb.size()][abFields.length];
        try {
            for (int i = 0; i < allAb.size(); i++) {
                for (int j = 0; j < abFields.length; j++) {
                    Class<?> type = abFields[j].getType();
                    if (type.isArray() && type.getComponentType().equals(String.class)) {
                        songs[i][j] = String.join("/", (String[]) abFields[j].get(allAb.get(i)));
                    } else {
                        songs[i][j] = abFields[j].get(allAb.get(i));
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            error(e.getMessage());
        }
        return songs;
    }

    private Object[][] getAlbums() {
        List<Album> allAlbums = library.getStoredAlbums();
        // listing total songs
        List<Song> allAlbumsSongs = new ArrayList<>();
        for (Album a : allAlbums) {
            if (a.songs != null)
                allAlbumsSongs.addAll(Arrays.asList(a.songs));
        }
        // removing Song[]
        ArrayList<Field> albumFields = new ArrayList<>(Arrays.asList(Album.class.getFields()));
        ArrayList<Field> fields = new ArrayList<>(albumFields);
        for (Field field : albumFields) {
            Class<?> fieldType = field.getType();
            if ((fieldType.isArray() && fieldType.getComponentType().equals(Song.class)))
                fields.remove(field);
        }
        int albumFieldsCount = albumFields.size();
        albumFields.addAll(Arrays.asList(Song.class.getFields()));
        Object[][] albums = new Object[allAlbumsSongs.size()][albumFields.size()];
        // Parsing
        // Desired format:
        // title author duration | title authors duration genre
        // ----------------------------------------------------
        // mytit myaut  1543     | hey   myaut   123      Pop
        //                       | ho    myaut   90       Rock
        // ...
        try {
            for (int i = 0; i < allAlbumsSongs.size(); i++) {
                Album currentAlbum = allAlbums.get(i); // pas bon
                for (int j = 0; j < albumFieldsCount; j++) { // album fields
                    if (i != 0 && albums[i - 1][j] != albumFields.get(j).get(currentAlbum))
                        albums[i][j] = albumFields.get(j).get(currentAlbum);
                }
                for (int j2 = albumFieldsCount; j2 < albumFields.size(); j2++) { // songs fields
                    albums[i][j2] = Song.class.getFields()[j2].get(currentAlbum); // 100% faux
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            error(e.getMessage());
        }
        return albums;
    }

    private Object[][] getPlaylists() {
        Field[] songFields = Song.class.getFields();
        Field[] abFields = AudioBook.class.getFields();
        Field[] playlistsFields = Playlist.class.getFields();
        List<Playlist> allPlaylists = library.getStoredPlaylists();
        Object[][] playlists = new Object[allPlaylists.size()][playlistsFields.length];
        return playlists;
    }

    // Popups
    public void info(String message) {
        info("Info", message);
    }

    public void info(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    public void error(String message) {
        error("Error", message);
    }

    public void error(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
