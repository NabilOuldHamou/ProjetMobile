package fr.devmobile.projetmobile.activities;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.models.User;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private TextView username;
    private TextView displayName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        username = (TextView) findViewById(R.id.profile_username);
        displayName = (TextView) findViewById(R.id.profile_displayname);

        User user = MainActivity.getSessionManager().getUser();

        username.setText(user.getUsername());
        displayName.setText(user.getDisplayName());
        Glide.with(this).load(user.getAvatar()).into(profilePicture);
    }
}
