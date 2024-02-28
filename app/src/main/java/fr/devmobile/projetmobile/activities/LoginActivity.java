package fr.devmobile.projetmobile.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    private TextView emailInput;
    private TextView passwordInput;
    private Button submitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = (TextView) findViewById(R.id.email_input);
        passwordInput = (TextView) findViewById(R.id.password_input);
        submitButton = (Button) findViewById(R.id.submit_button);
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

                            Log.d("error", "signIn: called");
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

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                });
        }

    }

}
