package lethimonnier.antoine.jmusichub.cli.interfaces;

import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;

public interface Parser {
    
    public Song getSongFromStringMatch(String match);

    public Playlist getPlaylistFromStringMatch(String match);

    public Album getAlbumFromStringMatch(String match);

    public AudioBook getAudioBookFromStringMatch(String match);
}
