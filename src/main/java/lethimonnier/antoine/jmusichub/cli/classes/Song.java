package lethimonnier.antoine.jmusichub.cli.classes;

import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

/**
 * Song
 */
public class Song extends AudioContent {

    private Genre genre;

    public Song(AudioContent content, Genre genre) {
        this(content.getTitle(), content.getAuthors(), content.getDuration(), genre);
    }

    public Song(String title, String author, int duration, Genre genre) {
        this(title, new String[] { author }, duration, genre);
    }

    public Song(String title, String[] authors, int duration, Genre genre) {
        super(title, authors, duration);
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }
}