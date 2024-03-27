package fr.devmobile.projetmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.network.Callback;
import fr.devmobile.projetmobile.network.UserRequest;

public class RegisterActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText username;
    private EditText email;
    private EditText password;
    private Button registerButton;
    private TextView errorOutput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.usernameId);
        email = findViewById(R.id.emailId);
        password = findViewById(R.id.passwordId);
        registerButton = findViewById(R.id.registerButton);
        errorOutput = findViewById(R.id.su_error_output);
        progressBar = findViewById(R.id.progress_bar);
    }

    public void signUp(View v) {
        String emailIn = email.getEditableText().toString();
        String usernameIn = username.getEditableText().toString();
        String passwordIn = password.getEditableText().toString();

        if (!emailIn.isEmpty() && !usernameIn.isEmpty() && !passwordIn.isEmpty()) {
            registerButton.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            new UserRequest().register(usernameIn, emailIn, passwordIn,new Callback(){

                @Override
                public void onResponse(Object data) {
                    runOnUiThread(() -> {
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    });
                }

                @Override
                public void onError(Object data) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        registerButton.setClickable(true);
                        String error = (String)data;
                        errorOutput.setText(error);
                        return;
                    });
                }
            });
        }
    }

    public void redirectToLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }

}
