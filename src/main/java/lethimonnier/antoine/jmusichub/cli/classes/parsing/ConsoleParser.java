package lethimonnier.antoine.jmusichub.cli.classes.parsing;

import lethimonnier.antoine.jmusichub.cli.Library;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.interfaces.Parser;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleParser implements Parser {

	Logger log = Logger.getGlobal();

	/**
	 * Returns a <code>Song</code> from a <code>String</code> containing its title.
	 *
	 * @param match   the <code>String</code> to parse
	 * @param library the <code>Library</code> to get data from
	 * @return the matched <code>Song</code>, or <code>null</code> if not found
	 */
	@Override
	public Song getSongFromString(String match, Library library) {
		Song toReturn = null;
		List<Song> librarySongs = library.getStoredSongs();
		for (Song s : librarySongs) {
			if (s.getTitle().toLowerCase().contains(match.toLowerCase())) {
				toReturn = s;
				log.log(Level.INFO, "Match found: {0}", s.getTitle());
				break;
			}
			if (librarySongs.indexOf(s) == librarySongs.size() - 1) {
				log.warning("No match found. Please try again.");
			}
		}
		return toReturn;
	}

	/**
	 * Returns an <code>Playlist</code> from a <code>String</code> containing its title.
	 *
	 * @param match   the <code>String</code> to parse
	 * @param library the <code>Library</code> to get data from
	 * @return the matched <code>Playlist</code>, or <code>null</code> if not found
	 */
	@Override
	public Playlist getPlaylistFromString(String match, Library library) {
		Playlist toReturn = null;
		List<Playlist> libraryPlaylists = library.getStoredPlaylists();
		for (Playlist a : libraryPlaylists) {
			if (a.getName().toLowerCase().contains(match.toLowerCase())) {
				toReturn = a;
				log.log(Level.INFO, "Playlist match found: {0}", a.getName());
				break;
			}
			if (libraryPlaylists.indexOf(a) == libraryPlaylists.size() - 1) {
				log.warning("No playlist match found. Please try again.");
			}
		}
		return toReturn;
	}

	/**
	 * Returns an <code>Album</code> from a <code>String</code> containing its title.
	 *
	 * @param match   the <code>String</code> to parse
	 * @param library the <code>Library</code> to get data from
	 * @return the matched <code>Album</code>, or <code>null</code> if not found
	 */
	@Override
	public Album getAlbumFromString(String match, Library library) {
		Album toReturn = null;
		List<Album> libraryAlbums = library.getStoredAlbums();
		for (Album a : libraryAlbums) {
			if (a.getTitle().toLowerCase().contains(match.toLowerCase())) {
				toReturn = a;
				log.log(Level.INFO, "Album match found: {0}", a.getTitle() + " - " + a.getAuthor());
				break;
			}
			if (libraryAlbums.indexOf(a) == libraryAlbums.size() - 1) {
				log.warning("No album match found. Please try again.");
			}
		}
		return toReturn;
	}

	/**
	 * Returns an <code>AudioBook</code> from a <code>String</code> containing its title.
	 *
	 * @param match   the <code>String</code> to parse
	 * @param library the <code>Library</code> to get data from
	 * @return the matched <code>AudioBook</code>, or <code>null</code> if not found
	 */
	@Override
	public AudioBook getAudioBookFromString(String match, Library library) {
		AudioBook toReturn = null;
		List<AudioBook> libraryAudioBooks = library.getStoredAudioBooks();
		for (AudioBook ab : libraryAudioBooks) {
			if (ab.getTitle().toLowerCase().contains(match.toLowerCase())) {
				toReturn = ab;
				log.log(Level.INFO, "Audiobook match found: {0}", ab.getTitle());
				break;
			}
			if (libraryAudioBooks.indexOf(ab) == libraryAudioBooks.size() - 1) {
				log.warning("No audiobook match found. Please try again.");
			}
		}
		return toReturn;
	}
}
