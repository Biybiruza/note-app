package com.biybiruza.noteapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biybiruza.noteapp.MyShared;
import com.biybiruza.noteapp.R;
import com.biybiruza.noteapp.data.NoteModels;
import com.biybiruza.noteapp.databinding.FragmentAddNoteBinding;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddNoteFragment extends Fragment {

    public AddNoteFragment() {
        super(R.layout.fragment_add_note);
    }

    FragmentAddNoteBinding binding;
    MyShared myShared;
    private List<NoteModels> noteList = new ArrayList<>();
    private String type;
    private int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myShared = new MyShared(requireActivity(), new Gson());

        type = requireArguments().getString("type");
        position = getArguments().getInt("position", 0);

        if (Objects.equals(type, "edit")) {
            editScreen();
        }

        binding.ivSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(type, "add")) {
                    isEmptyField();
                } else if (Objects.equals(type, "edit")){
                    editNote();
                }
            }
        });

        binding.ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentFragment(new MainFragment());
            }
        });

    }

    private void intentFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
    }

    private void editNote() {
        Long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(requireActivity().getApplication());

        noteList.set(position, new NoteModels(binding.etNoteTitle.getText().toString(),
                binding.etNote.getText().toString(),
                "" + dateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date)));

        myShared.putList("key",noteList);

        intentFragment(new MainFragment());
    }

    private void isEmptyField() {
        if (binding.etNoteTitle.getText().toString().isEmpty()){
            Toast.makeText(requireActivity(), "Enter note's title", Toast.LENGTH_SHORT).show();
        } else if (binding.etNote.getText().toString().isEmpty()){
            Toast.makeText(requireActivity(), "Enter notes", Toast.LENGTH_SHORT).show();
        }  else if (binding.etNote.getText().toString().isEmpty() && binding.etNoteTitle.getText().toString().isEmpty()){
            Toast.makeText(requireActivity(), "Enter notes", Toast.LENGTH_SHORT).show();
        } else {
            saveNotes();
        }
    }

    private void saveNotes() {
        List<NoteModels> models = new ArrayList<>();

        if (myShared.getList("key", NoteModels.class) != null) {
            models.addAll(myShared.getList("key", NoteModels.class));
        }

        Long timestamp = System.currentTimeMillis();

        Date date = new Date(timestamp);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(requireActivity().getApplication());

        models.add(new NoteModels(
                binding.etNoteTitle.getText().toString(),
                binding.etNote.getText().toString(),
                "" + dateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).format(date)));

        myShared.putList("key", models);

        Toast.makeText(requireActivity(), "Saved", Toast.LENGTH_SHORT).show();

        intentFragment(new MainFragment());
    }

    private void editScreen() {
        if (myShared.getList("key", NoteModels.class) != null){
            noteList.addAll(myShared.getList("key", NoteModels.class));

            binding.etNoteTitle.setText(noteList.get(position).getTitle());
            binding.etNote.setText(noteList.get(position).getNote());
        }
    }

}