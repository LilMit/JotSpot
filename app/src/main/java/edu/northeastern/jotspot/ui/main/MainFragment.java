package edu.northeastern.jotspot.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import edu.northeastern.jotspot.EntryTypeSelection;
import edu.northeastern.jotspot.MainActivity;
import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.db.models.TextEntry;

/**
 * This was initially created by following Chapter 68 of Android Studio 4.1 Development Essentials then modified
 */
public class MainFragment extends Fragment {

    private MainViewModel mainViewModel;
    private EntryListAdapter adapter;

//    private TextView entryId;
//    private TextView entryTimestamp;
//    private TextView entryType;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
////        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

//        entryId = getView().findViewById(R.id.entryId);
//        entryTimestamp = getView().findViewById(R.id.entryTimestamp);
//        entryType = getView().findViewById(R.id.entryType);

        listenerSetup();
        observerSetup();
        recyclerSetup();
    }

//    private void clearFields() {
//        entryId.setText("");
//        entryTimestamp.setText("");
//        entryType.setText("");
//    }

    private void listenerSetup() {
        Button addButton = getView().findViewById(R.id.add_button);
        Button findButton = getView().findViewById(R.id.search_button);
        Button deleteButton = getView().findViewById(R.id.delete_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EntryTypeSelection.class);
                startActivity(i);
            }
        });
        //TODO reimplement in textEntryActivity and remove from here

//            @Override
//            public void onClick(View view) {
//                String timestamp = entryTimestamp.getText().toString();
//                String type = entryType.getText().toString();
//
//                if (!timestamp.equals("") && !type.equals("")) {
//                    Entry entry = new TextEntry(Timestamp.valueOf(timestamp));
//                    mainViewModel.insertEntry(entry);
//                    clearFields();
//                } else {
//                    entryId.setText("Incomplete information");
//                }
//            }
//        });

        //TODO reimplement
//        findButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainViewModel.findEntry(entryTimestamp.getText().toString());
//            }
//        });
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mainViewModel.deleteEntry(entryId.getText().toString());
//                clearFields();
//            }
//        });

    }

    private void observerSetup() {
        mainViewModel.getAllEntries().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                adapter.setEntryList(entries);
            }
        });
        //TODO reimplement in search later
//        mainViewModel.getSearchResults().observe(getViewLifecycleOwner(), new Observer<List<Entry>>() {
//            @Override
//            public void onChanged(List<Entry> entries) {
//                if (entries.size() > 0) {
//                    entryId.setText(String.format(Locale.US, "%d", entries.get(0).getId()));
//                    entryTimestamp.setText(entries.get(0).getTimestamp().toString());
//                    entryType.setText(String.format(Locale.US, "%d", entries.get(0).getType()));
//                } else {
//                    entryId.setText("No matching entries.");
//                }
//            }
//        });
    }

    private void recyclerSetup() {
        RecyclerView recyclerView;
        adapter = new EntryListAdapter(R.layout.entry_list_item);
        recyclerView = getView().findViewById(R.id.entry_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}