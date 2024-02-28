package fr.devmobile.projetmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.managers.SessionManager;
import fr.devmobile.projetmobile.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button submitButton;
    private TextView errorOutput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = (EditText) findViewById(R.id.email_input);
        passwordInput = (EditText) findViewById(R.id.password_input);
        submitButton = (Button) findViewById(R.id.submit_button);
        errorOutput = (TextView) findViewById(R.id.si_error_output);
    }

    public void signIn(View v) {
        String emailIn = emailInput.getEditableText().toString();
        String passwordIn = passwordInput.getEditableText().toString();

        if (!emailIn.isEmpty() && !passwordIn.isEmpty()) {
            submitButton.setClickable(false);
            String body = String.format("{\"Email\": \"%s\", \"Password\": \"%s\"}",
                    emailIn, passwordIn);

            AppHttpClient appHttpClient = new AppHttpClient();
            appHttpClient.sendPostRequest("/login", body)
                .thenAccept(result -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("error")) {
                            submitButton.setClickable(true);

                            String error = jsonObject.getString("error");
                            errorOutput.setText(error);
                            return;
                        }

                        JSONObject userObjet = jsonObject.getJSONObject("user");
                        String token = jsonObject.getString("token");
                        String id = userObjet.getString("ID");
                        String username = userObjet.getString("Username");
                        String displayName = userObjet.getString("DisplayName");
                        String email = userObjet.getString("Email");

                        SessionManager sm = MainActivity.getSessionManager();
                        sm.saveToken(token);
                        sm.setUser(new User(id, username, displayName, email));

                        startActivity(new Intent(this, ProfileActivity.class));

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                });
        }

    }

    public void redirectToRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
