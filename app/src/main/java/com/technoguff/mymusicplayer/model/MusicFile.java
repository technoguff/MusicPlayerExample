package com.technoguff.mymusicplayer.model;

/**
 * Created by darshanz on 8/21/15.
 */
public class MusicFile {
    private int id;
    private String title;
    private String artist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
