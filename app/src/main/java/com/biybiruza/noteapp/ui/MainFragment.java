package com.biybiruza.noteapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.biybiruza.noteapp.MyShared;
import com.biybiruza.noteapp.R;
import com.biybiruza.noteapp.data.NoteModels;
import com.biybiruza.noteapp.databinding.FragmentMainBinding;
import com.biybiruza.noteapp.ui.network.DBHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    FragmentMainBinding binding;
    AdapterNote adapterNote;
    MyShared myShared;
    List<NoteModels> list = new ArrayList<>();
    DBHelper dbHelper = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(requireActivity());
        myShared = new MyShared(requireContext(),new Gson());

        Log.d("TAG", "onViewCreated: ");
        loadData();

        binding.fbAddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("type","add");
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, AddNoteFragment.class, bundle)
                        .addToBackStack("name")
                        .commit();
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchFunction(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void searchFunction(CharSequence charSequence) {
        List<NoteModels> searchList = new ArrayList<>();

        if (charSequence.length() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTitle().toUpperCase().contains(charSequence.toString().toUpperCase())) {
                    searchList.add(new NoteModels(
                            list.get(i).getTitle(),
                            list.get(i).getNote(),
                            list.get(i).getDate()
                    ));
                }
            }
            if (searchList.size() > 0) {
                AdapterNote adapter = new AdapterNote(searchList);
                binding.rvContact.setAdapter(adapter);
            }
        } else {
            binding.rvContact.setAdapter(adapterNote);
        }

    }

    private void loadData() {
        list.clear();

        if (dbHelper.getNotes() != null) {
             list.addAll(dbHelper.getNotes());

            binding.tvTextView.setVisibility(View.GONE);
            binding.rvContact.setVisibility(View.VISIBLE);

        } else {
            binding.tvTextView.setVisibility(View.VISIBLE);
            binding.rvContact.setVisibility(View.GONE);
        }

        adapterNote = new AdapterNote(
                list,
                new AdapterNote.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position,NoteModels models) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        bundle.putSerializable("model",models);

                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment, DetailFragment.class, bundle)
                                .addToBackStack("name1")
                                .commit();
                    }
                },
                new AdapterNote.OnEditClickListener() {
                    @Override
                    public void onEditClick(int position,NoteModels models) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type","edit");
                        bundle.putInt("position", position);
                        bundle.putSerializable("model", models);

                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment, AddNoteFragment.class, bundle)
                                .addToBackStack("name2")
                                .commit();
                    }
                },
                new AdapterNote.OnDeleteClickListener() {
                    @Override
                    public void onDeleteClick(int position,int id) {
                        deleteNotes(position,id);
                    }
                }
        );


        adapterNote.notifyDataSetChanged();
        binding.rvContact.setAdapter(adapterNote);
    }

    private void deleteNotes(int position,int noteId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage("Are you sure you want to delete this contact?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

//                list.remove(position);
                dbHelper.deleteNotes(noteId);
                list.remove(position);
                Toast.makeText(requireContext(), "deleted", Toast.LENGTH_SHORT).show();

                adapterNote.notifyItemRemoved(position);
                adapterNote.notifyDataSetChanged();
                binding.rvContact.setAdapter(adapterNote);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        // Show the Alert Dialog box
        alertDialog.show();
    }
}