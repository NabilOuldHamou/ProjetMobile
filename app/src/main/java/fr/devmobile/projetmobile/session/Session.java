package fr.devmobile.projetmobile.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import java.util.List;

import fr.devmobile.projetmobile.database.AppDatabase;
import fr.devmobile.projetmobile.models.Post;
import fr.devmobile.projetmobile.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Session {

    private static Session instance;
    private static AppDatabase appDatabase;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    private User user;
    private List<Post> posts;

    @SuppressLint("CheckResult")
    public Session(Context context, SessionListener listener) {
        instance = this;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
        sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

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

    // Modifiers


    public void setUser(User user) {
        this.user = user;
        appDatabase.userDao().insert(user);
    }

    public void deleteUser() {
        user = null;
        appDatabase.userDao().deleteUser(user);
    }

    public void clearPosts() {
        posts.forEach(p -> {
            appDatabase.postDao().deletePost(p);
        });
        posts.clear();
    }

    public void addPost(Post post) {
        posts.add(post);
        appDatabase.postDao().insert(post);
    }

    public void removePost(Post post) {
        posts.remove(post);
        appDatabase.postDao().deletePost(post);
    }

    // GETTERS
    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static Session getInstance() {
        return instance;
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public User getUser() {
        return user;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public interface SessionListener {
        void onTaskDone();
    }

}
