package fr.devmobile.projetmobile.session;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.room.Room;

import java.util.List;

import fr.devmobile.projetmobile.database.AppDatabase;
import fr.devmobile.projetmobile.database.AppDatabaseRepository;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Session {

    private static Session instance;
    private static AppDatabase appDatabase;
    private AppDatabaseRepository appDatabaseRepository;

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    private User user;
    private List<Post> posts;

    @SuppressLint("CheckResult")
    public Session(Context context, SessionListener listener) {
        instance = this;
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "app-database").build();
        appDatabaseRepository = new AppDatabaseRepository();
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
                                    appDatabaseRepository.deleteUser(user);
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
        appDatabaseRepository.insertUser(user);
    }

    public void deleteUser() {
        user = null;
        appDatabaseRepository.deleteUser(user);
    }

    public void clearPosts() {
        posts.forEach(p -> {
            appDatabaseRepository.deletePost(p);
        });
        posts.clear();
    }

    public void addPost(Post post) {
        posts.add(post);
        appDatabaseRepository.insertPost(post);
    }

    public void removePost(Post post) {
        posts.remove(post);
        appDatabaseRepository.deletePost(post);
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
