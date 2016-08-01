package com.ontherunvaro.evernoteclient.activity;

import android.content.Intent;
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
import com.myscript.atk.sltw.SingleLineWidget;
import com.myscript.atk.sltw.SingleLineWidgetApi;
import com.ontherunvaro.evernoteclient.R;
import com.ontherunvaro.evernoteclient.myscript.MyCertificate;

public class NewNoteActivity extends AppCompatActivity implements EvernoteCallback<Note>, SingleLineWidgetApi.OnConfiguredListener, SingleLineWidgetApi.OnTextChangedListener {

    private static final String TAG = "NewNoteActivity";
    public static final String PARAM_NOTE_RESULT = "com.ontherunvaro.evernoteclient.RESULT_NEW_NOTE";

    private SingleLineWidgetApi widget;
    private EditText contentField;

    @Override
    public void onSuccess(Note result) {
        if (result != null) {
            Log.d(TAG, "onPostExecute: Nota creada" + result);
            Toast.makeText(this, R.string.message_new_note_created, Toast.LENGTH_SHORT).show();

            Intent i = new Intent();
            i.putExtra(PARAM_NOTE_RESULT, result);
            setResult(RESULT_OK, i);
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

        contentField = (EditText) findViewById(R.id.field_new_note_content);


        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        Button saveBtn = (Button) findViewById(R.id.button_new_note_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText titleField = (EditText) findViewById(R.id.field_new_note_title);

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

        Button clearBtn = (Button) findViewById(R.id.button_new_note_clear);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearContent();
            }
        });

        configureReader();
    }

    private void configureReader() {
        widget = (SingleLineWidget) findViewById(R.id.singleLine_widget);
        if (!widget.registerCertificate(MyCertificate.getBytes())) {
            Toast.makeText(this, R.string.error_reader_not_configured, Toast.LENGTH_SHORT).show();
            ((View) widget).setVisibility(View.GONE);
            View v = findViewById(R.id.field_new_note_content);
            return;
        }

        widget.setOnConfiguredListener(this);
        widget.setOnTextChangedListener(this);
        widget.addSearchDir("zip://" + getPackageCodePath() + "!/assets/conf");
        widget.configure("es_ES", "cur_text");

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
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onDestroy() {
        widget.setOnTextChangedListener(null);
        widget.setOnConfiguredListener(null);

        super.onDestroy();
    }


    @Override
    public void onConfigured(SingleLineWidgetApi w, boolean success) {
        if (!success) {
            Toast.makeText(this, R.string.error_reader_not_configured, Toast.LENGTH_SHORT).show();
            ((View) widget).setVisibility(View.GONE);
        }
    }

    @Override
    public void onTextChanged(SingleLineWidgetApi w, String s, boolean intermediate) {
        contentField.setText(s);
    }


    public void clearContent() {
        widget.clear();
        contentField.setText("");
    }

}
