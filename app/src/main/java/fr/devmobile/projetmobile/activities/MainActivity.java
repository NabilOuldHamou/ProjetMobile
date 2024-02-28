package fr.devmobile.projetmobile.activities;

import androidx.activity.ComponentActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.database.AppDatabase;
import fr.devmobile.projetmobile.managers.SessionManager;

public class MainActivity extends ComponentActivity implements SessionManager.SessionManagerListener {

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
    public void onUserLoaded() {
        if (sessionManager.getToken().equals("")) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }
    }
}