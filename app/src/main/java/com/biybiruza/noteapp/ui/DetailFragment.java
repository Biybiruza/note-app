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
import com.biybiruza.noteapp.databinding.FragmentDetailBinding;
import com.biybiruza.noteapp.ui.network.DBHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    public DetailFragment() {
        super(R.layout.fragment_detail);
    }

    FragmentDetailBinding binding;
    private int position;
    private List<NoteModels> list;
    private MyShared myShared;
    DBHelper dbHelper = null;
    NoteModels models;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myShared = new MyShared(requireContext(), new Gson());
        dbHelper = new DBHelper(requireContext());

        position = requireArguments().getInt("position", 0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            models = getArguments().getSerializable("model", NoteModels.class);
        }

        loadData();

        binding.ivEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "edit");
                bundle.putSerializable("model",models);
                getFragmentManager().popBackStack();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, AddNoteFragment.class, bundle)
                        .addToBackStack("name3")
                        .commit();



                /*getFragmentManager().beginTransaction()
                        .remove(DetailFragment.this)
                        .commit();*/
            }
        });

        binding.ivBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();

                getFragmentManager().beginTransaction()
                        .remove(DetailFragment.this)
                        .commit();
            }
        });
    }

    private void loadData() {
        list = new ArrayList<>();
//        if (myShared.getList("key", NoteModels.class) != null) {
//            list.addAll(myShared.getList("key", NoteModels.class));
//        }

        if (dbHelper.getNotes() != null) {
            list.addAll(dbHelper.getNotes());
        }

        binding.tvNoteTitle.setText(list.get(position).getTitle());
        binding.tvNote.setText(list.get(position).getNote());
    }
}