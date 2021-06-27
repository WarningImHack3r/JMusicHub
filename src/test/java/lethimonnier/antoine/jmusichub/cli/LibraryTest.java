package lethimonnier.antoine.jmusichub.cli;

import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {


    @Test
    void addToAlbumsLibrary() {
        Library libraryTest = new Library();
        Date d1 = new Date(19/02/2001);
        Album a1 = new Album(null,"Album1","Test",d1);
        libraryTest.addToAlbumsLibrary(a1);
        assertEquals("["+a1.toString()+"]",libraryTest.getStoredAlbums().toString());
    }

    @Test
    void addToAudioBooksLibrary() {
        Library libraryTest = new Library();
        AudioBook ab = new AudioBook("AudioBookTest","Test",75, Language.ENGLISH, Category.NOVEL);
        libraryTest.addToAudioBooksLibrary(ab);
        assertEquals("["+ab.toString()+"]",libraryTest.getStoredAudioBooks().toString());
    }

    @Test
    void addToPlaylistsLibrary() {
        Library libraryTest = new Library();
        Playlist p = new Playlist("PlayTest",null);
        libraryTest.addToPlaylistsLibrary(p);
        assertEquals("["+p.toString()+"]",libraryTest.getStoredPlaylists().toString());
    }

    @Test
    void removeFromPlaylistsLibary() {
        Library libraryTest = new Library();
        Library library = new Library();
        Playlist p = new Playlist("PlayTest",null);
        Playlist p2 = new Playlist("PlayTest2",null);
        libraryTest.addToPlaylistsLibrary(p);
        libraryTest.addToPlaylistsLibrary(p2);
        library.addToPlaylistsLibrary(p);
        libraryTest.removeFromPlaylistsLibary(p2);
        assertEquals(library.getStoredPlaylists().toString(),libraryTest.getStoredPlaylists().toString());
    }

    @Test
    void addToSongsLibrary() {
        Library libraryTest = new Library();
        String[] authors = new String[]{"author1"};
        Song s = new Song("Song1",authors,75, Genre.POP);
        libraryTest.addToSongsLibrary(s);
        assertEquals("["+s.toString()+"]",libraryTest.getStoredSongs().toString());
    }

    @Test
    void getStoredAlbums() {
        Library libraryTest = new Library();
        Date d1 = new Date(19/02/2001);
        Album a1 = new Album(null,"Album1","Test",d1);
        libraryTest.addToAlbumsLibrary(a1);
        assertEquals("["+a1.toString()+"]",libraryTest.getStoredAlbums().toString());
    }

    @Test
    void getStoredAudioBooks() {
        Library libraryTest = new Library();
        AudioBook ab = new AudioBook("AudioBookTest","Test",75, Language.ENGLISH, Category.NOVEL);
        libraryTest.addToAudioBooksLibrary(ab);
        assertEquals("["+ab.toString()+"]",libraryTest.getStoredAudioBooks().toString());
    }

    @Test
    void getStoredPlaylists() {
        Library libraryTest = new Library();
        Playlist p = new Playlist("PlayTest",null);
        libraryTest.addToPlaylistsLibrary(p);
        assertEquals("["+p.toString()+"]",libraryTest.getStoredPlaylists().toString());
    }

    @Test
    void getStoredSongs() {
        Library libraryTest = new Library();
        String[] authors = new String[]{"author1"};
        Song s = new Song("Song1",authors,75, Genre.POP);
        libraryTest.addToSongsLibrary(s);
        assertEquals("["+s.toString()+"]",libraryTest.getStoredSongs().toString());
    }
}