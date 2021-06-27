package lethimonnier.antoine.jmusichub.cli;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.classes.parsing.CSVParser;
import lethimonnier.antoine.jmusichub.cli.logging.MusicLogger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The type Csv manager.
 */
public class CSVManager {

	private final Logger log = MusicLogger.getLogger("output.log");
	private final CSVParser csv = new CSVParser();

	/**
	 * Prompts a <code>JFileChooser</code> to choose a .csv file to import.
	 *
	 * @param path the optional path to open file chooser in
	 * @return the chosen <code>File</code>
	 * @throws IOException if an error occurs
	 */
	public File openFileFromChooser(String path) throws IOException {
		// Import file and read it
		log.info("Please choose your input .csv file.");
		JFileChooser dialog;
		if (path == null || path.equals(""))
			dialog = new JFileChooser();
		else
			dialog = new JFileChooser(path);
		dialog.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
			}

			@Override
			public String getDescription() {
				return "CSV file (*.csv)";
			}
		});
		return switch (dialog.showOpenDialog(null)) {
			case JFileChooser.APPROVE_OPTION -> dialog.getSelectedFile();
			case JFileChooser.ERROR_OPTION -> throw new IOException("An error occurred selecting file.");
			default -> null;
		};
	}

	/**
	 * Imports the content of a csv file into the libray. Can parse
	 * <code>Album</code>s, <code>Playlist</code>s, <code>AudioBook</code>s and
	 * <code>Song</code>s.
	 * <hr>
	 * <strong>Parsing rules</strong> (delimited by ',', [] means optional):
	 * <ul>
	 * <li><em>Album:</em> title, author, creation date (dd/MM/yyyy) [, songs]</li>
	 * <li><em>Playlist:</em> title[, audiocontents]</li>
	 * <li><em>Song:</em> title, author(s), duration (in seconds), genre</li>
	 * <li><em>AudioBook:</em> title, author, duration (in seconds), language,
	 * category</li>
	 * </ul>
	 * Please refer to the corresponding classes for more info.
	 * <hr>
	 *
	 * @param file    the csv file to import
	 * @param library the library
	 * @return the number of inputs added into the <code>Library</code>
	 * @throws IOException            if an input error occurs
	 * @throws CsvValidationException if the file cannot be parsed
	 */
	public int importSavedContentFromFile(File file, Library library) throws IOException, CsvValidationException {
		// Instantiate Albums, Playlists, AudioBooks and Songs
		var lineState = 0; // 0 = idle, 1 = alb, 2 = playl, 3 = ab, 4 = song
		int section = lineState;
		var importsCount = 0;

		try (var csvReader = new CSVReader(new FileReader(file))) {
			String[] line;
			while ((line = csvReader.readNext()) != null) {
				line = String.join(",", line).replace("\"", "").split(";");
				if (line.length <= 0)
					continue;
				String firstCell = line[0];
				section = lineState > 0 ? lineState : section;
				lineState = switch (firstCell) {
					case "ALBUMS" -> 1;
					case "PLAYLISTS" -> 2;
					case "AUDIOBOOKS" -> 3;
					case "SONGS" -> 4;
					default -> 0;
				};
				if (lineState > 0)
					continue;
				switch (section) {
					case 1: // Album
						var album = csv.getAlbumFromString(String.join(";", line), null);
						if (album == null)
							break;
						library.addToAlbumsLibrary(album);
						importsCount++;
						break;

					case 2: // Playlist
						var playlist = csv.getPlaylistFromString(String.join(";", line), null);
						if (playlist == null)
							break;
						library.addToPlaylistsLibrary(playlist);
						importsCount++;
						break;

					case 3: // AudioBook
						var audioBook = csv.getAudioBookFromString(String.join(";", line), null);
						if (audioBook == null)
							break;
						library.addToAudioBooksLibrary(audioBook);
						importsCount++;
						break;

					case 4: // Song
						var song = csv.getSongFromString(String.join(";", line), null);
						if (song == null)
							break;
						library.addToSongsLibrary(song);
						importsCount++;
						break;

					default:
						break;
				}
			}
		}

		return importsCount;
	}

	/**
	 * Saves the content of the <code>Library</code> into a csv file.
	 *
	 * @param library the <code>Library</code> to save on the csv file
	 * @param path    the path to open the chooser in
	 * @throws IOException the io exception
	 */
	public void saveLibaryToFile(Library library, String path) throws IOException {
		log.info("Please choose your output .csv file.");
		JFileChooser dialog;
		if (path == null || path.equals(""))
			dialog = new JFileChooser();
		else
			dialog = new JFileChooser(path);
		dialog.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".csv");
			}

			@Override
			public String getDescription() {
				return "CSV file (*.csv)";
			}
		});
		File f;
		switch (dialog.showSaveDialog(null)) {
			case JFileChooser.APPROVE_OPTION:
				f = dialog.getSelectedFile();
				break;

			case JFileChooser.ERROR_OPTION:
				throw new IOException("An error occurred selecting file.");

			case JFileChooser.CANCEL_OPTION:
			default:
				return;
		}
		var st = ";" + System.lineSeparator();
		var writer = new CSVWriter(new FileWriter(f), ';', '"', '\\', st);

		List<String[]> tosave = new ArrayList<>();
		tosave.add(new String[] { "SONGS" });
		for (Song s : library.getStoredSongs()) {
			if (s == null)
				continue;
			tosave.add(s.toString().split(";"));
		}
		tosave.add(new String[] { "PLAYLISTS" });
		for (Playlist p : library.getStoredPlaylists()) {
			if (p == null)
				continue;
			tosave.add(p.toString().split(";"));
		}
		tosave.add(new String[] { "AUDIOBOOKS" });
		for (AudioBook ab : library.getStoredAudioBooks()) {
			if (ab == null)
				continue;
			tosave.add(ab.toString().split(";"));
		}
		tosave.add(new String[] { "ALBUMS" });
		for (Album a : library.getStoredAlbums()) {
			if (a == null)
				continue;
			tosave.add(a.toString().split(";"));
		}
		for (String[] strings : tosave) {
			writer.writeNext(strings);
		}
		writer.close();
	}
}
