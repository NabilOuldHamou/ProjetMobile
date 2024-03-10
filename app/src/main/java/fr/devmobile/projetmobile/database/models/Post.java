package fr.devmobile.projetmobile.database.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "post")
public class Post {

    @PrimaryKey
    @NonNull
    private String id;
    private String text;
    private List<String> urls;

    public Post() {}

    public Post(String id, String text, List<String> urls) {
        this.id = id;
        this.text = text;
        this.urls = urls;
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
}
