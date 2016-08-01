package com.ontherunvaro.evernoteclient.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteClientFactory;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import com.ontherunvaro.evernoteclient.R;
import com.ontherunvaro.evernoteclient.adapter.NotesAdapter;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {

    private final static String TAG = "NoteListActivity";
    private final static Integer MAX_NOTES = 20;

    private final static int REQUEST_NEW_NOTE_SUCCESSFUL = 1;

    private final static String STATE_NOTELIST = "com.ontherunvaro.evernoteclient.state.notelist";

    private class GetNotesTask extends AsyncTask<Void, Void, NoteList> {

        ProgressDialog loadingDialog = new ProgressDialog(NoteListActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingDialog.setMessage(getString(R.string.message_retrieving_notes));
            loadingDialog.show();
        }

        @Override
        protected NoteList doInBackground(Void... voids) {

            EvernoteClientFactory f = EvernoteSession.getInstance().getEvernoteClientFactory();
            EvernoteNoteStoreClient nsc = f.getNoteStoreClient();
            NoteList notes;
            try {
                notes = nsc.findNotes(new NoteFilter(), 0, MAX_NOTES);
                return notes;
            } catch (EDAMUserException | TException | EDAMSystemException | EDAMNotFoundException e) {
                Log.e(TAG, "doInBackground: ", e);
            }


            return null;
        }

        @Override
        protected void onPostExecute(NoteList notes) {
            super.onPostExecute(notes);

            if (notes != null) {
                ListView lv = (ListView) findViewById(R.id.noteList);
                NotesAdapter na = new NotesAdapter(NoteListActivity.this, android.R.layout.simple_list_item_1, notes.getNotes());
                na.sort(NotesAdapter.SortType.TITLE);
                lv.setAdapter(na);
            }

            initSpinner();

            loadingDialog.dismiss();
        }
    }

    private void initSpinner() {
        Spinner sortSpinner = (Spinner) findViewById(R.id.spinner_notesort);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sortList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        ListView lv = (ListView) findViewById(R.id.noteList);
        if (savedInstanceState != null) {
            ArrayList<Note> noteList =
                    (ArrayList<Note>) savedInstanceState.getSerializable(STATE_NOTELIST);
            if (noteList != null) {
                NotesAdapter na = new NotesAdapter(NoteListActivity.this, android.R.layout.simple_list_item_1, noteList);
                lv.setAdapter(na);
                initSpinner();
            } else {
                new GetNotesTask().execute();
            }
        } else {
            new GetNotesTask().execute();
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                Note n = (Note) adapter.getItemAtPosition(pos);
                Intent i = new Intent(NoteListActivity.this, NoteDisplayActivity.class);
                i.putExtra(NoteDisplayActivity.PARAM_NOTE, n);
                startActivity(i);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.new_note_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteListActivity.this, NewNoteActivity.class);
                startActivityForResult(i, REQUEST_NEW_NOTE_SUCCESSFUL);
            }
        });

        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        ListView lv = (ListView) findViewById(R.id.noteList);
        ArrayList<Note> noteList = ((NotesAdapter) lv.getAdapter()).getValues();
        outState.putSerializable(STATE_NOTELIST, noteList);
        super.onSaveInstanceState(outState);
    }

    private void sortList() {
        ListView lv = (ListView) findViewById(R.id.noteList);
        NotesAdapter na = (NotesAdapter) lv.getAdapter();

        Spinner sortSpinner = (Spinner) findViewById(R.id.spinner_notesort);
        TextView tv = (TextView) sortSpinner.getSelectedView();
        if (tv != null) {
            String selected = tv.getText().toString();
            if (selected.equals(getString(R.string.option_sort_title))) {
                na.sort(NotesAdapter.SortType.TITLE);
            } else if (selected.equals(getString(R.string.option_sort_date))) {
                na.sort(NotesAdapter.SortType.DATE);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                confirmLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        confirmLogout();
    }

    private void confirmLogout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.message_logout_title)
                .setMessage(R.string.message_logout_message)
                .setPositiveButton(R.string.option_logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EvernoteSession.getInstance().logOut();
                        Intent i = new Intent(NoteListActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                })
                .setNegativeButton(R.string.option_cancel, null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_NOTE_SUCCESSFUL) {
            if (resultCode == RESULT_OK) {
                Note n = (Note) data.getExtras().get(NewNoteActivity.PARAM_NOTE_RESULT);
                ListView lv = (ListView) findViewById(R.id.noteList);
                NotesAdapter na = (NotesAdapter) lv.getAdapter();
                na.add(n);
                sortList();
            }
        }
    }
}
