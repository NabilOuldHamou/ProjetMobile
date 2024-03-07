package fr.devmobile.projetmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.session.Session;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private Button registerButton;
    private TextView errorOutput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.usernameId);
        email = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.passwordId);
        registerButton = (Button) findViewById(R.id.registerButton);
        errorOutput = (TextView) findViewById(R.id.su_error_output);
    }

    public void signUp(View v) {
        String emailIn = email.getEditableText().toString();
        String usernameIn = username.getEditableText().toString();
        String passwordIn = password.getEditableText().toString();

        if (!emailIn.isEmpty() && !usernameIn.isEmpty() && !passwordIn.isEmpty()) {
            registerButton.setClickable(false);
            String body = String.format("{\"Username\": \"%s\", \"DisplayName\": \"%s\", \"Email\": \"%s\", \"Password\": \"%s\"}",
                    usernameIn, usernameIn, emailIn, passwordIn);

            AppHttpClient appHttpClient = new AppHttpClient();
            appHttpClient.sendPostRequest("/signup", body)
                .thenAccept(result -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("error")) {
                            registerButton.setClickable(true);

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

                        Session session = Session.getInstance();
                        session.saveToken(token);
                        session.setUser(new User(id, username, displayName, email));

                        startActivity(new Intent(this, MainActivity.class));
                        this.finish();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                });
        }
    }

    public void redirectToLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }

}
