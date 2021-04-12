package edu.northeastern.jotspot.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.jotspot.db.models.Entry;

public class EntryListAdapter extends RecyclerView.Adapter<EntryListAdapter.ViewHolder>{

    private final int entryItemLayout;
    private List<Entry> entryList;

    public EntryListAdapter(int layoutId){
        entryItemLayout = layoutId;
    }

    public void setEntryList(List<Entry> entries){
        entryList = entries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        return entryList == null ? 0 : entryList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(entryItemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition){
        TextView item = holder.item;
        item.setText(entryList.get(listPosition).getTimestamp().toString());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item;
        ViewHolder(View itemView){
            super(itemView);
            item = itemView.findViewById(R.id.entry_row);
        }
    }

}
