package edu.northeastern.jotspot.ui.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.jotspot.R;
import edu.northeastern.jotspot.db.models.Entry;
import edu.northeastern.jotspot.db.models.EntryType;

/**
 * This was created by following Chapter 68 of Android Studio 4.1 Development Essentials
 */
public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.ViewHolder> {

    private final int entryItemLayout;
    private List<Entry> entryList;
    private ItemClickListener ocListener;

    public EntryListAdapter(int layoutId) {
        entryItemLayout = layoutId;
    }

    public void setEntryList(List<Entry> entries) {
        entryList = entries;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ItemClickListener listener) {
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

        public ViewHolder(View itemView, final ItemClickListener listener) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.entry_timestamp);
            content = itemView.findViewById(R.id.content_text);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.e("Adapter", "click detected");
                    if (listener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(entryList.get(position));
                        }
                    } else {
                        Log.e("Adapter", "listener null");
                    }
                }
            });
        }
    }

}
