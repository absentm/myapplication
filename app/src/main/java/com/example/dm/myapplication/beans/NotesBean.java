package com.example.dm.myapplication.beans;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * NotesBean
 * Created by dm on 16-9-23.
 */

public class NotesBean extends BmobObject implements Serializable {
    private String userNameStr;   // 用户名

    private String noteTitle;
    private String noteContent;
    private String noteTime;
    private long noteId;


    public NotesBean() {
    }

    public NotesBean(String noteTitle, String noteContent, String noteTime) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteTime = noteTime;
    }

    public NotesBean(String noteTitle, String noteContent, String noteTime, long noteId) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteTime = noteTime;
        this.noteId = noteId;
    }

    public NotesBean(String userNameStr, String noteTitle, String noteContent, String noteTime, long noteId) {
        this.userNameStr = userNameStr;
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

    public String getUserNameStr() {
        return userNameStr;
    }

    public void setUserNameStr(String userNameStr) {
        this.userNameStr = userNameStr;
    }
}
