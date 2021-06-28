package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AlbumTest {

    @Test
    void getSongs() {
        Date d = new Date(16/2/2017);
        String[] authors = new String[] {"AuthorTest1", "AuthorTest2"};
        Song s1 = new Song("SongTest", authors,75, Genre.RAP);
        Song s2 = new Song("SongTest2", authors,2, Genre.CLASSICAL);
        Song[] song = new Song[] {s1, s2};
        Album album = new Album("AlbumTest","authorTest", d, song);
        assertEquals(song, album.getSongs());
    }

    @Test
    void getGenres() {
        Date d = new Date(16/2/2017);
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song s1 = new Song("SongTest",authors,75, Genre.RAP);
        Song s2 = new Song("SongTest2",authors,2, Genre.CLASSICAL);
        Song[] song = new Song[] {s1,s2};
        Album album = new Album("AlbumTest","authorTest", d, song);
        Genre[] g = album.getGenres();
        assertEquals(g, album.getGenres());
    }

    @Test
    void getTitle() {
        Date d = new Date(16/2/2017);
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song s1 = new Song("SongTest",authors,75, Genre.RAP);
        Song s2 = new Song("SongTest2",authors,2, Genre.CLASSICAL);
        Song[] song = new Song[] {s1, s2};
        Album album = new Album("AlbumTest","authorTest", d, song);
        assertEquals("AlbumTest", album.getTitle());
    }

    @Test
    void getAuthor() {
        Date d = new Date(16/2/2017);
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song s1 = new Song("SongTest",authors,75, Genre.RAP);
        Song s2 = new Song("SongTest2",authors,2, Genre.CLASSICAL);
        Song[] song = new Song[] {s1, s2};
        Album album = new Album("AlbumTest","authorTest", d, song);
        assertEquals("authorTest", album.getAuthor());
    }

    @Test
    void getReleaseDate() {
        Date d = new Date(16/2/2017);
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song s1 = new Song("SongTest",authors,75, Genre.RAP);
        Song s2 = new Song("SongTest2",authors,2, Genre.CLASSICAL);
        Song[] song = new Song[] {s1, s2};
        Album album = new Album("AlbumTest","authorTest", d, song);
        assertEquals(d, album.getReleaseDate());
    }

    @Test
    void getTotalDuration() {
        Date d = new Date(16/2/2017);
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song s1 = new Song("SongTest",authors,75, Genre.RAP);
        Song s2 = new Song("SongTest2",authors,2, Genre.CLASSICAL);
        Song[] song = new Song[] {s1,s2};
        Album album = new Album("AlbumTest","authorTest", d, song);
        assertEquals(77, album.getTotalDuration());
    }
}
