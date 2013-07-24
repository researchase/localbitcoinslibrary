package com.stealthcopter.LocalBitcoinSample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.stealthcopter.localbitcoinslibrary.LocalBitcoinAction;
import com.stealthcopter.localbitcoinslibrary.Objects.AccessToken;

public class LoginActivity extends Activity implements View.OnClickListener {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        findViewById(R.id.connectButton).setOnClickListener(this);
        findViewById(R.id.registerButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.connectButton:

                // Grab username and password
                String username = ((EditText)findViewById(R.id.username)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();

                login(username, password);

                break;
            case R.id.registerButton:
                String url = "https://localbitcoins.com/?ch=364";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }

    private void login(final String username, final String password) {
        // Login uses a network connection so we run it in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Perform login using username and password
                AccessToken accessToken = App.get().getLocalBitcoinsAction().connect(username, password, LocalBitcoinAction.MODE_READ_WRITE);
                // TODO: Save this access token in your preferred way

                // Note if you have a saved access token, you can pass this instead of making user login again:
                // localBitcoinAction.connect(accessToken);

                if (accessToken!=null){
                    // We are logged in, start EscrowActivity
                    Intent intent = new Intent(LoginActivity.this, EscrowActivity.class);
                    startActivity(intent);
                }

            }
        }).start();
    }
}
