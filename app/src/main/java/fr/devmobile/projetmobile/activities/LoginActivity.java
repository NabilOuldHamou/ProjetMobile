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
        String email = emailInput.getEditableText().toString();
        String password = passwordInput.getEditableText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            submitButton.setClickable(false);
            String body = String.format("{\"Email\": \"%s\", \"Password\": \"%s\"}",
                    email, password);

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

                        // TODO : Handle when users actually logs in

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                });
        }

    }

}
