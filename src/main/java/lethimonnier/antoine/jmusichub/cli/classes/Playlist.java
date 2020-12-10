package lethimonnier.antoine.jmusichub.cli.classes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lethimonnier.antoine.jmusichub.cli.interfaces.AudioContent;

/**
 * Playlist
 */
public class Playlist {

    private ArrayList<AudioContent> content;
    private String name;
    private long totalDuration = 0;
    private Date creationDate;
    private Date lastModifiedDate;
    private UUID id;

    public Playlist(String name, List<AudioContent> content) {
        this.content = new ArrayList<>(content);
        this.name = name;
        for (AudioContent audioContent : this.content) {
            totalDuration += audioContent.getDuration();
        }
        creationDate = new Date();
        lastModifiedDate = creationDate;
        id = UUID.randomUUID();
    }

    public void setContent(List<AudioContent> content) {
        if (content == null)
            return;
        this.content = new ArrayList<>(content);
        totalDuration = 0;
        for (AudioContent audioContent : this.content) {
            if (audioContent != null)
                totalDuration += audioContent.getDuration();
        }
        lastModifiedDate = new Date();
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AudioContent> getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder printableContent = new StringBuilder();
        int i = 0;
        for (AudioContent audioContent : content) {
            printableContent.append(++i + " - " + audioContent.getTitle());
        }
        return "Playlist: " + name + "\n" + printableContent;
    }
}