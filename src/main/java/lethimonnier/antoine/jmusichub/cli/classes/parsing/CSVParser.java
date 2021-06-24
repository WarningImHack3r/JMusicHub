package lethimonnier.antoine.jmusichub.cli.classes.parsing;

import lethimonnier.antoine.jmusichub.cli.Library;
import lethimonnier.antoine.jmusichub.cli.MusicHub;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;
import lethimonnier.antoine.jmusichub.cli.interfaces.Parser;
import lethimonnier.antoine.jmusichub.cli.logging.MusicLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The type Csv parser.
 */
public class CSVParser implements Parser {

	/**
	 * The Log.
	 */
	Logger log = MusicLogger.getLogger("output.log");

	/**
	 * Checks if an <code>String</code> is a <code>Song</code> or an <code>AudioBook</code>. Use only for this class.
	 *
	 * @param audioContent the <code>String</code> to check
	 * @return true if <code>audiocontent</code> is a <code>Song</code>, false if it is an <code>AudioBook</code>
	 */
	private boolean isSong(String audioContent) throws IOException {
		String[] splitted = audioContent.contains("-") ? audioContent.split("-") : audioContent.split(";");
		if (splitted.length < 4)
			throw new IOException("Error parsing audiocontent");
		for (Genre genre : Genre.values()) {
			if (genre.getName().toLowerCase().contains(splitted[3].toLowerCase().trim())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a <code>Song</code> from a <code>String</code> from a csv file.
	 *
	 * @param str     the <code>String</code> to parse
	 * @param library the <code>Library</code> to get data from
	 * @return the created <code>Song</code>, or <code>null</code> if an error occurred
	 */
	@Override
	public Song getSongFromString(String str, Library library) {
		str = str.replace("\"", "");
		String[] splitted = str.split(";");
		if (splitted.length != 4) {
			log.warning("Incorrect argument length");
			return null;
		}
		int duration;
		try {
			duration = Integer.parseInt(splitted[2].trim());
		} catch (NumberFormatException e) {
			log.warning("Can't fetch duration from input");
			return null;
		}
		Genre genre = null;
		for (Genre g : Genre.values()) {
			if (g.getName().toLowerCase().contains(splitted[3].toLowerCase().trim())) {
				genre = g;
				break;
			}
		}
		if (genre == null) {
			log.warning("No match found for input genre");
			return null;
		}
		return new Song(splitted[0], splitted[1].split("/"), duration, genre);
	}

	/**
	 * Returns an <code>Playlist</code> from a <code>String</code> from a csv file.
	 *
	 * @param str     the <code>String</code> to parse
	 * @param library the <code>Library</code> to get data from
	 * @return the created <code>Playlist</code>, or <code>null</code> if an error occurred
	 */
	@Override
	public Playlist getPlaylistFromString(String str, Library library) {
		String[] splitted = str.split(";");
		List<AudioContent> content = null;
		if (splitted.length > 1) {
			content = new ArrayList<>();
			for (int i = 1; i < splitted.length; i++) {
				boolean isASong;
				try {
					isASong = isSong(splitted[i]);
				} catch (IOException e) {
					log.warning("Unparsable content found, skipped");
					continue;
				}
				if (isASong) {
					content.add(getSongFromString(splitted[i].replace("-", ";"), library));
				} else {
					content.add(getAudioBookFromString(splitted[i].replace("-", ";"), library));
				}
			}
		}
		return new Playlist(splitted[0], content);
	}

	/**
	 * Returns an <code>Album</code> from a <code>String</code> from a csv file.
	 *
	 * @param str     the <code>String</code> to parse
	 * @param library the <code>Library</code> to get data from
	 * @return the created <code>Album</code>, or <code>null</code> if an error occurred
	 */
	@Override
	public Album getAlbumFromString(String str, Library library) {
		String[] splitted = str.split(";");
		Song[] songs = null;
		if (splitted.length > 3) {
			songs = new Song[splitted.length - 3];
			for (int i = 0; i < songs.length; i++) {
				songs[i] = getSongFromString(splitted[i + 3].replace("-", ";"), null);
			}
		}
		return new Album(songs, splitted[0], splitted[1], MusicHub.getDateFromString(splitted[2]));
	}

	/**
	 * Returns an <code>AudioBook</code> from a <code>String</code> from a csv file.
	 *
	 * @param str     the <code>String</code> to parse
	 * @param library the <code>Library</code> to get data from
	 * @return the created <code>AudioBook</code>, or <code>null</code> if an error occurred
	 */
	@Override
	public AudioBook getAudioBookFromString(String str, Library library) {
		str = str.replace("\"", "");
		String[] splitted = str.split(";");
		if (splitted.length != 5) {
			log.warning("Incorrect argument length");
			return null;
		}
		int duration;
		try {
			duration = Integer.parseInt(splitted[2].trim());
		} catch (NumberFormatException e) {
			log.warning("Can't fetch duration from input");
			return null;
		}
		Language language = null;
		for (Language l : Language.values()) {
			if (l.getName().toLowerCase().contains(splitted[3].toLowerCase().trim())) {
				language = l;
				break;
			}
		}
		if (language == null) {
			log.warning("No match found for input language");
			return null;
		}
		Category category = null;
		for (Category c : Category.values()) {
			if (c.getName().toLowerCase().contains(splitted[4].toLowerCase().trim())) {
				category = c;
				break;
			}
		}
		if (category == null) {
			log.warning("No match found for input category");
			return null;
		}
		return new AudioBook(splitted[0], splitted[1], duration, language, category);
	}
}
