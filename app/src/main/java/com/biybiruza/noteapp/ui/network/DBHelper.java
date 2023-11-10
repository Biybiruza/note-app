package com.biybiruza.noteapp.ui.network;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.biybiruza.noteapp.data.NoteModels;

import java.util.ArrayList;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper implements DBService {

    public static final String DB_NAME = "noteApp";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "notes";
    public static final String NOTE_ID = "id";
    public static final String NOTE_TITLE = "title";
    public static final String NOTE_NOTE = "note";
    public static final String NOTE_DATA = "data";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" + NOTE_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + NOTE_TITLE + " TEXT NOT NULL, " + NOTE_NOTE + " TEXT NOT NULL, " + NOTE_DATA + " TEXT NOT NULL " + ")";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void addNotes(NoteModels note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_TITLE, note.getTitle());
        values.put(NOTE_NOTE, note.getNote());
        values.put(NOTE_DATA, note.getDate());

        db.insert(TABLE_NAME, null, values);
    }


    @Override
    public List<NoteModels> getNotes() {
        List<NoteModels> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String note = cursor.getString(2);
                String date = cursor.getString(3);

                NoteModels models = new NoteModels(id, title, note, date);
                list.add(models);
            } while (cursor.moveToNext());
        }
        return list;
    }

    @Override
    public void deleteNotes(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "DELETE FROM " + TABLE_NAME+" WHERE "+NOTE_ID+"="+id;
        db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void editNotes(NoteModels noteModels) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_TITLE, noteModels.getTitle());
        values.put(NOTE_NOTE, noteModels.getNote());
        values.put(NOTE_DATA, noteModels.getDate());

        db.update(TABLE_NAME, values, NOTE_ID + "=?", new String[]{String.valueOf(noteModels.getId())});
    }
}
