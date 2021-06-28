package lethimonnier.antoine.jmusichub.cli.enums;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Category
 */
public enum Category {

	/**
	 * Youth category.
	 */
	YOUTH("Youth"),
	/**
	 * Novel category.
	 */
	NOVEL("Novel"),
	/**
	 * Theater category.
	 */
	THEATER("Theater"),
	/**
	 * Speech category.
	 */
	SPEECH("Speech"),
	/**
	 * Documentary category.
	 */
	DOCUMENTARY("Documentary");

    private final String name;

    Category(String name) {
        this.name = name;
    }

	/**
	 * Gets name.
	 *
	 * @return the name
	 */
	@Contract(pure = true)
	public String getName() {
		return name;
	}

	/**
	 * Get string values string [ ].
	 *
	 * @return the string [ ]
	 */
	@NotNull
	public static String[] getStringValues() {
		var vals = new String[values().length];
		for (var i = 0; i < values().length; i++) {
			vals[i] = values()[i].getName();
		}
		return vals;
    }
}
