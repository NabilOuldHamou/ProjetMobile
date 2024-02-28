package fr.devmobile.projetmobile.activities;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import fr.devmobile.projetmobile.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profilePicture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        Glide.with(this).load(MainActivity.getSessionManager().getUser().getAvatar()).into(profilePicture);
    }
}
