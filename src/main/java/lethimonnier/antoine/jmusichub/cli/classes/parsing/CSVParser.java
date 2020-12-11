package lethimonnier.antoine.jmusichub.cli.classes.parsing;

import java.util.logging.Logger;

import lethimonnier.antoine.jmusichub.cli.Library;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.interfaces.Parser;

public class CSVParser implements Parser {

    Logger log = Logger.getGlobal();

    @Override
    public Song getSongFromString(String match, Library library) {
        return null;
    }

    @Override
    public Playlist getPlaylistFromString(String match, Library library) {
        return null;
    }

    @Override
    public Album getAlbumFromString(String match, Library library) {
        return null;
    }

    @Override
    public AudioBook getAudioBookFromString(String match, Library library) {
        return null;
    }
}
