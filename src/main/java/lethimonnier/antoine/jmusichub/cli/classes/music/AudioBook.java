package lethimonnier.antoine.jmusichub.cli.classes.music;

import lethimonnier.antoine.jmusichub.cli.enums.Category;
import lethimonnier.antoine.jmusichub.cli.enums.Language;
import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

/**
 * AudioBook
 */
public class AudioBook extends AudioContent {

	private static final long serialVersionUID = 4664945769348239740L;
	public final Language language;
	public final Category category;

	/**
	 * Instantiates a new Audio book.
	 *
	 * @param title    the title
	 * @param author   the author
	 * @param duration the duration
	 * @param language the language
	 * @param category the category
	 */
	public AudioBook(String title, String author, int duration, Language language, Category category) {
		super(title, new String[] { author }, duration);
		this.language = language;
		this.category = category;
	}

	/**
	 * Gets author.
	 *
	 * @return the author
	 */
	public String getAuthor() {
		return getAuthors()[0];
	}

	@Override
	public String toString() {
		return getTitle() + ";" + getAuthor() + ";" + getDuration() + ";" + language.getName() + ";" + category.getName();
	}
}
