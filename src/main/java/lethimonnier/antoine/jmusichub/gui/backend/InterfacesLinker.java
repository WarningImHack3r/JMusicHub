package lethimonnier.antoine.jmusichub.gui.backend;

import com.opencsv.exceptions.CsvValidationException;
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
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * The type Interfaces linker.
 */
public class InterfacesLinker {

	// Singleton
	private static InterfacesLinker singleton;
	// external libraries
	private final CSVManager csv = new CSVManager();

	// Other variables
	private String filePath;
	private ByteArrayOutputStream baos;
	private final Library library = new Library();

	private InterfacesLinker() {
	}

	/**
	 * Gets instance.
	 *
	 * @return the instance
	 */
	public static InterfacesLinker getInstance() {
		if (singleton == null) {
			singleton = new InterfacesLinker();
		}
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
    @NotNull
    private String stopLogCapture() {
	    // Resetting stdout & stderr to their default behavior
	    System.out.flush();
	    System.err.flush();
	    System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	    System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));

	    // Formatting output to remove Logger lines and format
	    String[] lines = baos.toString().split(System.lineSeparator());
	    for (var i = 0; i < lines.length; i++) {
		    if (lines[i].contains("lethimonnier.antoine.jmusichub")) {
			    lines[i] = "";
		    } else if (lines[i].startsWith("INFO: ")) {
			    lines[i] = lines[i].replace("INFO: ", "");
		    } else if (lines[i].startsWith("WARNING: ")) {
			    lines[i] = lines[i].replace("WARNING: ", "");
		    }
	    }
	    var finalStringBuilder = new StringBuilder();
	    for (String s : lines) {
            if (!s.equals("")) {
	            finalStringBuilder.append(s).append(System.lineSeparator());
            }
        }
        return finalStringBuilder.toString();
    }

	/**
	 * Import to library.
	 */
	// Links
    public void importToLibrary() {
        try {
	        var currentFile = csv.openFileFromChooser(filePath);
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

	/**
	 * Export from library.
	 */
	public void exportFromLibrary() {
        try {
            csv.saveLibaryToFile(library, filePath);
            info("Success", "Saved library in the designed file");
        } catch (IOException ex) {
            error(ex.getMessage());
        }
    }
    // ---

	/**
	 * Get library content for type object [ ] [ ].
	 *
	 * @param type the type
	 * @return the object [ ] [ ]
	 */
	public Object[][] getLibraryContentForType(@NotNull Class<?> type) {
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

    @NotNull
    private Object[][] getSongs() {
        Field[] songsFields = Song.class.getFields(); // all fields of the class
        List<Song> allSongs = library.getStoredSongs(); // all existing instances of the class
        // nested table with cols = fields and lines = objects
	    var songs = new Object[allSongs.size()][songsFields.length];
        // Parsing
	    try {
		    for (var i = 0; i < allSongs.size(); i++) {
			    for (var j = 0; j < songsFields.length; j++) {
				    Class<?> type = songsFields[j].getType();
				    if (type.isArray() && type.getComponentType().equals(String.class)) {
					    songs[i][j] = String.join("/", (String[]) songsFields[j].get(allSongs.get(i)));
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

    @NotNull
    private Object[][] getAudioBooks() {
        Field[] abFields = AudioBook.class.getFields();
	    List<AudioBook> allAb = library.getStoredAudioBooks();
	    var songs = new Object[allAb.size()][abFields.length];
	    try {
		    for (var i = 0; i < allAb.size(); i++) {
			    for (var j = 0; j < abFields.length; j++) {
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

    @NotNull
    private Object[][] getAlbums() {
        List<Album> allAlbums = library.getStoredAlbums();
        // listing total songs
	    List<Song> allAlbumsSongs = new ArrayList<>();
	    var emptyAlbums = 0;
        for (Album a : allAlbums) {
	        if (a.getSongs() != null)
		        allAlbumsSongs.addAll(Arrays.asList(a.getSongs()));
	        else
		        emptyAlbums++;
        }
        // removing Song[]
        ArrayList<Field> albumFields = new ArrayList<>(Arrays.asList(Album.class.getFields())); // fields of album
        ArrayList<Field> fields = new ArrayList<>(albumFields); // copy without Song[]
        for (Field field : albumFields) {
            Class<?> fieldType = field.getType();
            if (fieldType.isArray() && fieldType.getComponentType().equals(Song.class))
                fields.remove(field);
        }
        albumFields = fields;
        int albumOnlyFieldsCount = albumFields.size();
        albumFields.addAll(Arrays.asList(Song.class.getFields())); // add Song fields
	    var albums = new Object[allAlbumsSongs.size() + emptyAlbums][albumFields.size()];
        // Parsing
	    try {
		    var lines = 0;
		    // one of the hardest nested loops in my life
		    for (Album currentAlbum : allAlbums) { // parcourir tous les albums
			    if (currentAlbum.getSongs() == null) {
				    for (var j = 0; j < albumOnlyFieldsCount; j++) {
					    albums[lines][j] = albumFields.get(j).get(currentAlbum);
				    }
				    lines++;
			    } else {
				    for (var j = 0; j < currentAlbum.getSongs().length; j++) { // parcourir tous les songs de cet album
					    if (currentAlbum.getSongs()[j] != null) {
						    for (var k = 0; k < albumOnlyFieldsCount; k++) { // parcours largeur 1
							    if (lines > 0 && albums[lines - 1][k] != albumFields.get(k).get(currentAlbum))
								    albums[lines][k] = albumFields.get(k).get(currentAlbum);
						    }
						    for (var l = 0; l < albumFields.size() - albumOnlyFieldsCount; l++) { // parc. larg. 2
							    var songField = Song.class.getFields()[l];
							    albums[lines][l + albumOnlyFieldsCount] = songField.get(currentAlbum.getSongs()[j]);
							    if (songField.getType().isArray() && songField.getType().getComponentType().equals(String.class)) {
								    albums[lines][l + albumOnlyFieldsCount] = String.join("/",
										    (String[]) albums[lines][l + albumOnlyFieldsCount]);
							    }
						    }
					    }
                        lines++;
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            error(e.getMessage());
        }
        return albums;
    }

    @NotNull
    private Object[][] getPlaylists() { // getAlbums(), but a bit harder
        List<Playlist> allPlaylists = library.getStoredPlaylists();
        // listing total audio contents
	    List<AudioContent> allPlaylistsContents = new ArrayList<>();
	    var emptyPlaylists = 0;
        for (Playlist p : allPlaylists) {
	        if (p.getContent() != null && !p.getContent().isEmpty())
		        allPlaylistsContents.addAll(p.getContent());
	        else
		        emptyPlaylists++;
        }
        // removing Collection<AudioContent>
        ArrayList<Field> playlistFields = new ArrayList<>(Arrays.asList(Playlist.class.getFields()));
        ArrayList<Field> fields = new ArrayList<>(playlistFields); // copy without Collection
        for (Field field : playlistFields) {
            Class<?> fieldType = field.getType();
	        if (Collection.class.isAssignableFrom(fieldType) && ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].equals(AudioContent.class))
                fields.remove(field);
        }
        playlistFields = fields;
        int playlistOnlyFieldsCount = playlistFields.size();
        playlistFields.addAll(Arrays.asList(AudioContent.class.getFields())); // add AudioContent+ fields
        for (Field declaredField : Stream.concat(Arrays.stream(Song.class.getDeclaredFields()),
                Arrays.stream(AudioBook.class.getDeclaredFields())).toArray(Field[]::new)) {
            // add song and ab fields and remove serialVersionUID fields
            if (!declaredField.getName().equals("serialVersionUID"))
                playlistFields.add(declaredField);
        }
	    var playlists = new Object[allPlaylistsContents.size() + emptyPlaylists][playlistFields.size()];
        // Parsing
	    try {
		    var lines = 0;
		    // one of the hardest nested loops in my life, but even worse
		    for (Playlist currentPlaylist : allPlaylists) { // parcourir toutes les playlists
			    if (currentPlaylist.getContent() == null || currentPlaylist.getContent().isEmpty()) {
				    for (var j = 0; j < playlistOnlyFieldsCount; j++) {
					    playlists[lines][j] = playlistFields.get(j).get(currentPlaylist);
				    }
				    lines++;
			    } else {
				    for (var j = 0; j < currentPlaylist.getContent().size(); j++) {
					    if (currentPlaylist.getContent().get(j) != null) {
						    // parcourir tous les audiobook de cet playlist
						    for (var k = 0; k < playlistOnlyFieldsCount; k++) { // parcours largeur 1
							    if (lines > 0 && playlists[lines - 1][k] != playlistFields.get(k).get(currentPlaylist))
								    playlists[lines][k] = playlistFields.get(k).get(currentPlaylist);
						    }
						    for (var l = 0; l < playlistFields.size() - playlistOnlyFieldsCount - 3; l++) {
							    // title, authors, duration
							    var contentField = AudioContent.class.getFields()[l];
							    playlists[lines][l + playlistOnlyFieldsCount] =
									    contentField.get(currentPlaylist.getContent().get(j));
							    if (contentField.getType().isArray() && contentField.getType().getComponentType().equals(String.class)) {
								    playlists[lines][l + playlistOnlyFieldsCount] = String.join("/",
										    (String[]) playlists[lines][l + playlistOnlyFieldsCount]);
							    }
						    }
						    for (var m = 0; m < playlistFields.size() - playlistOnlyFieldsCount - 3; m++) {
							    // genre, language & category
							    Field[] af = Stream.concat(Arrays.stream(Song.class.getDeclaredFields()),
									    Arrays.stream(AudioBook.class.getDeclaredFields())).toArray(Field[]::new);
							    List<Field> aftmp = new ArrayList<>();
							    for (Field f : af) {
								    if (!f.getName().equals("serialVersionUID"))
									    aftmp.add(f);
							    }
							    Field[] additionalFields = aftmp.toArray(new Field[0]);
							    if ((additionalFields[m].getType().equals(Genre.class) && !(currentPlaylist.getContent().get(j) instanceof Song)) || (additionalFields[m].getType().equals(Language.class) && !(currentPlaylist.getContent().get(j) instanceof AudioBook)) || (additionalFields[m].getType().equals(Category.class) && !(currentPlaylist.getContent().get(j) instanceof AudioBook)))
								    // avoid exception
								    continue;
							    playlists[lines][m + playlistOnlyFieldsCount + 3] =
									    additionalFields[m].get(currentPlaylist.getContent().get(j));
						    }
                        }
                        lines++;
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            error(e.getMessage());
        }
        return playlists;
    }

	/**
	 * Gets library.
	 *
	 * @return the library
	 */
	public Library getLibrary() {
        return library;
    }

	/**
	 * Info.
	 *
	 * @param message the message
	 */
	// Popups
    public void info(String message) {
        info("Info", message);
    }

	/**
	 * Info.
	 *
	 * @param title   the title
	 * @param message the message
	 */
	public void info(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
    }

	/**
	 * Error.
	 *
	 * @param message the message
	 */
	public void error(String message) {
        error("Error", message);
    }

	/**
	 * Error.
	 *
	 * @param title   the title
	 * @param message the message
	 */
	public void error(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
