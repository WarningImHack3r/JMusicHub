package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioBookTest {

    @Test
    void getAuthor() {
        AudioBook ab = new AudioBook("AudioBookTest","authorTest",75, Language.ITALIAN, Category.YOUTH);
        assertEquals("authorTest", ab.getAuthor());
    }
    @Test
    void getTitle() {
        AudioBook ab = new AudioBook("AudioBookTest","authorTest",75, Language.ITALIAN, Category.YOUTH);
        assertEquals("AudioBookTest", ab.getTitle());
    }
    @Test
    void getDuration() {
        AudioBook ab = new AudioBook("AudioBookTest","authorTest",75, Language.ITALIAN, Category.YOUTH);
        assertEquals(75, ab.getDuration());
    }
}