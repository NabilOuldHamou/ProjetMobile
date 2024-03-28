package fr.devmobile.projetmobile.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.RoomDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.database.AppDatabase;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.session.Session;

public class SplashActivity extends AppCompatActivity implements Session.SessionListener {

    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new Session(getApplicationContext(), this);
    }

    @Override
    public void onTaskDone() {
        session.clearPosts();

        if (session.getToken().isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }

        AppHttpClient appHttpClient = new AppHttpClient(session.getToken());
        appHttpClient.sendGetRequest("/users/" + session.getUser().getId())
            .thenAccept(result -> {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.has("error")) {
                        startActivity(new Intent(this, LoginActivity.class));
                        return;
                    }

                    JSONArray posts = jsonObject.getJSONArray("Posts");
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

                    startActivity(new Intent(this, MainActivity.class));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });

    }
}
