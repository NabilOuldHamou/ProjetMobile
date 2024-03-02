package fr.devmobile.projetmobile.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.database.AppDatabase;
import fr.devmobile.projetmobile.managers.SessionManager;
import fr.devmobile.projetmobile.models.Post;
import fr.devmobile.projetmobile.network.AppHttpClient;

public class MainActivity extends AppCompatActivity implements SessionManager.SessionManagerListener {

    private static AppDatabase appDatabase;
    private static SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "app-database").build();
        sessionManager = new SessionManager(getApplicationContext(), this);
    }

    public static SessionManager getSessionManager() {
        return sessionManager;
    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    @Override
    public void onTaskDone() {
        sessionManager.clearPosts();

        if (sessionManager.getToken().isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            AppHttpClient appHttpClient = new AppHttpClient(sessionManager.getToken());
            appHttpClient.sendGetRequest("/users/" + sessionManager.getUser().getId())
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

                            sessionManager.addPost(new Post(postId, postContent, urls));
                            startActivity(new Intent(this, ProfileActivity.class));
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
        }
    }
}