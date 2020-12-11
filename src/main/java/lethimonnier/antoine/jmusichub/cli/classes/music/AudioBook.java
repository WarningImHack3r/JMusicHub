package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

/**
 * AudioBook
 */
public class AudioBook extends AudioContent {

    private Language language;
    private Category category;

    public AudioBook(AudioContent content, Language language, Category category) {
        this(content.getTitle(), content.getAuthors()[0], content.getDuration(), language, category);
    }

    public AudioBook(String title, String author, int duration, Language language, Category category) {
        super(title, new String[] { author }, duration);
        this.language = language;
        this.category = category;
    }

    public String getAuthor() {
        return getAuthors()[0];
    }

    public Language getLanguage() {
        return language;
    }

    public Category getCategory() {
        return category;
    }
}