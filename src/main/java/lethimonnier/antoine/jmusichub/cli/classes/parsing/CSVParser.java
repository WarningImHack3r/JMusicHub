package lethimonnier.antoine.jmusichub.cli.classes.parsing;

import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.interfaces.Parser;

public class CSVParser implements Parser {

    @Override
    public Song getSongFromStringMatch(String match) {
        return null;
    }

    @Override
    public Playlist getPlaylistFromStringMatch(String match) {
        return null;
    }

    @Override
    public Album getAlbumFromStringMatch(String match) {
        return null;
    }

    @Override
    public AudioBook getAudioBookFromStringMatch(String match) {
        return null;
    }
}
