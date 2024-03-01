package fr.devmobile.projetmobile.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import fr.devmobile.projetmobile.activities.MainActivity;
import fr.devmobile.projetmobile.database.AppDatabase;
import fr.devmobile.projetmobile.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SessionManager {

    private User user;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SessionManagerListener listener;

    @SuppressLint("CheckResult")
    public SessionManager(Context context, SessionManagerListener listener) {
        this.sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

        AppDatabase appDatabase = MainActivity.getAppDatabase();

        appDatabase.userDao().getUsers()
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    if (result.size() > 0) {
                        this.user = result.get(0);
                    }

                    AppHttpClient appHttpClient = new AppHttpClient(getToken());
                    appHttpClient.validateToken()
                        .thenAccept(b -> {
                            if (!b) {
                                deleteToken();
                                appDatabase.userDao().deleteUser(user);
                            }
                        });

                    listener.onUserLoaded();
                });

    }

    public void saveToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public void deleteToken() {
        editor.remove("token");
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

    public interface SessionManagerListener {
        void onUserLoaded();
    }

}
