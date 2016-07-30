package com.ontherunvaro.evernoteclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evernote.edam.type.Note;

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
}
