package lethimonnier.antoine.jmusichub.cli.enums;

/**
 * Category
 */
public enum Category {

    YOUTH("Youth"), NOVEL("Novel"), THEATER("Theater"), SPEECH("Speech"), DOCUMENTARY("Documentary");

    private String name;

    private Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
