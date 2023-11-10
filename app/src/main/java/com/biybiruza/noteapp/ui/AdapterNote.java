package com.biybiruza.noteapp.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.biybiruza.noteapp.MyApplication;
import com.biybiruza.noteapp.R;
import com.biybiruza.noteapp.data.NoteModels;

import java.io.Serializable;
import java.util.List;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHolderNote> {

    List<NoteModels> list;

    private OnItemClickListener onClickListener;
    private OnEditClickListener onEditClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position,NoteModels models);
    }

    public interface OnEditClickListener {
        void onEditClick(int position, NoteModels models);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position, int id);
    }

    public AdapterNote(List<NoteModels> list, OnItemClickListener onClickListener,
                       OnEditClickListener onEditClickListener,
                       OnDeleteClickListener onDeleteClickListener) {
        this.list = list;
        this.onClickListener = onClickListener;
        this.onEditClickListener = onEditClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public AdapterNote(List<NoteModels> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderNote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderNote(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNote holder, @SuppressLint("RecyclerView") int position) {
        holder.noteTitle.setText(list.get(position).getTitle());
        holder.date.setText(list.get(position).getDate());
        int id = list.get(position).getId();

        //item click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemClick(position,list.get(position));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemClick(position,list.get(position));
            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditClickListener.onEditClick(position, list.get(position));
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClickListener.onDeleteClick(position, id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderNote extends RecyclerView.ViewHolder {

        TextView noteTitle;
        TextView date;
        ImageView edit;
        ImageView delete;

        public ViewHolderNote(View itemview) {
            super(itemview);
            noteTitle = itemview.findViewById(R.id.tv_noteTitle);
            date = itemview.findViewById(R.id.tv_date);
            edit = itemview.findViewById(R.id.iv_editBtn);
            delete = itemview.findViewById(R.id.iv_deleteBtn);
        }
    }
}
