package fr.devmobile.projetmobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.adapters.ImageAdapter;
import fr.devmobile.projetmobile.managers.SessionManager;
import fr.devmobile.projetmobile.models.Post;
import fr.devmobile.projetmobile.models.User;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ImageView profilePicture;
    private TextView username;
    private TextView displayName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SessionManager sm = MainActivity.getSessionManager();
        User user = sm.getUser();
        List<Post> posts = sm.getPosts();

        // Affichage des informations de l'utilisateur en haut de la page

        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        username = (TextView) findViewById(R.id.profile_username);
        displayName = (TextView) findViewById(R.id.profile_displayname);

        username.setText("@" + user.getUsername());
        displayName.setText(user.getDisplayName());
        Glide.with(this).load(user.getAvatar()).into(profilePicture);

        // Affichage des posts de l'utilisateur
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_profile);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        imageAdapter = new ImageAdapter(this, posts);
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

    }

    public void openSettings(View v) {
        // TODO : Pop a menu with actions related to the account.

        SessionManager sm = MainActivity.getSessionManager();
        sm.deleteToken();
        User user = sm.getUser();
        MainActivity.getAppDatabase().userDao().deleteUser(user);
        startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }

}
