package lethimonnier.antoine.jmusichub.cli.interfaces;

import lethimonnier.antoine.jmusichub.cli.Library;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;

public interface Parser {

    /**
     * Returns a <code>Song</code> from a <code>String</code> containing its title.
     * 
     * @param input   the <code>String</code> to parse
     * @param library the <code>Library</code> to get data from
     * @return the matched <code>Song</code>
     */
    public Song getSongFromString(String match, Library library);

    /**
     * Returns an <code>Playlist</code> from a <code>String</code> containing its
     * title.
     * 
     * @param input   the <code>String</code> to parse
     * @param library the <code>Library</code> to get data from
     * @return the matched <code>Playlist</code>, or <code>null</code> if not found
     */
    public Playlist getPlaylistFromString(String match, Library library);

    /**
     * Returns an <code>Album</code> from a <code>String</code> containing its
     * title.
     * 
     * @param input   the <code>String</code> to parse
     * @param library the <code>Library</code> to get data from
     * @return the matched <code>Album</code>, or <code>null</code> if not found
     */
    public Album getAlbumFromString(String match, Library library);

    /**
     * Returns an <code>AudioBook</code> from a <code>String</code> containing its
     * title.
     * 
     * @param input   the <code>String</code> to parse
     * @param library the <code>Library</code> to get data from
     * @return the matched <code>AudioBook</code>, or <code>null</code> if not found
     */
    public AudioBook getAudioBookFromString(String match, Library library);
}
