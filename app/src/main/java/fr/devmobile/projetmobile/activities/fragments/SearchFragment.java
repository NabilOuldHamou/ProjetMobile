package fr.devmobile.projetmobile.activities.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.MainActivity;
import fr.devmobile.projetmobile.adapters.PostAdapter;
import fr.devmobile.projetmobile.adapters.UserAdapter;
import fr.devmobile.projetmobile.models.Post;
import fr.devmobile.projetmobile.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.session.Session;

public class SearchFragment extends Fragment {

    private EditText inputSearch;

    private Button postButton;

    private Button profilButton;

    private List<Button> filterBtn = new ArrayList<Button>();;

    private ViewSwitcher searchSwitcher;

    private RecyclerView postList;

    private UserAdapter userAdapter;

    private RecyclerView userList;

    private PostAdapter postAdapter;

    private List<UserAdapter.UserData> usersData;

    private List<PostAdapter.PostData> postsData;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        inputSearch = view.findViewById(R.id.input_search);
        postButton = view.findViewById(R.id.post_button);
        profilButton = view.findViewById(R.id.user_button);
        searchSwitcher = view.findViewById(R.id.search_switcher);
        postList = view.findViewById(R.id.post_list);
        userList = view.findViewById(R.id.user_list);

        searchSwitcher.setDisplayedChild(0);

        filterBtn.add(postButton);
        filterBtn.add(profilButton);
        postButton.setBackgroundColor(Color.BLACK);
        for(Button btn : filterBtn) {
            btn.setOnClickListener(clickListener);
        }
        inputSearch.setOnKeyListener(enterListener);
        usersData = new ArrayList<UserAdapter.UserData>();
        postsData = new ArrayList<PostAdapter.PostData>();
        refreshUsers("");
        refreshPost("");
        setupUserRecycleView();
        setupPostRecycleView();
        return view;
    }

    public View.OnClickListener clickListener = (view) -> {
        activeStyle((Button)view);
        if(view.getId() == R.id.post_button){
            searchSwitcher.setDisplayedChild(0);
        }else if(view.getId() == R.id.user_button){
            searchSwitcher.setDisplayedChild(1);
        }
    };

    public View.OnKeyListener enterListener = (view, keyCode, event) -> {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            String search = inputSearch.getText().toString();
            Log.i("Search", search);
            refreshUsers(search);
            refreshPost(search);
            return true;
        }
        return false;
    };

    private void userListToUserDataList(List<User> users){
        List<UserAdapter.UserData> data = new ArrayList<UserAdapter.UserData>();
        for(User user : users){
            data.add(new UserAdapter.UserData(user));
        }
        usersData = data;
        Log.i("users", usersData.toString());
        userAdapter.notifyDataSetChanged();
    }

    private void postListToPostDataList(Map<Integer, List<Object>> posts){
        List<PostAdapter.PostData> data = new ArrayList<PostAdapter.PostData>();
        for (Map.Entry<Integer, List<Object>> entry : posts.entrySet()) {
            data.add(new PostAdapter.PostData((Post)entry.getValue().get(0), (User)entry.getValue().get(1)));
        }
        postsData = data;
        Log.i("posts", postsData.toString());
        postAdapter.notifyDataSetChanged();
    }

    private void setupUserRecycleView() {
        userAdapter = new UserAdapter(usersData, requireContext());
        userList.setAdapter(userAdapter);
        userList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupPostRecycleView() {
        postAdapter = new PostAdapter(postsData, requireContext());
        postList.setAdapter(postAdapter);
        postList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void resetStyle(){
        for(Button btn : filterBtn){
            btn.setBackgroundColor(Color.GRAY);
        }
    }

    private void activeStyle(Button btn){
        resetStyle();
        btn.setBackgroundColor(Color.BLACK);
    }

    private void refreshUsers(String username){
        List<User> users = new ArrayList<User>();
        usersData.clear();
        AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
        appHttpClient.sendGetRequest("/users?username=" + username + "&page=1")
                .thenAccept(result -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonUsers = jsonObject.getJSONArray("users");
                        for(int i = 0; i<jsonUsers.length(); i++){
                            JSONObject jsonUser = jsonUsers.getJSONObject(i);
                            User user = new User(jsonUser.getString("id"), jsonUser.getString("username"), jsonUser.getString("display_name"), "");
                            users.add(user);
                        }
                        userListToUserDataList(users);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void refreshPost(String query){
        Map<Integer, List<Object>> posts = new HashMap<Integer, List<Object>>();
        postsData.clear();
        AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
        appHttpClient.sendGetRequest("/posts?query=" + query + "&page=1")
                .thenAccept(result -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonPosts = jsonObject.getJSONArray("posts");
                        for(int i = 0; i<jsonPosts.length(); i++){
                            JSONObject jsonPost = jsonPosts.getJSONObject(i);
                            JSONObject jsonUser = jsonPost.getJSONObject("user");
                            JSONArray files = jsonPost.getJSONArray("files");
                            List<String> postUrls = new ArrayList<String>();
                            for(int j = 0; j<files.length(); j++){
                                postUrls.add(files.getJSONObject(j).getString("FileName"));
                            }
                            Post post = new Post(jsonPost.getString("id"), jsonPost.getString("text"), postUrls);
                            User user = new User(jsonUser.getString("id"), jsonUser.getString("username"), jsonUser.getString("display_name"), "");
                            posts.put(i, Arrays.asList(post, user));
                        }
                        postListToPostDataList(posts);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}