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

	private final String title;
	private final String author;
	private final Date releaseDate;
	private final UUID id;
	private transient Song[] songs;
	private Genre[] genres;
	private long totalDuration = 0;

	public Album(Song[] songs, String title, String author, Date releaseDate) {
		setSongs(songs);
		this.title = title.trim();
		this.author = author.trim();
		this.releaseDate = releaseDate;
		id = UUID.randomUUID();
	}

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

	public void addSong(Song song) {
		Song[] newSongs = Arrays.copyOf(songs, songs.length + 1);
		newSongs[newSongs.length - 1] = song;
		setSongs(newSongs);
	}

	public Genre[] getGenres() {
		return genres;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public UUID getId() {
		return id;
	}

	public long getTotalDuration() {
		return totalDuration;
	}

	@Override
	public String toString() {
		StringBuilder printableSongs = new StringBuilder();
		for (Song song : songs) {
			printableSongs.append(";").append(song.toString());
		}
		return title + ";" + author + ";" + new SimpleDateFormat(MusicHub.dateFormat).format(releaseDate) + printableSongs;
	}
}
