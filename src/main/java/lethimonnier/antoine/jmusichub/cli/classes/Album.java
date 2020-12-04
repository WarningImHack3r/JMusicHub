package lethimonnier.antoine.jmusichub.cli.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import lethimonnier.antoine.jmusichub.cli.enums.Genre;

/**
 * Album
 */
public class Album {

    private Song[] songs;
    private Genre[] genres;
    private String title;
    private String author;
    private long totalDuration = 0;
    private Date releaseDate;
    private UUID id;

    public Album(Song[] songs, String title, String author, Date releaseDate) {
        setSongs(songs);
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        id = UUID.randomUUID();
    }
    
    public void setSongs(Song[] songs) {
        if (songs == null) return;
        if (this.songs != null) totalDuration = 0;
        this.songs = songs;
        ArrayList<Genre> genresTmp = new ArrayList<>();
        for (Song song : songs) {
            if (song == null) continue;
            totalDuration += song.getDuration();
            if (!genresTmp.contains(song.getGenre())) genresTmp.add(song.getGenre());
        }
        genres = genresTmp.toArray(new Genre[0]);
    }

    public void addSong(Song song) {
        Song[] newSongs = Arrays.copyOf(songs, songs.length + 1);
        newSongs[newSongs.length - 1] = song;
        setSongs(newSongs);
    }

    public Song[] getSongs() {
        return songs;
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

    public long getTotalDuration() {
        return totalDuration;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder printableSongs = new StringBuilder();
        int i = 0;
        for (Song song : songs) {
            printableSongs.append(++i + " - " + song.getTitle() + "\n");
        }
        return "Album: " + title + " from " + author + "\n" + printableSongs;
    }
}