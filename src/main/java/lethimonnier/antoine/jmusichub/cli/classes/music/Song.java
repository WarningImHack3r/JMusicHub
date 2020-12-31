package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

import java.io.Serial;

/**
 * Song
 */
public class Song extends AudioContent {

    @Serial
    private static final long serialVersionUID = -2212993214794023637L;
	/**
	 * The Genre.
	 */
	public final Genre genre;

	/**
	 * Instantiates a new Song.
	 *
	 * @param title    the title
	 * @param authors  the authors
	 * @param duration the duration
	 * @param genre    the genre
	 */
	public Song(String title, String[] authors, int duration, Genre genre) {
        super(title, authors, duration);
        this.genre = genre;
    }

	/**
	 * Gets genre.
	 *
	 * @return the genre
	 */
	public Genre getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return getTitle() + ";" + String.join("/", getAuthors()) + ";" + getDuration() + ";" + genre.getName();
    }
}
