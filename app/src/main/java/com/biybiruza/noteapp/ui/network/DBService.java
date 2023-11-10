package com.biybiruza.noteapp.ui.network;

import com.biybiruza.noteapp.data.NoteModels;

import java.util.List;

public interface DBService {

    public void addNotes(NoteModels note);
    public List<NoteModels> getNotes();
    public void editNotes(NoteModels note);
    public void deleteNotes(int id);
}
