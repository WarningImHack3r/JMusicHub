package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    @Test
    void getGenre() {
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song song = new Song("SongTest",authors,75, Genre.RAP);
        assertEquals(Genre.RAP, song.getGenre());
    }
    @Test
    void getTitle() {
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song song = new Song("SongTest",authors,75, Genre.RAP);
        assertEquals("SongTest", song.getTitle());
    }
    @Test
    void getAuthors() {
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song song = new Song("SongTest",authors,75, Genre.RAP);
        assertEquals(authors, song.getAuthors());
    }
    @Test
    void getDuration() {
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song song = new Song("SongTest",authors,75, Genre.RAP);
        assertEquals(75, song.getDuration());
    }
}