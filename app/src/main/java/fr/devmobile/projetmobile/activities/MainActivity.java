package fr.devmobile.projetmobile.activities;

import androidx.activity.ComponentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.database.AppDatabase;
import fr.devmobile.projetmobile.managers.SessionManager;

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
    public void onUserLoaded() {
        startActivity(new Intent(this,
                sessionManager.getToken().equals("") ?
                LoginActivity.class : ProfileActivity.class));
    }
}