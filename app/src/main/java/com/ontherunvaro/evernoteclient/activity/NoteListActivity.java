package com.ontherunvaro.evernoteclient.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteClientFactory;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.thrift.TException;
import com.ontherunvaro.evernoteclient.R;
import com.ontherunvaro.evernoteclient.adapter.NotesAdapter;

public class NoteListActivity extends AppCompatActivity {

    private final static String TAG = "NoteListActivity";
    private final static Integer MAX_NOTES = 20;

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
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NoteList notes) {
            super.onPostExecute(notes);

            if (notes != null) {
                ListView lv = (ListView) findViewById(R.id.noteList);
                NotesAdapter na = new NotesAdapter(NoteListActivity.this, android.R.layout.simple_list_item_1, notes.getNotes());
                lv.setAdapter(na);
            }

            loadingDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        new GetNotesTask().execute();

    }
}
