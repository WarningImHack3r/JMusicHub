package lethimonnier.antoine.jmusichub.cli.interfaces;

import lethimonnier.antoine.jmusichub.cli.Library;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;

public interface Parser {

    Song getSongFromString(String match, Library library);

    Playlist getPlaylistFromString(String match, Library library);

    Album getAlbumFromString(String match, Library library);

    AudioBook getAudioBookFromString(String match, Library library);
}
