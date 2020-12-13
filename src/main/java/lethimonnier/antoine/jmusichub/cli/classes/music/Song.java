package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

/**
 * Song
 */
public class Song extends AudioContent {

    private final Genre genre;

    public Song(String title, String[] authors, int duration, Genre genre) {
        super(title, authors, duration);
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return getTitle() + ";" + String.join("/", getAuthors()) + ";" + getDuration() + ";" + genre.getName();
    }
}
