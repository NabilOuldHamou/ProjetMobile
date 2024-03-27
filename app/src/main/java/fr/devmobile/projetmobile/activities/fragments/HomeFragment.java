package fr.devmobile.projetmobile.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.devmobile.projetmobile.R;
import fr.devmobile.projetmobile.adapters.PostAdapter;
import fr.devmobile.projetmobile.database.models.Post;
import fr.devmobile.projetmobile.database.models.User;
import fr.devmobile.projetmobile.network.AppHttpClient;
import fr.devmobile.projetmobile.network.Callback;
import fr.devmobile.projetmobile.network.PostRequest;
import fr.devmobile.projetmobile.session.Session;

public class HomeFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    private List<PostAdapter.PostData> postsData;

    private int currentPagePosts;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.home_recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);

        if(postsData == null){
            postsData = new ArrayList<PostAdapter.PostData>();
            currentPagePosts = 1;
            refreshPosts(currentPagePosts);
        }

        setupRecycleView();

        recyclerView.addOnScrollListener(scrollListenerPosts);

        return view;
    }

    public RecyclerView.OnScrollListener scrollListenerPosts = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (!recyclerView.canScrollVertically(1)) {
                currentPagePosts++;
                refreshPosts(currentPagePosts);
            }
        }
    };

    private void setupRecycleView() {
        postAdapter = new PostAdapter(postsData, requireContext(), requireActivity());
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void refreshPosts(int page) {
        if (page == 1) {
            postsData.clear();
        }
        recyclerView.removeOnScrollListener(scrollListenerPosts);
        progressBar.setVisibility(View.VISIBLE);
        new PostRequest().getAllPosts("", page, new Callback() {

            @Override
            public void onResponse(Object data) {
                requireActivity().runOnUiThread(() -> {
                    if(currentPagePosts != 1){
                        recyclerView.setPadding(0,0,0,0);
                    }
                    requireActivity().runOnUiThread(() -> postListToPostDataList((Map<Integer, List<Object>>) data));
                    recyclerView.addOnScrollListener(scrollListenerPosts);
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }

            @Override
            public void onError(Object data) {
                requireActivity().runOnUiThread(() -> {
                    if (currentPagePosts > 1) {
                        currentPagePosts--;
                    }
                    recyclerView.addOnScrollListener(scrollListenerPosts);
                    progressBar.setVisibility(View.INVISIBLE);
                });
            }
        });
    }

    private void postListToPostDataList(Map<Integer, List<Object>> posts){
        for (Map.Entry<Integer, List<Object>> entry : posts.entrySet()) {
            postsData.add(new PostAdapter.PostData((Post)entry.getValue().get(0), (User)entry.getValue().get(1)));
        }
        postAdapter.setPosts(postsData);
    }
}