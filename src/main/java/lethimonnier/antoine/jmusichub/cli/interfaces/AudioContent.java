package lethimonnier.antoine.jmusichub.cli.interfaces;

import java.io.Serializable;
import java.util.UUID;

/**
 * AudioContent
 */
public abstract class AudioContent implements Serializable {

    private String title;
    private String[] authors;
    private int duration;
    private final UUID id;

    protected AudioContent(String title, String[] authors, int duration) {
        setTitle(title.trim());
        for (int i = 0; i < authors.length; i++) {
            authors[i] = authors[i].trim();
        }
        setAuthors(authors);
        setDuration(duration);
        id = UUID.randomUUID();
    }

    public static String getFormattedDuration(long duration) {
        if (duration < 60)
            return duration + "s";
        if (duration < 3600)
            return (duration / 60) + "m " + (duration % 60) + "s";
        if (duration < 24 * 3600)
            return (duration / 3600) + "h " + ((duration % 3600) / 60) + "m " + ((duration % 3600) % 60) + "s";
        return (duration / 86400) + "d " + ((duration % 86400) / 3600) + "h " + (((duration % 86400) % 3600) / 60) +
		               "m " + (((duration % 86400) % 3600) % 60) + "s";
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String[] getAuthors() {
        return authors;
    }

    public int getDuration() {
        return duration;
    }

    public UUID getId() {
        return id;
    }

    public void setAuthors(String[] authors) {
        for (int i = 0; i < authors.length; i++) {
            authors[i] = authors[i].trim();
        }
        this.authors = authors;
    }
}
