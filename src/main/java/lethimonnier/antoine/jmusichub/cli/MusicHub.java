package lethimonnier.antoine.jmusichub.cli;

import com.opencsv.exceptions.CsvValidationException;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.classes.parsing.ConsoleParser;
import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MusicHub
 */
public final class MusicHub {

	/**
	 * The constant DATE_FORMAT.
	 */
	// Static final
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    // Final
    private final Logger log = Logger.getGlobal();
    private final Scanner sc = new Scanner(System.in);
    private final Library library;
    private final ConsoleParser console = new ConsoleParser();

    // Other variables
    private String filePath;

    private MusicHub() {
        log.info("Welcome to the MusicHub!");
        library = new Library();
        CSVManager csv = new CSVManager();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            File currentFile = csv.openFileFromChooser(null);
            if (currentFile == null) {
                log.warning("No input file found.");
            } else {
                filePath = currentFile.getAbsolutePath();
                int imported = csv.importSavedContentFromFile(currentFile, library);
                log.log(Level.INFO, "Successfully imported {0} elements", imported);
            }
        } catch (IOException | CsvValidationException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            log.info("What do you want to do? (Press 'h' to show help)");
            switch (sc.nextLine().toLowerCase().charAt(0)) {
                case 'c':
                    // add a new song
                    log.info("New song adding.");
                    createNewSongFromUserInput();
                    break;

                case 'a':
                    // creates a new album
                    log.info("Album creation.");
                    Album createdAlbum = createBlankAlbumFromUserInput();
                    log.log(Level.INFO, "Add songs to newly created album {0}? [yes/no]", createdAlbum.getTitle());
                    if (sc.nextLine().contains("y"))
                        addSongToAlbum(createdAlbum);
                    break;

                case '+':
                    // adds (an) existing song(s) to an album
                    log.info("Song add.");
                    addSongToAlbum(null);
                    break;

                case 'l':
                    // creates a new audio book
                    log.info("Audio book creation.");
                    createAudioBookFromUserInput();
                    break;

                case 'p':
                    // creates a new playlist from existing songs/audio books
                    log.info("Playlist creation.");
                    createPlaylistFromUserInput();
                    break;

                case '-':
                    // deletes a playlist
                    log.info("Playlist deletion.");
                    deletePlaylistFromUserInput();
                    break;

                case 'i':
                    // imports from a csv file
                    try {
                        File currentFile = csv.openFileFromChooser(filePath);
                        if (currentFile == null) {
                            log.warning("No input file found.");
                        } else {
                            filePath = currentFile.getAbsolutePath();
                            int imported = csv.importSavedContentFromFile(currentFile, library);
                            log.log(Level.INFO, "Successfully imported {0} elements", imported);
                        }
                    } catch (IOException | CsvValidationException e) {
                        e.printStackTrace();
                    }
                    break;

                case 's':
                    // saves everything in a csv file
                    log.info("Saving library.");
                    try {
                        csv.saveLibaryToFile(library, filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 't':
                    // additional option 1: calls 4 print functions of Library
                    log.info("Printing stored albums by genre");
                    Library.printAlbumsByGenre(library.getStoredAlbums());
                    log.info("Printing stored albums by release date");
                    Library.printAlbumsByReleaseDate(library.getStoredAlbums());
                    log.info("Printing stored audiobooks by author");
                    Library.printAudioBooksByAuthor(library.getStoredAudioBooks());
                    log.info("Printing stored playlists alphabetically");
                    Library.printPlaylistsAlphabetically(library.getStoredPlaylists());
                    break;

                case 'o':
                    // additional option 2: shows the content of all the Library
                    log.info("Printing stored Songs");
                    Library.printAllSongs(library.getStoredSongs());
                    log.info("Printing stored AudioBooks");
                    Library.printAllAudioBooks(library.getStoredAudioBooks());
                    log.info("Printing stored Albums");
                    Library.printAllAlbums(library.getStoredAlbums());
                    log.info("Printing stored Playlists");
                    Library.printAllPlaylists(library.getStoredPlaylists());
                    break;

                case 'x':
                    // additional option 3: clears the whole library
                    log.info("Are you sure to want to clear the library? [yes/no] ");
                    if (sc.nextLine().contains("y")) {
                        library.getStoredAlbums().clear();
                        library.getStoredAudioBooks().clear();
                        library.getStoredPlaylists().clear();
                        library.getStoredSongs().clear();
                        log.info("Library cleaned successfully");
                    } else {
                        log.info("Cancelling.");
                    }
                    break;

                case 'h':
                    // shows help
                    log.info("""
                            MusicHub help - available options
                            c: add a song from user input
                            a: creates an album without songs
                            +: add (an) existing song(s) to an album
                            l: creates a new audiobook
                            p: creates a playlist with existing audio contents
                            -: deletes a playlist
                            i: imports a csv file into the database
                            s: exports the whole database
                            t: additional option that shows stats of the database
                            o: additional option that shows all the content of the library
                            x: additional option that clears the library
                            h: shows this help menu
                            q: quit the application
                            """);
                    break;

                case 'q':
                    log.info("Bye!");
                    sc.close();
                    return;

                default:
                    break;
            }
        }
    }

    /**
     * Creates a <code>Song</code> from user inputs in console, and adds it to the
     * <code>Library</code>.
     */
    private void createNewSongFromUserInput() {
        log.info("Song title?");
        String songTitle = sc.nextLine();
        log.info("Song author(s)? (separated with commas)");
        String[] songAuthors = sc.nextLine().split(",");
        log.info("Song duration? (in seconds)");
        int songDuration = sc.nextInt();
        sc.nextLine();
        log.log(Level.INFO, "Song genre? {0}",
                Arrays.toString(Genre.getStringValues()).replace("[", "(").replace("]", ")"));
        String songGenre = sc.nextLine();
        for (Genre genreItem : Genre.values()) {
            if (genreItem.getName().toLowerCase().contains(songGenre.toLowerCase())) {
                library.addToSongsLibrary(new Song(songTitle, songAuthors, songDuration, genreItem));
                break;
            }
        }
    }

    /**
     * Creates a blank album from user inputs in console, and adds it to the
     * <code>Library</code>.
     *
     * @return the created <code>Album</code>
     */
    private Album createBlankAlbumFromUserInput() {
        log.info("Album title?");
        String albumTitle = sc.nextLine();
        log.info("Album author?");
        String albumAuthor = sc.nextLine();
        log.info("Album creation date?");
        String[] toPrint = { "Year (yyyy)", "Month (MM)", "Day (dd)" };
        int[] fields = { 0 /* year */, 0 /* month */, 0 /* day */ };
        for (int i = 0; i < fields.length; i++) {
            log.log(Level.INFO, "{0}: ", toPrint[i]);
            fields[i] = sc.nextInt();
        }
        sc.nextLine();
        Calendar c = Calendar.getInstance();
        c.set(fields[0], fields[1] - 1 /* Calendar.JANUARY = 0 */, fields[2]);
        Date albumParsedDate = c.getTime();
        Album album = new Album(null, albumTitle, albumAuthor, albumParsedDate);
        library.addToAlbumsLibrary(album);
        return album;
    }

    /**
     * Add songs to an album. Asks for a <code>Song</code> among every existing
     * <code>Song</code>s in the <code>Library</code>.
     *
     * If <code>targetAlbum</code> is <code>null</code>, also asks among every
     * existing <code>Album</code> in the <code>Library</code>.
     *
     * @param targetAlbum the album to add songs in. Can be <code>null</code>
     */
    private void addSongToAlbum(Album targetAlbum) {
        Album albumToAddIn = targetAlbum;
        if (albumToAddIn == null) {
            // Prompt albums to choose among them
            StringBuilder albumSb = new StringBuilder();
            for (Album a : library.getStoredAlbums()) {
                albumSb.append(library.getStoredAlbums().indexOf(a) + 1).append(") ").append(a.getTitle()).append(" - ")
                        .append(a.getAuthor()).append(System.getProperty("line.separator"));
            }
            log.log(Level.INFO, "Choose your album to add songs in:\n{0}", albumSb);
            String line = sc.nextLine();
            try {
                // Number input
                albumToAddIn = library.getStoredAlbums().get(Integer.parseInt(line) - 1);
            } catch (NumberFormatException e) {
                // Text input
                Album functionResult = console.getAlbumFromString(line, library);
                if (functionResult == null)
                    return;
                albumToAddIn = functionResult;
            }
        }
        // Prompt songs to choose which to add
        do {
            StringBuilder songSb = new StringBuilder();
            int i = 0;
            for (Song s : library.getStoredSongs()) {
                songSb.append(++i).append(" - ").append(s.getTitle()).append(" (").append(s.getDuration() / 60)
                        .append(":").append(String.format("%02d", s.getDuration() % 60)).append(")\n");
            }
            log.log(Level.INFO, "Choose the song to add in the album \"{0}\":\n{1}",
                    new Object[] { albumToAddIn.getTitle(), songSb });
            String songLine = sc.nextLine();
            try {
                // Number input
                albumToAddIn.addSong(library.getStoredSongs().get(Integer.parseInt(songLine) - 1));
            } catch (NumberFormatException e) {
                // Text input
                albumToAddIn.addSong(console.getSongFromString(songLine, library));
            }
            log.info("Add another song? [yes/no]");
        } while (sc.nextLine().contains("y"));
    }

    /**
     * Creates an <code>AudioBook</code> from user inputs in console, and adds it to
     * the <code>Library</code>.
     */
    private void createAudioBookFromUserInput() {
        log.info("Audiobook title?");
        String audiobookTitle = sc.nextLine();
        log.info("Audiobook author?");
        String audiobookAuthor = sc.nextLine();
        log.info("Audiobook duration? (in seconds)");
        int audiobookDuration = sc.nextInt();
        sc.nextLine();
        log.log(Level.INFO, "Audiobook language? {0}",
                Arrays.toString(Language.getStringValues()).replace("[", "(").replace("]", ")"));
        Language language = null;
        String audiobookLanguage = sc.nextLine();
        for (Language languageItem : Language.values()) {
            if (languageItem.getName().toLowerCase().contains(audiobookLanguage.toLowerCase())) {
                language = languageItem;
                break;
            }
        }
        if (language == null)
            log.warning("Error: language not found. Please try again.");
        log.log(Level.INFO, "Audiobook category? {0}",
                Arrays.toString(Category.getStringValues()).replace("[", "(").replace("]", ")"));
        Category category = null;
        String audiobookCategory = sc.nextLine();
        for (Category categoryItem : Category.values()) {
            if (categoryItem.getName().toLowerCase().contains(audiobookCategory.toLowerCase())) {
                category = categoryItem;
                break;
            }
        }
        if (category == null)
            log.warning("Error: category not found. Please try again.");
        library.addToAudioBooksLibrary(
                new AudioBook(audiobookTitle, audiobookAuthor, audiobookDuration, language, category));
    }

    /**
     * Creates a <code>Playlist</code> from user inputs in console, and adds it to
     * the <code>Library</code>. Allows to add <code>Song</code>s among existing
     * <code>Song</code>s in the <code>Library</code>.
     */
    private void createPlaylistFromUserInput() {
        // From existing sources
        log.info("Playlist title?");
        String playlistTitle = sc.nextLine();
        List<AudioContent> toAddIn = new ArrayList<>();
        do {
            String inputContent = null;
            log.info("Which type of content to add? [song/audiobook]");
            String contentType = sc.nextLine();
            if (contentType.toLowerCase().startsWith("a") || contentType.toLowerCase().startsWith("b")) {
                StringBuilder audiobookSb = new StringBuilder();
                for (AudioBook ab : library.getStoredAudioBooks()) {
                    audiobookSb.append(library.getStoredAudioBooks().indexOf(ab) + 1).append(") ")
                            .append(ab.getAuthor()).append(" - ").append(ab.getTitle())
                            .append(System.getProperty("line.separator"));
                }
                log.log(Level.INFO, "Which audiobook to add?\n{0}", audiobookSb);
                inputContent = sc.nextLine();
                contentType = "audiobook";
            } else if (contentType.toLowerCase().startsWith("s")) {
                StringBuilder songSb = new StringBuilder();
                for (Song s : library.getStoredSongs()) {
                    songSb.append(library.getStoredSongs().indexOf(s) + 1).append(" - ").append(s.getTitle())
                            .append(System.getProperty("line.separator"));
                }
                log.log(Level.INFO, "Which song to add?\n{0}", songSb);
                inputContent = sc.nextLine();
                contentType = "song";
            } else {
                log.warning("Error parsing content. Please try again.");
            }
            if (inputContent != null)
                toAddIn.add(getAudioContentFromInput(inputContent, contentType));
            log.info("Add another content? [yes/no]");
        } while (sc.nextLine().contains("y"));
        library.addToPlaylistsLibrary(new Playlist(playlistTitle, toAddIn));
    }

    /**
     * Returns the matching <code>AudioContent</code> from the <code>Library</code>
     * from an user input (index or text). Used by
     * <code>createPlaylistFromUserInput(Scanner)</code>.
     *
     * @param input       the index or text user input
     * @param listToParse the list to search in (<code>Song</code> or
     *                    <code>AudioBook</code>). Can be exactly "song" or
     *                    "audiobook".
     * @return the matched <code>AudioContent</code>, or <code>null</code> if not
     *         found
     */
    private AudioContent getAudioContentFromInput(String input, String listToParse) {
        if (listToParse.equals("song")) {
            try {
                // Number input
                return library.getStoredSongs().get(Integer.parseInt(input) - 1);
            } catch (NumberFormatException e) {
                // Text input
                return console.getSongFromString(input, library);
            }
        } else if (listToParse.equals("audiobook")) {
            try {
                // Number input
                return library.getStoredAudioBooks().get(Integer.parseInt(input) - 1);
            } catch (NumberFormatException e) {
                // Text input
                return console.getAudioBookFromString(input, library);
            }
        }
        return null;
    }

    /**
     * Deletes a <code>Playlist</code> from user inputs in console. Prompt existing
     * <code>Playlist</code>s, and searches for a match in index or name.
     */
    private void deletePlaylistFromUserInput() {
        StringBuilder sb = new StringBuilder();
        for (Playlist playlist : library.getStoredPlaylists()) {
            sb.append(library.getStoredPlaylists().indexOf(playlist) + 1).append(") ").append(playlist.getName())
                    .append(System.getProperty("line.separator"));
        }
        log.log(Level.INFO, "Which playlist do you want to delete?\n{0}", sb);
        String albumToParse = sc.nextLine();
        try {
            library.removeFromPlaylistsLibary(library.getStoredPlaylists().get(Integer.parseInt(albumToParse) - 1));
        } catch (Exception e) {
            List<Playlist> libraryPlaylists = library.getStoredPlaylists();
            for (Playlist ab : libraryPlaylists) {
                if (ab.getName().toLowerCase().contains(albumToParse.toLowerCase())) {
                    log.log(Level.INFO, "Remove the playlist \"{0}\"? [yes/no]", ab.getName());
                    if (sc.nextLine().toLowerCase().contains("y")) {
                        log.log(Level.INFO, "Playlist removed: {0}", ab.getName());
                        library.removeFromPlaylistsLibary(ab);
                    } else {
                        log.info("No playlist deleted, aborting.");
                    }
                    break;
                }
                if (libraryPlaylists.indexOf(ab) == libraryPlaylists.size() - 1) {
                    log.warning("No playlist match found. Please try again.");
                }
            }
        }
    }

	/**
	 * Returns a formatted <code>Date</code> as a <code>String</code>
	 *
	 * @param date the <code>Date</code> to format
	 * @return the formatted <code>Date</code> as a <code>String</code>
	 */
	public static String getFormattedDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

	/**
	 * Returns a <code>Date</code> object from a <code>String</code> input.
	 *
	 * @param dateToParse the <code>String</code> date to parse
	 * @return the parsed <code>Date</code>
	 */
	public static Date getDateFromString(String dateToParse) {
        return java.sql.Date.valueOf(LocalDate.parse(dateToParse, DateTimeFormatter.ofPattern(DATE_FORMAT)));
    }

	/**
	 * Starts the MusicHub.
	 *
	 * @param args The arguments of the program.
	 */
	public static void main(String[] args) {
        SwingUtilities.invokeLater(MusicHub::new);
    }
}
