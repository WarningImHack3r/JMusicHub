package lethimonnier.antoine.jmusichub.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import lethimonnier.antoine.jmusichub.cli.classes.Album;
import lethimonnier.antoine.jmusichub.cli.classes.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.Song;
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
    private File currentFile;

    private MusicHub() {
        log.info("Welcome to the MusicHub!");
        library = new Library();
        try {
            currentFile = openFileFromChooser();
            importSavedContentFromFile(currentFile);
        } catch (IOException e) {
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
                    createNewSong(sc);
                    innerSc.close();
                    break;

                case 'a':
                    // creates a new album
                    log.info("Album creation.");
                    innerSc = new Scanner(System.in);
                    Album createdAlbum = createBlankAlbum(innerSc);
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
                    createAudioBook(innerSc);
                    innerSc.close();
                    break;

                case 'p':
                    // creates a new playlist from existing songs/audio books
                    log.info("Playlist creation.");
                    innerSc = new Scanner(System.in);
                    createPlaylist(innerSc);
                    innerSc.close();
                    break;

                case '-':
                    // deletes a playlist
                    log.info("Playlist deletion.");
                    innerSc = new Scanner(System.in);
                    deletePlaylist(innerSc);
                    innerSc.close();
                    break;

                case 's':
                    // saves everything in a csv/xls/xlsx file
                    log.info("Saving library.");
                    innerSc = new Scanner(System.in);
                    saveLibaryToFile(currentFile);
                    innerSc.close();
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

    private void createNewSong(Scanner scanner) {
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

    private Album createBlankAlbum(Scanner scanner) {
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
                Album functionResult = parseAlbumFromStringInput(line);
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
                albumToAddIn.addSong(parseSongFromStringInput(songLine));
            }
            log.info("Add another song? [yes/no]");
        } while (anotherSong.nextLine().contains("y"));
        anotherSong.close();
    }

    private Album parseAlbumFromStringInput(String input) {
        Album toReturn = null;
        List<Album> libraryAlbums = library.getStoredAlbums();
        for (Album a : libraryAlbums) {
            if (a.getTitle().toLowerCase().contains(input.toLowerCase())
                    || a.getAuthor().toLowerCase().contains(input.toLowerCase())) {
                toReturn = a;
                log.log(Level.INFO, "Album match found: {0}", a.getTitle() + " - " + a.getAuthor());
                break;
            }
            if (libraryAlbums.indexOf(a) == libraryAlbums.size() - 1) {
                log.warning("No album match found. Please try again.");
            }
        }
        return toReturn;
    }

    private Song parseSongFromStringInput(String input) {
        Song toReturn = null;
        List<Song> librarySongs = library.getStoredSongs();
        for (Song s : librarySongs) {
            if (s.getTitle().toLowerCase().contains(input.toLowerCase())) {
                toReturn = s;
                log.log(Level.INFO, "Match found: {0}", s.getTitle());
                break;
            }
            if (librarySongs.indexOf(s) == librarySongs.size() - 1) {
                log.warning("No match found. Please try again.");
            }
        }
        return toReturn;
    }

    private void createAudioBook(Scanner scanner) {
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

    private void createPlaylist(Scanner scanner) {
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
            if (inputContent != null) {
                toAddIn.add(parseAudioContentFromInput(inputContent, contentType));
            }
            playlistScanner.close();
            log.info("Add another content? [yes/no]");
        } while (anotherContent.nextLine().contains("y"));
        anotherContent.close();
        library.addToPlaylistsLibrary(new Playlist(playlistTitle, null));
    }

    private AudioContent parseAudioContentFromInput(String input, String listToParse) {
        if (listToParse.equals("song")) {
            try {
                // Number input
                return library.getStoredSongs().get(Integer.parseInt(input) - 1);
            } catch (NumberFormatException e) {
                // Text input
                return parseSongFromStringInput(input);
            }
        } else if (listToParse.equals("audiobook")) {
            try {
                // Number input
                return library.getStoredAudioBooks().get(Integer.parseInt(input) - 1);
            } catch (NumberFormatException e) {
                // Text input
                return parseAudiobookFromStringInput(input);
            }
        }
        return null;
    }
    
    private AudioBook parseAudiobookFromStringInput(String input) {
        AudioBook toReturn = null;
        List<AudioBook> libraryAudioBooks = library.getStoredAudioBooks();
        for (AudioBook ab : libraryAudioBooks) {
            if (ab.getTitle().toLowerCase().contains(input.toLowerCase())) {
                toReturn = ab;
                log.log(Level.INFO, "Audiobook match found: {0}", ab.getTitle());
                break;
            }
            if (libraryAudioBooks.indexOf(ab) == libraryAudioBooks.size() - 1) {
                log.warning("No audiobook match found. Please try again.");
            }
        }
        return toReturn;
    }

    private void deletePlaylist(Scanner scanner) {
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

    private File openFileFromChooser() throws IOException {
        // Import file and read it
        log.info("Please choose your input .xls/.xlsx/.csv file.");
        // TODO
        throw new IOException();
    }

    private void importSavedContentFromFile(File file) {
        // Instantiate Albums, Playlists, AudioBooks and Songs
        // Throws exception if an error occurred
        // TODO
    }

    private void saveLibaryToFile(File file) {
        // TODO
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
