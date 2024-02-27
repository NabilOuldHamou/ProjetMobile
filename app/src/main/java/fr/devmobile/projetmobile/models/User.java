package fr.devmobile.projetmobile.models;

import java.util.Date;
import java.util.Optional;

public class User {

    private String id;
    private Date createdAt;
    private Date updatedAt;
    private String avatar = "https://oxyjen.io/assets/default.jpg";
    private String username;
    private String displayName;
    private String email;

    public User(String id, Date createdAt, Date updatedAt, String username, String displayName, String email) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
    }

    public User(String id, Date createdAt, Date updatedAt, String avatar, String username, String displayName, String email) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.avatar = avatar;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
