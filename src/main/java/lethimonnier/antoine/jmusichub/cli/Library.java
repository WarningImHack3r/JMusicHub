package lethimonnier.antoine.jmusichub.cli;

import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Library
 */
public class Library {

    private final ArrayList<Album> storedAlbums;
    private final ArrayList<Playlist> storedPlaylists;
    private final ArrayList<AudioBook> storedAudioBooks;
    private final ArrayList<Song> storedSongs;

    private static final Logger log = Logger.getGlobal();

    /**
     * Instantiates a new Library.
     */
    public Library() {
        storedAlbums = new ArrayList<>();
        storedPlaylists = new ArrayList<>();
        storedAudioBooks = new ArrayList<>();
        storedSongs = new ArrayList<>();
    }

    /**
     * Print albums by release date.
     *
     * @param albums the albums
     */
    // ADDITIONAL OPTIONS
    public static void printAlbumsByReleaseDate(List<Album> albums) {
        if (albums == null) {
            log.info("Null albums parameter");
            return;
        }
        albums.sort(Comparator.comparing(Album::getReleaseDate));
        StringBuilder sb = new StringBuilder();
        for (Album album : albums) {
            sb.append(album.getTitle()).append(" (").append(AudioContent.getFormattedDuration(album.getTotalDuration())).append(" - ").append(album.getReleaseDate()).append(")\n");
        }
        if (sb.isEmpty()) {
            log.info("Album list is empty");
            return;
        }
        log.log(Level.INFO, "Albums by release date:\n{0}", sb);
    }

    /**
     * Print albums by genre.
     *
     * @param albums the albums
     */
    public static void printAlbumsByGenre(List<Album> albums) {
        if (albums == null) {
            log.info("Null albums parameter");
            return;
        }
        StringBuilder genreSb;
        StringBuilder sbToPrint = new StringBuilder("Albums by genre:\n");
        for (Genre genre : Genre.values()) {
            genreSb = new StringBuilder();
            for (Album album : albums) {
                if (album.getGenres() != null && Arrays.asList(album.getGenres()).contains(genre)) {
                    genreSb.append(album.getTitle()).append("\n");
                }
            }
            if (!genreSb.isEmpty()) {
                sbToPrint.append(genre.getName()).append(":\n").append(genreSb);
            }
        }
        if (sbToPrint.isEmpty()) {
            log.info("Album list is empty");
            return;
        }
        log.log(Level.INFO, "{0}", sbToPrint);
    }

    /**
     * Print playlists alphabetically.
     *
     * @param playlists the playlists
     */
    public static void printPlaylistsAlphabetically(List<Playlist> playlists) {
        if (playlists == null) {
            log.info("Null playlists parameter");
            return;
        }
        playlists.sort(Comparator.comparing(Playlist::getName));
        StringBuilder sb = new StringBuilder();
        for (Playlist playlist : playlists) {
            sb.append(playlists.indexOf(playlist) + 1).append(" - ").append(playlist.getName()).append(" (").append(AudioContent.getFormattedDuration(playlist.getTotalDuration())).append(" - ").append(MusicHub.getFormattedDate(playlist.getLastModifiedDate())).append(")").append("\n");
        }
        if (sb.isEmpty()) {
            log.info("Playlists list is empty");
            return;
        }
        log.log(Level.INFO, "Playlists sorted alphabetically:\n{0}", sb);
    }

    /**
     * Print audio books by author.
     *
     * @param audioBooks the audio books
     */
    public static void printAudioBooksByAuthor(List<AudioBook> audioBooks) {
        if (audioBooks == null) {
            log.info("Null audiobooks parameter");
            return;
        }
        audioBooks.sort(Comparator.comparing(AudioBook::getAuthor));
        StringBuilder sb = new StringBuilder();
        for (AudioBook audioBook : audioBooks) {
            sb.append(audioBook.getAuthor()).append(" - ").append(audioBook.getTitle()).append("\n");
        }
        if (sb.isEmpty()) {
            log.info("Audiobooks list is empty");
            return;
        }
        log.log(Level.INFO, "Audiobooks by author:\n{0}", sb);
    }

