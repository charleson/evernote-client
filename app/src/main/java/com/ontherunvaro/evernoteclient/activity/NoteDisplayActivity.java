package com.ontherunvaro.evernoteclient.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.evernote.edam.type.Note;
import com.ontherunvaro.evernoteclient.R;
import com.ontherunvaro.evernoteclient.util.ENMLParser;

public class NoteDisplayActivity extends AppCompatActivity {

    public static final String PARAM_NOTE = "com.ontherunvaro.evernoteclient.PARAM_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_display);

        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.noteDisplayTitle);
        TextView content = (TextView) findViewById(R.id.noteDisplayContent);

        Note n = (Note) getIntent().getExtras().get(PARAM_NOTE);
        title.setText(n.getTitle());
        content.setText(ENMLParser.parseContent(n.getContent()));
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
