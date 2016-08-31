package com.example.dm.myapplication.com;

import com.litao.android.lib.entity.PhotoEntry;

import java.util.List;

/**
 * EventEntry
 * Created by dm on 16-8-30.
 */
public class EventEntry {
    public static final int RECEIVED_PHOTOS_ID = 0x00000010;
    public static final int SELECTED_PHOTOS_ID = 0x00000020;

    public List<PhotoEntry> photos;
    public int id;

    public EventEntry(List<PhotoEntry> photos, int id) {
        this.photos = photos;
        this.id = id;
    }
}
