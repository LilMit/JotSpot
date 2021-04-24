package edu.northeastern.jotspot.newEntry;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.ui.main.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoodFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "MoodFragment";
    private static final int SELECTED_COLOR = R.color.pale;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MainViewModel mainViewModel;
    private Entry currentEntry;

    ImageButton terribleButton;
    ImageButton badButton;
    ImageButton mehButton;
    ImageButton goodButton;
    ImageButton bestButton;

    public MoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoodFragment newInstance(String param1, String param2) {
        MoodFragment fragment = new MoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        currentEntry = new Entry();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.moods, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        mainViewModel.getSelectedEntry().observe(getViewLifecycleOwner(), new
                Observer<Entry>() {
                    @Override
                    public void onChanged(Entry entry) {
                        currentEntry = entry;
                        Log.e(TAG, "currentEntry =" + entry.toString());
                    }
                });
        listenerSetup(v);
    }

    private void listenerSetup(View v) {

        terribleButton = v.findViewById(R.id.imageButton);
        badButton = v.findViewById(R.id.imageButton2);
        mehButton = v.findViewById(R.id.imageButton3);
        goodButton = v.findViewById(R.id.imageButton4);
        bestButton = v.findViewById(R.id.imageButton5);
        terribleButton.setOnClickListener(this);
        badButton.setOnClickListener(this);
        mehButton.setOnClickListener(this);
        goodButton.setOnClickListener(this);
        bestButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int color = getResources().getColor(SELECTED_COLOR);
        deselectAll();
        switch (v.getId()) {
            case R.id.imageButton: {
                currentEntry.setMood(1);
                v.setBackgroundColor(color);
                break;
            }
            case R.id.imageButton2: {
                currentEntry.setMood(2);
                v.setBackgroundColor(color);
                break;
            }
            case R.id.imageButton3: {
                currentEntry.setMood(3);
                v.setBackgroundColor(color);
                break;
            }
            case R.id.imageButton4: {
                currentEntry.setMood(4);
                v.setBackgroundColor(color);
                break;
            }
            case R.id.imageButton5: {
                currentEntry.setMood(5);
                v.setBackgroundColor(color);
                break;
            }
        }
        if (currentEntry.getId() != null) {
            mainViewModel.updateEntry(currentEntry);
        } else {
            mainViewModel.setSelectedEntry(currentEntry);
        }
    }

    private void deselectAll(){
        terribleButton.setBackgroundColor(Color.TRANSPARENT);
        badButton.setBackgroundColor(Color.TRANSPARENT);
        mehButton.setBackgroundColor(Color.TRANSPARENT);
        goodButton.setBackgroundColor(Color.TRANSPARENT);
        bestButton.setBackgroundColor(Color.TRANSPARENT);
    }

}