    /**
     * Print all songs.
     *
     * @param storedSongs the stored songs
     */
    public static void printAllSongs(List<Song> storedSongs) {
        StringBuilder sb = new StringBuilder();
        for (Song song: storedSongs) {
            sb.append(song.toString().replace(";", " - ")).append("\n");
        }
        if (sb.isEmpty()) {
            log.info("Songs list is empty");
            return;
        }
        log.log(Level.INFO, "{0}", sb);
    }

    /**
     * Print all audio books.
     *
     * @param storedAudioBooks the stored audio books
     */
    public static void printAllAudioBooks(List<AudioBook> storedAudioBooks) {
        StringBuilder sb = new StringBuilder();
        for (AudioBook audioBook: storedAudioBooks) {
            sb.append(audioBook.toString().replace(";", " - ")).append("\n");
        }
        if (sb.isEmpty()) {
            log.info("Audiobooks list is empty");
            return;
        }
        log.log(Level.INFO, "{0}", sb);
    }

    /**
     * Print all albums.
     *
     * @param storedAlbums the stored albums
     */
    public static void printAllAlbums(List<Album> storedAlbums) {
        StringBuilder sb = new StringBuilder();
        for (Album album: storedAlbums) {
            sb.append(album.toString().replace(";", " - ")).append("\n");
        }
        if (sb.isEmpty()) {
            log.info("Albums list is empty");
            return;
        }
        log.log(Level.INFO, "{0}", sb);
    }

    /**
     * Print all playlists.
     *
     * @param storedPlaylists the stored playlists
     */
    public static void printAllPlaylists(List<Playlist> storedPlaylists) {
        StringBuilder sb = new StringBuilder();
        for (Playlist playlist: storedPlaylists) {
            sb.append(playlist.toString().replace(";", " - ")).append("\n");
        }
        if (sb.isEmpty()) {
            log.info("Playlists list is empty");
            return;
        }
        log.log(Level.INFO, "{0}", sb);
    }
    // END OF ADDITIONAL OPTIONS

    /**
     * Add to albums library.
     *
     * @param album the album
     */
    public void addToAlbumsLibrary(Album album) {
        if (album == null)
            throw new NullPointerException("Album cannot be null.");
        storedAlbums.add(album);
    }

    /**
     * Add to audio books library.
     *
     * @param audioBook the audio book
     */
    public void addToAudioBooksLibrary(AudioBook audioBook) {
        if (audioBook == null)
            throw new NullPointerException("AudioBook cannot be null.");
        storedAudioBooks.add(audioBook);
    }

    /**
     * Add to playlists library.
     *
     * @param playlist the playlist
     */
    public void addToPlaylistsLibrary(Playlist playlist) {
        if (playlist == null)
            throw new NullPointerException("Playlist cannot be null.");
        storedPlaylists.add(playlist);
    }

    /**
     * Remove from playlists libary.
     *
     * @param playlist the playlist
     */
    public void removeFromPlaylistsLibary(Playlist playlist) {
        if (playlist == null)
            throw new NullPointerException("Playlist cannot be null.");
        storedPlaylists.remove(playlist);
    }

    /**
     * Add to songs library.
     *
     * @param song the song
     */
    public void addToSongsLibrary(Song song) {
        if (song == null)
            throw new NullPointerException("Song cannot be null.");
        storedSongs.add(song);
    }

    /**
     * Gets stored albums.
     *
     * @return the stored albums
     */
    public List<Album> getStoredAlbums() {
        return storedAlbums;
    }

    /**
     * Gets stored audio books.
     *
     * @return the stored audio books
     */
    public List<AudioBook> getStoredAudioBooks() {
        return storedAudioBooks;
    }

    /**
     * Gets stored playlists.
     *
     * @return the stored playlists
     */
    public List<Playlist> getStoredPlaylists() {
        return storedPlaylists;
    }

    /**
     * Gets stored songs.
     *
     * @return the stored songs
     */
    public List<Song> getStoredSongs() {
        return storedSongs;
    }
}
