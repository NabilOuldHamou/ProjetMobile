package fr.devmobile.projetmobile.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import fr.devmobile.projetmobile.activities.MainActivity;
import fr.devmobile.projetmobile.database.AppDatabase;
import fr.devmobile.projetmobile.models.Post;
import fr.devmobile.projetmobile.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SessionManager {

    public User user;
    public List<Post> posts;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public SessionManagerListener listener;

    @SuppressLint("CheckResult")
    public SessionManager(Context context, SessionManagerListener listener) {
        this.sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

        AppDatabase appDatabase = MainActivity.getAppDatabase();

        appDatabase.postDao().getPosts()
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    this.posts = result;
                });

        appDatabase.userDao().getUsers()
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    if (!result.isEmpty()) {
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

                    listener.onTaskDone();
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

    public void deleteUser() {
        MainActivity.getAppDatabase().userDao().deleteUser(user);
    }

    public User getUser() {
        return user;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void clearPosts() {
        posts.forEach(p -> {
            MainActivity.getAppDatabase().postDao().deletePost(p);
        });
        posts.clear();
    }

    public void removePost(Post post) {
        posts.remove(post);
        MainActivity.getAppDatabase().postDao().deletePost(post);
    }

    public void addPost(Post post) {
        posts.add(post);
        MainActivity.getAppDatabase().postDao().insert(post);
    }

    public interface SessionManagerListener {
        void onTaskDone();
    }
}
