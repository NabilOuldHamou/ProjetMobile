package fr.devmobile.projetmobile.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.session.Session;

public class PostRequest {

    public void getAllPosts(String query, int page, Callback callback) {
        List<Post> posts = new ArrayList<>();
        AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
        appHttpClient.sendGetRequest("/posts?query=" + query + "&page=" + page)
                .thenAccept(result -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonPosts = jsonObject.getJSONArray("posts");
                        for(int i = 0; i<jsonPosts.length(); i++){
                            JSONObject jsonPost = jsonPosts.getJSONObject(i);
                            JSONObject jsonUser = jsonPost.getJSONObject("user");
                            JSONArray files = jsonPost.getJSONArray("files");
                            String avatar = jsonUser.getString("profile_picture");
                            String avatarUrl = avatar.isEmpty() ? "https://oxyjen.io/assets/default.jpg" : "https://oxyjen.io/assets/" + avatar;
                            List<String> postUrls = new ArrayList<>();
                            for(int j = 0; j<files.length(); j++){
                                postUrls.add("https://oxyjen.io/assets/" + files.getJSONObject(j).getString("FileName"));
                            }
                            User user = new User(jsonUser.getString("id"), avatarUrl, jsonUser.getString("username"), jsonUser.getString("display_name"), "");
                            Post post = new Post(jsonPost.getString("id"), jsonPost.getString("text"), postUrls, user);
                            posts.add(post);
                        }
                        callback.onResponse(posts);
                    } catch (JSONException e) {
                        callback.onError(e);
                    }
                });
    }
}
