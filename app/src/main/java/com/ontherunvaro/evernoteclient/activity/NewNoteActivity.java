package com.ontherunvaro.evernoteclient.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.asyncclient.EvernoteCallback;
import com.evernote.client.android.asyncclient.EvernoteNoteStoreClient;
import com.evernote.edam.type.Note;
import com.ontherunvaro.evernoteclient.R;

public class NewNoteActivity extends AppCompatActivity implements EvernoteCallback<Note> {

    private static final String TAG = "NewNoteActivity";

    @Override
    public void onSuccess(Note result) {
        if (result != null) {
            Log.d(TAG, "onPostExecute: Nota creada" + result);
            Toast.makeText(this, R.string.message_new_note_created, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onException(Exception exception) {
        Log.e(TAG, "createNoteAndFinish: ", exception);
        Toast.makeText(this, R.string.error_note_not_saved, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        Button saveBtn = (Button) findViewById(R.id.button_new_note_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleField = (EditText) findViewById(R.id.field_new_note_title);
                EditText contentField = (EditText) findViewById(R.id.field_new_note_content);


                boolean complete = true;
                String title = titleField.getText().toString().trim();
                String content = contentField.getText().toString().trim();
                if (title.isEmpty()) {
                    titleField.setError(getString(R.string.error_compulsory_field));
                    complete = false;
                }
                if (content.isEmpty()) {
                    contentField.setError(getString(R.string.error_compulsory_field));
                    complete = false;
                }
                if (complete) {
                    createNote(title, content);
                }
            }
        });

    }

    private void createNote(String title, String content) {
        Note note = new Note();
        note.setTitle(title);
        note.setContent(EvernoteUtil.NOTE_PREFIX + content + EvernoteUtil.NOTE_SUFFIX);
        EvernoteNoteStoreClient nsc = EvernoteSession.getInstance().getEvernoteClientFactory().getNoteStoreClient();
        nsc.createNoteAsync(note, this);
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
