package lethimonnier.antoine.jmusichub.cli.interfaces;

import lethimonnier.antoine.jmusichub.cli.Library;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;

/**
 * The interface Parser.
 */
public interface Parser {

	/**
	 * Gets song from string.
	 *
	 * @param match   the match
	 * @param library the library
	 * @return the song from string
	 */
	Song getSongFromString(String match, Library library);

	/**
	 * Gets playlist from string.
	 *
	 * @param match   the match
	 * @param library the library
	 * @return the playlist from string
	 */
	Playlist getPlaylistFromString(String match, Library library);

	/**
	 * Gets album from string.
	 *
	 * @param match   the match
	 * @param library the library
	 * @return the album from string
	 */
	Album getAlbumFromString(String match, Library library);

	/**
	 * Gets audio book from string.
	 *
	 * @param match   the match
	 * @param library the library
	 * @return the audio book from string
	 */
	AudioBook getAudioBookFromString(String match, Library library);
}
