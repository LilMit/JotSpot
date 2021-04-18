package edu.northeastern.jotspot.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.jotspot.MainActivity;
import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;
import edu.northeastern.jotspot.viewEntry.ViewAudioEntryActivity;
import edu.northeastern.jotspot.viewEntry.ViewTextEntryActivity;

/**
 * This was created by following Chapter 68 of Android Studio 4.1 Development Essentials
 */
public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.ViewHolder> {

    private final int entryItemLayout;
    private List<Entry> entryList;
    private View.OnClickListener ocListener;

    public EntryListAdapter(int layoutId) {
        entryItemLayout = layoutId;
    }

    public void setEntryList(List<Entry> entries) {
        entryList = entries;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(View.OnClickListener listener) {
        this.ocListener = listener;
    }

    @Override
    public int getItemCount() {
        return entryList == null ? 0 : entryList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(entryItemLayout, parent, false);
        return new ViewHolder(view, ocListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView date = holder.timestamp;
        TextView content = holder.content;
        Entry item = entryList.get(listPosition);
        EntryType type = item.getType();
        if (type.equals(EntryType.TEXT)) {
            content.setText(item.getContent());
        } else {
            content.setText(item.getType().toString());
        }
        date.setText(item.getDate().toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timestamp;
        TextView content;

        ViewHolder(View itemView, final View.OnClickListener listener) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.entry_timestamp);
            content = itemView.findViewById(R.id.content_text);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Entry item = entryList.get(position);
                            int id = item.getId();
                            EntryType type = item.getType();
                            String content = item.getContent();
                            String date = item.getDate().toString();
                            Intent intent;
                            if (type == EntryType.TEXT) {
                                intent = new Intent(ViewHolder.this.itemView.getContext(), ViewTextEntryActivity.class);
                            } else {
                                intent = new Intent(ViewHolder.this.itemView.getContext(), ViewAudioEntryActivity.class);
                            }
//                            Bundle bundle = new Bundle();
                            ArrayList<String> info = new ArrayList<>();
                            info.add(date);
                            info.add(content);
//                            bundle.putStringArrayList("ENTRY", info);
                            intent.putStringArrayListExtra("ENTRY", info);
//                            intent.putExtra("CONTENT", content);
                            ViewHolder.this.itemView.getContext().startActivity(intent);
                        }
                    }
                }
            });
        }
    }

}
