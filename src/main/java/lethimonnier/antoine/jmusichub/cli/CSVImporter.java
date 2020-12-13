package lethimonnier.antoine.jmusichub.cli;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import lethimonnier.antoine.jmusichub.cli.classes.music.Album;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.classes.parsing.CSVParser;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class CSVImporter {

	private final Logger log = Logger.getGlobal();
	private final CSVParser csv = new CSVParser();

	/**
	 * Prompts a <code>JFileChooser</code> to choose a .csv file to import.
	 *
	 * @return the chosen <code>File</code>
	 * @throws IOException if an error occurs
	 */
	public File openFileFromChooser() throws IOException {
		// Import file and read it
		log.info("Please choose your input .csv file.");
		JFileChooser dialog = new JFileChooser();
		dialog.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return !f.isDirectory() && f.getName().toLowerCase().endsWith(".csv");
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
	 * @param file the csv file to import
	 * @return the number of inputs added into the <code>Library</code>
	 * @throws IOException            if an input error occurs
	 * @throws CsvValidationException if the file cannot be parsed
	 */
	public int importSavedContentFromFile(File file, Library library) throws IOException, CsvValidationException {
		// Instantiate Albums, Playlists, AudioBooks and Songs
		int state; // 0 = idle, 1 = alb, 2 = playl, 3 = ab, 4 = song
		int importsCount = 0;

		try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
			String[] line;
			while ((line = csvReader.readNext()) != null) {
				String firstCell = line[0];
				state = switch (firstCell) {
					case "ALBUMS" -> 1;
					case "PLAYLISTS" -> 2;
					case "AUDIOBOOKS" -> 3;
					case "SONGS" -> 4;
					default -> 0;
				};
				if (state == 0)
					continue;
				switch (state) {
					case 1: // Album
						Album album = csv.getAlbumFromString(String.join(";", line), null);
						if (album == null)
							break;
						library.addToAlbumsLibrary(album);
						importsCount++;
						break;

					case 2: // Playlist
						Playlist playlist = csv.getPlaylistFromString(String.join(";", line), null);
						if (playlist == null)
							break;
						library.addToPlaylistsLibrary(playlist);
						importsCount++;
						break;

					case 3: // AudioBook
						AudioBook audioBook = csv.getAudioBookFromString(String.join(";", line), null);
						if (audioBook == null)
							break;
						library.addToAudioBooksLibrary(audioBook);
						importsCount++;
						break;

					case 4: // Song
						Song song = csv.getSongFromString(String.join(";", line), null);
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
	 */
	public void saveLibaryToFile() throws IOException {
		log.info("Please choose your output .csv file.");
		JFileChooser dialog = new JFileChooser();
		dialog.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return !f.isDirectory() && f.getName().toLowerCase().endsWith(".csv");
			}

			@Override
			public String getDescription() {
				return "CSV file (*.csv)";
			}
		});
		File f;
		switch (dialog.showOpenDialog(null)) {
			case JFileChooser.APPROVE_OPTION:
				f = dialog.getSelectedFile();
				break;

			case JFileChooser.ERROR_OPTION:
				throw new IOException("An error occurred selecting file.");

			case JFileChooser.CANCEL_OPTION:
			default:
				return;
		}

		CSVWriter writer = new CSVWriter(new FileWriter(f), ';', '0', '0', null);
		writer.close(); // juste pour enlever le warning
	}
}
