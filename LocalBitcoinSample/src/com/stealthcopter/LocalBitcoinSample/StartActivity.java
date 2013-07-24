package com.stealthcopter.LocalBitcoinSample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.stealthcopter.localbitcoinslibrary.LocalBitcoinAction;
import com.stealthcopter.localbitcoinslibrary.Objects.AccessToken;

public class StartActivity extends Activity implements View.OnClickListener {

    private Button loginButton;
    private Button escrowButton;
    private Button registerButton;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        loginButton = (Button) findViewById(R.id.loginButton);
        escrowButton = (Button) findViewById(R.id.escrowButton);
        registerButton = (Button) findViewById(R.id.registerButton);
    }

    @Override
    public void onResume() {
        super.onResume();
        loginButton.setOnClickListener(this);
        escrowButton.setOnClickListener(this);


        if (App.get().getAccessToken()==null){
            // We don't have an access token so offer a login
            escrowButton.setVisibility(View.GONE);
            registerButton.setVisibility(View.VISIBLE);
            loginButton.setText("Login");
        }
        else{
            // We have an access token so offer a logout
            loginButton.setText("Logout");
            escrowButton.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginButton:
                if (App.get().getAccessToken()==null){
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    App.get().removeAccessToken();
                    Toast.makeText(StartActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StartActivity.this, StartActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.escrowButton:
                // We are logged in, start EscrowActivity
                Intent intent = new Intent(StartActivity.this, EscrowActivity.class);
                startActivity(intent);
                break;
            case R.id.registerButton:
                String url = "https://localbitcoins.com/?ch=364";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
        }
    }

}
