package fr.devmobile.projetmobile.activities;

import androidx.activity.ComponentActivity;

import android.content.Intent;
import android.os.Bundle;

import fr.devmobile.projetmobile.R;

public class MainActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}