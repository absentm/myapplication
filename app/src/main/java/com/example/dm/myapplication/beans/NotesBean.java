package com.example.dm.myapplication.beans;

/**
 * NotesBean
 * Created by dm on 16-9-23.
 */

public class NotesBean {
    private String noteTitle;
    private String noteContent;
    private String noteTime;
    private long noteId;

    public NotesBean() {
    }

    public NotesBean(String noteTitle, String noteContent, String noteTime, long noteId) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteTime = noteTime;
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }
}
