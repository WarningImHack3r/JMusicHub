package lethimonnier.antoine.jmusichub.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;

/**
 * Library
 */
public class Library {

    private ArrayList<Album> storedAlbums;
    private ArrayList<Playlist> storedPlaylists;
    private ArrayList<AudioBook> storedAudioBooks;
    private ArrayList<Song> storedSongs;

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
            sb.append(album.getTitle() + " (" + album.getReleaseDate() + ")\n");
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
                    genreSb.append(album.getTitle() + "\n");
                }
            }
            if (!genreSb.isEmpty()) {
                sbToPrint.append(genre.getName() + ":\n" + genreSb);
            }
        }
        log.log(Level.INFO, "{0}", sbToPrint);
    }

    public static void printPlaylistsAlphabetically(List<Playlist> playlists) {
        playlists.sort(Comparator.comparing(Playlist::getName));
        StringBuilder sb = new StringBuilder();
        for (Playlist playlist : playlists) {
            sb.append((playlists.indexOf(playlist) + 1) + " - " + playlist.getName() + "\n");
        }
        log.log(Level.INFO, "Playlists sorted alphabetically:\n{0}", sb);
    }

    public static void printAudioBooksByAuthor(List<AudioBook> audioBooks) {
        audioBooks.sort(Comparator.comparing(AudioBook::getAuthor));
        StringBuilder sb = new StringBuilder();
        for (AudioBook audioBook : audioBooks) {
            sb.append(audioBook.getAuthor() + " - " + audioBook.getTitle() + "\n");
        }
        log.log(Level.INFO, "Audiobooks by author:\n{0}", sb);
    }

    public void addToAlbumsLibrary(Album album) {
        if (album == null)
            throw new NullPointerException("Album cannot be null.");
        storedAlbums.add(album);
    }

    public void removeFromAlbumsLibary(Album album) {
        if (album == null)
            throw new NullPointerException("Album cannot be null.");
        storedAlbums.remove(album);
    }

    public void addToAudioBooksLibrary(AudioBook audioBook) {
        if (audioBook == null)
            throw new NullPointerException("AudioBook cannot be null.");
        storedAudioBooks.add(audioBook);
    }

    public void removeFromAudioBooksLibary(AudioBook audioBook) {
        if (audioBook == null)
            throw new NullPointerException("AudioBook cannot be null.");
        storedAudioBooks.remove(audioBook);
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

    public void removeFromSongsLibary(Song song) {
        if (song == null)
            throw new NullPointerException("Song cannot be null.");
        storedSongs.remove(song);
    }

    public void setStoredAlbums(List<Album> storedAlbums) {
        this.storedAlbums = new ArrayList<>(storedAlbums);
    }

    public void setStoredAudioBooks(List<AudioBook> storedAudioBooks) {
        this.storedAudioBooks = new ArrayList<>(storedAudioBooks);
    }

    public void setStoredPlaylists(List<Playlist> storedPlaylists) {
        this.storedPlaylists = new ArrayList<>(storedPlaylists);
    }

    public void setStoredSongs(List<Song> storedSongs) {
        this.storedSongs = new ArrayList<>(storedSongs);
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
