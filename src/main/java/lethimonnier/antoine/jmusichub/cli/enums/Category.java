package lethimonnier.antoine.jmusichub.cli.enums;

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
	public String getName() {
        return name;
    }

	/**
	 * Get string values string [ ].
	 *
	 * @return the string [ ]
	 */
	public static String[] getStringValues() {
        String[] vals = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            vals[i] = values()[i].getName();
        }
        return vals;
    }
}
