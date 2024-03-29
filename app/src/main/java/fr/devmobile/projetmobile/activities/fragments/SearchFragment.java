package fr.devmobile.projetmobile.activities.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
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
import fr.devmobile.projetmobile.adapters.PostAdapter;
import fr.devmobile.projetmobile.adapters.UserAdapter;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.network.Callback;
import fr.devmobile.projetmobile.network.PostRequest;
import fr.devmobile.projetmobile.network.UserRequest;
import fr.devmobile.projetmobile.session.Session;

public class SearchFragment extends Fragment {

    private ProgressBar progressBarUsers;

    private ProgressBar progressBarPosts;

    private SearchView inputSearch;

    private Button postButton;

    private Button profilButton;

    private List<Button> filterBtn = new ArrayList<Button>();

    private Integer currentPagePos;

    private ViewSwitcher searchSwitcher;

    private RecyclerView postList;

    private UserAdapter userAdapter;

    private RecyclerView userList;

    private PostAdapter postAdapter;

    private List<User> usersData;

    private List<Post> postsData;

    private int currentPagePosts;

    private int currentPageUsers;

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
        progressBarUsers = view.findViewById(R.id.progress_bar_search_users);
        progressBarPosts = view.findViewById(R.id.progress_bar_search_posts);

        if(currentPagePos == null){
            currentPagePos = 0;
            searchSwitcher.setDisplayedChild(currentPagePos);
        }else{
            searchSwitcher.setDisplayedChild(currentPagePos);
        }

        filterBtn.add(postButton);
        filterBtn.add(profilButton);

        activeStyle(filterBtn.get(currentPagePos));

        for(Button btn : filterBtn) {
            btn.setOnClickListener(clickListener);
        }
        inputSearch.setOnQueryTextListener(enterListener);
        inputSearch.setOnClickListener((v) -> {inputSearch.setIconified(false);});

        if(usersData == null){
            usersData = new ArrayList<User>();
            currentPageUsers = 1;
            refreshUsers("", currentPageUsers);
        }
        if(postsData == null){
            postsData = new ArrayList<Post>();
            currentPagePosts = 1;
            refreshPosts("", currentPagePosts);
        }

        setupUserRecycleView();
        setupPostRecycleView();
        userList.addOnScrollListener(scrollListenerUsers);
        postList.addOnScrollListener(scrollListenerPosts);

        return view;
    }

    public View.OnClickListener clickListener = (view) -> {
        activeStyle((Button)view);
        if(view.getId() == R.id.post_button){
            currentPagePos = 0;
        }else if(view.getId() == R.id.user_button){
            currentPagePos = 1;
        }
        searchSwitcher.setDisplayedChild(currentPagePos);
    };

    public SearchView.OnQueryTextListener enterListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            currentPagePosts = 1;
            currentPageUsers = 1;
            refreshPosts(query, currentPagePosts);
            refreshUsers(query, currentPageUsers);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            currentPagePosts = 1;
            currentPageUsers = 1;
            refreshPosts(newText, currentPagePosts);
            refreshUsers(newText, currentPageUsers);
            return false;
        }
    };

    public RecyclerView.OnScrollListener scrollListenerPosts = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!recyclerView.canScrollVertically(1)) {
                currentPagePosts++;
                refreshPosts(inputSearch.getQuery().toString(), currentPagePosts);
            }
        }
    };

    public RecyclerView.OnScrollListener scrollListenerUsers = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!recyclerView.canScrollVertically(1)) {
                currentPageUsers++;
                refreshUsers(inputSearch.getQuery().toString(), currentPageUsers);
            }
        }
    };

    private void setupUserRecycleView() {
        userAdapter = new UserAdapter(usersData, requireContext(), requireActivity());
        userList.setAdapter(userAdapter);
        userList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setupPostRecycleView() {
        postAdapter = new PostAdapter(postsData, requireContext(), requireActivity());
        postList.setAdapter(postAdapter);
        postList.setLayoutManager(new LinearLayoutManager(requireContext()));
    }


    private void activeStyle(Button btn){
        for(Button b : filterBtn){
            if(b.getId() == btn.getId()){
                b.setBackgroundColor(Color.BLACK);
            }else{
                b.setBackgroundColor(Color.GRAY);
            }
        }
    }

    private void refreshUsers(String username, int page){
        if(page == 1){
            usersData.clear();
        }
        userList.removeOnScrollListener(scrollListenerUsers);
        progressBarUsers.setVisibility(View.VISIBLE);
        new UserRequest().findUsersByUsername(username, page,new Callback() {
            @Override
            public void onResponse(Object data) {
                requireActivity().runOnUiThread(() -> {
                    usersData.addAll((List<User>)data);
                    userAdapter.setUsers(usersData);
                    userList.addOnScrollListener(scrollListenerUsers);
                    progressBarUsers.setVisibility(View.INVISIBLE);
                });

            }

            @Override
            public void onError(Object data) {
                requireActivity().runOnUiThread(() -> {
                    if(currentPageUsers > 1){
                        currentPageUsers--;
                    }
                    userList.addOnScrollListener(scrollListenerUsers);
                    progressBarUsers.setVisibility(View.INVISIBLE);
                });
            }
        });
    }

    private void refreshPosts(String query, int page){
        if(page == 1){
            postsData.clear();
        }
        postList.removeOnScrollListener(scrollListenerPosts);
        progressBarPosts.setVisibility(View.VISIBLE);
        new PostRequest().getAllPosts(query, page, new Callback() {

            @Override
            public void onResponse(Object data) {
                requireActivity().runOnUiThread(() -> {
                    postsData.addAll((List<Post>)data);
                    postAdapter.setPosts(postsData);
                    postList.addOnScrollListener(scrollListenerPosts);
                    progressBarPosts.setVisibility(View.INVISIBLE);
                });

            }

            @Override
            public void onError(Object data) {
                requireActivity().runOnUiThread(() -> {
                    if(currentPagePosts > 1){
                        currentPagePosts--;
                    }
                    postList.addOnScrollListener(scrollListenerPosts);
                    progressBarPosts.setVisibility(View.INVISIBLE);
                });
            }
        });
    }

}