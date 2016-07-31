package com.ontherunvaro.evernoteclient.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.thrift.TException;
import com.ontherunvaro.evernoteclient.R;
import com.ontherunvaro.evernoteclient.util.ENMLParser;
public class NoteDisplayActivity extends AppCompatActivity {

    private final static String TAG = "NoteDisplayActivity";

    public static final String PARAM_NOTE = "com.ontherunvaro.evernoteclient.PARAM_NOTE";

    private class RecoverContentTask extends AsyncTask<Note, Void, Note> {

        ProgressDialog loadingDialog = new ProgressDialog(NoteDisplayActivity.this);

        @Override
        protected void onPreExecute() {
            loadingDialog.setMessage(getString(R.string.message_retrieving_content));
            loadingDialog.show();
        }

        @Override
        protected Note doInBackground(Note... notes) {
            EvernoteNoteStoreClient nsc = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
            Note n = notes[0];

            try {
                n.setContent(nsc.getNoteContent(n.getGuid()));
                return n;
            } catch (EDAMUserException | EDAMSystemException | TException | EDAMNotFoundException e) {
                Log.e(TAG, "doInBackground: ", e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(Note note) {
            if (note != null && note.getContent() != null) {
                TextView content = (TextView) findViewById(R.id.note_display_content);
                content.setText(ENMLParser.parseContent(note.getContent()));
                loadingDialog.dismiss();
            } else {
                Toast.makeText(NoteDisplayActivity.this, R.string.error_note_content_recover, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_display);

        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.noteDisplayTitle);
        TextView content = (TextView) findViewById(R.id.note_display_content);

        Note n = (Note) getIntent().getExtras().get(PARAM_NOTE);
        title.setText(n.getTitle());
        if (n.getContent() == null) {
            new RecoverContentTask().execute(n);
        } else {
            content.setText(ENMLParser.parseContent(n.getContent()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
