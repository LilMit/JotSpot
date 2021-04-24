package edu.northeastern.jotspot.viewEntry;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.newEntry.MoodFragment;
import edu.northeastern.jotspot.ui.main.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditEntryFragment extends Fragment {

    private MainViewModel mainViewModel;
    private Entry currentEntry;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "EditEntry";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText contentEditText;
    private Button saveButton;
    private Button cancelButton;
    private TextView entryDate;

    public EditEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditEntryFragment newInstance(String param1, String param2) {
        EditEntryFragment fragment = new EditEntryFragment();
        Bundle args = new Bundle();
        args.putString(TAG, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        currentEntry = new Entry();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(TAG);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_entry, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        contentEditText = v.findViewById(R.id.edit_entry_content_text);
        entryDate = v.findViewById(R.id.edit_text_entry_date);
        mainViewModel.getSelectedEntry().observe(getViewLifecycleOwner(), new
                Observer<Entry>() {
                    @Override
                    public void onChanged(Entry entry) {
                        currentEntry = entry;
                        contentEditText.setText(currentEntry.getContent(), TextView.BufferType.EDITABLE);
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                        String dateText = format.format(currentEntry.getDate().getTime());
                        entryDate.setText(dateText);
                        Log.e(TAG, "currentEntry =" + entry.toString());
                    }
                });

        saveButton = v.findViewById(R.id.edit_save_button);
        cancelButton = v.findViewById(R.id.edit_cancel_button);

        Log.e(TAG, "text= " + contentEditText.getText().toString());
        Log.e(TAG, "Content = " + currentEntry.getContent());
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEditText.getText().toString();
                currentEntry.setContent(content);
                mainViewModel.updateEntry(currentEntry);
                Log.e(TAG, "saving entry "+ currentEntry.toString());
                getActivity().getSupportFragmentManager().beginTransaction().remove(EditEntryFragment.this).commit();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(EditEntryFragment.this).commit();
            }
        });

    }


}