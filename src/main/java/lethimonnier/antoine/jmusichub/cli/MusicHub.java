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
import lethimonnier.antoine.jmusichub.cli.logging.MusicLogger;
import lethimonnier.antoine.jmusichub.cli.player.MusicManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MusicHub
 */
public final class MusicHub {

    /**
     * The constant scanner.
     */
    // Static final
    public static final Scanner scanner = new Scanner(System.in);

    // Final
    private final Logger logger = MusicLogger.getLogger("output.log");
    private final Library library = new Library();
    private final ConsoleParser parser = new ConsoleParser();
    private final CSVManager csvManager = new CSVManager();

    // Other variables
    private String filePath;

    private MusicHub() {
        logger.info("Welcome to the MusicHub!");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            var currentFile = csvManager.openFileFromChooser(null);
            if (currentFile == null) {
                logger.warning("No input file found.");
            } else {
                filePath = currentFile.getAbsolutePath();
                int imported = csvManager.importSavedContentFromFile(currentFile, library);
                logger.log(Level.INFO, "Successfully imported {0} elements", imported);
            }
        } catch (IOException | CsvValidationException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            startCli();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void startCli() throws IOException, CsvValidationException {
        while (true) {
            logger.info("What do you want to do? (Press 'h' to show help)");
            switch (scanner.nextLine().toLowerCase().charAt(0)) {
                case 'c' -> {
                    // add a new song
                    logger.info("New song adding.");
                    createNewSongFromUserInput();
                }

                case 'a' -> {
                    // creates a new album
                    logger.info("Album creation.");
                    var createdAlbum = createBlankAlbumFromUserInput();
                    logger.log(Level.INFO, "Add songs to newly created album {0}? [yes/no]", createdAlbum.getTitle());
                    if (scanner.nextLine().contains("y"))
                        addSongToAlbum(createdAlbum);
                }

                case '+' -> {
                    // adds (an) existing song(s) to an album
                    logger.info("Song add.");
                    addSongToAlbum(null);
                }

                case 'l' -> {
                    // creates a new audio book
                    logger.info("Audio book creation.");
                    createAudioBookFromUserInput();
                }

                case 'p' -> {
                    // creates a new playlist from existing songs/audio books
                    logger.info("Playlist creation.");
                    createPlaylistFromUserInput();
                }

                case '-' -> {
                    // deletes a playlist
                    logger.info("Playlist deletion.");
                    deletePlaylistFromUserInput();
                }

                case 'i' -> {
                    // imports from a csv file
                    var currentFile = csvManager.openFileFromChooser(filePath);
                    if (currentFile == null) {
                        logger.warning("No input file found.");
                        break;
                    }
                    filePath = currentFile.getAbsolutePath();
                    int imported = csvManager.importSavedContentFromFile(currentFile, library);
                    logger.log(Level.INFO, "Successfully imported {0} elements", imported);
                }

                case 's' -> {
                    // saves everything in a csv file
                    logger.info("Saving library.");
                    csvManager.saveLibaryToFile(library, filePath);
                }

                case 't' -> {
                    // additional option 1: calls 4 print functions of Library
                    logger.info("Printing stored albums by genre");
                    Library.printAlbumsByGenre(library.getStoredAlbums());
                    logger.info("Printing stored albums by release date");
                    Library.printAlbumsByReleaseDate(library.getStoredAlbums());
                    logger.info("Printing stored audiobooks by author");
                    Library.printAudioBooksByAuthor(library.getStoredAudioBooks());
                    logger.info("Printing stored playlists alphabetically");
                    Library.printPlaylistsAlphabetically(library.getStoredPlaylists());
                }

                case 'o' -> {
                    // additional option 2: shows the content of all the Library
                    logger.info("Printing stored Songs");
                    Library.printAllSongs(library.getStoredSongs());
                    logger.info("Printing stored AudioBooks");
                    Library.printAllAudioBooks(library.getStoredAudioBooks());
                    logger.info("Printing stored Albums");
                    Library.printAllAlbums(library.getStoredAlbums());
                    logger.info("Printing stored Playlists");
                    Library.printAllPlaylists(library.getStoredPlaylists());
                }

                case 'x' -> {
                    // additional option 3: clears the whole library
                    logger.info("Are you sure to want to clear the library? [yes/no] ");
                    if (!scanner.nextLine().contains("y")) {
                        logger.info("Cancelling.");
                        break;
                    }
                    library.getStoredAlbums().clear();
                    library.getStoredAudioBooks().clear();
                    library.getStoredPlaylists().clear();
                    library.getStoredSongs().clear();
                    logger.info("Library cleaned successfully");
                }

                case 'm' -> {
                    logger.info("Starting Music Player");
                    new MusicManager();
                }

                case 'h' ->
                        // shows help
                        logger.info("""
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
                                m: starts the music player
                                h: shows this help menu
                                q: quit the application
                                """);
                case 'q' -> {
                    logger.info("Bye!");
                    scanner.close();
                    return;
                }

                default -> logger.info("Unknown option");
            }
        }
    }

    /**
     * Creates a <code>Song</code> from user inputs in console, and adds it to the
     * <code>Library</code>.
     */
    private void createNewSongFromUserInput() {
        logger.info("Song title?");
        String songTitle = scanner.nextLine();
        logger.info("Song author(s)? (separated with commas)");
        String[] songAuthors = scanner.nextLine().split(",");
        logger.info("Song duration? (in seconds)");
        var songDuration = scanner.nextInt();
        scanner.nextLine();
        logger.log(Level.INFO, "Song genre? {0}",
                Arrays.toString(Genre.getStringValues()).replace("[", "(").replace("]", ")"));
        String songGenre = scanner.nextLine();
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
    @NotNull
    private Album createBlankAlbumFromUserInput() {
        logger.info("Album title?");
        String albumTitle = scanner.nextLine();
        logger.info("Album author?");
        String albumAuthor = scanner.nextLine();
        logger.info("Album creation date?");
        var toPrint = new String[] { "Year (yyyy)", "Month (MM)", "Day (dd)" };
        var fields = new int[] { 0 /* year */, 0 /* month */, 0 /* day */ };
        for (var i = 0; i < fields.length; i++) {
            logger.log(Level.INFO, "{0}: ", toPrint[i]);
            fields[i] = scanner.nextInt();
        }
        scanner.nextLine();
        var c = Calendar.getInstance();
        c.set(fields[0], fields[1] - 1 /* Calendar.JANUARY = 0 */, fields[2]);
        var albumParsedDate = c.getTime();
        var album = new Album(albumTitle, albumAuthor, albumParsedDate, new Song[] {});
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
        var albumToAddIn = targetAlbum;
        if (albumToAddIn == null) {
            // Prompt albums to choose among them
            var albumSb = new StringBuilder();
            for (Album a : library.getStoredAlbums()) {
                albumSb.append(library.getStoredAlbums().indexOf(a) + 1).append(") ").append(a.getTitle()).append(" - ").append(a.getAuthor()).append(System.lineSeparator());
            }
            logger.log(Level.INFO, "Choose your album to add songs in:{0}{1}", new Object[] { System.lineSeparator(), albumSb});
            String line = scanner.nextLine();
            try {
                // Number input
                albumToAddIn = library.getStoredAlbums().get(Integer.parseInt(line) - 1);
            } catch (NumberFormatException e) {
                // Text input
                var functionResult = parser.getAlbumFromString(line, library);
                if (functionResult == null)
                    return;
                albumToAddIn = functionResult;
            }
        }
        // Prompt songs to choose which to add
        do {
            var songSb = new StringBuilder();
            var i = 0;
            for (Song s : library.getStoredSongs()) {
                songSb.append(++i).append(" - ").append(s.getTitle()).append(" (").append(s.getDuration() / 60).append(":").append(String.format("%02d", s.getDuration() % 60)).append(")").append(System.lineSeparator());
            }
            logger.log(Level.INFO, "Choose the song to add in the album \"{0}\":{1}{2}",
                    new Object[] { albumToAddIn.getTitle(), System.lineSeparator(), songSb });
            String songLine = scanner.nextLine();
            try {
                // Number input
                albumToAddIn.addSong(library.getStoredSongs().get(Integer.parseInt(songLine) - 1));
            } catch (NumberFormatException e) {
                // Text input
                albumToAddIn.addSong(parser.getSongFromString(songLine, library));
            }
            logger.info("Add another song? [yes/no]");
        } while (scanner.nextLine().contains("y"));
    }

    /**
     * Returns the matching <code>AudioContent</code> from the <code>Library</code> from an user input (index or text).
     * Used by
     * <code>createPlaylistFromUserInput(Scanner)</code>.
     *
     * @param input       the index or text user input
     * @param listToParse the list to search in (<code>Song</code> or
     *                    <code>AudioBook</code>). Can be exactly "song" or
     *                    "audiobook".
     * @return the matched <code>AudioContent</code>, or <code>null</code> if not found
     */
    private AudioContent getAudioContentFromInput(String input, String listToParse) {
        if (listToParse.equals("song")) {
            try {
                // Number input
                return library.getStoredSongs().get(Integer.parseInt(input) - 1);
            } catch (NumberFormatException e) {
                // Text input
                return parser.getSongFromString(input, library);
            }
        } else if (listToParse.equals("audiobook")) {
            try {
                // Number input
                return library.getStoredAudioBooks().get(Integer.parseInt(input) - 1);
            } catch (NumberFormatException e) {
                // Text input
                return parser.getAudioBookFromString(input, library);
            }
        }
        return null;
    }

    /**
     * Creates an <code>AudioBook</code> from user inputs in console, and adds it to the <code>Library</code>.
     */
    private void createAudioBookFromUserInput() {
        logger.info("Audiobook title?");
        String audiobookTitle = scanner.nextLine();
        logger.info("Audiobook author?");
        String audiobookAuthor = scanner.nextLine();
        logger.info("Audiobook duration? (in seconds)");
        var audiobookDuration = scanner.nextInt();
        scanner.nextLine();
        logger.log(Level.INFO, "Audiobook language? {0}",
                Arrays.toString(Language.getStringValues()).replace("[", "(").replace("]", ")"));
        Language language = null;
        String audiobookLanguage = scanner.nextLine();
        for (Language languageItem : Language.values()) {
            if (languageItem.getName().toLowerCase().contains(audiobookLanguage.toLowerCase())) {
                language = languageItem;
                break;
            }
        }
        if (language == null)
            logger.warning("Error: language not found. Please try again.");
        logger.log(Level.INFO, "Audiobook category? {0}",
                Arrays.toString(Category.getStringValues()).replace("[", "(").replace("]", ")"));
        Category category = null;
        String audiobookCategory = scanner.nextLine();
        for (Category categoryItem : Category.values()) {
            if (categoryItem.getName().toLowerCase().contains(audiobookCategory.toLowerCase())) {
                category = categoryItem;
                break;
            }
        }
        if (category == null)
            logger.warning("Error: category not found. Please try again.");
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
        logger.info("Playlist title?");
        String playlistTitle = scanner.nextLine();
        List<AudioContent> toAddIn = new ArrayList<>();
        do {
            String inputContent = null;
            logger.info("Which type of content to add? [song/audiobook]");
            String contentType = scanner.nextLine();
            if (contentType.toLowerCase().startsWith("a") || contentType.toLowerCase().startsWith("b")) {
                var audiobookSb = new StringBuilder();
                for (AudioBook ab : library.getStoredAudioBooks()) {
                    audiobookSb.append(library.getStoredAudioBooks().indexOf(ab) + 1).append(") ").append(ab.getAuthor()).append(" - ").append(ab.getTitle()).append(System.lineSeparator());
                }
                logger.log(Level.INFO, "Which audiobook to add?{0}{1}", new Object[] { System.lineSeparator(),
                        audiobookSb });
                inputContent = scanner.nextLine();
                contentType = "audiobook";
            } else if (contentType.toLowerCase().startsWith("s")) {
                var songSb = new StringBuilder();
                for (Song s : library.getStoredSongs()) {
                    songSb.append(library.getStoredSongs().indexOf(s) + 1).append(" - ").append(s.getTitle()).append(System.lineSeparator());
                }
                logger.log(Level.INFO, "Which song to add?{0}{1}", new Object[] { System.lineSeparator(), songSb });
                inputContent = scanner.nextLine();
                contentType = "song";
            } else {
                logger.warning("Error parsing content. Please try again.");
            }
            if (inputContent != null)
                toAddIn.add(getAudioContentFromInput(inputContent, contentType));
            logger.info("Add another content? [yes/no]");
        } while (scanner.nextLine().contains("y"));
        library.addToPlaylistsLibrary(new Playlist(playlistTitle, toAddIn));
    }

    /**
     * Deletes a <code>Playlist</code> from user inputs in console. Prompt existing
     * <code>Playlist</code>s, and searches for a match in index or name.
     */
    private void deletePlaylistFromUserInput() {
        var sb = new StringBuilder();
        for (Playlist playlist : library.getStoredPlaylists()) {
            sb.append(library.getStoredPlaylists().indexOf(playlist) + 1).append(") ").append(playlist.getName()).append(System.lineSeparator());
        }
        logger.log(Level.INFO, "Which playlist do you want to delete?{0}{1}", new Object[] { System.lineSeparator(), sb });
        String albumToParse = scanner.nextLine();
        try {
            library.removeFromPlaylistsLibary(library.getStoredPlaylists().get(Integer.parseInt(albumToParse) - 1));
        } catch (Exception e) {
            List<Playlist> libraryPlaylists = library.getStoredPlaylists();
            for (Playlist ab : libraryPlaylists) {
                if (ab.getName().toLowerCase().contains(albumToParse.toLowerCase())) {
                    logger.log(Level.INFO, "Remove the playlist \"{0}\"? [yes/no]", ab.getName());
                    if (scanner.nextLine().toLowerCase().contains("y")) {
                        logger.log(Level.INFO, "Playlist removed: {0}", ab.getName());
                        library.removeFromPlaylistsLibary(ab);
                    } else {
                        logger.info("No playlist deleted, aborting.");
                    }
                    break;
                }
                if (libraryPlaylists.indexOf(ab) == libraryPlaylists.size() - 1) {
                    logger.warning("No playlist match found. Please try again.");
                }
            }
        }
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
