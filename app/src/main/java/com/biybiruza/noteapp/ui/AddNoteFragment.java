package com.biybiruza.noteapp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.biybiruza.noteapp.MyShared;
import com.biybiruza.noteapp.R;
import com.biybiruza.noteapp.data.NoteModels;
import com.biybiruza.noteapp.databinding.FragmentAddNoteBinding;
import com.biybiruza.noteapp.ui.network.DBHelper;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AddNoteFragment extends Fragment {

    public AddNoteFragment() {
        super(R.layout.fragment_add_note);
    }

    FragmentAddNoteBinding binding;
    private DBHelper dbHelper = null;

    private List<NoteModels> noteList = new ArrayList<>();
    private String type;
    private int position;
    NoteModels models = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(requireActivity());

        type = requireArguments().getString("type");
        position = getArguments().getInt("position", 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            models = getArguments().getSerializable("model", NoteModels.class);
        }

        if (Objects.equals(type, "edit")) {
            binding.etNoteTitle.setText(models.getTitle());
            binding.etNote.setText(models.getNote());
        }

        binding.ivSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(type, "add")) {
                    isEmptyField();
                } else if (Objects.equals(type, "edit")) {
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd | HH:mm");

        models.setTitle(binding.etNoteTitle.getText().toString());
        models.setNote(binding.etNote.getText().toString());
        models.setDate(dateFormat.format(date));

        dbHelper.editNotes(models);

        intentFragment(new MainFragment());
    }

    private void isEmptyField() {
        if (binding.etNoteTitle.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter note's title", Toast.LENGTH_SHORT).show();
        } else if (binding.etNote.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter notes", Toast.LENGTH_SHORT).show();
        } else if (binding.etNote.getText().toString().isEmpty() && binding.etNoteTitle.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Enter notes", Toast.LENGTH_SHORT).show();
        } else {
            saveNotes();
        }
    }

    private void saveNotes() {
        Long timestamp = System.currentTimeMillis();

        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd | HH:mm");

        dbHelper.addNotes(new NoteModels(binding.etNoteTitle.getText().toString(), binding.etNote.getText().toString(), dateFormat.format(date)));

        Toast.makeText(requireActivity(), "Saved", Toast.LENGTH_SHORT).show();

        intentFragment(new MainFragment());
    }
}