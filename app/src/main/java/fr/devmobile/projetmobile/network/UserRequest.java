package fr.devmobile.projetmobile.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.session.Session;

public class UserRequest {

    public void signIn(String emailIn, String passwordIn, Callback callback){
        String body = String.format("{\"Email\": \"%s\", \"Password\": \"%s\"}",
                emailIn, passwordIn);

        AppHttpClient appHttpClient = new AppHttpClient();
        appHttpClient.sendPostRequest("/login", body)
                .thenAccept(result -> {
                    try {
                        Session session = Session.getInstance();
                        session.clearPosts();
                        session.deleteToken();

                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("error")) {
                            callback.onError(jsonObject.getString("error"));
                        }

                        JSONObject userObject = jsonObject.getJSONObject("user");
                        String token = jsonObject.getString("token");
                        String id = userObject.getString("ID");
                        String username = userObject.getString("Username");
                        String displayName = userObject.getString("DisplayName");
                        String email = userObject.getString("Email");
                        String avatar = userObject.getJSONObject("Avatar").getString("FileName");

                        JSONArray posts = userObject.getJSONArray("Posts");
                        for (int i = 0; i < posts.length(); i++) {
                            JSONObject post = posts.getJSONObject(i);
                            String postId = post.getString("ID");
                            String postContent = post.getString("Text");
                            List<String> urls = new ArrayList<>();
                            JSONArray files = post.getJSONArray("Files");
                            for (int j = 0; j < files.length(); j++) {
                                JSONObject file = files.getJSONObject(j);
                                urls.add("https://oxyjen.io/assets/" + file.getString("FileName"));
                            }

                            session.addPost(new Post(postId, postContent, urls));
                        }

                        String avatarUrl = avatar.isEmpty() ? "https://oxyjen.io/assets/default.jpg" : "https://oxyjen.io/assets/" + avatar;

                        session.setUser(new User(id, avatarUrl, username, displayName, email));
                        session.saveToken(token);

                        callback.onResponse(null);

                    } catch (JSONException e) {
                        callback.onError("Error");
                    }

                });
    }

    public void register(String usernameIn, String emailIn, String passwordIn, Callback callback){
        String body = String.format("{\"Username\": \"%s\", \"DisplayName\": \"%s\", \"Email\": \"%s\", \"Password\": \"%s\"}",
                usernameIn, usernameIn, emailIn, passwordIn);

        AppHttpClient appHttpClient = new AppHttpClient();
        appHttpClient.sendPostRequest("/signup", body)
                .thenAccept(result -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("error")) {
                            callback.onError(jsonObject.getString("error"));
                        }

                        JSONObject userObjet = jsonObject.getJSONObject("user");
                        String token = jsonObject.getString("token");
                        String id = userObjet.getString("ID");
                        String username = userObjet.getString("Username");
                        String displayName = userObjet.getString("DisplayName");
                        String email = userObjet.getString("Email");

                        Session session = Session.getInstance();
                        session.saveToken(token);
                        session.setUser(new User(id, username, displayName, email));

                        callback.onResponse(null);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                });
    }

    public void findUsersByUsername(String username, int page, Callback callback) {
        List<User> users = new ArrayList<>();
        AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
        appHttpClient.sendGetRequest("/users?username=" + username + "&page=" + page)
                .thenAccept(result -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonUsers = jsonObject.getJSONArray("users");
                        for(int i = 0; i<jsonUsers.length(); i++){
                            JSONObject jsonUser = jsonUsers.getJSONObject(i);
                            String avatar = jsonUser.getString("profile_picture");
                            String avatarUrl = avatar.isEmpty() ? "https://oxyjen.io/assets/default.jpg" : "https://oxyjen.io/assets/" + avatar;
                            User user = new User(jsonUser.getString("id"), avatarUrl, jsonUser.getString("username"), jsonUser.getString("display_name"), "");
                            users.add(user);
                        }
                        callback.onResponse(users);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
                });
    }

    public void findUserById(String id, Callback callback){
        AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
        appHttpClient.sendGetRequest("/users/"+id)
                .thenAccept(result -> {
                    try {
                        JSONObject jsonUser = new JSONObject(result);
                        Log.i("user", jsonUser.toString());
                        String avatar = jsonUser.getJSONObject("Avatar").getString("FileName");
                        String avatarUrl = avatar.isEmpty() ? "https://oxyjen.io/assets/default.jpg" : "https://oxyjen.io/assets/" + avatar;
                        Log.w("user", avatarUrl);
                        User user = new User(jsonUser.getString("ID"), avatarUrl, jsonUser.getString("Username"), jsonUser.getString("DisplayName"), jsonUser.getString("Email"));
                        List<Post> posts = jsonArrayToPostArray(jsonUser.getJSONArray("Posts"));
                        List<Object> objects = new ArrayList<>();
                        objects.add(user);
                        objects.add(posts);
                        callback.onResponse(objects);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
                });
    }
    private List<Post> jsonArrayToPostArray(JSONArray posts) throws JSONException {
        Log.i("posts", posts.toString());
        List<Post> result = new ArrayList<>();
        for(int i = 0; i < posts.length(); i++){
            JSONObject p = posts.getJSONObject(i);
            JSONArray files = p.getJSONArray("Files");
            List<String> postUrls = new ArrayList<>();
            for(int j = 0; j<files.length(); j++){
                postUrls.add("https://oxyjen.io/assets/" + files.getJSONObject(j).getString("FileName"));
            }
            result.add(new Post(p.getString("ID"), p.getString("Text"), postUrls));
        }
        return result;
    }

}
