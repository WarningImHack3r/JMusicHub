package lethimonnier.antoine.jmusichub.cli;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.classes.parsing.CSVParser;
import lethimonnier.antoine.jmusichub.cli.classes.parsing.ConsoleParser;
import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

/**
 * MusicHub
 */
public final class MusicHub {

    private final Logger log = Logger.getGlobal();
    private Library library;
    private ConsoleParser console;
    private CSVParser csv;
    private File currentFile;

    private MusicHub() {
        log.info("Welcome to the MusicHub!");
        library = new Library();
        try {
            currentFile = openFileFromChooser();
            if (currentFile == null) {
                log.warning("No input file found.");
            } else {
                int imported = importSavedContentFromFile(currentFile);
                log.log(Level.INFO, "Successfully imported {0} elements", imported);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        log.info("What do you want to do?");
        Scanner sc = new Scanner(System.in);
        while (true) {
            Scanner innerSc;
            switch (sc.nextLine().toLowerCase().charAt(0)) {
                case 'c':
                    // creates a new song
                    log.info("New song creation.");
                    innerSc = new Scanner(System.in);
                    createNewSongFromUserInput(sc);
                    innerSc.close();
                    break;

                case 'a':
                    // creates a new album
                    log.info("Album creation.");
                    innerSc = new Scanner(System.in);
                    Album createdAlbum = createBlankAlbumFromUserInput(innerSc);
                    Scanner nextOrNot = new Scanner(System.in);
                    log.log(Level.INFO, "Add songs to newly created album {0}? [yes/no]", createdAlbum.getTitle());
                    if (nextOrNot.nextLine().contains("y"))
                        addSongToAlbum(innerSc, createdAlbum);
                    innerSc.close();
                    break;

                case '+':
                    // adds (an) existing song(s) to an album
                    log.info("Song add.");
                    innerSc = new Scanner(System.in);
                    addSongToAlbum(innerSc, null);
                    innerSc.close();
                    break;

                case 'l':
                    // creates a new audio book
                    log.info("Audio book creation.");
                    innerSc = new Scanner(System.in);
                    createAudioBookFromUserInput(innerSc);
                    innerSc.close();
                    break;

                case 'p':
                    // creates a new playlist from existing songs/audio books
                    log.info("Playlist creation.");
                    innerSc = new Scanner(System.in);
                    createPlaylistFromUserInput(innerSc);
                    innerSc.close();
                    break;

                case '-':
                    // deletes a playlist
                    log.info("Playlist deletion.");
                    innerSc = new Scanner(System.in);
                    deletePlaylistFromUserInput(innerSc);
                    innerSc.close();
                    break;

                case 's':
                    // saves everything in a csv file
                    log.info("Saving library.");
                    try {
                        saveLibaryToFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case 't':
                    // additional option: calls 4 print functions of Library
                    log.info("Printing stored albums by genre");
                    Library.printAlbumsByGenre(library.getStoredAlbums());
                    log.info("Printing stored albums by release date");
                    Library.printAlbumsByReleaseDate(library.getStoredAlbums());
                    log.info("Printing stored audiobooks by author");
                    Library.printAudioBooksByAuthor(library.getStoredAudioBooks());
                    log.info("Printing stored playlists alphabetically");
                    Library.printPlaylistsAlphabetically(library.getStoredPlaylists());
                    break;

                case 'h':
                    // shows help
                    log.info("MusicHub help - available options\n" + "c: creates a song from user input\n"
                            + "a: creates an album without songs\n" + "+: add (an) existing song(s) to an album\n"
                            + "l: creates a new audiobook\n" + "p: creates a playlist with existing audio contents\n"
                            + "-: deletes a playlist\n" + "s: exports the whole database\n"
                            + "t: additional option that shows stats of the database\n" + "h: shows this help menu\n");
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
     * 
     * @param scanner the <code>Scanner</code> to use
     */
    private void createNewSongFromUserInput(Scanner scanner) {
        log.info("Song title?");
        String songTitle = scanner.nextLine();
        log.info("Song author(s)? (separated with spaces)");
        String[] songAuthors = scanner.nextLine().split(" ");
        log.info("Song duration? (in seconds)");
        int songDuration = scanner.nextInt();
        log.log(Level.INFO, "Song genre? {0}", Arrays.toString(Genre.values()).replace("[", "(").replace("]", ")"));
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
     * @param scanner the <code>Scanner</code> to use
     * @return the created <code>Album</code>
     */
    private Album createBlankAlbumFromUserInput(Scanner scanner) {
        log.info("Album title?");
        String albumTitle = scanner.nextLine();
        log.info("Album author?");
        String albumAuthor = scanner.nextLine();
        log.info("Album creation date?");
        String[] toPrint = { "Year (yyyy)", "Month (MM)", "Day (dd)" };
        int[] fields = { 0 /* year */, 0 /* month */, 0 /* day */ };
        for (int i = 0; i < fields.length; i++) {
            log.log(Level.INFO, "{0}: ", toPrint[i]);
            fields[i] = scanner.nextInt();
        }
        scanner.nextLine();
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
     * @param scanner     the <code>Scanner</code> to use
     * @param targetAlbum the album to add songs in. Can be <code>null</code>
     */
    private void addSongToAlbum(Scanner scanner, Album targetAlbum) {
        Album albumToAddIn = targetAlbum;
        if (albumToAddIn == null) {
            // Prompt albums to choose among them
            StringBuilder albumSb = new StringBuilder();
            for (Album a : library.getStoredAlbums()) {
                albumSb.append((library.getStoredAlbums().indexOf(a) + 1) + ") " + a.getTitle() + " - " + a.getAuthor()
                        + "\n");
            }
            log.log(Level.INFO, "Choose your album to add songs in:\n{0}", albumSb);
            String line = scanner.nextLine();
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
        Scanner anotherSong = new Scanner(System.in);
        do {
            StringBuilder songSb = new StringBuilder();
            int i = 0;
            for (Song s : library.getStoredSongs()) {
                songSb.append(
                        ++i + " - " + s.getTitle() + "(" + s.getDuration() / 60 + ":" + s.getDuration() % 60 + ")\n");
            }
            log.log(Level.INFO, "Choose the song to add in the album \"{0}\":\n{1}",
                    new Object[] { albumToAddIn.getTitle(), songSb });
            String songLine = scanner.nextLine();
            try {
                // Number input
                albumToAddIn.addSong(library.getStoredSongs().get(Integer.parseInt(songLine) - 1));
            } catch (NumberFormatException e) {
                // Text input
                albumToAddIn.addSong(console.getSongFromString(songLine, library));
            }
            log.info("Add another song? [yes/no]");
        } while (anotherSong.nextLine().contains("y"));
        anotherSong.close();
    }

    /**
     * Creates an <code>AudioBook</code> from user inputs in console, and adds it to
     * the <code>Library</code>.
     * 
     * @param scanner the <code>Scanner</code> to use
     */
    private void createAudioBookFromUserInput(Scanner scanner) {
        log.info("Audiobook title?");
        String audiobookTitle = scanner.nextLine();
        log.info("Audiobook author?");
        String audiobookAuthor = scanner.nextLine();
        log.info("Audiobook duration? (in seconds)");
        int audiobookDuration = scanner.nextInt();
        log.log(Level.INFO, "Audiobook language? {0}",
                Arrays.toString(Language.values()).replace("[", "(").replace("]", ")"));
        Language language = null;
        String audiobookLanguage = scanner.nextLine();
        for (Language languageItem : Language.values()) {
            if (languageItem.getName().toLowerCase().contains(audiobookLanguage.toLowerCase())) {
                language = languageItem;
                break;
            }
        }
        if (language == null)
            log.warning("Error: language not found. Please try again.");
        log.log(Level.INFO, "Audiobook category? {0}",
                Arrays.toString(Category.values()).replace("[", "(").replace("]", ")"));
        Category category = null;
        String audiobookCategory = scanner.nextLine();
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
     * 
     * @param scanner the <code>Scanner</code> to use
     */
    private void createPlaylistFromUserInput(Scanner scanner) {
        // From existing sources
        log.info("Playlist title?");
        String playlistTitle = scanner.nextLine();
        List<AudioContent> toAddIn = new ArrayList<>();
        Scanner anotherContent = new Scanner(System.in);
        do {
            Scanner playlistScanner = new Scanner(System.in);
            String inputContent = null;
            log.info("Which type of content to add? [song/audiobook]");
            String contentType = playlistScanner.nextLine();
            if (contentType.toLowerCase().startsWith("a") || contentType.toLowerCase().startsWith("b")) {
                StringBuilder audiobookSb = new StringBuilder();
                for (AudioBook ab : library.getStoredAudioBooks()) {
                    audiobookSb.append((library.getStoredAudioBooks().indexOf(ab) + 1) + ") " + ab.getAuthor() + " - "
                            + ab.getTitle() + "\n");
                }
                log.log(Level.INFO, "Which audiobook to add?\n{0}", audiobookSb);
                inputContent = playlistScanner.nextLine();
                contentType = "audiobook";
            } else if (contentType.toLowerCase().startsWith("s")) {
                StringBuilder songSb = new StringBuilder();
                for (Song s : library.getStoredSongs()) {
                    songSb.append((library.getStoredSongs().indexOf(s) + 1) + " - " + s.getTitle() + "\n");
                }
                log.log(Level.INFO, "Which song to add?\n{0}", songSb);
                inputContent = playlistScanner.nextLine();
                contentType = "song";
            } else {
                log.warning("Error parsing content. Please try again.");
            }
            if (inputContent != null)
                toAddIn.add(getAudioContentFromInput(inputContent, contentType));
            playlistScanner.close();
            log.info("Add another content? [yes/no]");
        } while (anotherContent.nextLine().contains("y"));
        anotherContent.close();
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
     * 
     * @param scanner the <code>Scanner</code> to use
     */
    private void deletePlaylistFromUserInput(Scanner scanner) {
        StringBuilder sb = new StringBuilder();
        for (Playlist playlist : library.getStoredPlaylists()) {
            sb.append((library.getStoredPlaylists().indexOf(playlist) + 1) + ") " + playlist.getName() + "\n");
        }
        log.log(Level.INFO, "Which playlist do you want to delete?\n{0}", sb);
        String albumToParse = scanner.nextLine();
        try {
            library.removeFromPlaylistsLibary(library.getStoredPlaylists().get(Integer.parseInt(albumToParse) - 1));
        } catch (Exception e) {
            List<Playlist> libraryPlaylists = library.getStoredPlaylists();
            for (Playlist ab : libraryPlaylists) {
                if (ab.getName().toLowerCase().contains(albumToParse.toLowerCase())) {
                    log.log(Level.INFO, "Removed the playlist \"{0}\"? [yes/no]", ab.getName());
                    if (scanner.nextLine().toLowerCase().contains("y")) {
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
        scanner.close();
    }

    /**
     * Returns a <code>Date</code> object from a <code>String</code> input.
     * 
     * @param dateToParse the <code>String</code> date to parse
     * @return the parsed <code>Date</code>
     */
    private Date getDateFromString(String dateToParse) {
        return java.sql.Date.valueOf(LocalDate.parse(dateToParse, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    /**
     * Prompts a <code>JFileChooser</code> to choose a .csv file to import.
     * 
     * @return the chosen <code>File</code>
     * @throws IOException if an error occurs
     */
    private File openFileFromChooser() throws IOException {
        // Import file and read it
        log.info("Please choose your input .csv file.");
        JFileChooser dialog = new JFileChooser();
        dialog.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return !f.isDirectory() && f.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV file (*.csv)";
            }
        });
        switch (dialog.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                return dialog.getSelectedFile();

            case JFileChooser.ERROR_OPTION:
                throw new IOException("An error occurred selecting file.");

            case JFileChooser.CANCEL_OPTION:
            default:
                return null;
        }
    }

    /**
     * Imports the content of a csv file into the libray. Can parse
     * <code>Album</code>s, <code>Playlist</code>s, <code>AudioBook</code>s and
     * <code>Song</code>s.
     * <hr>
     * <strong>Parsing rules</strong> (delimited by ',', [] means optional):
     * <ul>
     * <li><em>Album:</em> title, author, creation date (dd/MM/yyyy) [, songs]</li>
     * <li><em>Playlist:</em> title[, audiocontents]</li>
     * <li><em>Song:</em> title, author(s), duration (in seconds), genre</li>
     * <li><em>AudioBook:</em> title, author, duration (in seconds), language,
     * category</li>
     * </ul>
     * Please refer to the corresponding classes for more info.
     * <hr>
     * 
     * @param file the csv file to import
     * @return the number of inputs added into the <code>Library</code>
     * @throws IOException            if an input error occurs
     * @throws CsvValidationException if the file cannot be parsed
     */
    private int importSavedContentFromFile(File file) throws IOException, CsvValidationException {
        // Instantiate Albums, Playlists, AudioBooks and Songs
        int state = 0; // 0 = idle, 1 = alb, 2 = playl, 3 = ab, 4 = song
        int importsCount = 0;

        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                String firstCell = line[0];
                switch (firstCell) {
                    case "ALBUMS":
                        state = 1;
                        break;

                    case "PLAYLISTS":
                        state = 2;
                        break;

                    case "AUDIOBOOKS":
                        state = 3;
                        break;

                    case "SONGS":
                        state = 4;
                        break;

                    default:
                        break;
                }
                if (firstCell.isEmpty() || state > 0)
                    continue;
                switch (state) {
                    case 1: // Album
                        Song[] songs = null;
                        if (line.length > 2) {
                            songs = new Song[line.length - 3];
                            for (int i = 3; i < line.length; i++) {
                                songs[i - 3] = csv.getSongFromString(line[i], null);
                            }
                        }
                        library.addToAlbumsLibrary(new Album(songs, firstCell, line[1], getDateFromString(line[2])));
                        importsCount++;
                        break;

                    case 2: // Playlist
                        importsCount++;
                        break;

                    case 3: // AudioBook
                        importsCount++;
                        break;

                    case 4: // Song
                        importsCount++;
                        break;

                    default:
                        break;
                }
            }
        }

        return importsCount;
    }

    /**
     * Saves the content of the <code>Library</code> into a csv file.
     * 
     * @param file the file to export in
     */
    private void saveLibaryToFile() throws IOException  {
        // TODO
        File f = null;
        log.info("Please choose your input .csv file.");
        JFileChooser dialog = new JFileChooser();
        dialog.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return !f.isDirectory() && f.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV file (*.csv)";
            }
        });
        switch (dialog.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                f = dialog.getSelectedFile();
                break;

            case JFileChooser.ERROR_OPTION:
                throw new IOException("An error occurred selecting file.");

            case JFileChooser.CANCEL_OPTION:
            default:
            break;
        }
        
        
        CSVWriter writer = new CSVWriter(new FileWriter(f), ';', '0', '0', null);
        
        
        
        
        
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
