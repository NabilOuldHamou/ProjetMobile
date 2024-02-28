package fr.devmobile.projetmobile.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import fr.devmobile.projetmobile.activities.MainActivity;
import fr.devmobile.projetmobile.models.User;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SessionManager {

    private User user;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

        MainActivity.getAppDatabase().userDao().getUsers()
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    if (result.size() > 0) {
                        this.user = result.get(0);
                    }
                });

    }

    public void saveToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void setUser(User user) {
        this.user = user;
        MainActivity.getAppDatabase().userDao().insert(user);
    }

    public User getUser() {
        return user;
    }
}
