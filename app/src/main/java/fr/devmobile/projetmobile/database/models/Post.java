package fr.devmobile.projetmobile.database.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "post")
public class Post {

    @PrimaryKey
    @NonNull
    private String id;
    private String text;
    private List<String> urls;

    @Ignore
    private User user;

    public Post() {}

    public Post(String id, String text, List<String> urls) {
        this.id = id;
        this.text = text;
        this.urls = urls;
    }

    public Post(String id, String text, List<String> urls, User user) {
        this.id = id;
        this.text = text;
        this.urls = urls;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
