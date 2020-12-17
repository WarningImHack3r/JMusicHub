package lethimonnier.antoine.jmusichub.cli.enums;

/**
 * Language
 */
public enum Language {

    FRENCH("French"), ENGLISH("English"), ITALIAN("Italian"), SPANISH("Spanish"), DEUTSCH("Deutsch");

    private final String name;

    Language(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String[] getStringValues() {
        String[] vals = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            vals[i] = values()[i].getName();
        }
        return vals;
    }
}
