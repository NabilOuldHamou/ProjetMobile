package fr.devmobile.projetmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.models.Post;
import fr.devmobile.projetmobile.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.session.Session;

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
                        Session session = Session.getInstance();
                        session.clearPosts();
                        session.deleteToken();

                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("error")) {
                            submitButton.setClickable(true);

                            String error = jsonObject.getString("error");
                            errorOutput.setText(error);
                            return;
                        }

                        JSONObject userObject = jsonObject.getJSONObject("user");
                        String token = jsonObject.getString("token");
                        String id = userObject.getString("ID");
                        String username = userObject.getString("Username");
                        String displayName = userObject.getString("DisplayName");
                        String email = userObject.getString("Email");

                        JSONArray posts = userObject.getJSONArray("Posts");
                        for (int i = 0; i < posts.length(); i++) {
                            JSONObject post = posts.getJSONObject(i);
                            String postId = post.getString("ID");
                            String postContent = post.getString("Text");
                            List<String> urls = new ArrayList<>();
                            JSONArray files = post.getJSONArray("Files");
                            for (int j = 0; j < files.length(); j++) {
                                JSONObject file = files.getJSONObject(j);
                                urls.add("https://oxyjen.io/assets/" + file.getString("FileName"));
                            }

                            session.addPost(new Post(postId, postContent, urls));
                        }

                        session.setUser(new User(id, username, displayName, email));
                        session.saveToken(token);

                        startActivity(new Intent(this, MainActivity.class));
                        this.finish();

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
