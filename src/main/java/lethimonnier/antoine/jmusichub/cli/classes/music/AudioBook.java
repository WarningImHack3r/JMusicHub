package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

/**
 * AudioBook
 */
public class AudioBook extends AudioContent {

	private final Language language;
	private final Category category;

	public AudioBook(String title, String author, int duration, Language language, Category category) {
		super(title, new String[] { author }, duration);
		this.language = language;
		this.category = category;
	}

	public String getAuthor() {
		return getAuthors()[0];
	}

	@Override
	public String toString() {
		return getTitle() + ";" + getAuthor() + ";" + getDuration() + ";" + language.getName() + ";" + category.getName();
	}
}
