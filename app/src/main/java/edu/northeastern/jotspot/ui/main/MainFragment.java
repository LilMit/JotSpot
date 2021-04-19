package edu.northeastern.jotspot.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.jotspot.EntryTypeSelection;
import edu.northeastern.jotspot.MainActivity;
import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.SettingsActivity;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.viewEntry.ViewAudioEntryActivity;
import edu.northeastern.jotspot.viewEntry.ViewTextEntryActivity;

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
        ImageButton addButton = getView().findViewById(R.id.add_button);
        ImageButton findButton = getView().findViewById(R.id.search_button);
        ImageButton deleteButton = getView().findViewById(R.id.delete_button);
        ImageButton preferencesButton = getView().findViewById(R.id.preferencesButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EntryTypeSelection.class);
                startActivity(i);
            }
        });



        preferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
            }
        });

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
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(Entry item) {
                int id = item.getId();
                EntryType type = item.getType();
                String content = item.getContent();
                String date = item.getDate().toString();
                Intent intent;
                if (type == EntryType.TEXT) {
                    intent = new Intent(MainFragment.this.getContext(), ViewTextEntryActivity.class);
                } else {
                    intent = new Intent(MainFragment.this.getContext(), ViewAudioEntryActivity.class);
                }
//                            Bundle bundle = new Bundle();
                ArrayList<String> info = new ArrayList<>();
                info.add(date);
                info.add(content);
//                            bundle.putStringArrayList("ENTRY", info);
                intent.putStringArrayListExtra("ENTRY", info);
//                            intent.putExtra("CONTENT", content);
                Log.e("Adapter", "about to start activity");
                startActivity(intent);
//                adapter.notifyItemChanged(position);
            }

        };
        adapter.setOnItemClickListener(itemClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}