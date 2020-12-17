package lethimonnier.antoine.jmusichub.cli.enums;

/**
 * Category
 */
public enum Category {

    YOUTH("Youth"), NOVEL("Novel"), THEATER("Theater"), SPEECH("Speech"), DOCUMENTARY("Documentary");

    private final String name;

    Category(String name) {
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
