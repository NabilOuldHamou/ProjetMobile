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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.network.Callback;
import fr.devmobile.projetmobile.network.UserRequest;
import fr.devmobile.projetmobile.session.Session;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText emailInput;
    private EditText passwordInput;
    private Button submitButton;
    private TextView errorOutput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        submitButton = findViewById(R.id.submit_button);
        errorOutput = findViewById(R.id.si_error_output);
        progressBar = findViewById(R.id.progress_bar);
    }

    public void signIn(View v) {
        String emailIn = emailInput.getEditableText().toString();
        String passwordIn = passwordInput.getEditableText().toString();

        if (!emailIn.isEmpty() && !passwordIn.isEmpty()) {
            submitButton.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            new UserRequest().signIn(emailIn, passwordIn, new Callback() {
                @Override
                public void onResponse(Object data) {
                    runOnUiThread(() -> {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    });
                }

                @Override
                public void onError(Object data) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        submitButton.setClickable(true);
                        String error = (String) data;
                        errorOutput.setText(error);
                        return;
                    });
                }
            });
        }

    }

    public void redirectToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
