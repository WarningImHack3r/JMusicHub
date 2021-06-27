package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {

    @Test
    void setName() {
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song a1 = new Song("SongTest",authors,75, Genre.RAP);
        Song a2 = new Song("SongTest",authors,75, Genre.RAP);
        List<AudioContent> audio = new ArrayList<AudioContent>();
        audio.add(a1);
        audio.add(a2);
        Playlist playlist = new Playlist("SongTest",audio);
        playlist.setName("Test");
        assertEquals("Test", playlist.getName());
    }

    @Test
    void getContent() {
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song a1 = new Song("SongTest",authors,75, Genre.RAP);
        Song a2 = new Song("SongTest",authors,75, Genre.RAP);
        List<AudioContent> audio = new ArrayList<AudioContent>();
        audio.add(a1);
        audio.add(a2);
        Playlist playlist = new Playlist("SongTest",audio);
        assertEquals(audio, playlist.getContent());
    }

    @Test
    void getName() {
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song a1 = new Song("SongTest",authors,75, Genre.RAP);
        Song a2 = new Song("SongTest",authors,75, Genre.RAP);
        List<AudioContent> audio = new ArrayList<AudioContent>();
        audio.add(a1);
        audio.add(a2);
        Playlist playlist = new Playlist("SongTest",audio);
        assertEquals("SongTest", playlist.getName());
    }

    @Test
    void getTotalDuration() {
        String[] authors = new String[] {"AuthorTest1","AuthorTest2"};
        Song a1 = new Song("SongTest",authors,75, Genre.RAP);
        Song a2 = new Song("SongTest",authors,75, Genre.RAP);
        List<AudioContent> audio = new ArrayList<AudioContent>();
        audio.add(a1);
        audio.add(a2);
        Playlist playlist = new Playlist("SongTest",audio);
        assertEquals(150, playlist.getTotalDuration());
    }

}