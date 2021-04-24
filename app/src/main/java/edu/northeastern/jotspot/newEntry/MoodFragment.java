package edu.northeastern.jotspot.newEntry;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MainViewModel mainViewModel;
    private Entry currentEntry;

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
        Log.e(TAG, "currentEntry =" + currentEntry.toString());
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

        ImageButton terribleButton = v.findViewById(R.id.imageButton);
        ImageButton badButton = v.findViewById(R.id.imageButton2);
        ImageButton mehButton = v.findViewById(R.id.imageButton3);
        ImageButton goodButton = v.findViewById(R.id.imageButton4);
        ImageButton bestButton = v.findViewById(R.id.imageButton5);
        terribleButton.setOnClickListener(this);
        badButton.setOnClickListener(this);
        mehButton.setOnClickListener(this);
        goodButton.setOnClickListener(this);
        bestButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton: {
                currentEntry.setMood(1);
                break;
            }
            case R.id.imageButton2: {
                currentEntry.setMood(2);
                break;
            }
            case R.id.imageButton3: {
                currentEntry.setMood(3);
                break;
            }
            case R.id.imageButton4: {
                currentEntry.setMood(4);
                break;
            }
            case R.id.imageButton5: {
                currentEntry.setMood(5);
                break;
            }
        }
        mainViewModel.setSelectedEntry(currentEntry);
    }

}