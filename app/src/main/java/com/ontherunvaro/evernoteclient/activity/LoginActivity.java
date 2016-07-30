package com.ontherunvaro.evernoteclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.login.EvernoteLoginFragment;
import com.ontherunvaro.evernoteclient.R;

public class LoginActivity extends AppCompatActivity implements EvernoteLoginFragment.ResultCallback {

    private final static String TAG = LoginActivity.class.getSimpleName();

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (EvernoteSession.getInstance().isLoggedIn()) {
            nextActivity();
            finish();
        }

        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        doLogin();
    }

    public void doLogin() {
        Log.d(TAG, "Opening login window");
        loginButton.setEnabled(false);
        EvernoteSession.getInstance().authenticate(this);
    }

    @Override
    public void onLoginFinished(boolean successful) {
        Log.d(TAG, successful ? "Login successful" : "Login unsuccessful");
        if (successful) {
            nextActivity();
        } else {
            loginButton.setEnabled(true);
        }
    }

    public void nextActivity() {
        Intent intent = new Intent(this, NoteListActivity.class);
        startActivity(intent);
    }
}
