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

    public Library() {
        storedAlbums = new ArrayList<>();
        storedPlaylists = new ArrayList<>();
        storedAudioBooks = new ArrayList<>();
        storedSongs = new ArrayList<>();
    }

    public static void printAlbumsByReleaseDate(List<Album> albums) {
        albums.sort(Comparator.comparing(Album::getReleaseDate));
        StringBuilder sb = new StringBuilder();
        for (Album album : albums) {
            sb.append(album.getTitle()).append(" (").append(AudioContent.getFormattedDuration(album.getTotalDuration())).append(" - ").append(album.getReleaseDate()).append(")\n");
        }
        log.log(Level.INFO, "Albums by release date:\n{0}", sb);
    }

    public static void printAlbumsByGenre(List<Album> albums) {
        StringBuilder genreSb;
        StringBuilder sbToPrint = new StringBuilder("Albums by genre:\n");
        for (Genre genre : Genre.values()) {
            genreSb = new StringBuilder();
            for (Album album : albums) {
                if (Arrays.asList(album.getGenres()).contains(genre)) {
                    genreSb.append(album.getTitle()).append("\n");
                }
            }
            if (!genreSb.isEmpty()) {
                sbToPrint.append(genre.getName()).append(":\n").append(genreSb);
            }
        }
        log.log(Level.INFO, "{0}", sbToPrint);
    }

    public static void printPlaylistsAlphabetically(List<Playlist> playlists) {
        playlists.sort(Comparator.comparing(Playlist::getName));
        StringBuilder sb = new StringBuilder();
        for (Playlist playlist : playlists) {
            sb.append(playlists.indexOf(playlist) + 1).append(" - ").append(playlist.getName()).append(" (").append(AudioContent.getFormattedDuration(playlist.getTotalDuration())).append(" - ").append(MusicHub.getFormattedDate(playlist.getLastModifiedDate())).append(")").append("\n");
        }
        log.log(Level.INFO, "Playlists sorted alphabetically:\n{0}", sb);
    }

    public static void printAudioBooksByAuthor(List<AudioBook> audioBooks) {
        audioBooks.sort(Comparator.comparing(AudioBook::getAuthor));
        StringBuilder sb = new StringBuilder();
        for (AudioBook audioBook : audioBooks) {
            sb.append(audioBook.getAuthor()).append(" - ").append(audioBook.getTitle()).append("\n");
        }
        log.log(Level.INFO, "Audiobooks by author:\n{0}", sb);
    }

    public void addToAlbumsLibrary(Album album) {
        if (album == null)
            throw new NullPointerException("Album cannot be null.");
        storedAlbums.add(album);
    }

    public void addToAudioBooksLibrary(AudioBook audioBook) {
        if (audioBook == null)
            throw new NullPointerException("AudioBook cannot be null.");
        storedAudioBooks.add(audioBook);
    }

    public void addToPlaylistsLibrary(Playlist playlist) {
        if (playlist == null)
            throw new NullPointerException("Playlist cannot be null.");
        storedPlaylists.add(playlist);
    }

    public void removeFromPlaylistsLibary(Playlist playlist) {
        if (playlist == null)
            throw new NullPointerException("Playlist cannot be null.");
        storedPlaylists.remove(playlist);
    }

    public void addToSongsLibrary(Song song) {
        if (song == null)
            throw new NullPointerException("Song cannot be null.");
        storedSongs.add(song);
    }

    public List<Album> getStoredAlbums() {
        return storedAlbums;
    }

    public List<AudioBook> getStoredAudioBooks() {
        return storedAudioBooks;
    }

    public List<Playlist> getStoredPlaylists() {
        return storedPlaylists;
    }

    public List<Song> getStoredSongs() {
        return storedSongs;
    }
}
