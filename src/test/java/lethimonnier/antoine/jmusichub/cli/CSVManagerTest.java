package lethimonnier.antoine.jmusichub.cli;

import com.opencsv.exceptions.CsvValidationException;
import lethimonnier.antoine.jmusichub.cli.classes.music.AudioBook;
import lethimonnier.antoine.jmusichub.cli.classes.music.Playlist;
import lethimonnier.antoine.jmusichub.cli.classes.music.Song;
import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Genre;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CSVManagerTest {

    @Test
    void importSavedContentFromFile() throws CsvValidationException, IOException {
        var csv = new CSVManager();
        Library libraryTest = new Library();
        Library library = new Library();
        Playlist p1 = new Playlist("Jpop",null);
        String[] authors = new String[] {"Lisa"};
        Song a1 = new Song("Guren",authors,90, Genre.POP);
        AudioBook ab1 = new AudioBook("abTitle","abAuthor",246, Language.ENGLISH, Category.NOVEL);
        library.addToSongsLibrary(a1);
        library.addToPlaylistsLibrary(p1);
        library.addToAudioBooksLibrary(ab1);
        File file = new File("io/Test/TestCSV.csv");
        csv.importSavedContentFromFile(file,libraryTest);
        assertEquals(library.getStoredAlbums().toString(),libraryTest.getStoredAlbums().toString());
        assertEquals(library.getStoredSongs().toString(),libraryTest.getStoredSongs().toString());
        assertEquals(library.getStoredPlaylists().toString(),libraryTest.getStoredPlaylists().toString());
        assertEquals(library.getStoredAudioBooks().toString(),libraryTest.getStoredAudioBooks().toString());
    }
}