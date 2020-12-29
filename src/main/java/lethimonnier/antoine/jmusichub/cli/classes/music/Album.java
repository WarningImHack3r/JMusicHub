package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.MusicHub;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * Album
 */
public class Album implements Serializable {

	private static final long serialVersionUID = -6929217727508986261L;
	public final String title;
	public final String author;
	public final Date releaseDate;
	private final UUID id;
	public transient Song[] songs;
	private Genre[] genres;
	private long totalDuration = 0;

	/**
	 * Instantiates a new Album.
	 *
	 * @param songs       the songs
	 * @param title       the title
	 * @param author      the author
	 * @param releaseDate the release date
	 */
	public Album(Song[] songs, String title, String author, Date releaseDate) {
		setSongs(songs);
		this.title = title.trim();
		this.author = author.trim();
		this.releaseDate = releaseDate;
		id = UUID.randomUUID();
	}

	/**
	 * Sets songs.
	 *
	 * @param songs the songs
	 */
	public void setSongs(Song[] songs) {
		if (songs == null)
			return;
		if (this.songs != null)
			totalDuration = 0;
		this.songs = songs;
		ArrayList<Genre> genresTmp = new ArrayList<>();
		for (Song song : songs) {
			if (song == null)
				continue;
			totalDuration += song.getDuration();
			if (!genresTmp.contains(song.getGenre()))
				genresTmp.add(song.getGenre());
		}
		genres = genresTmp.toArray(new Genre[0]);
	}

	/**
	 * Add song.
	 *
	 * @param song the song
	 */
	public void addSong(Song song) {
		Song[] newSongs = songs != null ? Arrays.copyOf(songs, songs.length + 1) : new Song[1];
		newSongs[newSongs.length - 1] = song;
		setSongs(newSongs);
	}

	/**
	 * Get genres genre [ ].
	 *
	 * @return the genre [ ]
	 */
	public Genre[] getGenres() {
		return genres;
	}

	/**
	 * Gets title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets author.
	 *
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Gets release date.
	 *
	 * @return the release date
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Gets total duration.
	 *
	 * @return the total duration
	 */
	public long getTotalDuration() {
		return totalDuration;
	}

	@Override
	public String toString() {
		StringBuilder printableSongs = new StringBuilder();
		if (songs != null) {
			for (Song song : songs) {
				printableSongs.append(";").append(song.toString().replace(";", "-"));
			}
		}
		return title + ";" + author + ";" + new SimpleDateFormat(MusicHub.DATE_FORMAT).format(releaseDate) + printableSongs;
	}
}
