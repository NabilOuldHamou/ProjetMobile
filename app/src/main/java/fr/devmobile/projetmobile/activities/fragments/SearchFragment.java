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
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.activities.MainActivity;
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

    private RecyclerView userList;

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
            List<User> userList = getUsers(search);
            List<Post> postList = getPost(search);
            return true;
        }
        return false;
    };

    private void resetStyle(){
        for(Button btn : filterBtn){
            btn.setBackgroundColor(Color.GRAY);
        }
    }

    private void activeStyle(Button btn){
        resetStyle();
        btn.setBackgroundColor(Color.BLACK);
    }

    private List<User> getUsers(String username){
        List<User> userList = new ArrayList<User>();
        AppHttpClient appHttpClient = new AppHttpClient(Session.getInstance().getToken());
        appHttpClient.sendGetRequest("/users?username=" + username + "&page=1")
                .thenAccept(result -> {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonUsers = jsonObject.getJSONArray("users");
                        for(int i = 0; i<jsonUsers.length(); i++){
                            JSONObject jsonUser = jsonUsers.getJSONObject(i);
                            User user = new User(jsonUser.getString("id"), jsonUser.getString("username"), jsonUser.getString("display_name"), "");
                            userList.add(user);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                });
        return userList;
    }

    private List<Post> getPost(String post){
        List<Post> postList = new ArrayList<Post>();

        return postList;
    }
}