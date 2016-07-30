package com.ontherunvaro.evernoteclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evernote.edam.type.Note;

import java.util.Comparator;
import java.util.List;

/**
 * Created by ontherunvaro on 30/07/16.
 */
public class NotesAdapter extends ArrayAdapter<Note> {

    public NotesAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            view = li.inflate(android.R.layout.simple_list_item_1, null);
        }

        Note note = getItem(position);

        if (note.getTitle() != null) {
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(note.getTitle());
        }

        return view;
    }


    public void sort(SortType type) {
        switch (type) {

            case TITLE:
                super.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note n1, Note n2) {
                        return n1.getTitle().compareTo(n2.getTitle());
                    }
                });
                break;
            case DATE:
                super.sort(new Comparator<Note>() {
                    @Override
                    public int compare(Note n1, Note n2) {
                        return Long.valueOf(n1.getUpdated()).compareTo(n2.getUpdated());
                    }
                });
                break;
        }

        notifyDataSetChanged();
    }

    public enum SortType {
        DATE, TITLE
    }
}
